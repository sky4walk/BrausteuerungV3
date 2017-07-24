// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by andre on 17.01.16.
 */
public class GlobalVars extends Application {
    private static final String TAG = "GlobalVars";
    private BluetoothChatService mChatService;
    private ArrayList<String> mMessageList;
    private String mConnectedDeviceName         = null;
    private String mConnectedMacAddress         = null;
    private String mDefaultFileName             = "";
    private BluetoothAdapter mBluetoothAdapter  = null;
    private Handler mProgramRunnerHandler       = null;
    private Handler mProgramBrewingHandler      = null;
    private ProgramRunner mWorker;
    private Thread mWorkthread;
    private SystemSettings mSettings;
    private final String lineEnd = "\r\n";
    private int mProgramState                       = 0;
    private final int mStateUndefined               = 1;
    private final int mStateFirstMessageWait        = 2;
    private final int mStateGetSettings             = 3;
    private final int mStateGetBrewSteps            = 4;
    private final int mStateSetBrewStep             = 5;
    private final int mStateReadBrewStep            = 6;
    private final int mStateInitialzed              = 7;
    private final int mStateMainMenu                = 8;
    private final int mStateStartSettingsStore      = 9;
    private final int mStateRunSettingsStore        = 10;
    private final int mStateRunSettingsValidate     = 11;
    private final int mStateInitBrewStepStore       = 12;
    private final int mStateStartBrewStepStore      = 13;
    private final int mStateSetBrewStepStore        = 14;
    private final int mStateRunBrewStepsStore       = 15;
    private final int mStateRunBrewStepsValidate    = 16;
    private final int mStateErrorBrewSteps          = 17;
    private final int mStateInitBrewing             = 18;
    private final int mStateSetBrewing              = 19;
    private final int mStateStartBrewing            = 20;
    private final int mStateRunBrewingValidate      = 21;
    private final int mStateRunBrewingRequest       = 22;
    private final int mStateRunBrewingReceive       = 23;
    private final int mStateStopBrewing             = 24;
    private final int mStateErrorBrewing            = 25;
    private final int mStateBrewingIsRunning        = 26;
    private final int mStateGetSettingsEnd          = 27;
    private final int mStateErrorCommunication      = 28;
    private final int mStateStopBrewingWait         = 29;
    private final int mStateWriteBrewReady          = 30;    
    private int mMissedCallCounter = 0;
    private static MediaPlayer mPlayer= null;
    private static Ringtone mRingtone = null;
    private boolean mRunBrew     = false;
    private boolean mInitialized = false;
    private boolean mTransfereRecipde = false;
    private boolean mTransfereInit = false;
    private BrewState mBrewState = new BrewState();
    private String mLastErrorStr = "";
    private boolean mDebugFileIsOn  = true;

    public String getFilePathData() {
        return this.mFilePath;
    }

    public void setFilePath(String filePath) {
        this.mFilePath = filePath;
    }

    private String mFilePath = "";

    private NanoHTTPD   mHttpServer = new NanoHTTPD(Constants.HTTP_PORT) {
        @Override
        public Response serve(IHTTPSession session) {
            Method method = session.getMethod();
            String uri = session.getUri();

            String msg = "";

            msg += "<META HTTP-EQUIV=\"refresh\" CONTENT=\"";
            msg += Constants.HTML_REFRESH;
            msg += "\">";
            //msg += "<meta http-equiv=\"expires\" content=\"5\">";

            /*
            msg += "<script type=\"text/JavaScript\">";
            msg += "<!--";
            msg += "function timedRefresh(timeoutPeriod) {";
            msg += "  setTimeout(\"location.reload(true);\",timeoutPeriod);";
            msg += "}";
            msg += "window.onload = timedRefresh(5000);";
            msg += "//   -->";
            msg += "</script>";
            */

            msg += "<html><body><h1>";
            msg += "<a href=\"http://" + mSettings.getAppDevice() + "\"> " +
                                         mSettings.getAppDevice() + "</a> ";
            msg += mSettings.getAppName() + " ";
            msg += "</h1>\n";

            if ( isChatConnected() ) {
                msg += "<p>";
                msg += getString(R.string.state_str_Bluetooth_Connected);
                msg += "</p>";
            } else {
                msg += "<p>";
                msg += getString(R.string.state_str_Bluetooth_NotConnected);
                msg += "</p>";
            }

            if ( isRunBrew() ) {
                msg += "<p>";
                msg += getString(R.string.state_str_BrewingIsRunning);
                msg += "</p>";

                //msg += "<p>";
                //msg += getBrewState().getTime();
                //msg += "</p>";

                int nr = getBrewState().getNr();
                if ( nr < getSettings().getBrewSteps() ) {
                    msg += "<p>";
                    msg += nr + ":";
                    msg += getSettings().getBrewStep(nr).getName();
                    msg += "</p>";
                }

                msg += "<p>";
                msg += getString(R.string.txt_run_str_IstTmp);
                msg += getBrewState().getActTemp();
                msg += "</p>";

                msg += "<p>";
                msg += getString(R.string.txt_run_str_SollTmp);
                msg += getBrewState().getSollTemp();
                msg += "</p>";

                if ( getBrewState().isActiveRest() ) {
                    msg += "<p>";
                    msg += getString(R.string.run_str_time);
                    msg += getBrewState().getTimeRestMinSek();
                    msg += "</p>";
                }

                if ( getBrewState().isWeiter() ) {
                    msg += "<p>";
                    msg += getString(R.string.txt_run_str_Wait);
                    msg += "</p>";
                }

                if ( getBrewState().isBatLow() ) {
                    msg += "<p>";
                    msg += getString(R.string.txt_run_str_BatLow);
                    msg += "</p>";
                }
            } else {
                msg += "<p>";
                msg += getString(R.string.state_str_BrewingIsNotRunning);
                msg += "</p>";
            }

            /*
            Map<String, String> parms = session.getParms();
            if (parms.get("username") == null) {
                msg += "<form action='?' method='get'>\n" + "  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
            } else {
                msg += "<p>Hello, " + parms.get("username") + "!</p>";
            }
            */

            msg += "</body></html>\n";

            return newFixedLengthResponse(msg);
        }
    };

    public String getIpAdresse() {
        String ip = "";
        WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if ( null != wifiMan ) {
            WifiInfo wifiInf = wifiMan.getConnectionInfo();
            if (null != wifiInf) {
                int ipAddress = wifiInf.getIpAddress();
                if ( 0 != ipAddress )
                    ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
            }
        }
        return ip;
    }

    public BrewState getBrewState() {
        return mBrewState;
    }
    public void startHttpServer() {
        try {
            mHttpServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch(IOException ioe) {
            Log.e(TAG, "startHttpServer()");
        }
    }
    public void stopHttpServer() {
        mHttpServer.stop();
    }
    private int mActBrewStep = 0;

    public SystemSettings getSettings() {
        return mSettings;
    }

    public Handler getProgramRunnerHandler() {
        return mProgramRunnerHandler;
    }

    public void setProgramRunnerHandler(Handler programRunnerHandler) {
        this.mProgramRunnerHandler = programRunnerHandler;
    }

    public String getDefaultFileName() {
        return mDefaultFileName;
    }

    public void semDefaultFileName(String defaultFileName) {
        this.mDefaultFileName = mDefaultFileName;
    }

    public void setPlayer(MediaPlayer mediaPlayer, Ringtone ringtone) {
        mPlayer = mediaPlayer;
        mRingtone = ringtone;
    }

    public void giveAlarm(boolean alarm) {
        if ( null != mRingtone && null != mPlayer ) {
            if (alarm) {
                mRingtone.play();
                mPlayer.start();
            } else {
                mPlayer.stop();
                mRingtone.stop();
            }
        }
    }

    public Handler getProgramBrewingHandler() {
        return mProgramBrewingHandler;
    }

    public void setProgramBrewingHandler(Handler programBrewingHandler) {
        this.mProgramBrewingHandler = programBrewingHandler;
    }

    public String getConnectedMacAdressConverted() {
        return mConnectedMacAddress.replace(":","");
    }

    public String getConnectedDeviceName() {
        return mConnectedDeviceName;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public boolean isRunBrew() {
        return mRunBrew;
    }

    public boolean isTransferRecipe() {
        return mTransfereRecipde;
    }

    public boolean isTransferInit() {
        return mTransfereInit;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mSettings = new SystemSettings();
        mMessageList = new ArrayList<String>();
        mWorker = new ProgramRunner();
        mWorkthread = new Thread(mWorker);
        mWorker.setPause(true);
        mProgramState = mStateUndefined;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mSettings.setAppVersion(pInfo.versionName);
        } catch(PackageManager.NameNotFoundException e) {
            mSettings.setAppVersion("0.0");
        }
        mSettings.setAppDevice(getString(R.string.app_dev));
        mSettings.setAppName(getString(R.string.app_name));
        mWorkthread.start();

        Log.d(TAG, mSettings.getAppVersion());

    }

    public void setupChat() {
        if ( mChatService == null ) {
            Log.d(TAG, "setupChat()");
            // Initialize the BluetoothChatService to perform bluetooth connections
            mChatService = new BluetoothChatService(mHandler,this);
        }
    }

    public boolean isInitialized() {
        return mInitialized;
    }


    public void startSettingStore() {
        if ( false == isTransferInit() ) {
            mProgramState = mStateStartSettingsStore;
            mWorker.setPause(false);
            mTransfereInit = true;
        }
    }

    public void startBrewStepsStore() {
        if ( false == mTransfereRecipde ) {
            mProgramState = mStateInitBrewStepStore;
            mWorker.setPause(false);
            mTransfereRecipde = true;
        }
    }

    public void startBrewing( int brewStep ) {
        if ( false == mRunBrew ) {
            if ( brewStep < getSettings().getBrewSteps() ) {
                this.mActBrewStep = brewStep;
            } else {
                this.mActBrewStep = 0;
            }
            mProgramState = mStateInitBrewing;
            mWorker.setPause(false);
        }
    }

    public void stopBrewing() {
        if ( mRunBrew ) {
            mProgramState = mStateStopBrewing;
        }
    }

    public void stopCalling() {
        if ( mRunBrew ) {
            sendToDevice("b\r\n");
        }
    }

    public void goFurther() {
        if ( mRunBrew ) {
            sendToDevice("w\r\n");
         }
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            clearMessageList();
                            mWorker.setPause(false);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    addMessage(readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), "Connected "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_CONNECTION_FAILED:
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();

                        Message msgActivity = Message.obtain();
                        msgActivity.what = Constants.MESSAGE_CONNECTION_FAILED;
                        getProgramRunnerHandler().sendMessage(msgActivity);
                    }
                case Constants.MESSAGE_LOST:
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();

                        Message msgActivity = Message.obtain();
                        msgActivity.what = Constants.MESSAGE_BtLost;
                        getProgramRunnerHandler().sendMessage(msgActivity);
                    }
                    break;
            }
        }
    };

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    public void sendToDevice(String message) {
        if ( isChatConnected()) {
            if (message.length() > 0) {
                // Get the message bytes and tell the BluetoothChatService to write
                byte[] send = message.getBytes();
                mChatService.write(send);

                writeString(getFilePathData()+"/" + "debugLog.txt","sendToDevice:"+message+"\n");
            }
        }
    }
    public boolean isChatConnected() {
        if ( null != mChatService ) {
            if (mChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
                return true;
            }
        }
        return false;
    }
    public void resumeChatService() {
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    public void stopChatService() {
        mWorker.reset();
        if(mChatService!=null) {
            mChatService.stop();
        }
    }

    public void resetWorker() {
        clearMessageList();
        mWorker.reset();
        mWorker.setPause(true);
    }
    public void connectDevice(String address) {
        if ( null != mBluetoothAdapter ) {
            if ( mBluetoothAdapter.isEnabled() ) {
                mConnectedMacAddress = address;
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                mChatService.connect(device, false);
            }
        }
    }

    public synchronized String getFirstMessage() {
        String msg = "";
        if ( receivedMessages() > 0 ) {
            msg = mMessageList.remove(0);
        }
        return msg;
    }
    private String trimAfterLineEnd(String message){
        String retStr = "";
        int pos = message.indexOf(lineEnd);
        if ( pos < 0 ) {
            retStr = message;
        } else {
            retStr = message.substring(pos + lineEnd.length());
        }
        return retStr;
    }
    private String trimBeforeLineEnd(String message){
        String retStr = "";
        int pos = message.indexOf(lineEnd);
        if ( pos < 0 ) {
            retStr = message;
        } else {
            retStr = message.substring(0,pos);
        }
        return retStr;
    }
    public synchronized void addMessage(String message) {
        mMessageList.add(message);
    }
    public synchronized int receivedMessages() {
        return mMessageList.size();
    }

    private boolean isMessageComplete(String message) {
        if ( message.contains(lineEnd)  )
            return true;
        return false;
    }

    public void endThread() {
        mWorker.setEndThread(true);
    }
    public synchronized void clearMessageList() {
        mMessageList.clear();
    }
    private class ProgramRunner implements Runnable {

        private int result = 0;
        private boolean pauseThread = false;
        private boolean endThread = false;
        private int brewStep = 0;
        private int stateVisitCounter = 0;
        @Override
        public void run() {
            clearMessageList();
            setEndThread(true);
            mProgramState = mStateUndefined;
            String messageBuffer = "";
            while(isEndThread()) {
                if (!isChatConnected()) {
                    if ( mProgramState != mStateUndefined && isRunBrew() ) {
                        if (getProgramBrewingHandler() != null) {
                            Message msg = Message.obtain();
                            msg.what = Constants.MESSAGE_BtLost;
                            getProgramBrewingHandler().sendMessage(msg);
                        }
                        mProgramState = mStateUndefined;
                        setPause(true);
                    }
                }
                if (isPause()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                } else {
                    switch(mProgramState) {
                        case mStateUndefined:
                        {
                            clearMessageList();
                            sendToDevice("a\r\n");
                            messageBuffer = "";
                            mInitialized = false;
                            mProgramState = mStateFirstMessageWait;
                            break;
                        }
                        case mStateFirstMessageWait:
                        {
                            if ( receivedMessages() > 0 ) {
                                mMissedCallCounter = 0;
                                messageBuffer += getFirstMessage();
                                Log.d(TAG, "ProgramRunner::mStateFirstMessageWait messageBuffer:"+messageBuffer);
                                if ( isMessageComplete(messageBuffer) ){
                                    if ( mSettings.stateMenu ==
                                            mSettings.getStateFromMessage(messageBuffer) ) {
                                        if ( mSettings.setMenu(messageBuffer) ) {
                                            String  rastName = getResources().getString(R.string.title_recipe_name);
                                            getSettings().createBrewSteps(rastName);
                                            mRunBrew = false;
                                            mProgramState = mStateGetSettings;
                                            clearMessageList();
                                            sendToDevice("1\r\n");
                                            messageBuffer = "";
                                        } else {
                                            mProgramState = mStateErrorCommunication;
                                        }
                                    } else if ( mSettings.stateSettings ==
                                        mSettings.getStateFromMessage(messageBuffer) ) {
                                        messageBuffer = "";
                                        sendToDevice("w\r\n");
                                    } else if ( mSettings.stateBrewSteps ==
                                            mSettings.getStateFromMessage(messageBuffer) ) {
                                        messageBuffer = "";
                                        sendToDevice("w\r\n");
                                    } else if ( mSettings.stateRun ==
                                            mSettings.getStateFromMessage(messageBuffer) ) {
                                        mRunBrew = true;
                                        mProgramState = mStateBrewingIsRunning;

                                        writeString(getFilePathData()+"/" + "debugLog.txt",
                                                "mStateBrewingIsRunning\n");

                                        writeString(getFilePathData(),"mStateBrewingIsRunning");
                                    } else if ( mSettings.stateTune ==
                                            mSettings.getStateFromMessage(messageBuffer) ) {
                                        sendToDevice("r\r\n");
                                    }else {
                                        messageBuffer = "";
                                        mProgramState = mStateErrorCommunication;
                                    }
                                }
                            } else {
                                mMissedCallCounter++;
                                try {
                                    Thread.sleep(Constants.PAUSE_TILL_CMD);
                                } catch (InterruptedException e) {
                                }
                            }
                            break;
                        }
                        case mStateErrorCommunication: {
                            setPause(true);
                            mRunBrew = false;
                            mInitialized = false;
                            mProgramState = mStateUndefined;

                            Message msg = getProgramRunnerHandler()
                                    .obtainMessage();
                            msg.what = Constants.MESSAGE_NotInitialized;
                            getProgramRunnerHandler().sendMessage(msg);

                            writeString(getFilePathData()+"/" + "debugLog.txt","mStateErrorCommunication:"+
                                    mLastErrorStr + " " + messageBuffer+"\n");

                            break;
                        }
                        case mStateGetSettings: {
                            if ( receivedMessages() > 0 ) {
                                messageBuffer += getFirstMessage();
                                Log.d(TAG, "ProgramRunner::mStateGetSettings messageBuffer:"+messageBuffer);
                                if (isMessageComplete(messageBuffer)) {
                                    if ( mSettings.stateMenu ==
                                            mSettings.getStateFromMessage(messageBuffer) ) {
                                        this.brewStep = 0;
                                        mProgramState = mStateGetSettingsEnd;
                                    } else if ( mSettings.stateSettings ==
                                            mSettings.getStateFromMessage(messageBuffer) ) {
                                        if ( mSettings.setValueSettings(messageBuffer) ) {
                                            sendToDevice("w\r\n");
                                        } else {
                                            mProgramState = mStateErrorCommunication;
                                            mLastErrorStr = "mStateGetSettings:setValueSettings";
                                        }
                                    } else {
                                        mProgramState = mStateErrorCommunication;
                                        mLastErrorStr = "mStateGetSettings:stateSettings";
                                    }
                                    messageBuffer = "";
                                }
                            }
                            break;
                        }
                        case mStateGetSettingsEnd: {
                            Message msg = getProgramRunnerHandler()
                                    .obtainMessage();
                            msg.what = Constants.MESSAGE_initializedEnd;
                            getProgramRunnerHandler().sendMessage(msg);
                            mProgramState = mStateInitialzed;

                            writeString(getFilePathData()+"/" + "debugLog.txt",
                                    "mStateGetSettingsEnd\n");

                            break;
                        }
                        case mStateGetBrewSteps:
                        {
                            if ( this.brewStep < mSettings.getBrewSteps() ) {
                                clearMessageList();
                                messageBuffer = "";
                                sendToDevice("2\r\n");
                                mProgramState = mStateReadBrewStep;
                            } else {
                                mProgramState = mStateInitialzed;
                            }
                            break;
                        }
                        case mStateSetBrewStep:
                        {
                            if ( receivedMessages() > 0 ) {
                                messageBuffer += getFirstMessage();
                                if (isMessageComplete(messageBuffer)) {
                                     if ( mSettings.isMenuBnr(messageBuffer) ) {
                                         String sendStr = String.format("%s\r\n",
                                                Integer.toString(this.brewStep));
                                         sendToDevice(sendStr);
                                         messageBuffer = "";
                                         mProgramState = mStateReadBrewStep;
                                    }else {
                                         mProgramState = mStateErrorCommunication;
                                    }
                                }
                            }
                            break;
                        }
                        case mStateReadBrewStep:
                        {
                            if ( receivedMessages() > 0 ) {
                                messageBuffer += getFirstMessage();
                                if (isMessageComplete(messageBuffer)) {
                                    if (mSettings.stateMenu ==
                                            mSettings.getStateFromMessage(messageBuffer)) {
                                        this.brewStep++;
                                        mProgramState = mStateGetBrewSteps;
                                    } else if (mSettings.stateBrewSteps ==
                                            mSettings.getStateFromMessage(messageBuffer)) {
                                        if ( mSettings.setValueBrewStep(this.brewStep,messageBuffer) ) {
                                            sendToDevice("w\r\n");
                                            mProgramState = mStateSetBrewStep;
                                        } else {
                                            mProgramState = mStateErrorCommunication;
                                        }
                                    } else {
                                        mProgramState = mStateErrorCommunication;
                                    }
                                    messageBuffer = "";
                                }
                            }
                            break;
                        }
						case mStateWriteBrewReady:
						{
							mInitialized = true;
                            mRunBrew     = false;
                            mTransfereRecipde = false;
                            mTransfereInit = false;

                            Message msg1 = getProgramRunnerHandler()
                                    .obtainMessage(Constants.MESSAGE_TOAST);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.TOAST
                                    , getResources().getString(R.string.state_str_Initialzed));
                            msg1.setData(bundle);
                            getProgramRunnerHandler().sendMessage(msg1);

                            Message msg2 = Message.obtain();
                            msg2.what = Constants.MESSAGE_RecipeTransfered;
                            getProgramRunnerHandler().sendMessage(msg2);

                            clearMessageList();
                            mProgramState = mStateMainMenu;
                            break;
						}
                        case mStateInitialzed:
                        {
                            mInitialized = true;
                            mRunBrew     = false;
                            mTransfereRecipde = false;
                            mTransfereInit = false;

                            Message msg1 = getProgramRunnerHandler()
                                    .obtainMessage(Constants.MESSAGE_TOAST);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.TOAST
                                    , getResources().getString(R.string.state_str_Initialzed));
                            msg1.setData(bundle);
                            getProgramRunnerHandler().sendMessage(msg1);

                            Message msg2 = Message.obtain();
                            msg2.what = Constants.MESSAGE_activateButtons;
                            getProgramRunnerHandler().sendMessage(msg2);

                            clearMessageList();
                            mProgramState = mStateMainMenu;
                            break;
                        }
                        case mStateBrewingIsRunning:
                        {
                            mInitialized = false;

                            Message msg1 = getProgramRunnerHandler()
                                    .obtainMessage(Constants.MESSAGE_TOAST);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.TOAST
                                    , getResources().getString(R.string.state_str_BrewingIsRunning));
                            msg1.setData(bundle);
                            getProgramRunnerHandler().sendMessage(msg1);

                            Message msg2 = Message.obtain();
                            msg2.what = Constants.MESSAGE_isBrewRunning;
                            getProgramRunnerHandler().sendMessage(msg2);

                            mProgramState = mStateRunBrewingRequest;
                            break;
                        }
                        case mStateMainMenu:
                        {
                            setPause(true);
                            break;
                        }
                        case mStateStartSettingsStore:
                        {
                            Message msg = Message.obtain();
                            msg.what = Constants.MESSAGE_disableButtons;
                            getProgramRunnerHandler().sendMessage(msg);

                            clearMessageList();
                            sendToDevice("1\r\n");
                            messageBuffer = "";
                            mProgramState = mStateRunSettingsStore;
                            break;
                        }
                        case mStateRunSettingsStore:
                        {
                            if ( receivedMessages() > 0 ||
                                    messageBuffer.length() > 0   ) {
                                messageBuffer += getFirstMessage();
                                Log.d(TAG, "ProgramRunner::mStateRunSettingsStore messageBuffer:"+messageBuffer);
                                if (isMessageComplete(messageBuffer)) {
                                    String message = trimBeforeLineEnd(messageBuffer);
                                    if ( mSettings.stateMenu ==
                                            mSettings.getStateFromMessage(message) ) {
                                        this.brewStep = 0;
                                        mProgramState = mStateInitialzed;
                                    } else if ( mSettings.stateSettings ==
                                            mSettings.getStateFromMessage(message) ) {
                                        String value = mSettings.getValueSettings(message);
                                        if ( value.length() != 0 ) {
                                            value += lineEnd;
                                            sendToDevice(value);
                                            mProgramState = mStateRunSettingsValidate;
                                        } else {
                                            mProgramState = mStateErrorCommunication;
                                        }
                                    } else {
                                        mProgramState = mStateErrorCommunication;
                                    }
                                    messageBuffer = trimAfterLineEnd(messageBuffer);
                                }
                            }
                            break;
                        }
                        case mStateRunSettingsValidate:
                        {
                            if ( receivedMessages() > 0 ) {
                                messageBuffer += getFirstMessage();
                                Log.d(TAG, "ProgramRunner::mStateRunSettingsValidate messageBuffer:"+messageBuffer);
                                if (isMessageComplete(messageBuffer)) {
                                    messageBuffer = trimAfterLineEnd(messageBuffer);
                                    mProgramState = mStateRunSettingsStore;
                                }
                            }
                            break;
                        }
                        case mStateInitBrewStepStore: {
                            // startet download vom Rezept auf Geraet
                            Message msg = Message.obtain();
                            msg.what = Constants.MESSAGE_disableButtonsAll;
                            getProgramRunnerHandler().sendMessage(msg);
                            this.brewStep = 0;
                            mProgramState = mStateStartBrewStepStore;
                            break;
                        }
                        case mStateStartBrewStepStore: {
                            clearMessageList();
                            sendToDevice("2\r\n");
                            messageBuffer = "";
                            mProgramState = mStateSetBrewStepStore;
                            break;
                        }
                        case mStateSetBrewStepStore:
                        {
                            if ( receivedMessages() > 0 ) {
                                messageBuffer += getFirstMessage();
                                if (isMessageComplete(messageBuffer)) {
                                    Log.d(TAG, "ProgramRunner::mStateSetBrewStepStore messageBuffer:"+messageBuffer);
                                    if ( mSettings.isMenuBnr(messageBuffer) ) {
                                        String sendStr = String.format("%s\r\n",
                                                Integer.toString(this.brewStep));
                                        sendToDevice(sendStr);
                                        messageBuffer = "";
                                        mProgramState = mStateRunBrewStepsValidate;
                                    }else {
                                        mProgramState = mStateErrorBrewSteps;
                                        mLastErrorStr = "mStateSetBrewStepStore";
                                    }
                                }
                            }
                            break;
                        }
                        case mStateRunBrewStepsStore:
                        {
                            if ( receivedMessages() > 0 ||
                                    messageBuffer.length() > 0   ) {
                                messageBuffer += getFirstMessage();
                                Log.d(TAG, "ProgramRunner::mStateRunBrewStepsStore messageBuffer:"+messageBuffer);
                                if (isMessageComplete(messageBuffer)) {
                                    String message = trimBeforeLineEnd(messageBuffer);
                                    if ( mSettings.stateMenu ==
                                            mSettings.getStateFromMessage(message) ) {

                                        writeString(getFilePathData()+"/" + "debugLog.txt",
                                                "ProgramRunner::mStateRunBrewStepsStore Rezept geladen "+this.brewStep+"\n");
                                        Message msg = getProgramRunnerHandler()
                                                .obtainMessage(Constants.MESSAGE_TOAST);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Constants.TOAST
                                                , getResources().getString(R.string.state_str_store_recipe_ok)
                                                + Integer.toString(this.brewStep + 1));
                                        msg.setData(bundle);
                                        getProgramRunnerHandler().sendMessage(msg);

                                        this.brewStep++;
                                        if( this.brewStep < getSettings().getBrewSteps() ) {
                                            mProgramState = mStateStartBrewStepStore;
                                        } else {
                                            mProgramState = mStateWriteBrewReady;
                                        }
                                    } else if ( mSettings.stateBrewSteps ==
                                            mSettings.getStateFromMessage(message) ) {
                                        String value = mSettings.getValueBrewStep(this.brewStep, message);



                                        if ( value.length() != 0 ) {
                                            value += lineEnd;
                                            sendToDevice(value);
                                            messageBuffer = "";
                                            writeString(getFilePathData()+"/" + "debugLog.txt",
                                                    "ProgramRunner::mStateRunBrewStepsStore " +
                                                            this.brewStep + "," + value +"\n");
                                            mProgramState = mStateRunBrewStepsValidate;
                                        } else {
                                            writeString(getFilePathData()+"/" + "debugLog.txt",
                                                    "ProgramRunner::mStateRunBrewStepsStore mStateErrorBrewSteps " +
                                                            this.brewStep + "," + value +"\n");
                                            mProgramState = mStateErrorBrewSteps;
                                            mLastErrorStr = "mStateRunBrewStepsStore:length";
                                        }
                                    } else {
                                        writeString(getFilePathData()+"/" + "debugLog.txt",
                                                "ProgramRunner::mStateRunBrewStepsStore " +
                                                        this.brewStep + "\n");
                                        mProgramState = mStateErrorBrewSteps;
                                        mLastErrorStr = "mStateRunBrewStepsStore:stateBrewSteps";
                                    }
                                    messageBuffer = trimAfterLineEnd(messageBuffer);
                                }
                            }
                            break;
                        }
                        case mStateRunBrewStepsValidate:
                        {
                            if ( receivedMessages() > 0 ) {
                                messageBuffer += getFirstMessage();
                                Log.d(TAG, "ProgramRunner::mStateRunBrewStepsValidate messageBuffer:"+messageBuffer);
                                if (isMessageComplete(messageBuffer)) {
                                    writeString(getFilePathData()+"/" + "debugLog.txt",
                                            "ProgramRunner::mStateRunBrewStepsStore " +
                                                    this.brewStep + "," + messageBuffer +"\n");
                                    messageBuffer = trimAfterLineEnd(messageBuffer);
                                    mProgramState = mStateRunBrewStepsStore;
                                }
                            }
                            break;
                        }
                        case mStateErrorBrewSteps: {
                            this.brewStep = 0;
                            mProgramState = mStateInitialzed;

                            Message msg = getProgramRunnerHandler()
                                    .obtainMessage(Constants.MESSAGE_TOAST);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.TOAST
                                    , getResources().getString(R.string.state_str_store_error));
                            msg.setData(bundle);
                            getProgramRunnerHandler().sendMessage(msg);

                            writeString(getFilePathData()+"/" + "debugLog.txt","mStateErrorBrewSteps:"+
                                    mLastErrorStr + " " + messageBuffer+"\n");

                            mProgramState = mStateMainMenu;
                            break;
                        }
                        case mStateInitBrewing: {
                            Message msg = Message.obtain();
                            msg.what = Constants.MESSAGE_disableButtonsAll;
                            getProgramBrewingHandler().sendMessage(msg);
                            mProgramState = mStateStartBrewing;
                            break;
                        }
                        case mStateStartBrewing: {
                            clearMessageList();
                            messageBuffer = "";
                            sendToDevice("3\r\n");
                            mProgramState = mStateSetBrewing;
                            stateVisitCounter = 0;
                            try {
                                Thread.sleep(Constants.PAUSE_TILL_CMD);
                            } catch (InterruptedException e) {
                            }
                            break;
                        }
                        case mStateSetBrewing: {
                            if ( receivedMessages() > 0 ) {
                                messageBuffer += getFirstMessage();
                                if (isMessageComplete(messageBuffer)) {
                                    Log.d(TAG, "ProgramRunner::mStateSetBrewing messageBuffer:"+messageBuffer);
                                    if ( mSettings.isMenuRnr(messageBuffer) ) {
                                        String sendStr = String.format("%s\r\n",
                                                Integer.toString(mActBrewStep));
                                        sendToDevice(sendStr);
                                        messageBuffer = "";

                                        mProgramState = mStateRunBrewingValidate;
                                        stateVisitCounter = 0;
                                    }else {
                                        mProgramState = mStateErrorBrewing;
                                        mLastErrorStr = "mStateSetBrewing";
                                    }
                                }
                            }
                            if ( stateVisitCounter > 10 ) {
                                mProgramState = mStateErrorBrewing;
                                writeString(getFilePathData()+"/" + "debugLog.txt",
                                        "mStateSetBrewing entered " + stateVisitCounter +"\n");
                            }
                            stateVisitCounter++;
                            break;
                        }
                        case mStateRunBrewingValidate:
                        {
                            if ( receivedMessages() > 0 ) {
                                messageBuffer += getFirstMessage();
                                Log.d(TAG, "ProgramRunner::mStateRunBrewingValidate messageBuffer:"+messageBuffer);
                                if (isMessageComplete(messageBuffer)) {
                                    messageBuffer = trimAfterLineEnd(messageBuffer);

                                    Message msg = getProgramBrewingHandler()
                                            .obtainMessage(Constants.MESSAGE_START);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.TEXT, messageBuffer);
                                    msg.setData(bundle);
                                    getProgramBrewingHandler().sendMessage(msg);

                                    mRunBrew = true;
                                    mProgramState = mStateRunBrewingRequest;
                                }
                            }
                            break;
                        }
                        case mStateRunBrewingRequest: {
                            clearMessageList();
                            messageBuffer = "";
                            Log.d(TAG, "ProgramRunner::mStateRunBrewingRequest");
                            sendToDevice("a\r\n");

                            //writeString(getFilePathData()+"/" + "debugLog.txt","Start \n");

                            try {
                                Thread.sleep(Constants.PAUSE_TILL_CMD);
                            } catch (InterruptedException e) {
                            }

                            if ( mStateStopBrewing != mProgramState)
                                mProgramState = mStateRunBrewingReceive;
                            break;
                        }
                        case mStateRunBrewingReceive: {
                            if ( receivedMessages() > 0 ) {
                                messageBuffer += getFirstMessage();

                                //writeString(getFilePathData()+"/" + "debugLog.txt",messageBuffer+"\n");

                                if (isMessageComplete(messageBuffer)) {
                                    Log.d(TAG, "ProgramRunner::mStateRunBrewingReceive messageBuffer:"+messageBuffer);
                                    if ( mSettings.stateMenu ==
                                            mSettings.getStateFromMessage(messageBuffer) ) {
                                        mRunBrew = false;
                                        mProgramState = mStateMainMenu;

                                        if ( getProgramBrewingHandler() != null ) {
                                            Message msg = getProgramBrewingHandler()
                                                    .obtainMessage(Constants.MESSAGE_STOP);
                                            Bundle bundle = new Bundle();
                                            bundle.putString(Constants.TEXT, messageBuffer);
                                            msg.setData(bundle);
                                            getProgramBrewingHandler().sendMessage(msg);
                                        }

                                        Log.d(TAG, "ProgramRunner::mStateRunBrewingReceive Ende:"+messageBuffer);
                                        writeString(getFilePathData()+"/" + "debugLog.txt",
                                                "ProgramRunner::mStateRunBrewingReceive Ende: " + messageBuffer+"\n");
                                    } else if ( mSettings.stateRun ==
                                            mSettings.getStateFromMessage(messageBuffer) ) {
                                        mRunBrew = true;

                                        if ( getProgramBrewingHandler() != null ) {
                                            Message msg = getProgramBrewingHandler()
                                                    .obtainMessage(Constants.MESSAGE_TEXT);
                                            Bundle bundle = new Bundle();
                                            bundle.putString(Constants.TEXT, messageBuffer);
                                            msg.setData(bundle);
                                            getProgramBrewingHandler().sendMessage(msg);

                                        }

                                        if ( mStateStopBrewing != mProgramState)
                                            mProgramState = mStateRunBrewingRequest;

                                    } else {
//                                        mRunBrew = false;
//                                        if ( mStateStopBrewing != mProgramState) {
//                                            mProgramState = mStateErrorBrewing;
//                                            mLastErrorStr = "mStateRunBrewingReceive";
//                                        }
                                    }
                                }
                            } else {
				                Log.d(TAG, "ProgramRunner::mStateRunBrewingReceive No message received:"+messageBuffer);
                                writeString(getFilePathData()+"/" + "debugLog.txt",
                                        "ProgramRunner::mStateRunBrewingReceive No message received:" + messageBuffer+"\n");
                                if ( mStateStopBrewing != mProgramState)
                                    mProgramState = mStateRunBrewingRequest;
                            }

                            try {
                                Thread.sleep(Constants.PAUSE_TILL_CMD);
                            } catch (InterruptedException e) {
                            }

                            break;
                        }
                        case mStateStopBrewing: {
                            mRunBrew = false;
                            clearMessageList();
                            messageBuffer = "";

                            Log.d(TAG, "ProgramRunner::mStateStopBrewing");
                            writeString(getFilePathData()+"/" + "debugLog.txt","ProgramRunner::mStateStopBrewing\n");

                            sendToDevice("r\r\n");
                            try {
                                Thread.sleep(Constants.PAUSE_TILL_CMD);
                            } catch (InterruptedException e) {
                            }

                            mProgramState = mStateStopBrewingWait;

                            break;
                        }
                        case mStateStopBrewingWait: {
                            if ( receivedMessages() > 0 ) {
                                messageBuffer += getFirstMessage();
                                if ( mSettings.stateRun ==
                                        mSettings.getStateFromMessage(messageBuffer) ) {
                                    mProgramState = mStateStopBrewing;
                                } else {
                                    mProgramState = mStateMainMenu;

                                    if ( getProgramBrewingHandler() != null ) {
                                        Message msg = getProgramBrewingHandler()
                                                .obtainMessage(Constants.MESSAGE_STOP);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(Constants.TEXT, messageBuffer);
                                        msg.setData(bundle);
                                        getProgramBrewingHandler().sendMessage(msg);
                                    }
                                }
                            }
                            break;
                        }
                        case mStateErrorBrewing: {
                            mRunBrew = false;
                            Log.d(TAG, "ProgramRunner::mStateErrorBrewing");
                            if ( mInitialized )
                                mProgramState = mStateStopBrewing;
                            else
                                mProgramState = mStateUndefined;

                            writeString(getFilePathData()+"/" + "debugLog.txt","Error Brewing:"+
                                    mLastErrorStr + " " + messageBuffer+"\n");

                            break;
                        }
                        default:
                        {
                            setEndThread(false);
                        }
                    }
                }
            }
        }

        public void setPause(boolean pause) {
            this.pauseThread = pause;
        }

        public boolean isPause() {
            return pauseThread;
        }

        public boolean isEndThread() {
            return endThread;
        }

        public void setEndThread(boolean endThread) {
            this.endThread = endThread;
        }



        public void reset() {
            this.result = 0;
            mProgramState = mStateUndefined;
        }
    }

    public boolean writeString(String filePath,String text) {
        if ( mDebugFileIsOn ) {
            FileOutputStream fos;
            File tempFile = new File(filePath);

            Date date = java.util.Calendar.getInstance().getTime();

            SimpleDateFormat dateFormatter =
                    new SimpleDateFormat("dd.MM.yyyy");
            String dateString = dateFormatter.format(date);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String uhrzeit = sdf.format(date);

            text = dateString + "-" + uhrzeit + "-" + text;
            try {
                fos = new FileOutputStream(tempFile, true);

                fos.write(text.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                Log.w(TAG, "FileOutputStream FileNotFoundException exception: - " + e.toString());
                return false;
            } catch (IOException e) {
                Log.w(TAG, "FileOutputStream IOException exception: - " + e.toString());
                return false;
            }
        }
        return true;
    }
}
