// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

/**
 * Created by andre on 21.01.16.
 */
public class SystemBrewStep {
    private int     nr;
    private String  name;
    private boolean call;
    private boolean halt;
    private boolean on;
    private float   sollTemp;
    private int     time;
    private float   minTemp;
    private float   maxTemp;
    private float   kp;
    private float   ki;
    private float   kd;
    private int     onTimePuls;
    private int     offTimePuls;
    private float   maxGradient;
    private String  infoField;

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCall() {
        return call;
    }

    public void setCall(boolean call) {
        this.call = call;
    }

    public boolean isHalt() {
        return halt;
    }

    public void setHalt(boolean halt) {
        this.halt = halt;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public float getSollTemp() {
        return sollTemp;
    }

    public void setSollTemp(float sollTemp) {
        this.sollTemp = sollTemp;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public float getKp() {
        return kp;
    }

    public void setKp(float kp) {
        this.kp = kp;
    }

    public float getKi() {
        return ki;
    }

    public void setKi(float ki) {
        this.ki = ki;
    }

    public float getKd() {
        return kd;
    }

    public void setKd(float kd) {
        this.kd = kd;
    }

    public int getOnTimePuls() {
        return onTimePuls;
    }

    public void setOnTimePuls(int onTimePuls) {
        this.onTimePuls = onTimePuls;
    }

    public int getOffTimePuls() {
        return offTimePuls;
    }

    public void setOffTimePuls(int offTimePuls) {
        this.offTimePuls = offTimePuls;
    }

    public float getMaxGradient() {
        return maxGradient;
    }

    public void setMaxGradient(float maxGradient) {
        this.maxGradient = maxGradient;
    }

    public String getInfoField() {
        return infoField;
    }

    public void setInfoField( String infoField ) {
        this.infoField = infoField;
    }

    public SystemBrewStep() {
        reset();
    }

    public void reset() {
        this.name           = "";
        this.nr             = 0;
        this.call           = false;
        this.halt           = false;
        this.on             = false;
        this.sollTemp       = 0;
        this.time           = 0;
        this.minTemp        = -0.1f;
        this.maxTemp        =  0.1f;
        this.kp             = 0.0f;
        this.ki             = 0;
        this.kd             = 0.0f;
        this.onTimePuls     = 0;
        this.offTimePuls    = 0;
        this.maxGradient    = 0.0f;
	    this.infoField      = "";
    }
    public void copy(SystemBrewStep brewStep) {
        this.name           = brewStep.getName();
        this.nr             = brewStep.getNr();
        this.call           = brewStep.isCall();
        this.halt           = brewStep.isHalt();
        this.on             = brewStep.isOn();
        this.sollTemp       = brewStep.getSollTemp();
        this.time           = brewStep.getTime();
        this.minTemp        = brewStep.getMinTemp();
        this.maxTemp        = brewStep.getMaxTemp();
        this.kp             = brewStep.getKp();
        this.ki             = brewStep.getKi();
        this.kd             = brewStep.getKd();
        this.onTimePuls     = brewStep.getOnTimePuls();
        this.offTimePuls    = brewStep.getOffTimePuls();
        this.maxGradient    = brewStep.getMaxGradient();
        this.infoField      = brewStep.getInfoField();
    }
};
