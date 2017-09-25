// brausteuerung@AndreBetz.de
#ifndef __SENSOR__
#define __SENSOR__

#include <Arduino.h>
#include <math.h>
#define ABSZERO 273.15
#define MAXVALUES 15
#define MAXANALOGREAD 1023
#define NTC_WAIT_TIME 10
#define NTC_AKKU_VAL  5


///////////////////////////////////////////////////////////////////////////////
// Temperatur Sensoren
///////////////////////////////////////////////////////////////////////////////
class TemperaturSensor
{
  public:
    TemperaturSensor(
      byte pin,
      SystemSettings& settings) :
      mPin(pin),
      mGradientPos(0),
      mSettings(settings)
    {
      mType = 0;
    }
    void resetStored(float val) {
      mGradientPos = 0;
      for ( int i = 0; i < MAXVALUES; i++ )
        mStored[i] = val;
    }
    void addVal(float val) {
      mStored[mGradientPos] = val;
      mGradientPos++;
      if ( MAXVALUES <= mGradientPos )
        mGradientPos = 0;
    }
    float getActVal() {
      if ( 0 == mGradientPos) {
        return mStored[MAXVALUES - 1];
      } else {
        return mStored[mGradientPos - 1];
      }
    }
    float getGradient() {
      // Temperaturabfrage alle TIMER_TEMP_MEASURE sekunde ist
      // und der Gradient auf 1C/Min berechnet werden soll
      //Serial.println("grad");
      //Serial.println(getActVal());
      //Serial.println(mStored[mGradientPos]);
      float res = (getActVal() - mStored[mGradientPos]) /
                  ( ((float)(TIMER_TEMP_MEASURE / 1000) * MAXVALUES) / 60 ) ;
      //Serial.println(res);
      return res;
    }
    virtual float getTemperatur() = 0;
    virtual void init() = 0;
    virtual byte getSensorType()
    {
      return mType;
    }
    byte getActGradPos() {
      return mGradientPos;
    }
  protected:
    byte mPin;
    SystemSettings& mSettings;
    byte mType;
    byte mGradientPos;
    float mStored[MAXVALUES];
};
///////////////////////////////////////////////////////////////////////////////
class TemperaturSensorDS18B20 :
  TemperaturSensor
{
    // DS18B20
    // DALLES 18B50 CONNECTION
    // Dallas     | waterproof | Arduino
    // ----------------------------------
    // PIN 1 GND  |  black     | GND
    // PIN 2 Data |  yellow    | D12
    // PIN 3 VCC  |  red       | 5V
    //   _______
    //  /  TOP  \
    // /_________\
    //    | | |
    //    1 2 3
    // 4.7KOhm zwischen PIN 2 und PIN 3
  public:
    TemperaturSensorDS18B20(
      byte pin,
      SystemSettings& settings,
      DallasTemperature& sensorsDig) :
      TemperaturSensor(pin, settings),
      mSensor(sensorsDig)
    {
      mType = 1;
    }
    void init()
    {
      pinMode(mPin, INPUT);
      setup();
    }
    void setup() {
      mSensor.begin();
      mSensor.setResolution(11);
      mSensor.setWaitForConversion(false);
      mSensor.requestTemperatures();
    }
    float getTemperatur()
    {
      float val = mSensor.getTempCByIndex(0);
      mSensor.requestTemperatures();
      if ( -126 > val ) {
        setup();
      }
      //      mSensor.requestTemperaturesByAddress(mThermoNr);
      return val * mSettings.getKalM() + mSettings.getKalT();
    }
  private:

    /*
      void adresseAusgeben(void)
      {
          byte i;
          byte present = 0;
          byte data[12];
          byte addr[8];

          while(oneWire.search(addr))
          {
            Serial.print("1-Wire-Device Adresse:\n");
            for( i = 0; i < 8; i++)
            {
              Serial.print(addr[i], HEX);
              if (i < 7)
                Serial.print(", ");
            }
            if ( OneWire::crc8( addr, 7) != addr[7])
            {
              Serial.print("CRC is not valid!\n\r");
              return;
            }
          }
          oneWire.reset_search();
        }
    */

    DallasTemperature& mSensor;
    //    DeviceAddress mThermoNr;
};
///////////////////////////////////////////////////////////////////////////////
#ifdef PROFI_COOK
class TemperaturSensorNTC :
  public TemperaturSensor
{
    //      ____       ____
    // o---| R1 |--o--|NTC |--o    R1=NTC=100KOhm
    // |    ----   |   ----   |
    // +5V         A0         GND
    //
  public:
    TemperaturSensorNTC(byte pin,
                        SystemSettings& settings) :
      TemperaturSensor(pin, settings)
    {
      mType = 2;
      mSettings.setNtc(true);
    }
    void init()
    {
      pinMode(mPin, INPUT);
    }
    int getRaw()
    {
      int val = 0;
      for ( int i = 0; i < NTC_AKKU_VAL; i++ )
      {
        val += analogRead(mPin);
        delay(NTC_WAIT_TIME);
      }
      val /= NTC_AKKU_VAL;
      return val;
    }
    float getTemperatur()
    {
      float val = temperature_NTC(getRaw() / MAXANALOGREAD);
      return val * mSettings.getKalM() + mSettings.getKalT();
    }
  protected:
    float temperature_NTC ( float inVal )
    {
      float T0 = mSettings.getNtcT0() + ABSZERO;
      float T1 = mSettings.getNtcT1() + ABSZERO;
      // Materialkonstante B berechnen
      float B = (T1 * T0) /
                (T1 - T0) *
                log10(mSettings.getNtcR0() /
                      mSettings.getNtcR1() );
      // aktueller Widerstand des NTC
      float RN = mSettings.getVorR() *
                 inVal /
                 (1 - inVal);
      return  T0 * B /
              (B + T0 *
               log10(RN /
                     mSettings.getNtcR0() ) ) -
              ABSZERO;
    }
};

///////////////////////////////////////////////////////////////////////////////
// T(U) = 6,3253112423*U^3 - 57,5722331623*U^2 + 194,9223589084*U - 186,5170899644
#define POL_GRAD3_D        6.3253112423
#define POL_GRAD3_C     - 57.5722331623
#define POL_GRAD3_B      194.9223589084
#define POL_GRAD3_A     -186.5170899644

class TemperaturSensorProfiCook :
  public TemperaturSensorNTC
{
    //      _____       ____
    // o---| NTC |--o--|R1  |--o    R1=NTC=?
    // |    -----   |   ----   |
    // +5V         A0         GND
    //
  public:
    TemperaturSensorProfiCook(byte pin,
                              SystemSettings& settings) :
      TemperaturSensorNTC(pin, settings)
    {
    }
    float getTemperatur()
    {
      float val = temperature_NTCProfiCok(getRaw());
      val += oldVal;
      val /= 2;
      oldVal = val;
      return val * mSettings.getKalM() + mSettings.getKalT();
    }
  protected:

    float temperature_NTCProfiCok ( float inVal )
    {
      float res = 0.0f;

      res = POL_GRAD3_D * inVal * inVal * inVal +
            POL_GRAD3_C * inVal * inVal +
            POL_GRAD3_B * inVal +
            POL_GRAD3_A;

      return res;
    }
    float oldVal;
};
#endif

#endif

