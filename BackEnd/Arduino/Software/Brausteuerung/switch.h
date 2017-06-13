// brausteuerung@AndreBetz.de
#ifndef __SWITCH__
#define __SWITCH__

#include <Arduino.h>
#include <avr/pgmspace.h>
#include "settings.h"

//#define HE300_SOLUTION2
//#define SEND_CODE_OLD


///////////////////////////////////////////////////////////////////////////////
// Switcher
///////////////////////////////////////////////////////////////////////////////
class Switcher
{
  public:
    Switcher(byte pin)
    {
      mState = false;
      mStateChanged = false;
      mPin = pin;
    }
    virtual void on()
    {
      if ( false == mStateChanged &&
           false == mState)
        mStateChanged = true;
      mState = true;
    }
    virtual void off()
    {
      if ( false == mStateChanged &&
           true  == mState)
        mStateChanged = true;
      mState = false;
    }
    void resetStateChanged()
    {
      mStateChanged = false;
    }
    virtual boolean getStateChanged()
    {
      return mStateChanged;
    }
    virtual boolean getState()
    {
      return mState;
    }
  protected:
    byte mPin;
    boolean mState;
    boolean mStateChanged;
};
///////////////////////////////////////////////////////////////////////////////

struct HighLow {
  uint8_t high;
  uint8_t low;
};

struct Protocol {
  int pulseLength;
  HighLow syncFactor;
  HighLow zero;
  HighLow one;
  bool invertedSignal;
  bool syncBefore;
};
static const Protocol PROGMEM proto[] = {
  { 350, {  1, 31 }, {  1,  3 }, {  3,  1 }, false, false },    // protocol 1
  { 650, {  1, 10 }, {  1,  2 }, {  2,  1 }, false, false },    // protocol 2
  { 100, { 30, 71 }, {  4, 11 }, {  9,  6 }, false, false },    // protocol 3
  { 380, {  1,  6 }, {  1,  3 }, {  3,  1 }, false, false },    // protocol 4 brennenstuhl
  { 500, {  6, 14 }, {  1,  2 }, {  2,  1 }, false, false },    // protocol 5
  { 450, { 23,  1 }, {  1,  2 }, {  2,  1 }, true,  false },    // protocol 6 (HT6P20B)
  {   0, { 0,   0 }, {  0,  1 }, {  1,  0 }, false, false }     // protocol 7 Switch
};
enum {
  numProto = sizeof(proto) / sizeof(proto[0])
};

class Switch433MHz : Switcher
{
    // RF-Link-Sender 434MHz Switch
    //--------------
    //  -------
    //  |     |
    //  | O   |
    //  -------
    //  | | | |
    //  1 2 3 4
    //
    // 1: GND
    // 2: Data in | D8
    // 3: VCC
    // 4: Antenne
  public:
    Switch433MHz( byte pin,
                  SystemSettings& settings ) :
      Switcher(pin),
      mSettings(settings)
    {
    }
    void init()
    {
      pinMode(mPin, OUTPUT);
      if ( 3 <=  mSettings.getSwitchType() )
        setProtocolRc(mSettings.getSwitchType() - 3);
    }
    void on()
    {
      Switcher::on();
      if ( 0 == mSettings.getSwitchType() )
        sendCode(true);
      else if ( 1 == mSettings.getSwitchType() )
        sendCodeOld( mSettings.getSwitchAddress() );
      else if ( 2 == mSettings.getSwitchType() )
        sendMumbiCode( mSettings.getSwitchAddress(),
                       mSettings.getSwitchBits() );  
      else
        sendOutRc(mSettings.getSwitchAddress(),
                  mSettings.getSwitchBits());
    }
    void off()
    {
      Switcher::off();
      if ( 0 == mSettings.getSwitchType() )
        sendCode(false);
      else if ( 1 == mSettings.getSwitchType() )
        sendCodeOld( mSettings.getSwitchUnit() );
      else if ( 2 == mSettings.getSwitchType() )
        sendMumbiCode(  mSettings.getSwitchUnit(),
	                    mSettings.getSwitchBits() );         
      else
        sendOutRc(mSettings.getSwitchUnit(),
                  mSettings.getSwitchBits());
    }
  private:
    void sendOnOff()
    {
      if ( getStateChanged() )
      {
        resetStateChanged();
        //        sendCode();
      }
    }
    void sendBit(boolean isBitOne)
    {
      if (isBitOne)
      {
        // Send '1'
        digitalWrite(mPin, HIGH);
        delayMicroseconds(mSettings.getSwitchPeriodusec());
        digitalWrite(mPin, LOW);
        delayMicroseconds(mSettings.getSwitchPeriodusec() * 5);
        digitalWrite(mPin, HIGH);
        delayMicroseconds(mSettings.getSwitchPeriodusec());
        digitalWrite(mPin, LOW);
        delayMicroseconds(mSettings.getSwitchPeriodusec());
      }
      else
      {
        // Send '0'
        digitalWrite(mPin, HIGH);
        delayMicroseconds(mSettings.getSwitchPeriodusec());
        digitalWrite(mPin, LOW);
        delayMicroseconds(mSettings.getSwitchPeriodusec());
        digitalWrite(mPin, HIGH);
        delayMicroseconds(mSettings.getSwitchPeriodusec());
        digitalWrite(mPin, LOW);
        delayMicroseconds(mSettings.getSwitchPeriodusec() * 5);
      }
    }

    void sendCode(boolean switchOn)
    {
      // repeate = 2^repeate-1
      //byte repeats = ( 1 << mSettings.getSwitchRepeats() ) - 1;
      for (int8_t i = mSettings.getSwitchRepeats(); i >= 0; i--)
      {
        unsigned long tempAdr = mSettings.getSwitchAddress();
        //start
        digitalWrite(mPin, HIGH);
        delayMicroseconds(mSettings.getSwitchPeriodusec());
        digitalWrite(mPin, LOW);

#ifdef HE300_SOLUTION2
        delayMicroseconds( mSettings.getSwitchPeriodusec() * 36 );
        digitalWrite(mPin, HIGH);
        delayMicroseconds(mSettings.getSwitchPeriodusec());
        digitalWrite(mPin, LOW);
        delayMicroseconds(mSettings.getSwitchPeriodusec() * 10);
#else
        delayMicroseconds( mSettings.getSwitchPeriodusec() * 10 +
                           (mSettings.getSwitchPeriodusec() >> 1) );
#endif


        // adresse
        for (int8_t i = 25; i >= 0; i--)
        {
          sendBit(
            (tempAdr >> i) & 1 );
        }

        // No group bit
        sendBit(
          false );

        // Switch on | off
        sendBit(
          switchOn );

        //unit
        for (int8_t i = 3; i >= 0; i--)
        {
          sendBit(
            mSettings.getSwitchUnit() & (1 << i) );
        }

        //stop
        digitalWrite(mPin, HIGH);
        delayMicroseconds(mSettings.getSwitchPeriodusec());
        digitalWrite(mPin, LOW);
#ifdef HE300_SOLUTION2
        delay(10);
#else
        delayMicroseconds(mSettings.getSwitchPeriodusec() * 40);
#endif
      }
    }

    void sendCodeOld(unsigned long code) {
#ifdef SEND_CODE_OLD      
      code &= 0xfffff; // Truncate to 20 bit ;
      // Convert the base3-code to base4, to avoid lengthy calculations when transmitting.. Messes op timings.
      // Also note this swaps endianess in the process. The MSB must be transmitted first, but is converted to
      // LSB here. This is easier when actually transmitting later on.
      unsigned long dataBase4 = 0;

      for (byte i = 0; i < 12; i++)
      {
        dataBase4 <<= 2;
        dataBase4 |= (code % 3);
        code /= 3;
      }

      byte repeats = 1 << ( mSettings.getSwitchRepeats() & B111 ); // repeats := 2^repeats;

      for (byte j = 0; j < repeats; j++)
      {
        // Sent one telegram

        // Recycle code as working var to save memory
        code = dataBase4;
        for (byte i = 0; i < 12; i++)
        {
          switch (code & B11)
          {
            case 0:
              digitalWrite(mPin, HIGH);
              delayMicroseconds(mSettings.getSwitchPeriodusec());
              digitalWrite(mPin, LOW);
              delayMicroseconds(mSettings.getSwitchPeriodusec() * 3);
              digitalWrite(mPin, HIGH);
              delayMicroseconds(mSettings.getSwitchPeriodusec());
              digitalWrite(mPin, LOW);
              delayMicroseconds(mSettings.getSwitchPeriodusec() * 3);
              break;
            case 1:
              digitalWrite(mPin, HIGH);
              delayMicroseconds(mSettings.getSwitchPeriodusec() * 3);
              digitalWrite(mPin, LOW);
              delayMicroseconds(mSettings.getSwitchPeriodusec());
              digitalWrite(mPin, HIGH);
              delayMicroseconds(mSettings.getSwitchPeriodusec() * 3);
              digitalWrite(mPin, LOW);
              delayMicroseconds(mSettings.getSwitchPeriodusec());
              break;
            case 2: // KA: X or float
              digitalWrite(mPin, HIGH);
              delayMicroseconds(mSettings.getSwitchPeriodusec());
              digitalWrite(mPin, LOW);
              delayMicroseconds(mSettings.getSwitchPeriodusec() * 3);
              digitalWrite(mPin, HIGH);
              delayMicroseconds(mSettings.getSwitchPeriodusec() * 3);
              digitalWrite(mPin, LOW);
              delayMicroseconds(mSettings.getSwitchPeriodusec());
              break;
          }
          // Next trit
          code >>= 2;
        }

        // Send termination/synchronization-signal. Total length: 32 periods
        digitalWrite(mPin, HIGH);
        delayMicroseconds(mSettings.getSwitchPeriodusec());
        digitalWrite(mPin, LOW);
        delayMicroseconds(mSettings.getSwitchPeriodusec() * 31);
      }
#endif      
    }

    void setProtocolRc(int nProtocol) {
      if (nProtocol < 1 || nProtocol > numProto) {
        nProtocol = 1;
      }
      memcpy_P(&this->protocol, &proto[nProtocol - 1], sizeof(Protocol));
    }
    void sendOutRc(unsigned long code, unsigned int length) {
      for (int nRepeat = 0; nRepeat < mSettings.getSwitchRepeats(); nRepeat++) {
        if ( true == protocol.syncBefore ) {
          this->transmitRc(protocol.syncFactor);
        }
        for (int i = length - 1; i >= 0; i--) {
          if (code & (1L << i))
            this->transmitRc(protocol.one);
          else
            this->transmitRc(protocol.zero);
        }
        if ( false == protocol.syncBefore ) {
          this->transmitRc(protocol.syncFactor);
        }
      }
    }
    void transmitRc(HighLow pulses) {
      uint8_t firstLogicLevel = (this->protocol.invertedSignal) ? LOW : HIGH;
      uint8_t secondLogicLevel = (this->protocol.invertedSignal) ? HIGH : LOW;
      int pulseLength = mSettings.getSwitchPeriodusec() ? 
                        mSettings.getSwitchPeriodusec() :
                        this->protocol.pulseLength;
      digitalWrite(mPin, firstLogicLevel);
      delayMicroseconds( pulseLength * pulses.high);
      digitalWrite(mPin, secondLogicLevel);
      delayMicroseconds( pulseLength * pulses.low);
    }
    void sendMumbiCode(unsigned long code, unsigned int length) {
      for (int i = 0; i < mSettings.getSwitchRepeats(); i++) {
        for (int i = length - 1; i >= 0; i--) {
          if (code & (1L << i))
            sendMumbi1();
          else
            sendMumbi0();
        }
        sendMumbiSync();
      }
    }
    void sendMumbi0() {
      digitalWrite(mPin, HIGH);
      delayMicroseconds(mSettings.getSwitchPeriodusec()*3);   
      digitalWrite(mPin, LOW);
      delayMicroseconds(mSettings.getSwitchPeriodusec());   
    }
    void sendMumbi1() {
      digitalWrite(mPin, HIGH);
      delayMicroseconds(mSettings.getSwitchPeriodusec());   
      digitalWrite(mPin, LOW);
      delayMicroseconds(mSettings.getSwitchPeriodusec()*3);   
    }
    void sendMumbiSync() {      
      sendMumbi1();      
      digitalWrite(mPin, HIGH);
      delayMicroseconds(mSettings.getSwitchPeriodusec());   
      digitalWrite(mPin, LOW);
      delayMicroseconds(mSettings.getSwitchPeriodusec()*37);   
    }
    SystemSettings& mSettings;
    Protocol protocol;
};

///////////////////////////////////////////////////////////////////////////////

class SwitchRcSwitch : Switcher
{
  public:


    SwitchRcSwitch( byte pin,
                    SystemSettings& settings ) :
      Switcher(pin),
      mSettings(settings)
    {
    }
    void init()
    {
      pinMode(mPin, OUTPUT);
      setProtocol(mSettings.getSwitchType());
    }
    void on()
    {
      Switcher::on();
      sendOut(mSettings.getSwitchAddress(), mSettings.getSwitchPeriodusec());
    }
    void off()
    {
      Switcher::off();
      sendOut(mSettings.getSwitchUnit(), mSettings.getSwitchPeriodusec());
    }
  private:
    void setProtocol(int nProtocol) {
      if (nProtocol < 1 || nProtocol > numProto) {
        nProtocol = 1;  // TODO: trigger an error, e.g. "bad protocol" ???
      }
      memcpy_P(&this->protocol, &proto[nProtocol - 1], sizeof(Protocol));
    }
    void sendOut(unsigned long code, unsigned int length) {
      for (int nRepeat = 0; nRepeat < mSettings.getSwitchRepeats(); nRepeat++) {
        for (int i = length - 1; i >= 0; i--) {
          if (code & (1L << i))
            this->transmit(protocol.one);
          else
            this->transmit(protocol.zero);
        }
        this->transmit(protocol.syncFactor);
      }
    }
    void transmit(HighLow pulses) {
      uint8_t firstLogicLevel  = (this->protocol.invertedSignal) ? LOW : HIGH;
      uint8_t secondLogicLevel = (this->protocol.invertedSignal) ? HIGH : LOW;

      if ( 0 !=  pulses.high ) {
        digitalWrite(mPin, firstLogicLevel);
        delayMicroseconds( this->protocol.pulseLength * pulses.high);
      }
      if ( 0 != pulses.low ) {
        digitalWrite(mPin, secondLogicLevel);
        delayMicroseconds( this->protocol.pulseLength * pulses.low);
      }
    }

    SystemSettings& mSettings;
    Protocol protocol;
};

///////////////////////////////////////////////////////////////////////////////
#ifdef SWITCH_TEST_MUMBI
class SwitchMumbi : Switcher
{
  public:
    char *timingsOn  = "01010110101010100101010110011001101010100101010110010110100110011012";
    char *timingsOff = "01010110101010100101010110011001101010100101011010010110100110101012";

    char *shortOn  = "00011111000010101111000010011010"; //‭520810650‬
    char *shortOff = "00011111000010101111000110011011"; //‭520810907‬
    
//    unsigned long buckets[8] = { 848, 308, 10420, 0, 0, 0, 0, 0 } ;
    unsigned long buckets[8] = { 840, 280, 10360, 0, 0, 0, 0, 0 } ;

    SwitchMumbi( byte pin ) :
      Switcher(pin)
    {
    }
    void init()
    {
      pinMode(mPin, OUTPUT);
    }
    virtual void on()
    {
      Switcher::on();
      //sendCode(8, buckets, timingsOn, 4);
      //sendShort(shortOn,280,4);
      sendNumber(520810650, 32,280, 4);
    }
    virtual void off()
    {
      Switcher::off();
      //sendCode(8, buckets, timingsOff, 4);
      //sendShort(shortOff,280,4);
      sendNumber(520810907, 32,280, 4);
    }
  private:

    void sendCode(unsigned long* buckets, char* compressTimings, unsigned int repeats) {
      unsigned int timings_size = strlen(compressTimings);
      for (unsigned int i = 0; i < repeats; i++) {
        digitalWrite(mPin, LOW);
        int state = LOW;
        for (unsigned int j = 0; j < timings_size; j++) {
          state = !state;
          digitalWrite(mPin, state);
          unsigned int index = compressTimings[j] - '0';
          delayMicroseconds(buckets[index]);
        }
      }
      digitalWrite(mPin, LOW);
    }

    void sendShort(char* code, int timing, unsigned int repeats) {
      unsigned int len = strlen(code);
      for (unsigned int i = 0; i < repeats; i++) {       
        for (unsigned int j = 0; j < len; j++) {
          unsigned int index = code[j] - '0';
          if ( 0 == index )
            send0(timing);
          else
            send1(timing);
        }
        sendSync(timing);
      }
      
    }
    void sendNumber(unsigned long code, unsigned int length,int timing, unsigned int repeats) {
      for (int i = 0; i < repeats; i++) {
        for (int i = length - 1; i >= 0; i--) {
          if (code & (1L << i))
            send1(timing);
          else
            send0(timing);
        }
        sendSync(timing);
      }
    }
    void send0(unsigned long timing) {
      digitalWrite(mPin, HIGH);
      delayMicroseconds(timing*3);   
      digitalWrite(mPin, LOW);
      delayMicroseconds(timing);   
    }
    void send1(unsigned long timing) {
      digitalWrite(mPin, HIGH);
      delayMicroseconds(timing);   
      digitalWrite(mPin, LOW);
      delayMicroseconds(timing*3);   
    }
    void sendSync(unsigned long timing) {      
      send1(timing);      
      digitalWrite(mPin, HIGH);
      delayMicroseconds(timing);   
      digitalWrite(mPin, LOW);
      delayMicroseconds(timing*37);   
    }
};
#endif

///////////////////////////////////////////////////////////////////////////////

#ifdef SWITCH_TEST_MCE26G
class SwitchMCE26G : Switcher
{
  public:
    char *timingsOn  = "00001102020200020200000002000000000202000000020000020202000202020200000200000000020202030000110202020002020000000200000000020200000002000002020200020202020000020000000002020204";
    char *timingsOff = "00001102020200020200000002000000000202000000020000020202000202020200000002020202000000030000110202020002020000000200000000020200000002000002020200020202020000000202020200000004";

    char *shortOn  = "00011111000010101111000010011010"; //‭520810650‬
    char *shortOff = "00011111000010101111000110011011"; //‭520810907‬
    
//    unsigned long buckets[8] = { 460, 3400, 1324, 10144, 21000, 0, 0, 0 } ;
    unsigned long buckets[8] = { 440, 3520, 1320, 10120, 21120, 0, 0, 0 } ;  // zeit-basis = 440ms

    SwitchMCE26G( byte pin ) :
      Switcher(pin)
    {
    }
    void init()
    {
      pinMode(mPin, OUTPUT);
    }
    virtual void on()
    {
      Switcher::on();
      sendCode(buckets, timingsOn, 7);
    }
    virtual void off()
    {
      Switcher::off();
      sendCode(buckets, timingsOff, 7);
    }
  private:

    void sendCode(unsigned long* buckets, char* compressTimings, unsigned int repeats) {
      unsigned int timings_size = strlen(compressTimings);
      for (unsigned int i = 0; i < repeats; i++) {
        digitalWrite(mPin, LOW);
        int state = LOW;
        for (unsigned int j = 0; j < timings_size; j++) {
          state = !state;
          digitalWrite(mPin, state);
          unsigned int index = compressTimings[j] - '0';
          delayMicroseconds(buckets[index]);
        }
      }
      digitalWrite(mPin, LOW);
    }

    void sendShort(char* code, int timing, unsigned int repeats) {
      unsigned int len = strlen(code);
      for (unsigned int i = 0; i < repeats; i++) {       
        for (unsigned int j = 0; j < len; j++) {
          unsigned int index = code[j] - '0';
          if ( 0 == index )
            send0(timing);
          else
            send1(timing);
        }
        sendSync(timing);
      }
      
    }
    void sendNumber(unsigned long code, unsigned int length,int timing, unsigned int repeats) {
      for (int i = 0; i < repeats; i++) {
        for (int i = length - 1; i >= 0; i--) {
          if (code & (1L << i))
            send1(timing);
          else
            send0(timing);
        }
        sendSync(timing);
      }
    }
    void send0(unsigned long timing) {
      digitalWrite(mPin, HIGH);
      delayMicroseconds(timing*3);   
      digitalWrite(mPin, LOW);
      delayMicroseconds(timing);   
    }
    void send1(unsigned long timing) {
      digitalWrite(mPin, HIGH);
      delayMicroseconds(timing);   
      digitalWrite(mPin, LOW);
      delayMicroseconds(timing*3);   
    }
    void sendSync(unsigned long timing) {      
      send1(timing);      
      digitalWrite(mPin, HIGH);
      delayMicroseconds(timing);   
      digitalWrite(mPin, LOW);
      delayMicroseconds(timing*37);   
    }
};
#endif
///////////////////////////////////////////////////////////////////////////////

class SwitchRelay : Switcher
{
    // 2: Data in | D13
  public:
    SwitchRelay( byte pin ) :
      Switcher(pin)
    {
    }
    void init()
    {
      pinMode(mPin, OUTPUT);
    }
    virtual void on()
    {
      Switcher::on();
      digitalWrite(mPin, HIGH);
    }
    virtual void off()
    {
      Switcher::off();
      digitalWrite(mPin, LOW);
    }
};
///////////////////////////////////////////////////////////////////////////////

#endif

