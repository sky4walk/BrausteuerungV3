// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class SetupActivity extends AppCompatActivity {
    private static Button mBtn_back;
    private static Button mBtn_store;
    private static CheckBox mChB_pid;
    private static EditText mEditTextSettingKalM;
    private static EditText mEditTextSettingKalT;
    private static EditText mEditTextSettingPidWndSize;
    private static EditText mEditTextSettingPidSampleTime;
    private static EditText mEditTextSettingPidDelta;
//    private static EditText mEditTextSettingNtcT0;
//    private static EditText mEditTextSettingNtcR0;
//    private static EditText mEditTextSettingNtcT1;
//    private static EditText mEditTextSettingNtcR1;
//    private static EditText mEditTextSettingNtcVorR;
//    private static EditText mEditTextSettingTuneStep;
//    private static EditText mEditTextSettingTuneNoise;
//    private static EditText mEditTextSettingLookBack;
    private static EditText mEditTextSettingSwitchType;
    private static EditText mEditTextSettingSwitchBits;
    private static EditText mEditTextSettingSwitchRepeats;
    private static EditText mEditTextSettingSwitchPeriodic;
    private static EditText mEditTextSettingSwitchAddress;
    private static EditText mEditTextSettingSwitchUnit;
    private static EditText mEditTextSettingBatLow;
    private static EditText mEditTextSettingPassword;

    private GlobalVars getGlobalVars() {
        return (GlobalVars)getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        setupFields();
        readSettingValues();
        onClickButtonListener();
    }


    private void setupFields() {
        mBtn_back = (Button) findViewById(R.id.btn_id_setup_back);
        mBtn_store = (Button) findViewById(R.id.btn_id_setup_store);
        mChB_pid = (CheckBox) findViewById(R.id.setup_id_spi);
        mEditTextSettingKalM = (EditText) findViewById(R.id.setting_id_et_skm);
        mEditTextSettingKalT = (EditText) findViewById(R.id.setting_id_et_skt);

//        mEditTextSettingNtcT0 = (EditText) findViewById(R.id.setting_id_et_stz);
//        mEditTextSettingNtcR0 = (EditText) findViewById(R.id.setting_id_et_srz);
//        mEditTextSettingNtcT1 = (EditText) findViewById(R.id.setting_id_et_sto);
//        mEditTextSettingNtcR1 = (EditText) findViewById(R.id.setting_id_et_sro);
//        mEditTextSettingNtcVorR = (EditText) findViewById(R.id.setting_id_et_svw);
//        mEditTextSettingTuneStep = (EditText) findViewById(R.id.setting_id_et_sts);
//        mEditTextSettingTuneNoise = (EditText) findViewById(R.id.setting_id_et_stn);
//        mEditTextSettingLookBack = (EditText) findViewById(R.id.setting_id_et_slb);
        mEditTextSettingPidWndSize = (EditText) findViewById(R.id.setting_id_et_sps);
        mEditTextSettingPidSampleTime = (EditText) findViewById(R.id.setting_id_et_spt);
        mEditTextSettingPidDelta = (EditText) findViewById(R.id.setting_id_et_spd);
        mEditTextSettingSwitchType = (EditText) findViewById(R.id.setting_id_et_sst);
        mEditTextSettingSwitchBits = (EditText) findViewById(R.id.setting_id_et_ssb);
        mEditTextSettingSwitchRepeats = (EditText) findViewById(R.id.setting_id_et_ssr);
        mEditTextSettingSwitchPeriodic = (EditText) findViewById(R.id.setting_id_et_ssp);
        mEditTextSettingSwitchAddress = (EditText) findViewById(R.id.setting_id_et_ssa);
        mEditTextSettingSwitchUnit = (EditText) findViewById(R.id.setting_id_et_ssu);
        mEditTextSettingBatLow = (EditText) findViewById(R.id.setting_id_et_sbl);
        mEditTextSettingPassword = (EditText) findViewById(R.id.setting_id_et_spw);
    }
    private void readSettingValues() {
        mEditTextSettingKalM.setText(
                Float.toString(getGlobalVars().getSettings().getKalM()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingKalT.setText(
                Float.toString(getGlobalVars().getSettings().getKalT()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingPidWndSize.setText(
                Integer.toString(getGlobalVars().getSettings().getPidWindowSize()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingPidSampleTime.setText(
                Integer.toString(getGlobalVars().getSettings().getPidSampleTime()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingPidDelta.setText(
                Float.toString(getGlobalVars().getSettings().getPidDelta()),
                TextView.BufferType.EDITABLE);
//        mEditTextSettingNtcT0.setText(
//                Float.toString(getGlobalVars().getSettings().getNtcT0()),
//                TextView.BufferType.EDITABLE);
//        mEditTextSettingNtcR0.setText(
//                Float.toString(getGlobalVars().getSettings().getNtcR0()),
//                TextView.BufferType.EDITABLE);
//        mEditTextSettingNtcT1.setText(
//                Float.toString(getGlobalVars().getSettings().getNtcT1()),
//                TextView.BufferType.EDITABLE);
//        mEditTextSettingNtcR1.setText(
//                Float.toString(getGlobalVars().getSettings().getNtcR1()),
//                TextView.BufferType.EDITABLE);
//        mEditTextSettingNtcVorR.setText(
//                Integer.toString(getGlobalVars().getSettings().getVorR()),
//                TextView.BufferType.EDITABLE);
//        mEditTextSettingTuneStep.setText(
//                Float.toString(getGlobalVars().getSettings().getTuneStep()),
//                TextView.BufferType.EDITABLE);
//        mEditTextSettingTuneNoise.setText(
//                Float.toString(getGlobalVars().getSettings().getTuneNoise()),
//                TextView.BufferType.EDITABLE);
//        mEditTextSettingLookBack.setText(
//                Integer.toString(getGlobalVars().getSettings().getTuneLookBack()),
//                TextView.BufferType.EDITABLE);
        mEditTextSettingSwitchType.setText(
                Integer.toString(getGlobalVars().getSettings().getSwitchType()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingSwitchBits.setText(
                Integer.toString(getGlobalVars().getSettings().getSwitchBits()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingSwitchRepeats.setText(
                Integer.toString(getGlobalVars().getSettings().getSwitchRepeats()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingSwitchPeriodic.setText(
                Integer.toString(getGlobalVars().getSettings().getSwitchPeriodusec()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingSwitchAddress.setText(
                Integer.toString(getGlobalVars().getSettings().getSwitchAddress()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingSwitchUnit.setText(
                Integer.toString(getGlobalVars().getSettings().getSwitchUnit()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingBatLow.setText(
                Float.toString(getGlobalVars().getSettings().getBatLowLevel()),
                TextView.BufferType.EDITABLE);
        mEditTextSettingPassword.setText(
                Integer.toString(getGlobalVars().getSettings().getPasswd()),
                TextView.BufferType.EDITABLE);

//        if ( getGlobalVars().getSettings().isNtc()){
//        } else {
//            enableEditText(mEditTextSettingNtcT0,false);
//            enableEditText(mEditTextSettingNtcR0,false);
//            enableEditText(mEditTextSettingNtcT1,false);
//            enableEditText(mEditTextSettingNtcR1,false);
//            enableEditText(mEditTextSettingNtcVorR,false);
//        }

        if ( getGlobalVars().getSettings().isPid()){
            mChB_pid.setChecked(true);
            enableEditText(mEditTextSettingPidWndSize,true);
            enableEditText(mEditTextSettingPidSampleTime,true);
            enableEditText(mEditTextSettingPidDelta,true);
        } else {
            mChB_pid.setChecked(false);
            enableEditText(mEditTextSettingPidWndSize,false);
            enableEditText(mEditTextSettingPidSampleTime,false);
            enableEditText(mEditTextSettingPidDelta,false);
//            enableEditText(mEditTextSettingTuneStep,false);
//            enableEditText(mEditTextSettingTuneNoise,false);
//            enableEditText(mEditTextSettingLookBack,false);
        }
    }

    private void enableEditText(EditText editText, boolean enable) {
        if (enable) { // disable editing password
            editText.setEnabled(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setClickable(true);
            editText.setCursorVisible(true);
            editText.setVisibility(View.VISIBLE);
        }
        else { // enable editing of password
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            editText.setClickable(false); // user navigates with wheel and selects widget
            editText.setEnabled(false);
            editText.setCursorVisible(false);
            editText.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickButtonListener() {
        mBtn_back.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             getGlobalVars().getSettings().copyOrig2Settings();
                                             finish();
                                         }
                                     }
        );
        mBtn_store.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                         public void onClick(View v) {
                                             getGlobalVars().getSettings().copySettings2Orig();
                                             getGlobalVars().startSettingStore();
                                             finish();
                                         }
                                     }
        );

        mChB_pid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    getGlobalVars().getSettings().setPid(true);
                    enableEditText(mEditTextSettingPidWndSize,true);
                    enableEditText(mEditTextSettingPidSampleTime,true);
                    enableEditText(mEditTextSettingPidDelta,true);
//                    enableEditText(mEditTextSettingTuneStep,true);
//                    enableEditText(mEditTextSettingTuneNoise,true);
//                    enableEditText(mEditTextSettingLookBack,true);
                } else {
                    getGlobalVars().getSettings().setPid(false);
                    enableEditText(mEditTextSettingPidWndSize,false);
                    enableEditText(mEditTextSettingPidSampleTime,false);
                    enableEditText(mEditTextSettingPidDelta,false);
//                    enableEditText(mEditTextSettingTuneStep,false);
//                    enableEditText(mEditTextSettingTuneNoise,false);
//                    enableEditText(mEditTextSettingLookBack,false);
                }
            }
        });

        mEditTextSettingKalM.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingKalM.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float value = Float.parseFloat(s.toString());
                    getGlobalVars().getSettings().setKalM(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingKalM.setText(oldValue);
                }
            }
        });
        mEditTextSettingKalT.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingKalT.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float value = Float.parseFloat(s.toString());
                    getGlobalVars().getSettings().setKalT(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingKalT.setText(oldValue);
                }
            }
        });
//        mEditTextSettingNtcT0.addTextChangedListener(new TextWatcher() {
//            String oldValue;
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                oldValue = mEditTextSettingNtcT0.getText().toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    float value = Float.parseFloat(s.toString());
//                    getGlobalVars().getSettings().setNtcT0(value);
//                } catch (NumberFormatException e) {
//                    mEditTextSettingNtcT0.setText(oldValue);
//                }
//            }
//        });
//        mEditTextSettingNtcR0.addTextChangedListener(new TextWatcher() {
//            String oldValue;
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                oldValue = mEditTextSettingNtcR0.getText().toString();
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    float value = Float.parseFloat(s.toString());
//                    getGlobalVars().getSettings().setNtcR0(value);
//                } catch (NumberFormatException e) {
//                    mEditTextSettingNtcR0.setText(oldValue);
//                }
//            }
//        });
//
//        mEditTextSettingNtcT1.addTextChangedListener(new TextWatcher() {
//            String oldValue;
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                oldValue = mEditTextSettingNtcT1.getText().toString();
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    float value = Float.parseFloat(s.toString());
//                    getGlobalVars().getSettings().setNtcT1(value);
//                } catch (NumberFormatException e) {
//                    mEditTextSettingNtcT1.setText(oldValue);
//                }
//            }
//        });
//        mEditTextSettingNtcR1.addTextChangedListener(new TextWatcher() {
//            String oldValue;
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                oldValue = mEditTextSettingNtcR1.getText().toString();
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    float value = Float.parseFloat(s.toString());
//                    getGlobalVars().getSettings().setNtcR1(value);
//                } catch (NumberFormatException e) {
//                    mEditTextSettingNtcT1.setText(oldValue);
//                }
//            }
//        });
//        mEditTextSettingNtcVorR.addTextChangedListener(new TextWatcher() {
//            String oldValue;
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                oldValue = mEditTextSettingNtcVorR.getText().toString();
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    int value = Integer.parseInt(s.toString());
//                    getGlobalVars().getSettings().setVorR(value);
//                } catch (NumberFormatException e) {
//                    mEditTextSettingNtcVorR.setText(oldValue);
//                }
//            }
//        });
        mEditTextSettingPidWndSize.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingPidWndSize.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    getGlobalVars().getSettings().setPidWindowSize(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingPidWndSize.setText(oldValue);
                }
            }
        });
        mEditTextSettingPidSampleTime.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingPidSampleTime.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    getGlobalVars().getSettings().setPidSampleTime(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingPidSampleTime.setText(oldValue);
                }
            }
        });
        mEditTextSettingPidDelta.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingPidDelta.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float value = Float.parseFloat(s.toString());
                    getGlobalVars().getSettings().setPidDelta(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingPidDelta.setText(oldValue);
                }
            }
        });
//        mEditTextSettingTuneStep.addTextChangedListener(new TextWatcher() {
//            String oldValue;
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                oldValue = mEditTextSettingTuneStep.getText().toString();
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    float value = Float.parseFloat(s.toString());
//                    getGlobalVars().getSettings().setTuneStep(value);
//                } catch (NumberFormatException e) {
//                    mEditTextSettingTuneStep.setText(oldValue);
//                }
//            }
//        });
//        mEditTextSettingTuneNoise.addTextChangedListener(new TextWatcher() {
//            String oldValue;
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                oldValue = mEditTextSettingTuneNoise.getText().toString();
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    float value = Float.parseFloat(s.toString());
//                    getGlobalVars().getSettings().setTuneNoise(value);
//                } catch (NumberFormatException e) {
//                    mEditTextSettingTuneNoise.setText(oldValue);
//                }
//            }
//        });
//        mEditTextSettingLookBack.addTextChangedListener(new TextWatcher() {
//            String oldValue;
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                oldValue = mEditTextSettingLookBack.getText().toString();
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    int value = Integer.parseInt(s.toString());
//                    getGlobalVars().getSettings().setTuneLookBack(value);
//                } catch (NumberFormatException e) {
//                    mEditTextSettingLookBack.setText(oldValue);
//                }
//            }
//        });
        mEditTextSettingSwitchType.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingSwitchType.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    byte value = Byte.parseByte(s.toString());
                    getGlobalVars().getSettings().setSwitchType(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingSwitchType.setText(oldValue);
                }
            }
        });
        mEditTextSettingSwitchBits.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingSwitchBits.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    byte value = Byte.parseByte(s.toString());
                    getGlobalVars().getSettings().setSwitchBits(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingSwitchBits.setText(oldValue);
                }
            }
        });
        mEditTextSettingSwitchRepeats.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingSwitchRepeats.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    byte value = Byte.parseByte(s.toString());
                    getGlobalVars().getSettings().setSwitchRepeats(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingSwitchRepeats.setText(oldValue);
                }
            }
        });
        mEditTextSettingSwitchPeriodic.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingSwitchPeriodic.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    getGlobalVars().getSettings().setSwitchPeriodusec(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingSwitchPeriodic.setText(oldValue);
                }
            }
        });
        mEditTextSettingSwitchAddress.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingSwitchAddress.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    getGlobalVars().getSettings().setSwitchAddress(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingSwitchAddress.setText(oldValue);
                }
            }
        });
        mEditTextSettingSwitchUnit.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingSwitchUnit.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    getGlobalVars().getSettings().setSwitchUnit(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingSwitchUnit.setText(oldValue);
                }
            }
        });
        mEditTextSettingBatLow.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingBatLow.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float value = Float.parseFloat(s.toString());
                    getGlobalVars().getSettings().setBatLowLevel(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingBatLow.setText(oldValue);
                }
            }
        });
        mEditTextSettingPassword.addTextChangedListener(new TextWatcher() {
            String oldValue;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mEditTextSettingPassword.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    getGlobalVars().getSettings().setPasswd(value);
                } catch (NumberFormatException e) {
                    mEditTextSettingPassword.setText(oldValue);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
