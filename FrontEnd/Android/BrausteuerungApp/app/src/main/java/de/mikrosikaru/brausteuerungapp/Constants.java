// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_LOST = 6;
    public static final int MESSAGE_TEXT = 7;
    public static final int MESSAGE_START= 8;
    public static final int MESSAGE_STOP= 9;
    public static final int MESSAGE_activateButtons    = 10;
    public static final int MESSAGE_disableButtons     = 11;
    public static final int MESSAGE_disableButtonsAll  = 12;
    public static final int MESSAGE_isBrewRunning      = 13;
    public static final int MESSAGE_initializedEnd     = 14;
    public static final int MESSAGE_BtLost             = 15;
    public static final int MESSAGE_NotInitialized     = 16;
    public static final int MESSAGE_RecipeTransfered   = 17;
    public static final int MESSAGE_CONNECTION_FAILED  = 18;


    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static final String TEXT = "text";

    public static final int RAST_ID_START = 12345;
    public static final int MAX_BREW_STEPS = 16;

    public static final int PAUSE_TILL_CMD   = 500;

    public static final int xsudVersion1 = 9;

    public static final int HTTP_PORT = 8080;
    public static final int HTML_REFRESH = 5;

    public static final float maxBatteryLevel = 9;
}
