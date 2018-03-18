// brausteuerung@AndreBetz.de
///////////////////////////////////////////////////////////////////////////////
// selected hardware
///////////////////////////////////////////////////////////////////////////////

//#define SWITCH_TEST
//#define INOUT_USB
//#define PROFI_COOK
//#define AUTO_TUNE
#define PID_REG

///////////////////////////////////////////////////////////////////////////////

#ifdef PROFI_COOK
  #define SENSOR_PROFICOOK
  #define SWITCH_RELAY
#else
  #define SENSOR_DALLAS
  #define SWITCH_433MHZ
#endif


#define SWITCH_SETTINGS_ARENDO
//#define SWITCH_SETTINGS_HE263

#ifdef INOUT_USB
#else
	//#define FW_LINVOR
#endif

#ifdef SWITCH_TEST
  #define INOUT_USB
//  #define SWITCH_TEST_BRENNENSTUHL
//  #define SWITCH_TEST_MUMBI
//  #define SWITCH_TEST_MCE26G
  #define SWITCH_TEST_ARENDO
#endif

///////////////////////////////////////////////////////////////////////////////
// Pin Belegung
///////////////////////////////////////////////////////////////////////////////
#define PIN_TEMP_SENSOR_DIGITAL  12
#define PIN_BLUETOOTH_RX         11
#define PIN_BLUETOOTH_TX         10
#define PIN_BUZZER                9
#define PIN_SWITCH                8
#define PIN_LED_BAT               3
#define PIN_RESET                 2
#define PIN_BAT_LOW_ANALOG       A1
#define PIN_TEMP_SENSOR_ANALOG   A0
///////////////////////////////////////////////////////////////////////////////
// defines
///////////////////////////////////////////////////////////////////////////////
#define VERSION                    301709
#define TIMER_TEMP_MEASURE         4000
#define TIMER_SAVE_STATE           30000
#define SERIAL_BAUD_RATE           57600

///////////////////////////////////////////////////////////////////////////////
// includes
///////////////////////////////////////////////////////////////////////////////
#include <Arduino.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#include <EEPROM.h>
#include <SoftwareSerial.h>
#ifdef PID_REG
  #include <PID_v1.h>
#endif
#ifdef AUTO_TUNE
  #include <PID_AutoTune_v0.h>
#endif
#include "storage.h"
#include "switch.h"
#include "sensor.h"
#include "waittime.h"
#include "buzzer.h"
#include "batlow.h"
#include "inoutput.h"
#include "settings.h"
#include "menu.h"
#include "heatcontroler.h"
#include "brew.h"

///////////////////////////////////////////////////////////////////////////////
// class objects
///////////////////////////////////////////////////////////////////////////////
StorageEEProm store;
SystemSettings settings((Storage&)store, VERSION);

#ifdef SENSOR_DALLAS
  OneWire oneWire(PIN_TEMP_SENSOR_DIGITAL);
  DallasTemperature sensorsDig(&oneWire);
  TemperaturSensorDS18B20 tempSensor(PIN_TEMP_SENSOR_DIGITAL, settings, sensorsDig);
#endif
#ifdef SENSOR_NTC
  TemperaturSensorNTC tempSensor(PIN_TEMP_SENSOR_ANALOG,settings);
#endif
#ifdef SENSOR_PROFICOOK
  TemperaturSensorProfiCook tempSensor(PIN_TEMP_SENSOR_ANALOG,settings);
#endif
#ifdef SWITCH_433MHZ
//  SwitchRcSwitch switcher(PIN_SWITCH, settings);
  #ifdef SWITCH_TEST_MUMBI
    SwitchMumbi switcher(PIN_SWITCH);
  #elif defined SWITCH_TEST_MCE26G
    SwitchMCE26G switcher(PIN_SWITCH);
  #else
    Switch433MHz switcher(PIN_SWITCH, settings);
  #endif
#endif
#ifdef SWITCH_RELAY
  SwitchRelay switcher(PIN_SWITCH);
#endif
#ifdef INOUT_USB
  InOutPutUSB inout(SERIAL_BAUD_RATE);
#else
  #ifdef FW_LINVOR
	InOutPutBTHC06Linvor inout(settings,SERIAL_BAUD_RATE, PIN_BLUETOOTH_TX, PIN_BLUETOOTH_RX);
  #else
	InOutPutBTHC06V3 inout(settings,SERIAL_BAUD_RATE, PIN_BLUETOOTH_TX, PIN_BLUETOOTH_RX);
  #endif
#endif

BatLow batLevel(PIN_BAT_LOW_ANALOG, settings);
Buzzer buzzer(PIN_BUZZER);

HystereseControler controlerHyst(settings);
#ifdef PID_REG
  PIDControler controlerPid(settings);
#endif

Brew brewing(
  settings,
  batLevel,
#ifdef PID_REG
  controlerPid,
#endif
  controlerHyst,
  (TemperaturSensor&)tempSensor,
  (Switcher&)switcher,
  TIMER_TEMP_MEASURE);

Menu menu((InOutPut&)inout,
          settings,
          brewing,
          buzzer,
          (TemperaturSensor&)tempSensor,
          (Switcher&)switcher,        
          TIMER_SAVE_STATE,
          PIN_RESET,
          PIN_LED_BAT);

///////////////////////////////////////////////////////////////////////////////
// Arduino Start
///////////////////////////////////////////////////////////////////////////////
void setup()
{
  pinMode(PIN_LED_BAT, OUTPUT);
  digitalWrite(PIN_LED_BAT, HIGH); 
  
  settings.init();

#ifdef SWITCH_TEST_BRENNENSTUHL
 settings.setSwitchType(6);          // protocol 3
 settings.setSwitchBits(24);   // 24 Bit
 settings.setSwitchPeriodusec(0);
 settings.setSwitchRepeats(10);
 settings.setSwitchAddress(1749040); // on
 settings.setSwitchUnit(1560992);    // off
#endif
#ifdef SWITCH_TEST_MUMBI
 settings.setSwitchType(2);          
 settings.setSwitchPeriodusec(280);   
 settings.setSwitchBits(32);
 settings.setSwitchRepeats(4);
 settings.setSwitchAddress(520810650); // on
 settings.setSwitchUnit(520810907);    // off
#endif
#ifdef SWITCH_TEST_ARENDO
 settings.setSwitchType(3);          // protocol 1
 settings.setSwitchBits(24);   // 24 Bit
// settings.setSwitchPeriodusec(310);
 settings.setSwitchPeriodusec(0);
 settings.setSwitchRepeats(7);
 settings.setSwitchAddress(12501119); // on
 settings.setSwitchUnit(12501118);    // off
#endif
  inout.init();
  switcher.init();
  tempSensor.init();
  batLevel.init();
  buzzer.init();
#ifdef PID_REG  
  controlerPid.init();
#endif
  controlerHyst.init();
  brewing.init();
  menu.init();



}
///////////////////////////////////////////////////////////////////////////////
void loop()
{
#ifdef SWITCH_TEST
  delay(TIMER_TEMP_MEASURE);  
  inout.println("on");
  switcher.on();
  delay(TIMER_TEMP_MEASURE);
  inout.println("off");
  switcher.off();
#else  
   menu.showMenu();
#endif
}
///////////////////////////////////////////////////////////////////////////////

