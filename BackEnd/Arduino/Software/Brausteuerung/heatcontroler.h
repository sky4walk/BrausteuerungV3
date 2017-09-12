// brausteuerung@AndreBetz.de
#ifndef __CONTROLER__
#define __CONTROLER__

#include <Arduino.h>
///////////////////////////////////////////////////////////////////////////////
// Controler
///////////////////////////////////////////////////////////////////////////////

class HeatControler
{
  public:
    HeatControler( SystemSettings& settings ) :
      mSettings(settings),
      mActSettingsNr(0),
      mHeatingOn(false),
      mHeatStateChange(false)
    {
    }
    virtual boolean isHeating(float actTemp, float gradient) = 0;
    virtual void init()
    {
      mHeatingOn = false;
      mHeatStateChange = false;
    }
    void setActParams(int actSettingsNr)
    {
      mActSettingsNr = actSettingsNr;
    }
    virtual boolean isHeatingStateChanged()
    {
      boolean tempHeatStateChange = mHeatStateChange;
      mHeatStateChange = false;
      return tempHeatStateChange;
    }
    virtual boolean getHeatingState()
    {
      return mHeatingOn;
    }
  protected:
    boolean heatingStateChange(boolean heaterGetOn)
    {
      if ( mHeatingOn != heaterGetOn )
        mHeatStateChange = true;
      return heaterGetOn;
    }
    SystemSettings& mSettings;
    int mActSettingsNr;
    boolean mHeatingOn;
    boolean mHeatStateChange;
};

///////////////////////////////////////////////////////////////////////////////

#ifdef PID_REG
class PIDControler : HeatControler
{
  public:
    PIDControler(SystemSettings& settings) :
      HeatControler( settings ),
      mPid(&mInput, &mOutput, &mSetPoint, 16.16, 0.14, 480.10, DIRECT),
#ifdef AUTO_TUNE
      mTuningOn(false),
      mTune(&mInput, &mOutput),
#endif
      mPwmWindowStartTime(0)
    {
    }
    void init()
    {
#ifdef AUTO_TUNE
      mTuningOn = false;
#endif
      HeatControler::init();
      mPid.SetSampleTime(mSettings.getPidSampleTime());
      mPid.SetTunings(
        mSettings.getBrewStepKp(mActSettingsNr),
        mSettings.getBrewStepKi(mActSettingsNr),
        mSettings.getBrewStepKd(mActSettingsNr));
      mPid.SetOutputLimits(0.0, mSettings.getPidWindowSize());
      mPid.SetMode(AUTOMATIC);
#ifdef AUTO_TUNE
      mTune.SetControlType(1);
      mTune.SetNoiseBand(mSettings.getTuneNoise());
      mTune.SetOutputStep(mSettings.getTuneStep());
      mTune.SetLookbackSec((int)mSettings.getTuneLookBack());
#endif
      mPwmWindowStartTime = millis();
    }
    double getOutput()
    {
      return mOutput;
    }
#ifdef AUTO_TUNE
    boolean isTuning(float actTemp)
    {
      mTuningOn = true;
      if ( 0 != (mTune.Runtime() ) )
      {
        mTune.Cancel();
        mSettings.setBrewStepKp(mActSettingsNr, mTune.GetKp());
        mSettings.setBrewStepKi(mActSettingsNr, mTune.GetKi());
        mSettings.setBrewStepKd(mActSettingsNr, mTune.GetKd());
        mTuningOn = false;
        return false;
      }
      return true;
    }
#endif
    boolean isHeating(float actTemp, float gradient)
    {
      mInput    = actTemp;
      mSetPoint = mSettings.getBrewStepSollTemp(mActSettingsNr);

      unsigned long now = millis();
      unsigned long diff = now - mPwmWindowStartTime;

#ifdef AUTO_TUNE
      if ( !mTuningOn )
#endif
      {
        if ( (mSetPoint - mInput) < mSettings.getPidDelta() )
        {
          mPid.Compute();
        }
        else
        {
          mOutput = mSettings.getPidWindowSize();
        }
      }

      if ( diff > mSettings.getPidWindowSize() )
      {
        mPwmWindowStartTime += mSettings.getPidWindowSize();
        diff = 0;
      }

      if ( mOutput >= diff )
        mHeatingOn = heatingStateChange(true);
      else
        mHeatingOn = heatingStateChange(false);

      return mHeatingOn;
    }
  private:
    PID mPid;
#ifdef AUTO_TUNE
    PID_ATune mTune;
    boolean mTuningOn;
#endif
    double mInput;
    double mOutput;
    double mSetPoint;
    unsigned long mPwmWindowStartTime;
};
#endif

///////////////////////////////////////////////////////////////////////////////

class HystereseControler : HeatControler
{
  public:
    HystereseControler(SystemSettings& settings) :
      HeatControler( settings ),
      mTimePulsOn(0),
      mTimePulsOff(0)
    {
    }
    virtual void init()
    {
      HeatControler::init();
      mTimePulsOn.init();
      mTimePulsOff.init();
    }
    boolean isCooling()
    {
      if ( mSettings.getBrewStepMaxTemp(mActSettingsNr) <
           mSettings.getBrewStepMinTemp(mActSettingsNr) )
        return true;
      else
        return false;
    }
    boolean isOverHeating(float actTemp) {
      if (  actTemp >
            mSettings.getBrewStepSollTemp(mActSettingsNr) +
            mSettings.getMaxOverHeat() &&
            0 != mSettings.getBrewStepSollTemp(mActSettingsNr) &&
            0 != mSettings.getMaxOverHeat() &&
            !isCooling() ) {
        return true;
      }
      return false;
    }
    boolean isHeating(float actTemp, float gradient)
    {
      boolean heating;
      // zweipunktregler mit hysterese
      if ( mHeatingOn )
      {
        if ( actTemp > ( mSettings.getBrewStepSollTemp(mActSettingsNr) +
                         mSettings.getBrewStepMaxTemp(mActSettingsNr)  -
                         gradient * mSettings.getBrewStepKp(mActSettingsNr) ) )
        {
          heating = false;
        }
        else
        {
          heating = true;
        }
      }
      else
      {
        if ( actTemp < ( mSettings.getBrewStepSollTemp(mActSettingsNr) +
                         mSettings.getBrewStepMinTemp(mActSettingsNr)  -
                         gradient * mSettings.getBrewStepKp(mActSettingsNr) ) )
        {
          heating = true;
        }
        else
        {
          heating = false;
        }
      }

      if ( 0 < mSettings.getBrewStepOnTimePuls(mActSettingsNr) &&
           0 < mSettings.getBrewStepOffTimePuls(mActSettingsNr)  )
      {
        if ( heating )
        {
          mTimePulsOn.start( mSettings.getBrewStepOnTimePuls(mActSettingsNr) * 1000);
          if ( mTimePulsOn.timeOver() )
          {
            heating = false;
            mTimePulsOff.start( mSettings.getBrewStepOffTimePuls(mActSettingsNr) * 1000);
            if ( mTimePulsOff.timeOver() )
            {
              mTimePulsOn.init();
              mTimePulsOff.init();
            }
          }

        }
        else
        {
          mTimePulsOn.init();
          mTimePulsOff.init();
        }
      }
      if (  0 < mSettings.getMaxGradient(mActSettingsNr) &&
            mSettings.getMaxGradient(mActSettingsNr) < gradient ) {
        heating = false;
      }

      if (isCooling()) heating = !heating;
      mHeatingOn = heatingStateChange(heating);

      return mHeatingOn;
    }
  private:
    WaitTime mTimePulsOn;
    WaitTime mTimePulsOff;
};

///////////////////////////////////////////////////////////////////////////////

#endif

