// brausteuerung@AndreBetz.de
#ifndef __BUZZER__
#define __BUZZER__

#include <Arduino.h>
///////////////////////////////////////////////////////////////////////////////
// Buzzer
///////////////////////////////////////////////////////////////////////////////
#define BUZ_VAL 180

class Buzzer
{
    // Buzzer  | Arduino
    // black   | GND
    // red     | D9 (PWM)
  public:
    Buzzer( byte pin ) :
      mPin(pin)
    {
    }
    void init()
    {
      pinMode(mPin, OUTPUT);
    }
    void on()
    {
      analogWrite (mPin, BUZ_VAL);
    }
    void off()
    {
      analogWrite (mPin, 0);
    }
  private:
    byte mPin;
};

#endif

