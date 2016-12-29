SET AVRPATH=avrdude
REM SET AVRPATH=hardware\tools\avr/bin
SET COMPORT=COM7
%AVRPATH%\avrdude.exe -C%AVRPATH%\avrdude.conf -v -patmega328p -carduino -P%COMPORT% -b57600 -D -Uflash:w:Software\bin\Brausteuerung.hex:i 
REM %AVRPATH%\avrdude.exe -C%AVRPATH%\avrdude.conf -v -patmega328p -carduino -P%COMPORT% -b57600 -D -Uflash:w:Software\bin\Brausteuerung_ProfiCook.hex:i