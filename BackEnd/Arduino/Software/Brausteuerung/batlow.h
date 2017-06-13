// brausteuerung@AndreBetz.de
#ifndef __BATLOW__
#define __BATLOW__

#include <Arduino.h>
#define MAXANALOGREAD 1023
#define MAXVOLT 5.0f
#define SPANNUNGSTEILER_R1_UP 150
#define SPANNUNGSTEILER_R2_DOWN 47
///////////////////////////////////////////////////////////////////////////////
// Bat Low
///////////////////////////////////////////////////////////////////////////////

class BatLow
{
    //      ____       ____
    // o---| R1 |--o--| R2 |--o    R1=150KOhm
    // |    ----   |   ----   |    R2=50KOhm
    // +9V         A1         GND
    //
  public:
    BatLow( byte pin,
            SystemSettings& settings) :
      mPin(pin),
      mSettings(settings)
    {
    }
    void init()
    {
      pinMode(mPin,INPUT);
    }
    boolean checkBatterieVoltage()
    {
      float val = getVoltage();
      if ( val < mSettings.getBatLowLevel() )
      {
        return true;
      }
      return false;
    }
    float getVoltage()
    {
      int val = analogRead(mPin);
      return (float)val * ( 
        MAXVOLT *
        ( SPANNUNGSTEILER_R1_UP + SPANNUNGSTEILER_R2_DOWN) /
        SPANNUNGSTEILER_R2_DOWN / 
        MAXANALOGREAD ) ;
    }
  private:
    byte mPin;
    SystemSettings& mSettings;
};

#endif

