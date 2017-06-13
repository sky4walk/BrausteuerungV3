// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

public interface BrewRecipeFile {
    public boolean Load(SystemSettings settings, String fileName);
    public boolean Save(SystemSettings settings, String fileName);
    public String getError();
}
