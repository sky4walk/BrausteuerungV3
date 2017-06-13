// brausteuerung@AndreBetz.de
#ifndef __MENU__
#define __MENU__

#include <Arduino.h>
#include "brew.h"
#include "waittime.h"
///////////////////////////////////////////////////////////////////////////////
// MENU
///////////////////////////////////////////////////////////////////////////////


class Menu
{
  public:
    Menu( InOutPut& interface,
          SystemSettings& settings,
          Brew& brewing,
          Buzzer& buzzer,
          TemperaturSensor& tempSensor,
          Switcher&           switcher,
          int timerSaveState,
          byte pinReset,
          byte pinLed) :
      mInterface( interface ),
      mSettings(settings),
      mBrewing(brewing),
      mBuzzer(buzzer),
      mTempSensor(tempSensor),
      mSwitcher(switcher),
      mTimerSaveState(timerSaveState),
      mStateTop(0),
      mStateSetup(0),
      mStateBrewRecipe(0),
      mStateGo(0),
      mStateTune(0),
      mRecipeEditNr(0),
      mWaitInput(false),
      mFirstEntry(false),
      mPinReset(pinReset),
      mPinLed(pinLed)
    {
    }
    void init()
    {
      pinMode(mPinReset, INPUT_PULLUP);
      //pinMode(mPinLed, OUTPUT);
      reset();
    }
    void reset()
    {
      mFirstEntry = false;
      mFirstEntryRun = false;
      mWaitInput = false;
      mStateTop = 0;
      mStateSetup = 0;
      mStateBrewRecipe = 0;
      mStateGo = 0;
      mStateTune = 0;
      mRecipeEditNr = 0;
      mBuzzer.off();
      mBrewing.resetBrew();
    }
    void showMenu()
    {
      if ( false == mFirstEntry )
      {
        mFirstEntry = true;
        if ( mSettings.isStateSet() )
        {
          mSettings.setStateSet(false);
          mFirstEntry = false;
          mFirstEntryRun = true;
          mStateTop = 3;
          mStateGo = 1;
          mBuzCall = mSettings.getBrewStepCall(mRecipeEditNr);
          mRecipeEditNr = mSettings.getActBrewState();
          mBrewing.setActParams(mRecipeEditNr);
          mBrewing.initTimerBrew( mSettings.getActTime() );
        }
      }
      if ( LOW == digitalRead(mPinReset) ) {
        resetAll();
        mStateTop = 0;
      }
      if (mBrewing.isBatLow()) {
        digitalWrite(mPinLed, LOW);
      } else {
        digitalWrite(mPinLed, HIGH);
      }
      switch ( mStateTop )
      {
        case 0:
          {
            showMenuTop();
            break;
          }
        case 1:
          {
            showMenuSetup();
            break;
          }
        case 2:
          {
            showMenuRezept();
            break;
          }
        case 3:
          {
            showMenuGo();
            break;
          }
#ifdef AUTO_TUNE
        case 4:
          {
            showMenuTune();
            break;
          }
#endif
        case 5:
          {
            resetAll();
            mStateTop = 0;
            break;
          }
        default:
          {
            mStateTop = 0;
            break;
          }
      }
    }
    int getStateTop()
    {
      return mStateTop;
    }
  private:
    boolean getInputboolean(const __FlashStringHelper* text, boolean& setting)
    {
      if ( !mWaitInput )
      {
        mWaitInput = true;
        mInterface.print(text);
        if (setting) mInterface.println(F("1"));
        else mInterface.println(F("0"));
      }
      int a = mInterface.readByte();
      if ( '0' == a || '1' == a )
      {
        mWaitInput = false;
        if ('0' == a)
        {
          setting = false;
          mInterface.println(F("0"));
        }
        else
        {
          setting = true;
          mInterface.println(F("1"));
        }
        return true;
      }
      else if ( 'w' == a )
      {
        return true;
      }
      else if ( 'a' == a )
      {
        mWaitInput = false;
      }
      return false;
    }
    boolean getInputLong(const __FlashStringHelper* text, long& setting)
    {
      boolean ret = false;
      if ( !mWaitInput )
      {
        dtostrf(setting, 1, 0, mCharBuf);
        mWaitInput = true;
        mInterface.print(text);
        mInterface.println(mCharBuf);
      }
      char* in = mInterface.readString();
      if ( 0 != in )
      {
        if ( strcmp(in, "w" ) == 0 )
        {
          ret = true;
        }
        else if ( strcmp(in, "a") == 0 )
        {
          ret = false;
          mWaitInput = false;
        }
        else
        {
          setting = atol(in);
          mInterface.println(in);
          ret = true;
        }
      }
      return ret;
    }
    boolean getInputFloat(const __FlashStringHelper* text, float& setting)
    {
      boolean ret = false;
      if ( !mWaitInput )
      {
        dtostrf(setting, 1, 2, mCharBuf);
        mWaitInput = true;
        mInterface.print(text);
        mInterface.println(mCharBuf);
      }
      char* in = mInterface.readString();
      if ( 0 != in )
      {
        if ( strcmp(in, "w" ) == 0 )
        {
          ret = true;
        }
        else if ( strcmp(in, "a") == 0 )
        {
          ret = false;
          mWaitInput = false;
        }
        else
        {
          setting = atof(in);
          mInterface.println(in);
          ret = true;
        }
      }
      return ret;
    }
    void resetAll()
    {
      mSettings.resetData();
      mInterface.println(F("r"));
    }
    void showMenuTop()
    {
      if ( !mWaitInput )
      {
        if ( 1 == mTempSensor.getSensorType() )
          mInterface.print(F("md"));
        else
          mInterface.print(F("ma"));
        dtostrf(mSettings.getMaxBrewSteps(), 1, 0, mCharBuf);
        mInterface.print(mCharBuf);
        mInterface.print(F(":"));
        dtostrf(mSettings.getVersion(), 1, 0, mCharBuf);
        mInterface.println(mCharBuf);
        mWaitInput = true;
      }
      int a = mInterface.readByte();
      mBrewing.runBrewing(false);

      switch (a)
      {
        case '1':
          {
            mWaitInput = false;
            mStateTop = 1;
            break;
          }
        case '2':
          {
            mWaitInput = false;
            mStateTop = 2;
            break;
          }
        case '3':
          {
            mWaitInput = false;
            mStateTop = 3;
            break;
          }
        case 't':
          {
            mWaitInput = false;
            mStateTop = 4;
            break;
          }
        case 'd':
          {
            mStateTop = 5;
            break;
          }
        case 'p':
          {
            printBrew(false, false, true);
            break;
          }
        case 'h':
          {
            ((Switcher&)mSwitcher).on();
            break;
          }
        case 'c':
          {
            ((Switcher&)mSwitcher).off();
            break;
          }
        case '4':
          {
            mBrewing.pauseResume();
            break;
          }
        case '0' :
        case 'a' :
          mWaitInput = false;
          break;
        default:
          break;
      }
    }
    void showMenuSetup()
    {
      switch (mStateSetup)
      {
        case 0:
          {
#ifdef PID_REG
            boolean val = mSettings.getPid();
            if ( getInputboolean (F("spi:"), val) )
            {
              mSettings.setPid(val);
              mWaitInput = false;
              mStateSetup++;
            }
#else
            mStateSetup++;
#endif
            break;
          }
        case 1:
          {
#ifdef PROFI_COOK
            float val = mSettings.getNtcT0();
            if ( getInputFloat(F("stz:"), val) )
            {
              mSettings.setNtcT0(val);
              mWaitInput = false;
              mStateSetup++;
            }
#else
            mStateSetup++;
#endif
          }
          break;
        case 2:
          {
            float val = mSettings.getKalM();
            if ( getInputFloat(F("skm:"), val) )
            {
              mSettings.setKalM(val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 3:
          {
#ifdef PROFI_COOK
            float val = mSettings.getNtcR0();
            if ( getInputFloat(F("srz:"), val) )
            {
              mSettings.setNtcR0(val);
              mWaitInput = false;
              mStateSetup++;
            }
#else
            mStateSetup++;
#endif
            break;
          }
        case 4:
          {
#ifdef PROFI_COOK
            float val = mSettings.getKalT();
            if ( getInputFloat(F("skt:"), val) )
            {
              mSettings.setKalT(val);
              mWaitInput = false;
              mStateSetup++;
            }
#else
            mStateSetup++;
#endif
            break;
          }
        case 5:
          {
#ifdef PROFI_COOK
            float val = mSettings.getNtcT1();
            if ( getInputFloat(F("sto:"), val) )
            {
              mSettings.setNtcT1(val);
              mWaitInput = false;
              mStateSetup++;
            }
#else
            mStateSetup++;
#endif
            break;
          }
        case 6:
          {
#ifdef PROFI_COOK
            float val = mSettings.getNtcR1();
            if ( getInputFloat(F("sro:"), val) )
            {
              mSettings.setNtcR1(val);
              mWaitInput = false;
              mStateSetup++;
            }
#else
            mStateSetup++;
#endif
            break;
          }
        case 7:
          {
#ifdef PROFI_COOK
            long val = mSettings.getVorR();
            if ( getInputLong(F("svw:"), val) )
            {
              mSettings.setVorR(val);
              mWaitInput = false;
              mStateSetup++;
            }
#else
            mStateSetup++;
#endif
            break;
          }
        case 8:
          {
#ifdef PID_REG
            long val = mSettings.getPidWindowSize();
            if ( getInputLong(F("sps:"), val) )
            {
              mSettings.setPidWindowSize((unsigned int)val);
              mWaitInput = false;
              mStateSetup++;
            }
#else
            mStateSetup++;
#endif
            break;
          }
        case 9:
          {
#ifdef PID_REG
            long val = mSettings.getPidSampleTime();
            if ( getInputLong(F("spt:"), val) )
            {
              mSettings.setPidSampleTime((unsigned int)val);
              mWaitInput = false;
              mStateSetup++;
            }
#else
            mStateSetup++;
#endif
            break;
          }
        case 10:
          {
#ifdef PID_REG
            float val = mSettings.getPidDelta();
            if ( getInputFloat(F("spd:"), val) )
            {
              mSettings.setPidDelta(val);
              mWaitInput = false;
              mStateSetup++;
            }
#else
            mStateSetup++;
#endif
            break;
          }
        case 11:
          {
#ifdef AUTO_TUNE
            float val = (float)mSettings.getTuneStep();
            if ( getInputFloat(F("sts:"), val) )
            {
              mSettings.setTuneStep(val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 12:
          {
            float val = (float)mSettings.getTuneNoise();
            if ( getInputFloat(F("stn:"), val) )
            {
              mSettings.setTuneNoise(val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 13:
          {
            long val = mSettings.getTuneLookBack();
            if ( getInputLong(F("slb:"), val) )
            {
              mSettings.setTuneLookBack((unsigned int)val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
#else
            mStateSetup = 14;
            break;
#endif
          }
        case 14:
          {
            long val = mSettings.getSwitchType();
            if ( getInputLong(F("sst:"), val) )
            {
              mSettings.setSwitchType((byte)val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 15:
          {
            long val = mSettings.getSwitchRepeats();
            if ( getInputLong(F("ssr:"), val) )
            {
              mSettings.setSwitchRepeats((byte)val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 16:
          {
            long val = mSettings.getSwitchPeriodusec();
            if ( getInputLong(F("ssp:"), val) )
            {
              mSettings.setSwitchPeriodusec((unsigned int)val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 17:
          {
            long val = mSettings.getSwitchBits();
            if ( getInputLong(F("ssb:"), val) )
            {
              mSettings.setSwitchBits((byte)val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 18:
          {
            long val = mSettings.getSwitchAddress();
            if ( getInputLong(F("ssa:"), val) )
            {
              mSettings.setSwitchAddress((unsigned long)val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 19:
          {
            long val = mSettings.getSwitchUnit();
            if ( getInputLong(F("ssu:"), val) )
            {
              mSettings.setSwitchUnit(val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 20:
          {
            float val = (float)mSettings.getBatLowLevel();
            if ( getInputFloat(F("sbl:"), val) )
            {
              mSettings.setBatLowLevel(val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 21:
          {
            long val = mSettings.getPassWd();
            if ( getInputLong(F("spw:"), val) )
            {
              mSettings.setPassWd((unsigned int)val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 22:
          {
            long val = mSettings.getSerNr();
            if ( getInputLong(F("snr:"), val) )
            {
              mSettings.setSerNr((unsigned int)val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 23:
          {
            boolean val = mSettings.getLoadStateOn();
            if ( getInputboolean(F("sls:"), val) )
            {
              mSettings.setLoadStateOn(val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 24:
          {
            long val = mSettings.getMeasureHeatingTime();
            if ( getInputLong(F("sls:"), val) )
            {
              mSettings.setMeasureHeatingTime((byte)val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        case 25:
          {
            long val = mSettings.getMaxOverHeat();
            if ( getInputLong(F("soh:"), val) )
            {
              mSettings.setMaxOverHeat((byte)val);
              mWaitInput = false;
              mStateSetup++;
            }
            break;
          }
        default:
          {
            mSettings.saveSettings();
            mStateTop = 0;
            mStateSetup = 0;
            mSettings.setWriteBT(true);
            mSettings.saveState();
          }
      }
    }
    void showMenuRezept()
    {
      switch (mStateBrewRecipe)
      {
        case 0:
          {
            long val = mRecipeEditNr;
            if ( getInputLong(F("bnr:"), val) )
            {
              if ( val < mSettings.getMaxBrewSteps() )
              {
                mRecipeEditNr = (int)val;
                mStateBrewRecipe++;
              }
              mWaitInput = false;
            }
            break;
          }
        case 1:
          {
            boolean val = mSettings.getBrewStepOn(mRecipeEditNr);
            if ( getInputboolean (F("bon:"), val) )
            {
              mSettings.setBrewStepOn(mRecipeEditNr, val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        case 2:
          {
            boolean val = mSettings.getBrewStepHalt(mRecipeEditNr);
            if ( getInputboolean (F("bha:"), val) )
            {
              mSettings.setBrewStepHalt(mRecipeEditNr, val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        case 3:
          {
            boolean val = mSettings.getBrewStepCall(mRecipeEditNr);
            if ( getInputboolean (F("bbc:"), val) )
            {
              mSettings.setBrewStepCall(mRecipeEditNr, val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        case 4:
          {
            float val = (float)mSettings.getBrewStepSollTemp(mRecipeEditNr);
            if ( getInputFloat(F("bst:"), val) )
            {
              mSettings.setBrewStepSollTemp(mRecipeEditNr, val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        case 5:
          {
            long val = mSettings.getBrewStepTime(mRecipeEditNr);
            if ( getInputLong(F("btd:"), val) )
            {
              mSettings.setBrewStepTime(mRecipeEditNr, (unsigned long)val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        case 6:
          {
            float val = (float)mSettings.getBrewStepMinTemp(mRecipeEditNr);
            if ( getInputFloat(F("bit:"), val) )
            {
              mSettings.setBrewStepMinTemp(mRecipeEditNr, val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        case 7:
          {
            float val = (float)mSettings.getBrewStepMaxTemp(mRecipeEditNr);
            if ( getInputFloat(F("bat:"), val) )
            {
              mSettings.setBrewStepMaxTemp(mRecipeEditNr, val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        case 8:
          {
            float val = (float)mSettings.getBrewStepKp(mRecipeEditNr);
            if ( getInputFloat(F("bkp:"), val) )
            {
              mSettings.setBrewStepKp(mRecipeEditNr, (double)val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        case 9:
          {
#ifdef PID_REG
            float val = (float)mSettings.getBrewStepKi(mRecipeEditNr);
            if ( getInputFloat(F("bki:"), val) )
            {
              mSettings.setBrewStepKi(mRecipeEditNr, (double)val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
#else
            mStateBrewRecipe++;
#endif
            break;
          }
        case 10:
          {
#ifdef PID_REG
            float val = (float)mSettings.getBrewStepKd(mRecipeEditNr);
            if ( getInputFloat(F("bkd:"), val) )
            {
              mSettings.setBrewStepKd(mRecipeEditNr, (double)val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
#else
            mStateBrewRecipe++;
#endif
            break;
          }
        case 11:
          {
            long val = mSettings.getBrewStepOnTimePuls(mRecipeEditNr);
            if ( getInputLong(F("bpn:"), val) )
            {
              mSettings.setBrewStepOnTimePuls(mRecipeEditNr, (unsigned int)val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        case 12:
          {
            long val = mSettings.getBrewStepOffTimePuls(mRecipeEditNr);
            if ( getInputLong(F("bpf:"), val) )
            {
              mSettings.setBrewStepOffTimePuls(mRecipeEditNr, (unsigned int)val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        case 13:
          {
            float val = (float)mSettings.getMaxGradient(mRecipeEditNr);
            if ( getInputFloat(F("bmg:"), val) )
            {
              mSettings.setMaxGradient(mRecipeEditNr, val);
              mWaitInput = false;
              mStateBrewRecipe++;
            }
            break;
          }
        default:
          {
            //            if ( mSettings.sysSettings.loadStateOn ) {
            mSettings.saveSettings();
            //            }
            mStateTop = 0;
            mStateBrewRecipe = 0;
          }
      }
    }
    void saveState()
    {
      if ( mSettings.getLoadStateOn() ) {
        mTimerSaveState.start();
        if ( mTimerSaveState.timeOver() )
        {
          mTimerSaveState.init();
          mSettings.setStateSet(true);
          if ( mBrewing.isTempReached() )
            mSettings.setActTime(mBrewing.getActTime());
          else
            mSettings.setActTime(
              mSettings.getBrewStepTime(mRecipeEditNr)*MIL2SEC * SECPROMIN);
          mSettings.setActBrewState(mRecipeEditNr);
          mSettings.saveState();
        }
      }
    }
    void printBrew(boolean wait, boolean tune, boolean actTemp)
    {
      if ( tune )
        mInterface.print(F("T"));
      else
        mInterface.print(F("A"));
      dtostrf(millis() - mStartTime, 1, 0, mCharBuf);
      mInterface.print(mCharBuf);

      mInterface.print(F(" N"));
      dtostrf(mRecipeEditNr, 1, 0, mCharBuf);
      mInterface.print(mCharBuf);

      mInterface.print(F(" I"));
      if ( actTemp )
        dtostrf(mTempSensor.getTemperatur(), 1, 2, mCharBuf);
      else
        dtostrf(mTempSensor.getActVal(), 1, 2, mCharBuf);
      mInterface.print(mCharBuf);

      mInterface.print(F(" G"));
      dtostrf(mTempSensor.getGradient(), 2, 2, mCharBuf);
      mInterface.print(mCharBuf);

      if ( 0 < mBrewing.getAlarmType() ) {
        mInterface.print(F(" Q"));
        dtostrf(mBrewing.getAlarmType(), 1, 0, mCharBuf);
        mInterface.print(mCharBuf);
        mBuzzer.on();
      } else {
        mBuzzer.off();
      }

      mInterface.print(F(" S"));
      dtostrf(mSettings.getBrewStepSollTemp(mRecipeEditNr), 1, 2, mCharBuf);
      mInterface.print(mCharBuf);

      mInterface.print(F(" J"));
      dtostrf(mBrewing.getActGradPos(), 1, 0, mCharBuf);
      mInterface.print(mCharBuf);

      mInterface.print(F(" H"));
      if ( mBrewing.isSwitchOn() ) mInterface.print(F("1"));
      else mInterface.print(F("0"));

      mInterface.print(F(" B"));
      dtostrf(mBrewing.getBatLevel(), 1, 2, mCharBuf);
      mInterface.print(mCharBuf);

      if ( mBrewing.isTempReached() )
      {
        mInterface.print(F(" D"));
        dtostrf(mBrewing.getActTime() / MIL2SEC , 1, 0, mCharBuf);
        mInterface.print(mCharBuf);        
      }

      if ( mBrewing.isPause() ) {
          mInterface.print(F(" P"));
      }
      
#ifdef PID_REG
      if ( mSettings.getPid() )
      {
        mInterface.print(F(" O"));
        dtostrf(mBrewing.getOutput(), 1, 2, mCharBuf);
        mInterface.print(mCharBuf);
      }
#endif
      if ( wait )
      {
        mInterface.print(F(" W"));

        if ( mBuzCall )
        {
          mInterface.print(F(" K"));
        }
      }
#ifdef PROFI_COOK
      mInterface.print((" V"));
      dtostrf(((TemperaturSensorNTC&)mTempSensor).getRaw(), 1, 2, mCharBuf);
      mInterface.print(mCharBuf);
#endif
      mInterface.println(F(" "));
    }
    
    void showMenuGo()
    {
      int key = 0;
      switch (mStateGo)
      {
        case 0:
          {
            long val = mRecipeEditNr;
            if ( getInputLong(F("rnr:"), val) )
            {
              if ( val < mSettings.getMaxBrewSteps() )
              {
                mRecipeEditNr = (int)val;
                mBuzCall = mSettings.getBrewStepCall(mRecipeEditNr);
                mFirstEntry = false;
                mFirstEntryRun = false;
                mStateGo++;
              }
              mWaitInput = false;
            }
            break;
          }
        case 1:
          {
            key = mInterface.readByte();
            if ( key == 'r' || LOW == digitalRead(mPinReset))
            {
              mSettings.setStateSet(false);
              mStateGo = 20;
            }
            if ( mRecipeEditNr < mSettings.getMaxBrewSteps() )
            {
              if ( mSettings.getBrewStepOn(mRecipeEditNr) )
              {
                if ( key == 'a' )
                {
                  printBrew(mWaitInput, false, false);
                }
                else if ( key == '4' )
                {
                  mBrewing.pauseResume();
                }
                saveState();
                if ( false == mFirstEntryRun )
                {
                  mFirstEntryRun = true;
                  mBrewing.setActParams(mRecipeEditNr);
                  mBuzCall = mSettings.getBrewStepCall(mRecipeEditNr);
                  mWaitInput = false;
                  mStartTime = millis();
                }
                if ( mBrewing.runBrewing(true) )
                {
                  boolean weiter = true;
                  if ( mSettings.getBrewStepHalt(mRecipeEditNr) )
                  {
                    weiter = false;
                    mWaitInput = true;
                    if ( mBuzCall )
                    {
                      mBuzzer.on();
                    }
                    if ( key == 'w' )
                    {
                      mBuzzer.off();
                      weiter = true;
                      mWaitInput = false;
                    }
                    else if ( key == 'b' )
                    {
                      mBuzzer.off();
                      mBuzCall = false;
                    }
                  }
                  if ( weiter )
                  {
                    mRecipeEditNr++;
                    mFirstEntryRun = false;
                  }
                }
              }
              else
              {
                mRecipeEditNr++;
              }
            }
            else
            {
              mStateGo++;
            }
            break;
          }
        default:
          {
            if ( mSettings.getLoadStateOn() ) {
              mSettings.setStateSet(false);
              mSettings.saveState();
            }
            reset();
          }
      }
    }
#ifdef AUTO_TUNE
    void showMenuTune()
    {

      if ( !mSettings.getPid() )
        mStateTune = 20;

      switch (mStateTune)
      {
        case 0:
          {
            if ( false == mFirstEntry )
            {
              mFirstEntry = true;
              mRecipeEditNr = 0;
            }
            long val = mRecipeEditNr;
            if ( getInputLong(F("tnr:"), val) )
            {
              if ( val < mSettings.getMaxBrewSteps() )
              {
                mRecipeEditNr = (int)val;
                mFirstEntry = false;
                mStartTime = millis();
                mStateTune++;
              }
              mWaitInput = false;
            }
            break;
          }
        case 1:
          {
            int key = mInterface.readByte();
            if ( key == 'r' )
            {
              mStateTune = 20;
            }
            if ( false == mFirstEntry )
            {
              mFirstEntry = true;
              mBrewing.setActParams(mRecipeEditNr);
              mBrewing.initTimerBrew(1);
            }
            if ( mBrewing.runTuning() )
            {
              mStateTune++;
            }
            if ( key == 'a' )
            {
              printBrew(false, true, false);
            }
          }
        default:
          {
            reset();
          }
      }

    }
#endif
    InOutPut& mInterface;
    SystemSettings& mSettings;
    Brew& mBrewing;
    Buzzer& mBuzzer;
    TemperaturSensor& mTempSensor;
    Switcher&         mSwitcher;
    WaitTime mTimerSaveState;
    byte mStateTop;
    byte mStateSetup;
    byte mStateBrewRecipe;
    byte mStateGo;
    byte mStateTune;
    int  mRecipeEditNr;
    boolean mWaitInput;
    boolean mFirstEntry;
    boolean mFirstEntryRun;
    boolean mBuzCall;
    char mCharBuf[20];
    unsigned long mStartTime;
    byte mPinReset;
    byte mPinLed;
};


#endif

