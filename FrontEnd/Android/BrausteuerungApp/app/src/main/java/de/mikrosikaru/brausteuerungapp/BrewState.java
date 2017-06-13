// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by andre on 04.03.16.
 */
public class BrewState {
    private static final String TAG = "BrewState";
    private int time;
    private  int nr;
    private float actTemp;
    private float gradient;
    private float sollTemp;
    private int gradientPos;
    private boolean heat;
    private boolean onOff;
    private float batLevel;
    private boolean batLow;
    private int timeRest;
    private boolean activeRest;
    private float outPut;
    private float ntcVolt;
    private boolean activeOut;
    private boolean weiter;
    private boolean call;
    private boolean ntcOut;
    private int alarmNr;
    private String rastName = "";
    private String versionLog = "";
    private String mCsvSeperator = ";";

    BrewState() {
        reset();
    }

    public void reset() {
        time = 0;
        nr = 0;
        actTemp = 0.0f;
        sollTemp = 0.0f;
        gradientPos = 0;
        heat = false;
        onOff = false;
        batLevel = 0.0f;
        batLow = false;
        timeRest = 0;
        activeRest = false;
        outPut = 0;
        activeOut = false;
        weiter = false;
        call = false;
        ntcVolt = 0.0f;
        ntcOut = false;
        rastName = "";
        alarmNr = 0;
    }

    public synchronized String getRastName() {
        return rastName;
    }

    public synchronized void setRastName(String rastName) {
        this.rastName = rastName;
    }

    public synchronized int getTime() {
        return time;
    }

    public synchronized int getNr() {
        return nr;
    }

    public synchronized float getActTemp() {
        return actTemp;
    }

    public synchronized float getSollTemp() {
        return sollTemp;
    }

    public synchronized int getGradPos() { return gradientPos; }

    public synchronized boolean isHeat() {
        return heat;
    }

    public synchronized boolean isOnOff() {
        return onOff;
    }

    public synchronized float getBatLevel() {
        return batLevel;
    }

    public synchronized boolean isBatLow() {
        return batLow;
    }

    public synchronized int getTimeRest() {
        return timeRest;
    }
	
	public synchronized int getAlarmNr() {
		return this.alarmNr;
	}
	
    public synchronized String getTimeRestMinSek() {
        int restTime = getTimeRest();
        int minuten = restTime / 60;
        int sekunden = restTime % 60;
        DecimalFormat df =   new DecimalFormat( "00" );
        String minSek = Integer.toString(minuten) + ":" + df.format(new Integer(sekunden));
        return minSek;
    }

    public synchronized boolean isActiveRest() {
        return activeRest;
    }

    public synchronized float getOutPut() {
        return outPut;
    }

    public synchronized boolean isActiveOut() {
        return activeOut;
    }

    public synchronized boolean isWeiter() {
        return weiter;
    }

    public synchronized boolean isCall() {
        return call;
    }

    public synchronized float getNtcVolt() {
        return ntcVolt;
    }

    public synchronized boolean isNtcOut() {
        return ntcOut;
    }

    public void setVersionString(String version) {
	    versionLog = version;
    }

    public synchronized String getVersionString() {
	return versionLog;
    }

    public synchronized float getGradient() {
        return gradient;
    }

    public synchronized boolean appendState( String filepath,
                                String time) {
        String stateStr = time;
        stateStr += mCsvSeperator + getStateString();

        boolean fileExists = SimpleFileDialog.file_exists(filepath);

        FileOutputStream fos;
        File tempFile = new File(filepath);

        try {
            fos = new FileOutputStream(tempFile, true);
            if ( !fileExists ) {
                String csvSep = "'sep=";
                csvSep+= mCsvSeperator + "'\n";
                fos.write(csvSep.getBytes());
            }
            fos.write(stateStr.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.w(TAG, "FileOutputStream FileNotFoundException exception: - " + e.toString());
            return false;
        }catch(IOException e){
            Log.w(TAG, "FileOutputStream IOException exception: - " + e.toString());
            return false;
        }
        return true;
    }
    private synchronized String getStateString() {
        String stateStr = getVersionString() 		                        + mCsvSeperator;
        stateStr += "A" + mCsvSeperator + Integer.toString(getTime())       + mCsvSeperator;
        stateStr += "N" + mCsvSeperator + Integer.toString(getNr())         + mCsvSeperator;
        stateStr += "I" + mCsvSeperator + Float.toString(getActTemp())      + mCsvSeperator;
        stateStr += "G" + mCsvSeperator + Float.toString(getGradient())     + mCsvSeperator;
        stateStr += "J" + mCsvSeperator + Integer.toString(getGradPos())    + mCsvSeperator;
        stateStr += "S" + mCsvSeperator + Float.toString(getSollTemp())     + mCsvSeperator;
        stateStr += "Q" + mCsvSeperator + Integer.toString(getAlarmNr())    + mCsvSeperator;

        stateStr += "C" + mCsvSeperator;
        if ( isOnOff() )    stateStr += "1" + mCsvSeperator;
        else                stateStr += "0" + mCsvSeperator;

        stateStr += "H" + mCsvSeperator;
        if ( isHeat() )     stateStr += "1" + mCsvSeperator;
        else                stateStr += "0" + mCsvSeperator;

        stateStr += "B" + mCsvSeperator + Float.toString(getBatLevel()) + mCsvSeperator;
        if ( isBatLow() )   stateStr += "L" + mCsvSeperator;
        else                stateStr += mCsvSeperator;

        if ( isActiveRest() )
            stateStr += "R"  + mCsvSeperator+ Integer.toString(getTimeRest()) + mCsvSeperator;
        else
            stateStr +=  mCsvSeperator + mCsvSeperator;

        if ( isActiveOut() )
            stateStr += "O"  + mCsvSeperator + Float.toString(getOutPut()) + mCsvSeperator;
        else
            stateStr +=  mCsvSeperator + mCsvSeperator;

        if ( isWeiter() ) stateStr += "W" + mCsvSeperator;
        else              stateStr += mCsvSeperator;

        if ( isCall()   ) stateStr += "K" + mCsvSeperator;
        else              stateStr += mCsvSeperator;

        if ( isNtcOut()   )
            stateStr += "V"  + mCsvSeperator+ Float.toString(getNtcVolt()) + mCsvSeperator;
        else
            stateStr +=  mCsvSeperator + mCsvSeperator;

    	stateStr += "\n";
        return stateStr;
    }
    public synchronized boolean setInfos(String infoString) {
        boolean correct = true;
        reset();
        // time    nbr   Acttmp Gradient SolTmp Heat   OnOff BatLe     TimRest   Output  Weiter Call
        // r000000 N0000 I00000 E00000 G00000 S00000 C00000 H0000 B0000 Q0 [L] [R00000] [O00000] [W] [K] [V00000]
        try {
            String[] infoArray = infoString.split(" ");
            for (int i = 0; i < infoArray.length; i++) {
                String value = infoArray[i];
                if (value.length() > 0) {
                    char first = value.toUpperCase().charAt(0);
                    String numberStr = "";
                    if ( 1 < value.length() ) {
                        numberStr = value.substring(1, value.length());
                    }
                    switch (first) {
                        case 'A': {
                            time = Integer.parseInt(numberStr);
                            break;
                        }
                        case 'N': {
                            nr = Integer.parseInt(numberStr);
                            break;
                        }
                        case 'I': {
                            actTemp = Float.parseFloat(numberStr);
                            break;
                        }
                        case 'G': {
                            gradient = Float.parseFloat(numberStr);
                            break;
                        }
                        case 'J': {
                            gradientPos = Integer.parseInt(numberStr);
                            break;
                        }
                        case 'S': {
                            sollTemp = Float.parseFloat(numberStr);
                            break;
                        }
                        case 'C': {
                            if ( 0 == Integer.parseInt(numberStr) )
                                heat = false;
                            else
                                heat = true;
                            break;
                        }
                        case 'H': {
                            if ( 0 == Integer.parseInt(numberStr) )
                                onOff = false;
                            else
                                onOff = true;
                            break;
                        }
                        case 'B': {
                            batLevel = Float.parseFloat(numberStr);
                            break;
                        }
                        case 'L': {
                            batLow = true;
                            break;
                        }
                        case 'D': {
                            activeRest = true;
                            timeRest = Integer.parseInt(numberStr);
                            break;
                        }
                        case 'O': {
                            activeOut = true;
                            outPut = Float.parseFloat(numberStr);
                            break;
                        }
                        case 'W': {
                            weiter = true;
                            break;
                        }
                        case 'K': {
                            call = true;
                            break;
                        }
                        case 'V': {
                            ntcOut = true;
                            ntcVolt = Float.parseFloat(numberStr);
                            break;
                        }
                        case 'Q': {
                            alarmNr = Integer.parseInt(numberStr);
                            break;
                        }
                        default: {
                            correct = false;
                            break;
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            correct = false;
        }
        return correct;
    }
}
