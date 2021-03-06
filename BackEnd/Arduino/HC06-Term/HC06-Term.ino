// brausteuerung@AndreBetz.de
#include <SoftwareSerial.h>
#define HC06_BAUD 9600
//#define HC06_BAUD 57600
//#define TERMINAL
//#define HC06V3
// Programm HC-06
// load programm to Arduino
// no bluetooth connection to HC-06 Module
// send AT commands over Serial
// mit PUTTY auf Arduino COM verbinden
// kein NL und CR setzen
// AT -> OK
// AT+NAME<Name> -> Oksetname
// AT+PIN<4stellige zahl> -> OKsetPIN
// AT+BAUD<X> -> OK<X>
// X=4 : 9600bps (Default)
// X=6 : 38400bps
// X=7 : 57600bps
// X=8 : 115200bps
/////////////////////////
// HC-06 | Arduino
// TX    | D10
// RX    | D11
////////////////////////
SoftwareSerial mySerial(10, 11);
boolean setBaudOnce = true;

boolean needCLRF() {
  while ( mySerial.available() ) {
    char a = mySerial.read();
  }
  mySerial.print("AT");
  delay(1000);
  if ( mySerial.available() ) {
    while ( mySerial.available() ) {
      char a = mySerial.read();
    }
    return false;
  }
  mySerial.println("\r\n");
  return true;
}

void setup() {
  Serial.begin(9600);
  delay(500);
  mySerial.begin(HC06_BAUD);
  delay(1000);
}
void loop()
{
  #ifndef TERMINAL
  if ( setBaudOnce ) {
    if ( needCLRF() ) {
      Serial.println("new FW");
      mySerial.println("AT+NAME=mikroSikaru.de\r\n");
      delay(1500);
      mySerial.println("AT+PSWD=\"1101\"\r\n");
      delay(1500);
      mySerial.println("AT+UART=57600,0,0");      
    } else {
      Serial.println("old FW");
      mySerial.print("AT+NAMEmikroSikaru.de");      
      delay(1500);
      mySerial.print("AT+PIN1101");
      delay(1500);
      mySerial.print("AT+BAUD7");
    }
    delay(1000);
    setBaudOnce = false;
    Serial.println("set OK");
  }
  #else
  if ( Serial.available() )
  {
    char a = Serial.read();
    mySerial.print(a);
  }
  if ( mySerial.available() )
  {
    char a = mySerial.read();
    Serial.print(a);
  }
  #endif
}
