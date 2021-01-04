platineX    = 61;
platineY    = 61;
bauteilZ    = 25;
batteryX    = 30;
batteryY    = 40;
batteryZ    = 20;
wand        = 1.5;
bohrlochD   = 1.5;
TempKabelD  = 8;
SchalterD   = 6;
LedD        = 2;
StromD      = 2;
StromHole   = 2;

$fn = 100; 
module bohrHalterung(bohrungD,h)
{
    sizeXY = bohrungD*2;
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
    difference() 
    {
        union()
        {
            schachtel(platineX,platineY,hoeheS,wand);
            translate([platineX+wand,0,0])
                schachtel(batteryX,platineY,hoeheS,wand);
        }
        translate([-bohrlochD*2+wand,-bohrlochD*2+wand,-1])
            cube(size = [bohrlochD*2,bohrlochD*2,hoeheS+wand+2], center = false);
        translate([-bohrlochD*2+wand,platineY+wand,-1])
            cube(size = [bohrlochD*2,bohrlochD*2,hoeheS+wand+2], center = false);
        translate([platineX+batteryX+2*wand,-bohrlochD*2+wand,-1])
            cube(size = [bohrlochD*2,bohrlochD*2,hoeheS+wand+2], center = false);
        translate([platineX+batteryX+2*wand,platineY+wand,-1])
            cube(size = [bohrlochD*2,bohrlochD*2,hoeheS+wand+2], center = false);
    }

    translate([-bohrlochD*2+wand,-bohrlochD*2+wand,0])
        bohrHalterung(bohrlochD,hoeheS+wand);
    translate([-bohrlochD*2+wand,platineY+wand,0])
        bohrHalterung(bohrlochD,hoeheS+wand);
    translate([platineX+batteryX+2*wand,-bohrlochD*2+wand,0])
        bohrHalterung(bohrlochD,hoeheS+wand);
    translate([platineX+batteryX+2*wand,platineY+wand,0])
        bohrHalterung(bohrlochD,hoeheS+wand);
}


difference()
{
    doppelschachtel(bauteilZ);
    translate([-1,platineY/2,bauteilZ/2])
        rotate([0,90,0])
            cylinder(r=TempKabelD/2,wand+2);
    translate([platineX/2,2,bauteilZ/2]) rotate([90,0,0]) cylinder(r=SchalterD/2,wand+2);
    translate([platineX/2+SchalterD*2,2,bauteilZ/2]) rotate([90,0,0]) cylinder(r=LedD/2,wand+2);
    translate([platineX+batteryX/2+wand,2,bauteilZ/2]) rotate([90,0,0]) cylinder(r=LedD/2,wand+2);
    translate([platineX+1,platineY/2,bauteilZ]) 
        cube(size = [wand+2,StromHole,StromHole], center = false);
    translate([platineX+wand+batteryX/2,-1,bauteilZ]) 
        cube(size = [StromHole,wand+2,StromHole], center = false);
}
translate([0,platineY+bohrlochD*6,0])
    doppelschachtel(0);

