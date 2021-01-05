echo off
SET AVR_PATH=C:\Users\andre\portableApps\arduino-1.8.1\
REM SET AVR_PATH=C:\andre\portableApps\arduino-1.6.9\
REM SET PROFI_COOK=""

SET AVR_PATH=%AVR_PATH%\hardware
REM del /Q bin\*.hex
copy Brausteuerung\Brausteuerung.ino Brausteuerung\Brausteuerung.cpp


%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs -Ilibs Brausteuerung\Brausteuerung.cpp -o bin\Brausteuerung.cpp.o 
if not "%PROFI_COOK%"=="" (
  ECHO PROFI_COOK
  %AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DPROFI_COOK -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs -Ilibs Brausteuerung\Brausteuerung.cpp -o bin\Brausteuerung.cpp.o 
) else (
  ECHO BRAUSTEUERUNGV3
  %AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs -Ilibs Brausteuerung\Brausteuerung.cpp -o bin\Brausteuerung.cpp.o 
)

%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs -Ilibs libs\OneWire.cpp -o bin\OneWire.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs -Ilibs libs\DallasTemperature.cpp -o bin\DallasTemperature.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs -Ilibs libs\SoftwareSerial.cpp -o bin\SoftwareSerial.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs -Ilibs libs\PID_v1.cpp -o bin\PID_v1.cpp.o 
REM %AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs -Ilibs libs\PID_AutoTune_v0.cpp -o bin\PID_AutoTune_v0.cpp.o 

%AVR_PATH%\tools\avr/bin/avr-gcc -c -g -Os -w -ffunction-sections -fdata-sections -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\hooks.c -o bin\hooks.c.o 
%AVR_PATH%\tools\avr/bin/avr-gcc -c -g -Os -w -ffunction-sections -fdata-sections -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\WInterrupts.c -o bin\WInterrupts.c.o 
%AVR_PATH%\tools\avr/bin/avr-gcc -c -g -Os -w -ffunction-sections -fdata-sections -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\wiring.c -o bin\wiring.c.o 
%AVR_PATH%\tools\avr/bin/avr-gcc -c -g -Os -w -ffunction-sections -fdata-sections -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\wiring_analog.c -o bin\wiring_analog.c.o 
%AVR_PATH%\tools\avr/bin/avr-gcc -c -g -Os -w -ffunction-sections -fdata-sections -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\wiring_digital.c -o bin\wiring_digital.c.o 
%AVR_PATH%\tools\avr/bin/avr-gcc -c -g -Os -w -ffunction-sections -fdata-sections -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\wiring_pulse.c -o bin\wiring_pulse.c.o 
%AVR_PATH%\tools\avr/bin/avr-gcc -c -g -Os -w -ffunction-sections -fdata-sections -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\wiring_shift.c -o bin\wiring_shift.c.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\abi.cpp -o bin\abi.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\CDC.cpp -o bin\CDC.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\HardwareSerial.cpp -o bin\HardwareSerial.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\HardwareSerial0.cpp -o bin\HardwareSerial0.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\HardwareSerial1.cpp -o bin\HardwareSerial1.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\HardwareSerial2.cpp -o bin\HardwareSerial2.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\HardwareSerial3.cpp -o bin\HardwareSerial3.cpp.o 
REM %AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\HID.cpp -o bin\HID.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\IPAddress.cpp -o bin\IPAddress.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\main.cpp -o bin\main.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\new.cpp -o bin\new.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\Print.cpp -o bin\Print.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\Stream.cpp -o bin\Stream.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\Tone.cpp -o bin\Tone.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\USBCore.cpp -o bin\USBCore.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\WMath.cpp -o bin\WMath.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-g++ -c -g -Os -w -fno-exceptions -ffunction-sections -fdata-sections -fno-threadsafe-statics -MMD -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10604 -DARDUINO_AVR_NANO -DARDUINO_ARCH_AVR -I%AVR_PATH%\arduino\avr\cores\arduino -I%AVR_PATH%\arduino\avr\variants\eightanaloginputs %AVR_PATH%\arduino\avr\cores\arduino\WString.cpp -o bin\WString.cpp.o 

%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\hooks.c.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\WInterrupts.c.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\wiring.c.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\wiring_analog.c.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\wiring_digital.c.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\wiring_pulse.c.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\wiring_shift.c.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\abi.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\CDC.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\HardwareSerial.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\HardwareSerial0.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\HardwareSerial1.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\HardwareSerial2.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\HardwareSerial3.cpp.o 
REM %AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\HID.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\IPAddress.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\main.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\new.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\Print.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\Stream.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\Tone.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\USBCore.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\WMath.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-ar rcs bin/core.a bin\WString.cpp.o 
%AVR_PATH%\tools\avr/bin/avr-gcc -w -Os -Wl,--gc-sections -mmcu=atmega328p -o bin/Brausteuerung.cpp.elf bin\Brausteuerung.cpp.o bin\OneWire.cpp.o bin\DallasTemperature.cpp.o bin\SoftwareSerial.cpp.o bin\PID_v1.cpp.o bin/core.a -Lbin -lm 
REM %AVR_PATH%\tools\avr/bin/avr-gcc -w -Os -Wl,--gc-sections -mmcu=atmega328p -o bin/Brausteuerung.cpp.elf bin\Brausteuerung.cpp.o bin\OneWire.cpp.o bin\DallasTemperature.cpp.o bin\SoftwareSerial.cpp.o bin\PID_v1.cpp.o bin\PID_AutoTune_v0.cpp.o bin/core.a -Lbin -lm 
%AVR_PATH%\tools\avr/bin/avr-objcopy -O ihex -j .eeprom --set-section-flags=.eeprom=alloc,load --no-change-warnings --change-section-lma .eeprom=0 bin/Brausteuerung.cpp.elf bin/Brausteuerung.cpp.eep 

if not "%PROFI_COOK%"=="" (
  %AVR_PATH%\tools\avr/bin/avr-objcopy -O ihex -R .eeprom bin/Brausteuerung.cpp.elf bin/Brausteuerung_ProfiCook.hex 
) else (
  %AVR_PATH%\tools\avr/bin/avr-objcopy -O ihex -R .eeprom bin/Brausteuerung.cpp.elf bin/Brausteuerung.hex 
)

del Brausteuerung\Brausteuerung.cpp
del bin\*.o
del bin\*.d
del bin\*.eep
del bin\*.elf
del bin\*.a

REM cmd .