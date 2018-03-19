SET AVRPATH=avrdude
REM SET AVRPATH=hardware\tools\avr/bin
SET COMPORT=COM3
REM %AVRPATH%\avrdude.exe -C%AVRPATH%\avrdude.conf -v -patmega328p -carduino -P%COMPORT% -b57600 -D -Uflash:w:HC06-Term\HC06-Term.cpp.hex:i 
%AVRPATH%\avrdude.exe -C%AVRPATH%\avrdude.conf -v -patmega328p -carduino -P%COMPORT% -b57600 -D -Uflash:w:HC06-Term\HC06-TermV3.cpp.hex:i 
