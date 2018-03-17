// brausteuerung@AndreBetz.de
#ifndef __INOUTPUT__
#define __INOUTPUT__

#include <Arduino.h>
#include "inoutput.h"
///////////////////////////////////////////////////////////////////////////////
// In- Output
///////////////////////////////////////////////////////////////////////////////

#define READBUFFER 20

class InOutPut
{
  public:
    InOutPut(unsigned long baud) :
      mBaud(baud)
    {};
    virtual void init() = 0;
    virtual void print(const char* txt) = 0;
    virtual void println(const char* txt) = 0;
    virtual void print(const __FlashStringHelper* txt) = 0;
    virtual void println(const __FlashStringHelper* txt) = 0;

    virtual int readByte() = 0;
    char* readString()
    {
      if ( readline( readByte() ) > 0)
      {
        return mReadBuffer;
      }
      else return 0;
    }
  protected:
    int readline(int readch)
    {
      int rpos;

      if (readch > 0)
      {
        switch (readch)
        {
          case 20 :
            break;
          case 255 :
            break;
          case '\n': // Ignore new-lines
            break;
          case '\r': // Return on CR
            rpos = mReadPos;
            mReadPos = 0;  // Reset position index ready for next time
            return rpos;
          default:
            if (mReadPos < READBUFFER - 1)
            {
              //              Serial.println(mReadPos);
              //              Serial.println((int)readch);
              mReadBuffer[mReadPos++] = readch;
              mReadBuffer[mReadPos] = 0;
            }
        }
      }
      return -1;
    }

    char mReadBuffer[READBUFFER];
    unsigned int mReadPos;
    /*
      boolean isDigit(char in)
      {
          if ( in == '0'||'1'||'2'||'3'||'4'||'5'||'6'||'7'||'8'||'9' )
              return true;
           else
              return false;
      }
    */
    unsigned long mBaud;
};
///////////////////////////////////////////////////////////////////////////////
class InOutPutBTHC06Linvor : InOutPut
{
    // HC-06   |   Arduino
    // VCC     |   +5V/+3,3V
    // GND     |   GND
    // TXD     |   pinRx (10)
    // RXD     |   pinTx (11)
  public:
    InOutPutBT(SystemSettings& settings,
               unsigned long baud,
               byte pinTx,byte pinRx) :
      InOutPut(baud),
      mySerial(pinTx, pinRx),
      mSettings(settings)
    {
    }
    void init()
    {
      mySerial.begin(mBaud);
      while (!mySerial) {
        ; // wait for serial port to connect. Needed for Leonardo only
      }
      delay(1000);
      if ( mSettings.getWriteBT() ) {
        sprintf(mReadBuffer, "AT+PIN%04d", (unsigned int)mSettings.getPassWd());
        print(mReadBuffer);     
        delay(1500);
        print("AT+NAMEmikroSikaru.de");
        delay(1500);
        mSettings.setWriteBT(false);
        mSettings.saveState();
      }
      //AT+BAUD4
    }
    void print(const char* txt)
    {
      mySerial.print(txt);
    }
    void print(const __FlashStringHelper* txt)
    {
      mySerial.print(txt);
    }
    void println(const __FlashStringHelper* txt)
    {
      mySerial.println(txt);
    }
    void println(int val)
    {
      mySerial.println(val);
    }
    void println(const char* txt)
    {
      mySerial.println(txt);
    }
    virtual int readByte()
    {
      if ( mySerial.available() )
      {
        return mySerial.read();
      }
      return 0;
    }
  private:
    SoftwareSerial mySerial;
    SystemSettings& mSettings;
};
///////////////////////////////////////////////////////////////////////////////
class InOutPutBTHC06V3 : InOutPut
{
    // HC-06   |   Arduino
	// FW      |   V3.0 (ZS-040)
    // VCC     |   +5V/+3,3V
    // GND     |   GND
    // TXD     |   pinRx (10)
    // RXD     |   pinTx (11)
  public:
    InOutPutBT(SystemSettings& settings,
               unsigned long baud,
               byte pinTx,byte pinRx) :
      InOutPut(baud),
      mySerial(pinTx, pinRx),
      mSettings(settings)
    {
    }
    void init()
    {
      mySerial.begin(mBaud);
      while (!mySerial) {
        ; // wait for serial port to connect. Needed for Leonardo only
      }
      delay(1000);
      if ( mSettings.getWriteBT() ) {
        sprintf(mReadBuffer, "AT+PSWD=\"%04d\"", (unsigned int)mSettings.getPassWd());
        println(mReadBuffer);     
        delay(1500);
        println("AT+NAME=mikroSikaru.de");
        delay(1500);
        mSettings.setWriteBT(false);
        mSettings.saveState();
      }
      //AT+BAUD4
    }
    void print(const char* txt)
    {
      mySerial.print(txt);
    }
    void print(const __FlashStringHelper* txt)
    {
      mySerial.print(txt);
    }
    void println(const __FlashStringHelper* txt)
    {
      mySerial.println(txt);
    }
    void println(int val)
    {
      mySerial.println(val);
    }
    void println(const char* txt)
    {
      mySerial.println(txt);
    }
    virtual int readByte()
    {
      if ( mySerial.available() )
      {
        return mySerial.read();
      }
      return 0;
    }
  private:
    SoftwareSerial mySerial;
    SystemSettings& mSettings;
};
///////////////////////////////////////////////////////////////////////////////
class InOutPutUSB : InOutPut
{
  public:
    InOutPutUSB(unsigned long baud) :
      InOutPut(baud)
    {
    }
    void init()
    {
      Serial.begin(mBaud);
      while (!Serial)
      {
      }
    }
    void print(const char* txt)
    {
      Serial.print(txt);
    }
    void print(const __FlashStringHelper* txt)
    {
      Serial.print(txt);
    }
    void println(const __FlashStringHelper* txt)
    {
      Serial.println(txt);
    }
    void println(const char* txt)
    {
      Serial.println(txt);
    }
    virtual int readByte()
    {
      if ( Serial.available() )
      {
        return Serial.read();
      }
      return 0;
    }

};
///////////////////////////////////////////////////////////////////////////////

#endif


