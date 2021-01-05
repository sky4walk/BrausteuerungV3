platineX    = 62;
platineY    = 65;
bauteilZ    = 25;
batteryX    = 30;
batteryY    = 40;
batteryZ    = 20;
wand        = 1.8;
bohrlochD   = 2;
bohrlochH   = 3;
TempKabelD  = 8;
SchalterD   = 6;
LedD        = 3;
StromCon    = 4;
StromD      = 2;
StromHole   = 4;

$fn = 100; 
module bohrHalterung(bohrungD,h)
{
    sizeXY = bohrungD*bohrlochH;
    difference()
    {
        cube(size = [sizeXY,sizeXY,h], center = false);
        translate([sizeXY/2,sizeXY/2,-1])
            cylinder(r=bohrungD/2,h+2);
    }
}

module schachtel(x,y,z,d)
{
    difference()
    {
        cube(size = [x+2*d,y+2*d,z+d], center = false);
        translate([d,d,d])
            cube(size = [x,y,z+1], center = false);
    }
}

module doppelschachtel(hoeheS)
{
    schachtel(platineX,platineY,hoeheS,wand);
    translate([platineX+wand,0,0])
        schachtel(batteryX,platineY,hoeheS,wand);

    translate([-bohrlochD*bohrlochH+wand,0,0])
        bohrHalterung(bohrlochD,hoeheS+wand);
    translate([-bohrlochD*bohrlochH+wand,platineY+2*wand-bohrlochD*bohrlochH,0])
        bohrHalterung(bohrlochD,hoeheS+wand);
    translate([platineX+batteryX+2*wand,0,0])
        bohrHalterung(bohrlochD,hoeheS+wand);
    translate([platineX+batteryX+2*wand,platineY+2*wand-bohrlochD*bohrlochH,0])
        bohrHalterung(bohrlochD,hoeheS+wand);
}


difference()
{
    doppelschachtel(bauteilZ);
    translate([-1,platineY*2/3,bauteilZ*2/3]) rotate([0,90,0]) cylinder(r=TempKabelD/2,wand+2);
    translate([platineX/2,2,bauteilZ*2/3])    rotate([90,0,0]) cylinder(r=SchalterD/2,wand+2);
    translate([platineX/2+SchalterD*2,2,bauteilZ*2/3]) rotate([90,0,0]) cylinder(r=LedD/2,wand+2);
    translate([platineX+batteryX/2+wand,2,bauteilZ*2/3]) rotate([90,0,0]) cylinder(r=StromCon/2,wand+2);
    translate([platineX+1,wand,bauteilZ-StromHole+wand+1]) 
        cube(size = [wand+2,StromHole,StromHole], center = false);
    translate([platineX+wand+batteryX-StromHole,-1,bauteilZ-StromHole+wand+1]) 
        cube(size = [StromHole,wand+2,StromHole], center = false);
}
translate([0,platineY+bohrlochD*bohrlochH+wand,0])
    doppelschachtel(0);
translate([wand,platineY+bohrlochD*bohrlochH+wand*2,wand])
    cube(size = [wand,platineY,wand], center = false);
translate([platineX+wand+batteryX,platineY+bohrlochD*bohrlochH+wand*2,wand])
    cube(size = [wand,platineY,wand], center = false);

