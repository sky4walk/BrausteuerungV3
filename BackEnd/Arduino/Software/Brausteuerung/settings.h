// brausteuerung@AndreBetz.de
#ifndef __SETTINGS__
#define __SETTINGS__

#include <Arduino.h>
///////////////////////////////////////////////////////////////////////////////
// Settings
///////////////////////////////////////////////////////////////////////////////
#define MAXBREWSTEPS 16

///////////////////////////////////////////////////////////////////////////////

class SystemSettings
{
  public:
    SystemSettings(Storage& store, unsigned long header) :
      mStore(store),
      mHeader(header),
      mNewSystem(true)
    {
    }
    void init()
    {
      loadSettings();
      if ( mHeader != sysSettings.header )
      {
        resetData();

        sysSettings.header = mHeader;
        sysState.writeBT = true;

        setPassWd(1101);
        setBatLowLevel(7.0);
#ifdef SWITCH_SETTINGS_ARENDO
        setSwitchType(3);          // protocol 1
        setSwitchBits(24);   // 24 Bit
        setSwitchPeriodusec(0);
        setSwitchAddress(12501119); // on
        setSwitchUnit(12501118);    // off
        setSwitchRepeats(7);
#endif
#ifdef SWITCH_SETTINGS_HE263
        setSwitchType(0);
        setSwitchAddress(1859584);
        setSwitchUnit(1);
        setSwitchPeriodusec(263);	// Home Easy braucht 263
        setSwitchRepeats(7); 		// Home Easy braucht 1 Repeats
#endif
        
        setKalM(1.01);
        setMaxOverHeat(10);
        setMeasureHeatingTime(0);

#ifdef PID_REG
        setPid(false);
        setPidWindowSize(5000.0);
        setPidSampleTime(100.0);
        setPidDelta(10.0);
#endif
#ifdef AUTO_TUNE
        setTuneStep(300);
        setTuneNoise(1);
        setTuneLookBack(60000);
#endif

#ifdef PROFI_COOK
        sysSettings.ntcT0 = 21.0;
        sysSettings.ntcR0 = 311;
        sysSettings.ntcT1 = 48.0;
        sysSettings.ntcR1 = 591;
        sysSettings.vorR  = 10000;
        setBatLowLevel(0);
#endif

        for ( int i = 0; i < MAXBREWSTEPS; i++)
        {
#ifdef PID_REG
          setBrewStepKp(i, 0.0);
          setBrewStepKi(i, 0.0);
          setBrewStepKd(i, 1.0);
#endif          
          setBrewStepMinTemp(i, -0.1);
          setBrewStepMaxTemp(i, 0.1);

        }

        saveSettings();
        saveState();
        mNewSystem = true;
      }
      else
      {
        mNewSystem = false;
        loadState();
      }
    }
    void resetData()
    {
      memset((char*)&sysSettings, 0, sizeof(sysSettings));
      for ( int i = 0; i < MAXBREWSTEPS; i++ )
      {
        memset((char*) & (brewSteps[i]), 0, sizeof(BrewStep));
      }
      memset((char*)&sysState, 0, sizeof(sysState));
      saveSettings();
      saveState();
    }
    int getMaxBrewSteps()
    {
      return MAXBREWSTEPS;
    }
    boolean getPid() {
      return sysSettings.pid;
    }
    void setPid(boolean pid) {
      sysSettings.pid = pid;
    }
#ifdef PROFI_COOK     
    boolean getNtc() {
      return sysSettings.ntc;
    }
    void setNtc(boolean ntc) {
      sysSettings.ntc = ntc;
    }

    float getNtcT0() {
      return sysSettings.ntcT0;
    }
    void setNtcT0(float val) {
      sysSettings.ntcT0 = val;
    }
    float getNtcR0() {
      return sysSettings.ntcR0;
    }
    void setNtcR0(float val) {
      sysSettings.ntcR0 = val;
    }
    float getNtcT1() {
      return sysSettings.ntcT1;
    }
    void setNtcT1(float val) {
      sysSettings.ntcT1 = val;
    }

    float getNtcR1() {
      return sysSettings.ntcR1;
    }
    void setNtcR1(float val) {
      sysSettings.ntcR1 = val;
    }

    unsigned long  getVorR() {
      return sysSettings.vorR;
    }
    void  setVorR(unsigned long val) {
      sysSettings.vorR = val;
    }
#endif
    float getKalM() {
      return sysSettings.kalM;
    }
    void setKalM(float kalM) {
      sysSettings.kalM = kalM;
    }

    float getKalT() {
      return sysSettings.kalT;
    }
    void setKalT(float kalT) {
      sysSettings.kalT = kalT;
    }
    
    unsigned int getSerNr() {
      return sysSettings.serNr;
    }
    void setSerNr(unsigned int val) {
      sysSettings.serNr = val;
    }

    float getPidWindowSize() {
      return sysSettings.pidWindowSize;
    }
    void setPidWindowSize(float pidWindowSize) {
      sysSettings.pidWindowSize = pidWindowSize;
    }

    unsigned int getPidSampleTime() {
      return sysSettings.pidSampleTime;
    }
    void setPidSampleTime(unsigned int pidSampleTime) {
      sysSettings.pidSampleTime = pidSampleTime;
    }

    float getPidDelta() {
      return sysSettings.pidDelta;
    }
    void setPidDelta(float pidDelta) {
      sysSettings.pidDelta = pidDelta;
    }
#ifdef AUTO_TUNE
    double getTuneStep() {
      return sysSettings.tuneStep;
    }
    void setTuneStep(double tuneStep) {
      sysSettings.tuneStep = tuneStep;
    }

    double getTuneNoise() {
      return sysSettings.tuneNoise;
    }
    void setTuneNoise(double tuneNoise) {
      sysSettings.tuneNoise = tuneNoise;
    }

    unsigned int getTuneLookBack() {
      return sysSettings.tuneLookBack;
    }
    void setTuneLookBack(unsigned int tuneLookBack) {
      sysSettings.tuneLookBack = tuneLookBack;
    }
#endif
    byte getSwitchType() {
      return sysSettings.switchType;
    }
    void setSwitchType(byte switchType) {
      sysSettings.switchType = switchType;
    }
    byte getSwitchRepeats() {
      return sysSettings.switchRepeats;
    }
    void setSwitchRepeats(byte switchRepeats) {
      sysSettings.switchRepeats = switchRepeats;
    }

    unsigned int getSwitchPeriodusec() {
      return sysSettings.switchPeriodusec;
    }
    void setSwitchPeriodusec(unsigned int switchPeriodusec) {
      sysSettings.switchPeriodusec = switchPeriodusec;
    }

	byte getSwitchBits() {
      return sysSettings.switchBits;
    }
    void setSwitchBits(byte switchBits) {
      sysSettings.switchBits = switchBits;
    }
	
    unsigned long  getSwitchAddress() {
      return sysSettings.switchAddress;
    }
    void setSwitchAddress(unsigned long switchAddress) {
      sysSettings.switchAddress = switchAddress;
    }

    unsigned long getSwitchUnit() {
      return sysSettings.switchUnit;
    }
    void setSwitchUnit(unsigned long switchUnit) {
      sysSettings.switchUnit = switchUnit;
    }

    float getBatLowLevel() {
      return sysSettings.batLowLevel;
    }
    void setBatLowLevel(float batLowLevel) {
      sysSettings.batLowLevel = batLowLevel;
    }

    unsigned int getPassWd() {
      return sysSettings.passwd;
    }
    void setPassWd(unsigned int passwd) {
      sysSettings.passwd = passwd;
    }

    byte getMaxOverHeat() {
      return sysSettings.maxOverHeat;
    }
    void setMaxOverHeat(byte maxOverHeat) {
      sysSettings.maxOverHeat = maxOverHeat;
    }

    byte getMeasureHeatingTime() {
      return sysSettings.measureHeatingTime;
    }
    void setMeasureHeatingTime(byte measureHeatingTime) {
      sysSettings.measureHeatingTime = measureHeatingTime;
    }

    boolean getBrewStepOn(int i) {
      return brewSteps[i].on;
    }
    void setBrewStepOn(int i, boolean on) {
      brewSteps[i].on = on;
    }

    boolean getBrewStepHalt(int i) {
      return brewSteps[i].halt;
    }
    void setBrewStepHalt(int i, boolean halt) {
      brewSteps[i].halt = halt;
    }

    boolean getBrewStepCall(int i) {
      return brewSteps[i].call;
    }
    void setBrewStepCall(int i, boolean call) {
      brewSteps[i].call = call;
    }

    float getBrewStepSollTemp(int i) {
      return brewSteps[i].sollTemp;
    }
    void setBrewStepSollTemp(int i, float sollTemp) {
      brewSteps[i].sollTemp = sollTemp;
    }

    unsigned long getBrewStepTime(int i) {
      return brewSteps[i].time;
    }
    void setBrewStepTime(int i, unsigned long time) {
      brewSteps[i].time = time;
    }

    float getBrewStepMinTemp(int i) {
      return brewSteps[i].minTemp;
    }
    void setBrewStepMinTemp(int i, float minTemp) {
      brewSteps[i].minTemp = minTemp;
    }

    float getBrewStepMaxTemp(int i) {
      return brewSteps[i].maxTemp;
    }
    void setBrewStepMaxTemp(int i, float maxTemp) {
      brewSteps[i].maxTemp = maxTemp;
    }

    double getBrewStepKp(int i) {
      return brewSteps[i].kp;
    }
    void setBrewStepKp(int i, double kp) {
      brewSteps[i].kp = kp;
    }
#ifdef PID_REG
    double getBrewStepKi(int i) {
      return brewSteps[i].ki;
    }
    void setBrewStepKi(int i, double ki) {
      brewSteps[i].ki = ki;
    }

    double getBrewStepKd(int i) {
      return brewSteps[i].kd;
    }
    void setBrewStepKd(int i, double kd) {
      brewSteps[i].kd = kd;
    }
#endif
    float getMaxGradient(int i) {
      return brewSteps[i].maxGradient;
    }
    void setMaxGradient(int i, float maxGradient) {
      brewSteps[i].maxGradient = maxGradient;
    }

    unsigned int getBrewStepOnTimePuls(int i) {
      return brewSteps[i].onTimePuls;
    }
    void setBrewStepOnTimePuls(int i, unsigned int onTimePuls) {
      brewSteps[i].onTimePuls = onTimePuls;
    }
    unsigned int getBrewStepOffTimePuls(int i) {
      return brewSteps[i].offTimePuls;
    }
    void setBrewStepOffTimePuls(int i, unsigned int OffTimePuls) {
      brewSteps[i].offTimePuls = OffTimePuls;
    }

    unsigned int getActBrewState() {
      return sysState.actState;
    }
    void setActBrewState(unsigned int actState) {
      sysState.actState = actState;
    }

    unsigned long getActTime() {
      return sysState.actTime;
    }
    void setActTime(unsigned long actTime) {
      sysState.actTime = actTime;
    }

    boolean isStateSet() {
      return sysState.set;
    }
    void setStateSet(boolean set) {
      sysState.set = set;
    }

    unsigned long getVersion() {
      return sysSettings.header;
    }

    boolean getWriteBT() {
      return sysState.writeBT;
    }
    void setWriteBT(boolean val) {
      sysState.writeBT = val;
    }

    boolean getLoadStateOn() {
      return sysSettings.loadStateOn;
    }
    void setLoadStateOn(boolean val) {
      sysSettings.loadStateOn = val;
    }

    void loadSettings()
    {
      mStore.load(0, sizeof(sysSettings), (char*)&sysSettings);
      for ( int i = 0; i < MAXBREWSTEPS; i++ )
      {
        mStore.load(sizeof(sysSettings) + sizeof(BrewStep)*i ,
                    sizeof(BrewStep), (char*) & (brewSteps[i]));
      }
    }
    void saveSettings()
    {
      mStore.save(0, sizeof(sysSettings), (char*)&sysSettings);
      for ( int i = 0; i < MAXBREWSTEPS; i++ )
      {
        mStore.save(sizeof(sysSettings) + sizeof(BrewStep)*i ,
                    sizeof(BrewStep), (char*) & (brewSteps[i]));
      }
    }
    void loadState()
    {
      mStore.load(sizeof(sysSettings) + sizeof(BrewStep)*MAXBREWSTEPS,
                  sizeof(sysState), (char*)&sysState);
    }
    void saveState()
    {
      mStore.save(sizeof(sysSettings) + sizeof(BrewStep)*MAXBREWSTEPS,
                  sizeof(sysState), (char*)&sysState);
    }
  private:
    typedef struct
    {
      boolean  call;
      boolean  halt;
      boolean  on;
      float sollTemp;
      unsigned long  time;
      float minTemp;
      float maxTemp;
      double kp;
#ifdef PID_REG      
      double ki;
      double kd;
#endif
      unsigned int onTimePuls;
      unsigned int offTimePuls;
      float maxGradient;
    } BrewStep;
    //public:
    struct
    {
      unsigned long header;
      boolean pid;
      boolean ntc;
      float kalM;
      float kalT;
#ifdef PROFI_COOK      
      float ntcT0;   // Nenntemperatur des NTC-Widerstands in °C
      float ntcR0;   // Nennwiderstand des NTC-Sensors in Ohm
      float ntcT1;   // erhöhte Temperatur des NTC-Widerstands in °C
      float ntcR1;   // Widerstand des NTC-Sensors bei erhöhter Temperatur in Ohm
      unsigned long  vorR; // vorwiderstand
#endif      
      unsigned int pidWindowSize;
      unsigned int pidSampleTime;
      float pidDelta;
#ifdef AUTO_TUNE
      double tuneStep;
      double tuneNoise;
      unsigned int tuneLookBack;
#endif
      byte switchType;
      byte switchRepeats;
      unsigned int switchPeriodusec;
	  byte switchBits;
      unsigned long switchAddress;
      unsigned long switchUnit;
      float batLowLevel;
      unsigned int passwd;
      unsigned int serNr;
      boolean loadStateOn;
      byte maxOverHeat;
      byte measureHeatingTime;
    } sysSettings;
  private:
    struct
    {
      boolean writeBT;
      boolean set;
      unsigned int actState;
      unsigned long actTime;
    } sysState;
    Storage& mStore;
    unsigned long mHeader;
    boolean mNewSystem;
    BrewStep brewSteps[MAXBREWSTEPS];
};

///////////////////////////////////////////////////////////////////////////////

#endif

