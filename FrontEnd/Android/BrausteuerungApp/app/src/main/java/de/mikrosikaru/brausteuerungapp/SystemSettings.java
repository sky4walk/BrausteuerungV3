// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

import java.util.ArrayList;


/**
 * Created by andre on 18.01.16.
 */
public class SystemSettings {
    private static final String TAG = "SystemSettings";

    private boolean pid = false;
    private boolean pid_orig = false;
    private boolean ntc = false;
    private boolean ntc_orig = false;
    private float ntcT0 = 25.0f;
    private float ntcT0_orig = 25.0f;
    private float kalM  = 1.0f;
    private float kalM_orig  = 1.0f;
    private float ntcR0 = 100000;
    private float ntcR0_orig = 100000;
    private float kalT  = 0.0f;
    private float kalT_orig  = 0.0f;
    private float ntcT1;
    private float ntcT1_orig;
    private float ntcR1;
    private float ntcR1_orig;
    private int  vorR;
    private int  vorR_orig;
    private int pidWindowSize;
    private int pidWindowSize_orig;
    private int pidSampleTime;
    private int pidSampleTime_orig;
    private float pidDelta;
    private float pidDelta_orig;
    private float tuneStep;
    private float tuneStep_orig;
    private float tuneNoise;
    private float tuneNoise_orig;
    private int tuneLookBack;
    private int tuneLookBack_orig;
    private byte switchType;
    private byte switchType_orig;
    private byte switchRepeats;
    private byte switchRepeats_orig;
    private int switchPeriodusec;
    private int switchPeriodusec_orig;
    private int  switchAddress;
    private int  switchAddress_orig;
    private byte  switchBits;
    private byte  switchBits_orig;
    private int switchUnit;
    private int switchUnit_orig;
    private float batLowLevel;
    private float batLowLevel_orig;
    private int passwd;
    private int passwd_orig;
    private byte measureHeatingTime;
    private byte measureHeatingTime_orig;
    private byte overHeating;
    private byte overHeating_orig;

    private int brewSteps = Constants.MAX_BREW_STEPS;
    private int version;
    private int serNr;
    private boolean loadStateOn;

    private ArrayList<SystemBrewStep> mBrewStepsList = new ArrayList<SystemBrewStep>();

    private String appDevice;
    private String appName;
    private String appVersion;

    private String mBrewRecipeDescription = "";

    public final int stateMenu      = 1;
    public final int stateSettings  = 2;
    public final int stateBrewSteps = 3;
    public final int stateRun       = 4;
    public final int stateTune      = 5;


    public SystemSettings() {
		Reset();
	}
	
	public void Reset() {
		pid = false;
        ntc = false;
        ntcT0 = 0;
        kalM  = 1.0f;
        ntcR0 = 0;
        kalT  = 0;
        ntcT1 = 0;
        ntcR1 = 0;
        vorR = 0;
        pidWindowSize = 0;
        pidSampleTime = 0;
        pidDelta = 0;
        tuneStep = 0;
        tuneNoise = 0;
        tuneLookBack = 0;
        switchType = 0;
        switchRepeats = 0;
        switchPeriodusec = 0;
        switchAddress = 0;
        switchUnit = 0;
        batLowLevel = 0;
        passwd = 0;
        switchBits = 0;
        measureHeatingTime = 0;
        overHeating = 0;
        copySettings2Orig();
	}
	
    public String getBrewRecipeDescription() {
        return mBrewRecipeDescription;
    }

    public void setBrewRecipeDescription(String BrewRecipeDescription) {
        this.mBrewRecipeDescription = BrewRecipeDescription;
    }

    public void copySettings2Orig() {
        pid_orig = pid;
        ntc_orig = ntc;
        ntcT0_orig = ntcT0;
        kalM_orig  = kalM;
        ntcR0_orig = ntcR0;
        kalT_orig  = kalT;
        ntcT1_orig = ntcT1;
        ntcR1_orig = ntcR1;
        vorR_orig = vorR;
        pidWindowSize_orig = pidWindowSize;
        pidSampleTime_orig = pidSampleTime;
        pidDelta_orig = pidDelta;
        tuneStep_orig = tuneStep;
        tuneNoise_orig = tuneNoise;
        tuneLookBack_orig = tuneLookBack;
        switchType_orig = switchType;
        switchRepeats_orig = switchRepeats;
        switchPeriodusec_orig = switchPeriodusec;
        switchAddress_orig = switchAddress;
        switchUnit_orig = switchUnit;
        batLowLevel_orig = batLowLevel;
        passwd_orig = passwd;
        switchBits_orig = switchBits;
        measureHeatingTime_orig = measureHeatingTime;
        overHeating_orig = overHeating;
    }
    public void copyOrig2Settings() {
        pid = pid_orig;
        ntc = ntc_orig;
        ntcT0 = ntcT0_orig;
        kalM  = kalM_orig;
        ntcR0 = ntcR0_orig;
        kalT  = kalT_orig;
        ntcT1 = ntcT1_orig;
        ntcR1 = ntcR1_orig;
        vorR = vorR_orig;
        pidWindowSize = pidWindowSize_orig;
        pidSampleTime = pidSampleTime_orig;
        pidDelta = pidDelta_orig;
        tuneStep = tuneStep_orig;
        tuneNoise = tuneNoise_orig;
        tuneLookBack = tuneLookBack_orig;
        switchType = switchType_orig;
        switchRepeats = switchRepeats_orig;
        switchPeriodusec = switchPeriodusec_orig;
        switchAddress = switchAddress_orig;
        switchUnit = switchUnit_orig;
        batLowLevel = batLowLevel_orig;
        passwd = passwd_orig;
        switchBits = switchBits_orig;
        measureHeatingTime = measureHeatingTime_orig;
        overHeating = overHeating_orig;
    }
    public String getAppDevice() {
        return appDevice;
    }

    public void setAppDevice(String appDevice) {
        this.appDevice = appDevice;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public byte getSwitchType() {
        return switchType;
    }

    public void setSwitchType(byte switchType) {
        this.switchType = switchType;
    }

    public float getNtcT0() {
        return ntcT0;
    }

    public void setNtcT0(float ntcT0) {
        this.ntcT0 = ntcT0;
    }

    public boolean isPid() {
        return pid;
    }

    public void setPid(boolean pid) {
        this.pid = pid;
    }

    public boolean isNtc() {
        return ntc;
    }

    public void setNtc(boolean ntc) {
        this.ntc = ntc;
    }

    public float getKalM() {
        return kalM;
    }

    public void setKalM(float kalM) {
        this.kalM = kalM;
    }

    public float getNtcR0() {
        return ntcR0;
    }

    public void setNtcR0(float ntcR0) {
        this.ntcR0 = ntcR0;
    }

    public float getKalT() {
        return kalT;
    }

    public void setKalT(float kalT) {
        this.kalT = kalT;
    }

    public float getNtcT1() {
        return ntcT1;
    }

    public void setNtcT1(float ntcT1) {
        this.ntcT1 = ntcT1;
    }

    public float getNtcR1() {
        return ntcR1;
    }

    public void setNtcR1(float ntcR1) {
        this.ntcR1 = ntcR1;
    }

    public int getVorR() {
        return vorR;
    }

    public void setVorR(int vorR) {
        this.vorR = vorR;
    }

    public int getPidWindowSize() {
        return pidWindowSize;
    }

    public void setPidWindowSize(int pidWindowSize) {
        this.pidWindowSize = pidWindowSize;
    }

    public int getPidSampleTime() {
        return pidSampleTime;
    }

    public void setPidSampleTime(int pidSampleTime) {
        this.pidSampleTime = pidSampleTime;
    }

    public float getPidDelta() {
        return pidDelta;
    }

    public void setPidDelta(float pidDelta) {
        this.pidDelta = pidDelta;
    }

    public float getTuneStep() {
        return tuneStep;
    }

    public void setTuneStep(float tuneStep) {
        this.tuneStep = tuneStep;
    }

    public float getTuneNoise() {
        return tuneNoise;
    }

    public void setTuneNoise(float tuneNoise) {
        this.tuneNoise = tuneNoise;
    }

    public int getTuneLookBack() {
        return tuneLookBack;
    }

    public void setTuneLookBack(int tuneLookBack) {
        this.tuneLookBack = tuneLookBack;
    }

    public byte getSwitchBits() {
        return switchBits;
    }

    public void setSwitchBits(Byte switchBits) {
        this.switchBits = switchBits;
    }

    public byte getSwitchRepeats() {
        return switchRepeats;
    }

    public void setSwitchRepeats(byte switchRepeats) {
        this.switchRepeats = switchRepeats;
    }

    public int getSwitchPeriodusec() {
        return switchPeriodusec;
    }

    public void setSwitchPeriodusec(int switchPeriodusec) {
        this.switchPeriodusec = switchPeriodusec;
    }

    public int getSwitchAddress() {
        return switchAddress;
    }

    public void setSwitchAddress(int switchAddress) {
        this.switchAddress = switchAddress;
    }

    public int getSwitchUnit() {
        return switchUnit;
    }

    public void setSwitchUnit(int switchUnit) {
        this.switchUnit = switchUnit;
    }

    public float getBatLowLevel() {
        return batLowLevel;
    }

    public void setBatLowLevel(float batLowLevel) {
        this.batLowLevel = batLowLevel;
    }

    public int getPasswd() {
        return passwd;
    }

    public void setPasswd(int passwd) {
        this.passwd = passwd;
    }

    public int getVersion() {
        return this.version;
    }

    public int getSerialNr() {
        return this.serNr;
    }

    public boolean isLoadStateOn() {
        return loadStateOn;
    }

    public void setLoadStateOn(boolean loadStateOn) {
        this.loadStateOn = loadStateOn;
    }

    public int getBrewSteps() {
        return this.brewSteps;
    }
    public int getStoredBrewSteps() {
        return this.mBrewStepsList.size();
    }
    public byte getMeasureHeatingTime() {
		return this.measureHeatingTime;
	}
    public void setMeasureHeatingTime(byte MeasureHeatingTime) {
		this.measureHeatingTime = MeasureHeatingTime;
	}
	public byte getOverHeating() {
		return this.overHeating;
	}	
	public void setOverHeating(byte OverHeating) {
		this.overHeating = OverHeating;
	}	
    public void clearBrewSteps() {
        this.mBrewStepsList.clear();
    }
    public void createBrewSteps(String recipName) {
        clearBrewSteps();
        for ( int i = 0; i < getBrewSteps(); i++ ) {
            SystemBrewStep brewStep = new SystemBrewStep();
            brewStep.setNr(i);
            brewStep.setName(recipName+Integer.toString(i+1));
            this.mBrewStepsList.add(brewStep);
        }
    }
    public SystemBrewStep getBrewStep(int nr) {
        if (this.mBrewStepsList.size() > nr)
            if (nr >= 0) {
                return this.mBrewStepsList.get(nr);
            }
        return null;
    }
    public boolean setMenu(String value) {
        String[] splitVal = value.split(":");
        if (2 == splitVal.length ) {
            if ( splitVal[0].length() < 2 ) return false;
            String versionStr = splitVal[1].substring(0, splitVal[1].length()-2);
            String infoStr = splitVal[0].substring(0, 2);
            String brewStepsStr = splitVal[0].substring(2);

            if ( 'm' != infoStr.charAt(0) ) return false;
            if ( 'a' == infoStr.charAt(1) ) ntc = true;
            else ntc = false;

            try {
                version = Integer.parseInt(versionStr);
                brewSteps = Integer.parseInt(brewStepsStr);
            } catch (NumberFormatException e) {
                version = 0;
                brewSteps = 0;
                return false;
            }

        } else {
            return false;
        }
        return true;
    }
    public int getStateFromMessage(String message) {
        if (message.length() > 0) {
            char c = message.charAt(0);
            if ('m' == c) return stateMenu;
            else if ('s' == c) return stateSettings;
            else if ('b' == c) return stateBrewSteps;
            else if ('A' == c) return stateRun;
            else if ('T' == c) return stateTune;
            else return 0;
        } else {
            return 0;
        }
    }
    public String getValueBrewStep(int i, String message) {
        String valueStr = "";
        String[] values = message.split(":");
        String menuStr = values[0];
        SystemBrewStep brewStep = getBrewStep(i);
        if ( null == brewStep ) return valueStr;
        if (menuStr.compareToIgnoreCase("bon") == 0) {
            if ( brewStep.isOn() )  valueStr = "1";
            else                valueStr = "0";
        } else if (menuStr.compareToIgnoreCase("bha") == 0) {
            if ( brewStep.isHalt() )  valueStr = "1";
            else                  valueStr = "0";
        } else if (menuStr.compareToIgnoreCase("bbc") == 0) {
            if ( brewStep.isCall() )  valueStr = "1";
            else                  valueStr = "0";
        } else if (menuStr.compareToIgnoreCase("bst") == 0) {
            valueStr = Float.toString(brewStep.getSollTemp());
        } else if (menuStr.compareToIgnoreCase("btd") == 0) {
            valueStr = Integer.toString(brewStep.getTime());
        } else if (menuStr.compareToIgnoreCase("bit") == 0) {
            valueStr = Float.toString(brewStep.getMinTemp());
        } else if (menuStr.compareToIgnoreCase("bat") == 0) {
            valueStr = Float.toString(brewStep.getMaxTemp());
        } else if (menuStr.compareToIgnoreCase("bkp") == 0) {
            valueStr = Float.toString(brewStep.getKp());
        } else if (menuStr.compareToIgnoreCase("bki") == 0) {
            valueStr = Float.toString(brewStep.getKi());
        } else if (menuStr.compareToIgnoreCase("bkd") == 0) {
            valueStr = Float.toString(brewStep.getKd());
        } else if (menuStr.compareToIgnoreCase("bpn") == 0) {
            valueStr = Integer.toString(brewStep.getOnTimePuls());
        } else if (menuStr.compareToIgnoreCase("bpf") == 0) {
            valueStr = Integer.toString(brewStep.getOffTimePuls());
        } else if (menuStr.compareToIgnoreCase("bmg") == 0) {
            valueStr = Float.toString(brewStep.getMaxGradient());
        } else {
        }
        return valueStr;
    }
    public boolean setValueBrewStep(int i, String value) {
        SystemBrewStep brewStep = getBrewStep(i);
        if ( null == brewStep ) return false;
        
        String[] splitVal = value.split(":");
        if (splitVal.length == 2) {
            try {
                if (splitVal[0].compareToIgnoreCase("bon") == 0) {
                    if (splitVal[1].compareTo("0") == 0) {
                        brewStep.setOn(false);
                    } else {
                        brewStep.setOn(true);
                    }
                } else if (splitVal[0].compareToIgnoreCase("bha") == 0) {
                    if (splitVal[1].compareTo("0") == 0) {
                        brewStep.setHalt(false);
                    } else {
                        brewStep.setHalt(true);
                    }
                } else if (splitVal[0].compareToIgnoreCase("bbc") == 0) {
                    if (splitVal[1].compareTo("0") == 0) {
                        brewStep.setCall(false);
                    } else {
                        brewStep.setCall(true);
                    }
                } else if (splitVal[0].compareToIgnoreCase("bst") == 0) {
                    brewStep.setSollTemp(Float.parseFloat(splitVal[1]));
                } else if (splitVal[0].compareToIgnoreCase("btd") == 0) {
                    brewStep.setTime(Integer.parseInt(splitVal[1]));
                } else if (splitVal[0].compareToIgnoreCase("bit") == 0) {
                    brewStep.setMinTemp(Float.parseFloat(splitVal[1]));
                } else if (splitVal[0].compareToIgnoreCase("bat") == 0) {
                    brewStep.setMaxTemp(Float.parseFloat(splitVal[1]));
                } else if (splitVal[0].compareToIgnoreCase("bkp") == 0) {
                    brewStep.setKp(Float.parseFloat(splitVal[1]));
                } else if (splitVal[0].compareToIgnoreCase("bki") == 0) {
                    brewStep.setKi(Float.parseFloat(splitVal[1]));
                } else if (splitVal[0].compareToIgnoreCase("bkd") == 0) {
                    brewStep.setKd(Float.parseFloat(splitVal[1]));
                } else if (splitVal[0].compareToIgnoreCase("bpn") == 0) {
                    brewStep.setOnTimePuls(Integer.parseInt(splitVal[1]));
                } else if (splitVal[0].compareToIgnoreCase("bpf") == 0) {
                    brewStep.setOffTimePuls(Integer.parseInt(splitVal[1]));
                } else if (splitVal[0].compareToIgnoreCase("bmg") == 0) {
                    brewStep.setMaxGradient(Float.parseFloat(splitVal[1]));
                } else {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean isMenuBnr(String message) {
        String[] splitVal = message.split(":");
        if (splitVal.length == 2) {
            if (splitVal[0].compareToIgnoreCase("bnr") == 0) {
                return true;
            }
        }
        return false;
    }
    public boolean isMenuRnr(String message) {
        String[] splitVal = message.split(":");
        if (splitVal.length == 2) {
            if (splitVal[0].compareToIgnoreCase("rnr") == 0) {
                return true;
            }
        }
        return false;
    }
    public boolean isMenuTnr(String message) {
        String[] splitVal = message.split(":");
        if (splitVal.length == 2) {
            if (splitVal[0].compareToIgnoreCase("tnr") == 0) {
                return true;
            }
        }
        return false;
    }
    public String getValueSettings(String value) {
        String valueStr = "";
        String[] values = value.split(":");
        String menuStr = values[0];
        if ( menuStr.compareToIgnoreCase("spi") == 0 ) {
            if ( pid )  valueStr = "1";
            else        valueStr = "0";                
        } else if (menuStr.compareToIgnoreCase("stz") == 0) {
            valueStr = Float.toString(ntcT0);
        } else if (menuStr.compareToIgnoreCase("skm") == 0) {
            valueStr = Float.toString(kalM);
        } else if (menuStr.compareToIgnoreCase("srz") == 0) {
            valueStr = Float.toString(ntcR0);
        } else if (menuStr.compareToIgnoreCase("skt") == 0) {
            valueStr = Float.toString(kalT);
        } else if (menuStr.compareToIgnoreCase("sto") == 0) {
            valueStr = Float.toString(ntcT1);
        } else if (menuStr.compareToIgnoreCase("sro") == 0) {
            valueStr = Float.toString(ntcR1);
        } else if (menuStr.compareToIgnoreCase("svw") == 0) {
            valueStr = Integer.toString(vorR);
        } else if (menuStr.compareToIgnoreCase("sps") == 0) {
            valueStr = Integer.toString(pidWindowSize);
        } else if (menuStr.compareToIgnoreCase("spt") == 0) {
            valueStr = Integer.toString(pidSampleTime);
        } else if (menuStr.compareToIgnoreCase("spd") == 0) {
            valueStr = Float.toString(pidDelta);
        } else if (menuStr.compareToIgnoreCase("sts") == 0) {
            valueStr = Float.toString(tuneStep);
        } else if (menuStr.compareToIgnoreCase("stn") == 0) {
            valueStr = Float.toString(tuneNoise);
        } else if (menuStr.compareToIgnoreCase("slb") == 0) {
            valueStr = Integer.toString(tuneLookBack);
        } else if (menuStr.compareToIgnoreCase("ssr") == 0) {
            valueStr = Byte.toString(switchRepeats);
        } else if (menuStr.compareToIgnoreCase("sst") == 0) {
                valueStr = Byte.toString(switchType);
        } else if (menuStr.compareToIgnoreCase("ssp") == 0) {
            valueStr = Integer.toString(switchPeriodusec);
        } else if (menuStr.compareToIgnoreCase("ssb") == 0) {
            valueStr = Byte.toString(switchBits);
        } else if (menuStr.compareToIgnoreCase("ssa") == 0) {
            valueStr = Integer.toString(switchAddress);
        } else if (menuStr.compareToIgnoreCase("ssu") == 0) {
            valueStr = Integer.toString(switchUnit);
        } else if (menuStr.compareToIgnoreCase("sbl") == 0) {
            valueStr = Float.toString(batLowLevel);
        } else if (menuStr.compareToIgnoreCase("spw") == 0) {
            valueStr = Integer.toString(passwd);
        } else if (menuStr.compareToIgnoreCase("snr") == 0) {
            valueStr = Integer.toString(serNr);
        } else if (menuStr.compareToIgnoreCase("sls") == 0) {
            if ( loadStateOn )  valueStr = "1";
            else                valueStr = "0";
        } else if (menuStr.compareToIgnoreCase("sht") == 0) {
            valueStr = Byte.toString(measureHeatingTime);
        } else if (menuStr.compareToIgnoreCase("soh") == 0) {
            valueStr = Byte.toString(overHeating);
        } else {
        }
        return valueStr;
    }

    public boolean setValueSettings(String value) {
        String[] splitVal = value.split(":");
        if (splitVal.length == 2) {
            String numberStr = splitVal[1].substring(0, splitVal[1].length()-2);
            try {
                if (splitVal[0].compareToIgnoreCase("spi") == 0) {
                    if (numberStr.compareTo("0") == 0) {
                        pid = false;
                    } else {
                        pid = true;
                    }
                } else if (splitVal[0].compareToIgnoreCase("stz") == 0) {
                    ntcT0 = Float.parseFloat(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("skm") == 0) {
                    kalM = Float.parseFloat(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("srz") == 0) {
                    ntcR0 = Float.parseFloat(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("skt") == 0) {
                    kalT = Float.parseFloat(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("sto") == 0) {
                    ntcT1 = Float.parseFloat(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("sro") == 0) {
                    ntcR1 = Float.parseFloat(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("svw") == 0) {
                    vorR = Integer.parseInt(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("sps") == 0) {
                    pidWindowSize = Integer.parseInt(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("spt") == 0) {
                    pidSampleTime = Integer.parseInt(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("spd") == 0) {
                    pidDelta = Float.parseFloat(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("sts") == 0) {
                    tuneStep = Float.parseFloat(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("stn") == 0) {
                    tuneNoise = Float.parseFloat(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("slb") == 0) {
                    tuneLookBack = Integer.parseInt(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("sst") == 0) {
                    switchType = Byte.parseByte(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("ssr") == 0) {
                    switchRepeats = Byte.parseByte(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("ssp") == 0) {
                    switchPeriodusec = Integer.parseInt(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("ssb") == 0) {
                    switchBits = Byte.parseByte(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("ssa") == 0) {
                    switchAddress = Integer.parseInt(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("ssu") == 0) {
                    switchUnit = Integer.parseInt(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("sbl") == 0) {
                    batLowLevel = Float.parseFloat(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("spw") == 0) {
                    passwd = Integer.parseInt(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("snr") == 0) {
                    serNr = Integer.parseInt(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("sls") == 0) {
                    loadStateOn = Boolean.parseBoolean(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("sht") == 0) {
                    measureHeatingTime = Byte.parseByte(numberStr);
                } else if (splitVal[0].compareToIgnoreCase("soh") == 0) {
                    overHeating = Byte.parseByte(numberStr);
                } else {
                    return false;
                }
            }catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }





}
