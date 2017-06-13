// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainMenu extends AppCompatActivity {
    private static final String TAG = "MainMenu";
    private static Button mBtn_connect;
    private static Button mBtn_setup;
    private static Button mBtn_recipe;
    private static Button mBtn_start;
    private static Button mBtn_manual;
    private static Button mBtn_end;
    private static TextView mTextview;
    private static TextView mTextviewVersionApp;
    private static TextView mTextviewIPAddress;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private boolean mConnectingBT = false;
    private String mBtAddress = "";

    private GlobalVars getGlobalVars() {
        return (GlobalVars)getApplication();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mBtAddress = "";

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.colorApp));

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (notification == null) {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }
        getGlobalVars().setPlayer(
                MediaPlayer.create(getApplicationContext(), notification),
                RingtoneManager.getRingtone(getApplicationContext(), notification));
        getGlobalVars().setProgramRunnerHandler(handlerUI);
        getGlobalVars().getSettings().createBrewSteps(getString(R.string.title_recipe_name));

        onClickButtonListener();

        getWritePermissions();

        getGlobalVars().setFilePath(SimpleFileDialog.getFilePath(getApplicationContext()));

        getGlobalVars().startHttpServer();

    }

    public void onClickButtonListener() {

        mBtn_connect = (Button)findViewById(R.id.bt_id_connect);
        mBtn_connect.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                getGlobalVars().resetWorker();
                                                if (getGlobalVars().isChatConnected()) {
                                                    getGlobalVars().stopChatService();
                                                    getGlobalVars().giveAlarm(false);
                                                } else {
                                                    if (getGlobalVars().getBluetoothAdapter() == null) {
                                                        //getGlobalVars().getSettings().createBrewSteps(getString(R.string.title_recipe_name));
                                                        Toast.makeText(getApplicationContext(),
                                                                "Bluetooth is not available",
                                                                Toast.LENGTH_LONG).show();
                                                    } else {
                                                        if (!getGlobalVars().getBluetoothAdapter().isEnabled()) {
                                                            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                                            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                                                            // Otherwise, setup the chat session
                                                        } else if ( mBtAddress.isEmpty() ){
                                                            Intent serverIntent = new Intent(
                                                                    getApplicationContext(),
                                                                    DeviceListActivity.class);
                                                            startActivityForResult(
                                                                    serverIntent,
                                                                    REQUEST_CONNECT_DEVICE_INSECURE);
                                                        } else {
                                                            mConnectingBT = true;
                                                            checkState();
                                                            showText();
                                                            getGlobalVars().connectDevice(mBtAddress);
                                                        }
                                                    }


                                                }
                                                checkState();
                                                showText();
                                            }
                                        }
        );

        mBtn_setup = (Button)findViewById(R.id.bt_id_setting);
        mBtn_setup.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent("de.mikrosikaru.brausteuerungapp.SetupMenu");
                                              startActivity(intent);
                                          }
                                      }
        );

        mBtn_recipe = (Button)findViewById(R.id.bt_id_rast);
        mBtn_recipe.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent("de.mikrosikaru.brausteuerungapp.RecipesActivity");
                                             startActivity(intent);
                                         }
                                     }
        );

        mBtn_start = (Button)findViewById(R.id.bt_id_start);
        mBtn_start.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent("de.mikrosikaru.brausteuerungapp.RunActivity");
                                             startActivity(intent);
                                         }
                                     }
        );

        mBtn_manual = (Button)findViewById(R.id.btn_id_main_manual);
        mBtn_manual.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              String url = "http://" + getGlobalVars().getSettings().getAppDevice();
                                              Intent i = new Intent(Intent.ACTION_VIEW);
                                              i.setData(Uri.parse(url));
                                              startActivity(i);
                                          }
                                      }
        );

        mBtn_end = (Button)findViewById(R.id.btn_id_main_end);
        mBtn_end.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            getGlobalVars().stopHttpServer();
                                            getGlobalVars().giveAlarm(false);
                                            getGlobalVars().stopChatService();
                                            getGlobalVars().endThread();
                                            getGlobalVars().getSettings().setBrewRecipeDescription("");
                                            checkState();
                                            showText();
                                            finish();
                                        }
                                    }
        );

        mTextview = (TextView) findViewById(R.id.mainmenu_id_textView);
        mTextview.setSingleLine(false);

        mTextviewVersionApp = (TextView) findViewById(R.id.mainmenu_id_tv_VersionApp);
        mTextviewIPAddress = (TextView) findViewById(R.id.id_main_ipaddress);

        checkState();
        showText();
    }

    public void getWritePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (
                    ContextCompat.checkSelfPermission(
                            this.getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(
                                    this.getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        new String[]{   android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            } else {
                //do something
            }
        } else {
            //do something
        }
    }

    private String getLocalAddress() {
        String ipAddress = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String sAddr = inetAddress.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (isIPv4) {
                            ipAddress = sAddr;
                        } else {
                            int delim = sAddr.indexOf('%');
                            ipAddress = delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IP Address", ex.toString());
        }
        return ipAddress;
    }

    public void enableButtons(
            boolean connect,
            boolean setup,
            boolean recipe,
            boolean start) {
        mBtn_recipe.setEnabled(recipe);
        mBtn_setup.setEnabled(setup);
        mBtn_start.setEnabled(start);
        mBtn_connect.setEnabled(connect);
    }
    public void showText() {
        mTextviewVersionApp.setText(
                getGlobalVars().getSettings().getAppDevice()
                + " " + getGlobalVars().getSettings().getAppName()
                        + " " + getGlobalVars().getSettings().getAppVersion() );

        if ( 0 != getGlobalVars().getIpAdresse().length() )
            mTextviewIPAddress.setText("IP: " +getGlobalVars().getIpAdresse() + ":" + Constants.HTTP_PORT);

        if (getGlobalVars().isChatConnected()) {
            String msg = getGlobalVars().getConnectedDeviceName()
                    + " "
                    + getGlobalVars().getSettings().getVersion()
                    + "-"
                    + getGlobalVars().getConnectedMacAdressConverted();
            mTextview.setText(msg);

            mBtn_connect.setText(getString(R.string.bt_str_disconnect));

            //if ( getGlobalVars().isRunBrew() )
            //    mTextviewState.setText("Start");
        } else {
            mTextview.setText("");
            mBtn_connect.setText(getString(R.string.bt_str_connect));
        }
    }

    private void checkState() {
        if (getGlobalVars().getBluetoothAdapter() != null) {
            if ( getGlobalVars().isChatConnected() ) {
                if ( getGlobalVars().isRunBrew() ) {
                    enableButtons(false,false,false,true);
                } else if ( getGlobalVars().isTransferRecipe() ) {
                    enableButtons(false, false, false, false);
                } else if ( getGlobalVars().isTransferInit() ) {
                    enableButtons(false, false, false, false);
                } else {
                    enableButtons(true,true,true,true);
                }
            } else if ( mConnectingBT ) {
                enableButtons(false,false,false,false);
            } else {
                enableButtons(true,false,true,false);
            }
        } else {
            enableButtons(true,false,true,false);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        getGlobalVars().stopChatService();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkState();
        showText();
    }

    @Override
    public void onPause() {
        super.onPause();

    }
    public Handler handlerUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, String.format("Handler.handleMessage(): msg=%s", msg));
            if (msg.what == Constants.MESSAGE_activateButtons) {
                checkState();
                showText();                
            }else if (msg.what == Constants.MESSAGE_RecipeTransfered) {
			   String tmpFileName = SimpleFileDialog.getFilePath(getApplicationContext());
  			   tmpFileName += "/" + getApplicationContext().getString(R.string.file_str_file_defaut_name);
			   tmpFileName += getGlobalVars().getConnectedMacAdressConverted();
			   tmpFileName += "." + getString(R.string.file_str_file_ending);

  			   BrewRecipeFile recipeFile = new BrewRecipeFileBml();
			   if ( !recipeFile.Save(getGlobalVars().getSettings(),tmpFileName)) {
					 Toast.makeText(getApplicationContext(),
							 getString(R.string.file_str_file_save_err) +
									 tmpFileName, Toast.LENGTH_LONG).show();
			   }
               checkState();
               showText();
            }else if (msg.what == Constants.MESSAGE_disableButtons) {
                checkState();
                showText();
            } else if ( msg.what == Constants.MESSAGE_BtLost) {
                mConnectingBT = false;
                checkState();
                showText();
            } else if (msg.what == Constants.MESSAGE_disableButtonsAll) {
                checkState();
                showText();
            } else if (msg.what == Constants.MESSAGE_NotInitialized) {
                mConnectingBT = false;
                checkState();
                showText();
            } else if (msg.what == Constants.MESSAGE_isBrewRunning ) {
                checkState();
                showText();
                if (null != getApplicationContext()) {

                    String tmpFileName = SimpleFileDialog.getFilePath(getApplicationContext());
                    tmpFileName += "/" + getApplicationContext().getString(R.string.file_str_file_defaut_name);
                    tmpFileName += getGlobalVars().getConnectedMacAdressConverted();
                    tmpFileName += "." + getString(R.string.file_str_file_ending);
                    BrewRecipeFile recipdeFile = new BrewRecipeFileBml();
                    if ( SimpleFileDialog.file_exists(tmpFileName)) {
                        if (!recipdeFile.Load(getGlobalVars().getSettings(),tmpFileName)) {
                            Toast.makeText(getApplicationContext(), getString(R.string.file_str_file_load_err)+
                                    recipdeFile.getError(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                Intent intent = new Intent("de.mikrosikaru.brausteuerungapp.RunActivity");
                startActivity(intent);
            } else if (msg.what == Constants.MESSAGE_TOAST ) {
                if (null != getApplicationContext()) {
                    Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == Constants.MESSAGE_initializedEnd ) {
                getGlobalVars().getSettings().copySettings2Orig();
                mConnectingBT = false;
                String tmpFileName = SimpleFileDialog.getFilePath(getApplicationContext());
                tmpFileName += "/" + getApplicationContext().getString(R.string.file_str_file_defaut_name);
                tmpFileName += getGlobalVars().getConnectedMacAdressConverted();
                tmpFileName += "." + getString(R.string.file_str_file_ending);

                if ( SimpleFileDialog.file_exists(tmpFileName)) {
                    BrewRecipeFileBml recipeFileBml = new BrewRecipeFileBml();
                    if (!recipeFileBml.Load(getGlobalVars().getSettings(),tmpFileName)) {
                        Toast.makeText(getApplicationContext(), getString(R.string.file_str_file_load_err),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_INSECURE:
                getGlobalVars().setupChat();
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    mBtAddress = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    if ( mBtAddress.isEmpty() ) {
                        mConnectingBT = false;
                    } else {
                        mConnectingBT = true;
                        getGlobalVars().connectDevice(mBtAddress);
                    }
                    checkState();
                    showText();
                } else {
                    mConnectingBT = false;
                    mBtAddress = "";
                    checkState();
                    showText();
                }

                break;
            case REQUEST_ENABLE_BT:
                mBtAddress = "";
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chgetApplicationContext()at session
                    Intent serverIntent = new Intent(
                            getApplicationContext(),
                            DeviceListActivity.class);
                    startActivityForResult(
                            serverIntent,
                            REQUEST_CONNECT_DEVICE_INSECURE);
                } else {
                    mBtAddress = "";
                    // User did not enable Bluetooth or an error occurred
                    mConnectingBT = false;
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getApplicationContext(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                }
        }
    }
}
