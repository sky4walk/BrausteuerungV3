// brausteuerung@AndreBetz.de
#ifndef __STORAGE__
#define __STORAGE__

#include <Arduino.h>
///////////////////////////////////////////////////////////////////////////////
// Storage
///////////////////////////////////////////////////////////////////////////////

class Storage
{
  public:
    virtual void load(unsigned int address,
                      unsigned int lengthByte,
                      char* data) = 0;
    virtual void save(unsigned int address,
                      unsigned int lengthByte,
                      char* data) = 0;
};

///////////////////////////////////////////////////////////////////////////////

class StorageEEProm : Storage
{
  public:
    virtual void load(unsigned int address,
              unsigned int lengthByte,
              char* data)
    {
      for ( unsigned int i = 0; 
            i < lengthByte && address+i < EEPROM.length(); 
            i++)
      {
        eeprom_busy_wait();
        *(data + i) = EEPROM.read(address + i);
      }
    }
    virtual void save(unsigned int address,
              unsigned int lengthByte,
              char* data)
    {
      for ( unsigned int i = 0; 
            i < lengthByte && address+i < EEPROM.length() ; 
            i++)
      {
        eeprom_busy_wait();
        EEPROM.update(address + i, *(data + i) );
      }
    }
};

#endif

