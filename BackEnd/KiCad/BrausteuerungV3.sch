EESchema Schematic File Version 2
LIBS:myPiezzo
LIBS:khtaster
LIBS:hc-06
LIBS:BrausteuerungV3-rescue
LIBS:Transmitter433MHz
LIBS:DS1820
LIBS:power
LIBS:device
LIBS:transistors
LIBS:conn
LIBS:linear
LIBS:regul
LIBS:74xx
LIBS:cmos4000
LIBS:adc-dac
LIBS:memory
LIBS:xilinx
LIBS:microcontrollers
LIBS:dsp
LIBS:microchip
LIBS:analog_switches
LIBS:motorola
LIBS:texas
LIBS:intel
LIBS:audio
LIBS:interface
LIBS:digital-audio
LIBS:philips
LIBS:display
LIBS:cypress
LIBS:siliconi
LIBS:opto
LIBS:atmel
LIBS:contrib
LIBS:arduino
LIBS:valves
LIBS:BrausteuerungV3-cache
EELAYER 25 0
EELAYER END
$Descr A4 11693 8268
encoding utf-8
Sheet 1 1
Title "BrausteuerungV3"
Date ""
Rev ""
Comp "mikroSikaru"
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L R R4
U 1 1 5788D6A3
P 8550 2050
F 0 "R4" V 8630 2050 50  0000 C CNN
F 1 "4,7K" V 8550 2050 50  0000 C CNN
F 2 "Resistors_ThroughHole:Resistor_Horizontal_RM10mm" V 8480 2050 50  0001 C CNN
F 3 "" H 8550 2050 50  0000 C CNN
	1    8550 2050
	1    0    0    -1  
$EndComp
$Comp
L R R1
U 1 1 5788D779
P 6650 3400
F 0 "R1" V 6730 3400 50  0000 C CNN
F 1 "330" V 6650 3400 50  0000 C CNN
F 2 "Resistors_ThroughHole:Resistor_Horizontal_RM10mm" V 6580 3400 50  0001 C CNN
F 3 "" H 6650 3400 50  0000 C CNN
	1    6650 3400
	0    1    1    0   
$EndComp
$Comp
L R R2
U 1 1 5788D7F7
P 7250 1600
F 0 "R2" V 7330 1600 50  0000 C CNN
F 1 "47K" V 7250 1600 50  0000 C CNN
F 2 "Resistors_ThroughHole:Resistor_Horizontal_RM10mm" V 7180 1600 50  0001 C CNN
F 3 "" H 7250 1600 50  0000 C CNN
	1    7250 1600
	0    1    1    0   
$EndComp
$Comp
L R R3
U 1 1 5788D863
P 7700 1600
F 0 "R3" V 7780 1600 50  0000 C CNN
F 1 "150K" V 7700 1600 50  0000 C CNN
F 2 "Resistors_ThroughHole:Resistor_Horizontal_RM10mm" V 7630 1600 50  0001 C CNN
F 3 "" H 7700 1600 50  0000 C CNN
	1    7700 1600
	0    1    1    0   
$EndComp
$Comp
L LED D1
U 1 1 5788DA4B
P 6150 3400
F 0 "D1" H 6150 3500 50  0000 C CNN
F 1 "LED" H 6150 3300 50  0000 C CNN
F 2 "LEDs:LED-3MM" H 6150 3400 50  0001 C CNN
F 3 "" H 6150 3400 50  0000 C CNN
	1    6150 3400
	-1   0    0    1   
$EndComp
$Comp
L DS18B20 IC1
U 1 1 5788DF8B
P 8650 1300
F 0 "IC1" H 8550 1700 60  0000 C CNN
F 1 "DS18B20" H 8650 1600 60  0000 C CNN
F 2 "DS18B20:DS18B20" H 8650 1300 60  0001 C CNN
F 3 "" H 8650 1300 60  0000 C CNN
	1    8650 1300
	1    0    0    -1  
$EndComp
$Comp
L GNDREF #PWR2
U 1 1 5789029A
P 6700 1650
F 0 "#PWR2" H 6700 1400 50  0001 C CNN
F 1 "GNDREF" H 6700 1500 50  0000 C CNN
F 2 "" H 6700 1650 50  0000 C CNN
F 3 "" H 6700 1650 50  0000 C CNN
	1    6700 1650
	1    0    0    -1  
$EndComp
$Comp
L +9V #PWR1
U 1 1 5789049D
P 6200 1250
F 0 "#PWR1" H 6200 1100 50  0001 C CNN
F 1 "+9V" H 6200 1390 50  0000 C CNN
F 2 "" H 6200 1250 50  0000 C CNN
F 3 "" H 6200 1250 50  0000 C CNN
	1    6200 1250
	1    0    0    -1  
$EndComp
$Comp
L arduino_mini U1
U 1 1 5788E7B1
P 5150 2950
F 0 "U1" H 5650 2000 70  0000 C CNN
F 1 "arduino_nano" H 5900 1900 70  0000 C CNN
F 2 "arduino:arduino_mini" H 5150 2900 60  0000 C CNN
F 3 "" H 5150 2950 60  0000 C CNN
	1    5150 2950
	1    0    0    -1  
$EndComp
$Comp
L Transmitter433MHz-RESCUE-BrausteuerungV3 J3
U 1 1 578A305F
P 7700 3600
F 0 "J3" H 8400 3350 60  0000 C CNN
F 1 "Transmitter433MHz" H 8100 3750 60  0000 C CNN
F 2 "Transmitter433MHz:Transmitter433MHz" H 7700 3600 60  0001 C CNN
F 3 "" H 7700 3600 60  0000 C CNN
	1    7700 3600
	0    1    1    0   
$EndComp
$Comp
L HC-06 J4
U 1 1 578A34FC
P 8950 2800
F 0 "J4" H 9800 2450 60  0000 C CNN
F 1 "HC-06" H 10050 2400 60  0000 C CNN
F 2 "HC-06:HC-06" H 8950 2800 60  0001 C CNN
F 3 "" H 8950 2800 60  0000 C CNN
	1    8950 2800
	0    1    1    0   
$EndComp
$Comp
L KHTaster J1
U 1 1 578A6220
P 6350 3400
F 0 "J1" H 6350 3400 60  0000 C CNN
F 1 "KHTaster" H 6350 2950 60  0000 C CNN
F 2 "KHTaster:KHTaster" H 6350 3400 60  0001 C CNN
F 3 "" H 6350 3400 60  0000 C CNN
	1    6350 3400
	1    0    0    -1  
$EndComp
$Comp
L myPiezzo J2
U 1 1 579E0D29
P 6450 2950
F 0 "J2" H 6450 2950 60  0000 C CNN
F 1 "myPiezzo" H 6450 3100 60  0000 C CNN
F 2 "myPiezzo:PIEZZO" H 6450 2950 60  0001 C CNN
F 3 "" H 6450 2950 60  0000 C CNN
	1    6450 2950
	1    0    0    -1  
$EndComp
Wire Wire Line
	7400 1600 7550 1600
Connection ~ 7500 1600
Wire Wire Line
	6700 1600 7100 1600
Wire Wire Line
	6700 1600 6700 1650
Connection ~ 6950 1600
Wire Wire Line
	5000 1800 5000 850 
Wire Wire Line
	5000 850  7150 850 
Wire Wire Line
	7500 2100 7500 1600
Wire Wire Line
	5650 2100 7500 2100
Wire Wire Line
	5650 2100 5650 1450
Wire Wire Line
	5650 1450 3950 1450
Wire Wire Line
	3950 1450 3950 2850
Connection ~ 6950 3550
Wire Wire Line
	8550 1700 8550 1900
Wire Wire Line
	8650 2200 8550 2200
Wire Wire Line
	5150 1800 5150 750 
Wire Wire Line
	5150 750  8000 750 
Wire Wire Line
	8000 1800 8550 1800
Connection ~ 8000 1800
Connection ~ 8550 1800
Wire Wire Line
	8750 2350 8750 1700
Wire Wire Line
	6950 2350 8750 2350
Connection ~ 6950 2350
Wire Wire Line
	5850 2400 6450 2400
Wire Wire Line
	6450 2400 6450 2250
Wire Wire Line
	6450 2250 8650 2250
Connection ~ 8650 2200
Connection ~ 8400 2350
Wire Wire Line
	8450 1800 8450 2500
Wire Wire Line
	8450 2500 8700 2500
Wire Wire Line
	8700 2500 8700 3000
Connection ~ 8450 1800
Wire Wire Line
	6950 3750 7350 3750
Wire Wire Line
	7450 3750 7450 2450
Wire Wire Line
	7450 2450 8000 2450
Wire Wire Line
	8400 2650 8600 2650
Wire Wire Line
	8600 2650 8600 3000
Wire Wire Line
	8400 2650 8400 2350
Wire Wire Line
	5850 2600 7850 2600
Wire Wire Line
	7850 2600 7850 2800
Wire Wire Line
	7850 2800 8500 2800
Wire Wire Line
	8500 2800 8500 3000
Wire Wire Line
	5850 2500 7400 2500
Wire Wire Line
	7400 2500 7400 2900
Wire Wire Line
	8400 2900 8400 3000
Wire Wire Line
	5850 3550 6000 3550
Wire Wire Line
	6950 3550 6750 3550
Wire Wire Line
	7550 2800 7550 3750
Wire Wire Line
	5850 2800 7550 2800
Wire Wire Line
	6950 2950 6800 2950
Connection ~ 6950 2950
Wire Wire Line
	5850 2700 6000 2700
Wire Wire Line
	6000 2700 6000 2950
$Comp
L CONN_01X02 P1
U 1 1 579E1182
P 5950 1400
F 0 "P1" H 5950 1550 50  0000 C CNN
F 1 "Power 9V" V 6050 1400 50  0000 C CNN
F 2 "LEDs:LED-3MM" H 5950 1400 50  0001 C CNN
F 3 "" H 5950 1400 50  0000 C CNN
	1    5950 1400
	-1   0    0    1   
$EndComp
Wire Wire Line
	6150 1450 6950 1450
Wire Wire Line
	6950 1450 6950 4500
Wire Wire Line
	8000 2450 8000 750 
Connection ~ 6950 3750
Wire Wire Line
	5850 3450 5950 3450
Wire Wire Line
	5950 3450 5950 3400
Wire Wire Line
	6350 3400 6500 3400
Wire Wire Line
	6800 3400 6950 3400
Connection ~ 6950 3400
Wire Wire Line
	3950 2850 4450 2850
Wire Wire Line
	6950 4500 5150 4500
$Comp
L D D2
U 1 1 58B2CCBB
P 6850 1350
F 0 "D2" H 6850 1450 50  0000 C CNN
F 1 "1n4001" H 6850 1250 50  0000 C CNN
F 2 "Resistors_ThroughHole:R_Axial_DIN0207_L6.3mm_D2.5mm_P7.62mm_Horizontal" H 6850 1350 50  0001 C CNN
F 3 "" H 6850 1350 50  0000 C CNN
	1    6850 1350
	-1   0    0    1   
$EndComp
Wire Wire Line
	7000 1350 7850 1350
Wire Wire Line
	7850 1350 7850 1600
Wire Wire Line
	7150 850  7150 1350
Connection ~ 7150 1350
Wire Wire Line
	6600 1350 6700 1350
Wire Wire Line
	8650 2250 8650 1700
$Comp
L R R5
U 1 1 5A568ECA
P 7850 2900
F 0 "R5" V 7930 2900 50  0000 C CNN
F 1 "1K" V 7850 2900 50  0000 C CNN
F 2 "" V 7780 2900 50  0000 C CNN
F 3 "" H 7850 2900 50  0000 C CNN
	1    7850 2900
	0    1    1    0   
$EndComp
Wire Wire Line
	7400 2900 7700 2900
Wire Wire Line
	8000 2900 8400 2900
$Comp
L R R6
U 1 1 5A5691D6
P 7850 3100
F 0 "R6" V 7930 3100 50  0000 C CNN
F 1 "2K" V 7850 3100 50  0000 C CNN
F 2 "" V 7780 3100 50  0000 C CNN
F 3 "" H 7850 3100 50  0000 C CNN
	1    7850 3100
	0    1    1    0   
$EndComp
Wire Wire Line
	8000 3100 8150 3100
Wire Wire Line
	8150 3100 8150 2900
Connection ~ 8150 2900
Wire Wire Line
	7700 3100 6950 3100
Connection ~ 6950 3100
Wire Wire Line
	6150 1350 6500 1350
Wire Wire Line
	6200 1350 6200 1250
$Comp
L CONN_01X02 P2
U 1 1 5A56A656
P 6550 1150
F 0 "P2" H 6550 1300 50  0000 C CNN
F 1 "Switch" V 6650 1150 50  0000 C CNN
F 2 "LEDs:LED-3MM" H 6550 1150 50  0001 C CNN
F 3 "" H 6550 1150 50  0000 C CNN
	1    6550 1150
	0    -1   -1   0   
$EndComp
Connection ~ 6200 1350
$EndSCHEMATC
