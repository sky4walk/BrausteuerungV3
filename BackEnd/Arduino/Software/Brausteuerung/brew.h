// brausteuerung@AndreBetz.de
#ifndef __BREW__
#define __BREW__

#include <Arduino.h>
#include "settings.h"
#include "heatcontroler.h"
#include "batlow.h"
#include "buzzer.h"
#include "sensor.h"
#include "switch.h"
#include "waittime.h"

#define TEMP_STORE_INTERVAL 6
#define TIMER_MULTIPLEX     2

#define ALARM_NO        0
#define ALARM_BATLOW    1
#define ALARM_OVERHEAT  2
#define ALARM_NOTEMP    3
#define ALARM_TMPERR    4

#define UEBERHITZUNGSSCHUTZ

///////////////////////////////////////////////////////////////////////////////
// Brew
///////////////////////////////////////////////////////////////////////////////


class Brew
{
  public:
    Brew(
      SystemSettings&     settings,
      BatLow&             batLow,
#ifdef PID_REG
      PIDControler&       controlerPid,
#endif
      HystereseControler& controlerHyst,
      TemperaturSensor&   sensor,
      Switcher&           switcher,
      int                 timerTempMeasure) :
      mSettings(settings),
      mBatLow(batLow),
#ifdef PID_REG
      mControlerPid(controlerPid),
#endif
      mControlerHyst(controlerHyst),
      mSensor(sensor),
      mSwitcher(switcher),
      mTimerBrew(0),
      mTimerTempMeasure(timerTempMeasure),
      mFirstCall(false),
      mTempReached(false),
      mIsBatLow(false),
      mActSettingsNr(0),
      mActTemp(0)
    {
    }
    void init()
    {
      resetBrew();
    }
    void resetBrew()
    {
      mFirstCall = false;
      mTempReached = false;
      mSwitchOnOff = false;
      ((Switcher&)mSwitcher).off();
      mActSettingsNr = 0;
      mTimerBrew.init();
      mActTemp = mSensor.getTemperatur();
      mSensor.resetStored(mActTemp);
      mTimerCnt = TIMER_MULTIPLEX;
      mAlarmType = ALARM_NO;
      mGradientCycleCnt = 0;
    }
    void setActParams(int actSettingsNr)
    {
      resetBrew();
      mActSettingsNr = actSettingsNr;
#ifdef PID_REG
      ((HeatControler&)mControlerPid).setActParams(mActSettingsNr);
      mControlerPid.init();
#endif
      ((HeatControler&)mControlerHyst).setActParams(mActSettingsNr);
      mControlerHyst.init();

    }
    void resetAlarm() {
      mAlarmType = ALARM_NO;
    }
    boolean isTempReached()
    {
      return mTempReached;
    }
    boolean isPause(){
      mTimerBrew.isPause();
    }
#ifdef PID_REG
    double getOutput()
    {
      return mControlerPid.getOutput();
    }
#endif
    unsigned long getActTime()
    {
      return mTimerBrew.getDuration();
    }
    byte getAlarmType() {
      return mAlarmType;
    }
    boolean isBatLow()
    {
      return mIsBatLow;
    }
    boolean isSwitchOn()
    {
      return ((Switcher&)mSwitcher).getState();
    }
    boolean getHeatingState()
    {
      return mSwitchOnOff;
    }
    float getBatLevel()
    {
      return mBatLow.getVoltage();
    }
    void initTimerBrew(unsigned long timer)
    {
      mTimerBrew.init(timer);
    }
    int getActGradPos() {
      mSensor.getActGradPos();
    }
#ifdef AUTO_TUNE
    boolean runTuning()
    {
      if ( runBrewing() )
      {
        if ( !mControlerPid.isTuning( mActTemp) )
          return true;
      }
      return false;
    }
#endif
    void pauseResume()
    {
      if ( true == mTimerBrew.isPause() )
      {
        mTimerBrew.resume();
      }
      else
      {
        mTimerBrew.pause();
      }
    }
    boolean runBrewing(boolean on)
    {
      if ( false == mTimerBrew.isPause() ) {
        mTimerTempMeasure.start();
        if ( mTimerTempMeasure.timeOver() ) {
          mTimerTempMeasure.init();

          mActTemp = mSensor.getTemperatur();

          // Temperatursensor hat eine Fehler
          if ( mActTemp < -120 ) {
            mAlarmType = ALARM_TMPERR;
          } else {
            mAlarmType = ALARM_NO;
          }
          mSensor.addVal(mActTemp);

#ifdef UEBERHITZUNGSSCHUTZ
          // Temperaturueberwachung falls geheizt wird
          // aber Temperatur steigt nicht
          if ( 0 == mGradientCycleCnt ) {
            mLastTemp = mActTemp;
          }
          if ( on && 0 == mSensor.getActGradPos() &&
               0 != mSettings.getMeasureHeatingTime() ) {
            mGradientCycleCnt++;
          }
          if ( false == ((HeatControler&)mControlerHyst).getHeatingState() ) {
            mGradientCycleCnt = 0;
          }
          if ( mSettings.getMeasureHeatingTime() < mGradientCycleCnt ) {
            mGradientCycleCnt = 0;
            if ( mActTemp <= mLastTemp ) {
              mAlarmType = ALARM_NOTEMP;
            }
          }
#endif

          if ( ((HeatControler&)mControlerHyst).isHeatingStateChanged()
#ifdef PID_REG
               || ((HeatControler&)mControlerPid).isHeatingStateChanged()
#endif
             ) {
            mTimerCnt = TIMER_MULTIPLEX;
          }

          mTimerCnt++;
          if ( TIMER_MULTIPLEX <= mTimerCnt ) {
            mTimerCnt = 0;

            // Batterie ist zu schwach
            mIsBatLow = mBatLow.checkBatterieVoltage();
            if ( mIsBatLow ) {
              mAlarmType = ALARM_BATLOW;
            }

            // Ueberhitzungsschutz
            if ( on && mControlerHyst.isOverHeating(mActTemp) ) {
              mAlarmType = ALARM_OVERHEAT;
            }

            if ( mSwitchOnOff && on && (ALARM_NO == mAlarmType) ) {
              ((Switcher&)mSwitcher).on();
            } else {
              ((Switcher&)mSwitcher).off();
            }

          }
        }

        if ( on && aufHeizen( mActTemp ) ) {
          if ( isTimerReached() )
          {
            return true;
          }
        }
      }
      return false;
    }
  private:
    boolean aufHeizen( float actTemp )
    {
#ifdef PID_REG
      if ( mSettings.getPid() ) {
        mSwitchOnOff = mControlerPid.isHeating( actTemp, 0.0f );
      }
      else
#endif
      {
        mSwitchOnOff = mControlerHyst.isHeating( actTemp, mSensor.getGradient() );
      }

      if ( mTempReached == false )
      {
        if ( mControlerHyst.isCooling() )
        {
          if ( actTemp <= mSettings.getBrewStepSollTemp(mActSettingsNr) )
          {
            mTempReached = true;
          }
        }
        else
        {
          if ( actTemp >= mSettings.getBrewStepSollTemp(mActSettingsNr) )
          {
            mTempReached = true;
          }
        }
      }
      return mTempReached;
    }
    boolean isTimerReached()
    {
      mTimerBrew.start(
        mSettings.getBrewStepTime(mActSettingsNr) *
        MIL2SEC * SECPROMIN);
      if (mTimerBrew.timeOver())
      {
        return true;
      }
      return false;
    }
    SystemSettings& mSettings;
    BatLow& mBatLow;
#ifdef PID_REG
    PIDControler& mControlerPid;
#endif
    HystereseControler& mControlerHyst;
    TemperaturSensor& mSensor;
    Switcher& mSwitcher;
    WaitTime mTimerBrew;
    WaitTime mTimerTempMeasure;
    byte mTimerCnt;
    boolean mFirstCall;
    boolean mTempReached;
    boolean mSwitchOnOff;
    boolean mIsBatLow;
    int mActSettingsNr;
    float mActTemp;
    float mLastTemp;
    byte mAlarmType;
    byte mGradientCycleCnt;
};

#endif

