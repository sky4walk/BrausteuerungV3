// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RunActivity extends AppCompatActivity {
    private static final String TAG = "RunActivity";
    private int mSystemStartTime;
    private static Spinner mRastSpinner;
    private static Button mBtn_start;
    private static Button mBtn_weiter;
    private static Button mBtn_mute;
    private static Button mBtn_back;
    private static TextView mEditTextIstTempOut;
    private static TextView mEditTextSollTempOut;
    private static TextView mEditTextGradientOut;
    private static TextView mEditTextTimerOut;
    private static TextView mEditTextStatusOut;
    private static TextView mEditTextBatterieOut;
    private static TextView mEditTextInfosOut;
    private static TextView mEditTextHeaterState;
    private static MediaPlayer mPlayer;
    private static Ringtone mRingtone;
    private boolean mTicToc = false;
    private int mChooseRastNr = 0;
    private GlobalVars getGlobalVars() {
        return (GlobalVars)getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(getResources().getColor(R.color.colorApp));

        String versionLog = getGlobalVars().getSettings().getAppDevice();
        versionLog += " " + getGlobalVars().getSettings().getAppName();
        versionLog += " " + getGlobalVars().getSettings().getAppVersion();
        versionLog += " " + getGlobalVars().getConnectedDeviceName();
        versionLog += " " + getGlobalVars().getSettings().getVersion();
        versionLog += " " + getGlobalVars().getSettings().getSerialNr();
        versionLog += " " + getGlobalVars().getConnectedMacAdressConverted();
        getGlobalVars().getBrewState().setVersionString(versionLog);


        mRastSpinner            = (Spinner) findViewById(R.id.run_spinner);
        mBtn_start              = (Button) findViewById(R.id.btn_run_id_start);
        mBtn_weiter             = (Button) findViewById(R.id.btn_run_weiter);
        mBtn_mute               = (Button) findViewById(R.id.btn_run_call_stop);
        mBtn_back               = (Button) findViewById(R.id.run_btn_back);
        mEditTextIstTempOut     = (TextView) findViewById(R.id.run_id_temp_ist_out);
        mEditTextSollTempOut    = (TextView) findViewById(R.id.run_id_temp_soll_out);
        mEditTextGradientOut    = (TextView) findViewById(R.id.run_id_gradient_out);
        mEditTextTimerOut       = (TextView) findViewById(R.id.run_id_time_out);
        mEditTextStatusOut      = (TextView) findViewById(R.id.run_id_status_out);
        mEditTextBatterieOut    = (TextView) findViewById(R.id.run_id_battery_out);
        mEditTextInfosOut       = (TextView) findViewById(R.id.run_id_info_out);
        mEditTextHeaterState    = (TextView) findViewById(R.id.run_id_heaterstate_out);

        mBtn_start.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             mBtn_start.setEnabled(false);
                                             if ( getGlobalVars().isRunBrew() ) {
                                                 getGlobalVars().stopBrewing();
                                                 getGlobalVars().giveAlarm(false);
                                             } else {
                                                 getGlobalVars().startBrewing(mChooseRastNr);
                                             }
                                         }
                                     }
        );
        mBtn_weiter.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              getGlobalVars().goFurther();
                                              getGlobalVars().giveAlarm(false);
                                          }
                                      }
        );
        mBtn_mute.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               getGlobalVars().stopCalling();
                                               getGlobalVars().giveAlarm(false);
                                           }
                                       }
        );

        mBtn_back.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             finish();
                                         }
                                     }
        );


        getGlobalVars().giveAlarm(false);

        fillSpinnerRast();
        checkState();


        getGlobalVars().setProgramBrewingHandler(handlerUI);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkState();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void checkState() {
        if (getGlobalVars().isChatConnected()) {
            if (getGlobalVars().isRunBrew() ) {
                if ( mTicToc )  mTicToc = false;
                else            mTicToc = true;
                enableButtons(false, true, getGlobalVars().getBrewState().isWeiter(), getGlobalVars().getBrewState().isCall());
                if ( getGlobalVars().getBrewState().isCall() || getGlobalVars().getBrewState().isBatLow() )
                    getGlobalVars().giveAlarm(true);
                else
                    getGlobalVars().giveAlarm(false);
            } else {
                enableButtons(true, true, false, false);
            }
        } else {
            enableButtons(true, false,false,false);
            getGlobalVars().giveAlarm(false);
        }
    }
    private void enableButtons(
            boolean spinner,
            boolean start,
            boolean weiter,
            boolean mute) {
//        mRastSpinner.setEnabled(spinner);
        mRastSpinner.setClickable(spinner);
        mBtn_weiter.setEnabled(weiter);
        mBtn_mute.setEnabled(mute);
        mBtn_start.setEnabled(start);
    }
    private void showInfos() {
        if (getGlobalVars().isRunBrew() ) {
            mBtn_start.setText(R.string.btn_run_str_stop);
            if ( getGlobalVars().getBrewState().getNr() < getGlobalVars().getSettings().getBrewSteps() ) {
                mRastSpinner.setSelection(getGlobalVars().getBrewState().getNr());
                mEditTextInfosOut.setText(
                        getGlobalVars().getSettings().getBrewStep(getGlobalVars().getBrewState().getNr()).getInfoField());
            } else {
                mRastSpinner.setSelection(getGlobalVars().getSettings().getBrewSteps() - 1);
                mEditTextInfosOut.setText("");
            }
            mEditTextIstTempOut.setText(Float.toString(getGlobalVars().getBrewState().getActTemp()));
            mEditTextSollTempOut.setText(Float.toString(getGlobalVars().getBrewState().getSollTemp()));
            mEditTextGradientOut.setText(Float.toString(getGlobalVars().getBrewState().getGradient()));

            if ( getGlobalVars().getBrewState().isOnOff() ) {
                mEditTextHeaterState.setText(getString(R.string.txt_run_str_HeaterState_on));
            } else {
                mEditTextHeaterState.setText(getString(R.string.txt_run_str_HeaterState_off));
            }

            if (getGlobalVars().getBrewState().isActiveRest()) {
                mEditTextTimerOut.setText(getGlobalVars().getBrewState().getTimeRestMinSek());
            } else {
                mEditTextTimerOut.setText("");
            }

            if ( getGlobalVars().getBrewState().isBatLow() ) {
                mEditTextStatusOut.setText(getString(R.string.txt_run_str_BatLow));
            } else if ( getGlobalVars().getBrewState().isCall() ) {
                mEditTextStatusOut.setText(getString(R.string.txt_run_str_Call));
            } else if ( getGlobalVars().getBrewState().isWeiter() ) {
                mEditTextStatusOut.setText(getString(R.string.txt_run_str_Wait));
            } else {
                if ( mTicToc )
                    mEditTextStatusOut.setText("-");
                else
                    mEditTextStatusOut.setText("|");
            }


            float batLowLevel = getGlobalVars().getSettings().getBatLowLevel();
            float batActLevel = getGlobalVars().getBrewState().getBatLevel();

            if ( batActLevel > Constants.maxBatteryLevel )
                batActLevel = Constants.maxBatteryLevel;

            if ( Constants.maxBatteryLevel > batLowLevel ) {
                int batLevelPerc = (int) (100 * (
                        (batActLevel - batLowLevel) /
                                (Constants.maxBatteryLevel - batLowLevel)));
                mEditTextBatterieOut.setText(Integer.toString(batLevelPerc));
            }


            Date date = java.util.Calendar.getInstance().getTime();

            SimpleDateFormat dateFormatter =
                    new SimpleDateFormat("dd.MM.yyyy");
            String dateString = dateFormatter.format(date);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String uhrzeit = sdf.format(date);

            String filePath = getGlobalVars().getFilePathData();
            filePath += "/" + getString(R.string.run_str_logfilename);
            filePath += "_" + dateString + ".csv";
            getGlobalVars().getBrewState().appendState(filePath,uhrzeit);

        } else {
            mBtn_start.setText(R.string.btn_run_str_start);
            mEditTextIstTempOut.setText("");
            mEditTextSollTempOut.setText("");
            mEditTextGradientOut.setText("");
            mEditTextTimerOut.setText("");
            mEditTextStatusOut.setText("");
            mEditTextBatterieOut.setText("");
            mEditTextInfosOut.setText("");
            mEditTextHeaterState.setText("");
        }
    }
    private void fillSpinnerRast() {
        List<String> list = new ArrayList<String>();
        for ( int i = 0; i < getGlobalVars().getSettings().getBrewSteps(); i++) {
            String name = getGlobalVars().getSettings().getBrewStep(i).getName();
            list.add(name);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        mRastSpinner.setAdapter(dataAdapter);

        mRastSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                mChooseRastNr = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }



    public Handler handlerUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, String.format("Handler.handleMessage(): msg=%s", msg));
            if (msg.what == Constants.MESSAGE_activateButtons ) {
            }else if (msg.what == Constants.MESSAGE_START ) {
                checkState();
            }else if (msg.what == Constants.MESSAGE_STOP) {
                checkState();
                showInfos();
            }else if (msg.what == Constants.MESSAGE_disableButtons) {
            } else if (msg.what == Constants.MESSAGE_disableButtonsAll ) {
                enableButtons(false,false,false,false);
            }else if (msg.what == Constants.MESSAGE_TOAST ) {
                if (null != getApplicationContext()) {
                    Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                }
            }else if (msg.what == Constants.MESSAGE_TEXT ) {
                if (null != getApplicationContext()) {
                    getGlobalVars().getBrewState().setInfos(msg.getData().getString(Constants.TEXT));
                    checkState();
                    showInfos();
                }
            }else if (msg.what == Constants.MESSAGE_BtLost ) {
                if (null != getApplicationContext()) {
                    checkState();
                    showInfos();
                    getGlobalVars().giveAlarm(true);
                    finish();
                }
            }
            super.handleMessage(msg);
        }
    };

}
