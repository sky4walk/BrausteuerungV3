REM Arduino über USB Bus anschliessen
REM %AVRPATH%\listComPorts.exe aufrufen um COM Port zu bestimmen
REM erhaltenen COM Port anstatt COM7 eintragen

SET AVRPATH=avrdude
SET COMPORT=COM8

REM install bluetooth V3
%AVRPATH%\avrdude.exe -C%AVRPATH%\avrdude.conf -v -patmega328p -carduino -P%COMPORT% -b57600 -D -Uflash:w:HC06-Term\HC06-Term.ino.hex:i 

timeout 10

REM install firmware
%AVRPATH%\avrdude.exe -C%AVRPATH%\avrdude.conf -v -patmega328p -carduino -P%COMPORT% -b57600 -D -Uflash:w:Software\bin\Brausteuerung_301709V3.hex:i 
