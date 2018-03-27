REM Arduino �ber USB Bus anschliessen
REM %AVRPATH%\listComPorts.exe aufrufen um COM Port zu bestimmen
REM erhaltenen COM Port anstatt COM7 eintragen

SET AVRPATH=avrdude
SET COMPORT=COM3
%AVRPATH%\avrdude.exe -C%AVRPATH%\avrdude.conf -v -patmega328p -carduino -P%COMPORT% -b57600 -D -Uflash:w:Software\bin\Brausteuerung_301709.hex:i 
REM %AVRPATH%\avrdude.exe -C%AVRPATH%\avrdude.conf -v -patmega328p -carduino -P%COMPORT% -b57600 -D -Uflash:w:Software\bin\Brausteuerung_301709V3.hex:i 
REM %AVRPATH%\avrdude.exe -C%AVRPATH%\avrdude.conf -v -patmega328p -carduino -P%COMPORT% -b57600 -D -Uflash:w:Software\bin\Brausteuerung_ProfiCook.hex:i