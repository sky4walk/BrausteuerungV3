// brausteuerung@AndreBetz.de
package de.mikrosikaru.brausteuerungapp;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by andre on 03.02.16.
 */
public class RecipeStepFragment extends Fragment {
    private static CheckBox mChEnabled;
    private static CheckBox mChHalt;
    private static CheckBox mChCall;
    private static EditText mETRastName;
    private static EditText mETRastTemp;
    private static EditText mETRastTime;
    private static EditText mETRastMin;
    private static EditText mETRastMax;
    private static EditText mETRastKP;
    private static TextView mTVRastKP;
    private static TextView mTVRastKD;
    private static EditText mETRastKI;
    private static EditText mETRastKD;
    private static EditText mETRastPulseOn;
    private static EditText mETRastPulseOff;
    private static EditText mETRastMaxGradient;
    private static EditText mETRastInfo;

    private int mRastNr = 0;
    private GlobalVars getGlobalVars() {
        return (GlobalVars) getActivity().getApplication();
    }

    public void setRastNumber(int i) {
        mRastNr = i;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_rast, container, false);

        mChEnabled         = (CheckBox) myFragmentView.findViewById(R.id.rast_id_checkBox_onoff);
        mChHalt            = (CheckBox) myFragmentView.findViewById(R.id.rast_id_checkBox_halt);
        mChCall            = (CheckBox) myFragmentView.findViewById(R.id.rast_id_checkBox_call);
        mETRastName        = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_name);
        mETRastTemp        = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_sollTemp);
        mETRastTime        = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_time);
        mETRastMin         = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_minTmp);
        mETRastMax         = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_maxTmp);
        mETRastKP          = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_kp);
        mTVRastKP          = (TextView) myFragmentView.findViewById(R.id.rast_id_textView_kp);
        mTVRastKD          = (TextView) myFragmentView.findViewById(R.id.rast_id_textView_kd);
        mETRastKI          = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_ki);
        mETRastKD          = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_kd);
        mETRastPulseOn     = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_onTimePuls);
        mETRastPulseOff    = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_offTimePuls);
        mETRastMaxGradient = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_maxGradient);
        mETRastInfo        = (EditText) myFragmentView.findViewById(R.id.rast_id_editText_info);


        onClickButtonListener();
//        readSettingValues();
        enablePid();
        return myFragmentView;
    }

    public void onClickButtonListener() {
        mChEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setOn(true);
                } else {
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setOn(false);
                }
            }
        });
        mChHalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setHalt(true);
                } else {
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setHalt(false);
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setCall(false);
                    mChCall.setChecked(false);
                }
            }
        });
        mChCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setCall(true);
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setHalt(true);
                    mChHalt.setChecked(true);
                } else {
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setCall(false);
                }
            }
        });

        mETRastName.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastName.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setName(s.toString());
                } catch (Exception e) {
                    mETRastName.setText(oldValue);
                }
            }
        });

        mETRastTemp.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastTemp.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float value = Float.parseFloat(s.toString());
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setSollTemp(value);
                } catch (NumberFormatException e) {
                    mETRastTemp.setText(oldValue);
                }
            }
        });

        mETRastTime.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastTime.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setTime(value);
                } catch (NumberFormatException e) {
                    mETRastTime.setText(oldValue);
                }
            }
        });

        mETRastMin.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastMin.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float maxVal = getGlobalVars().getSettings().getBrewStep(mRastNr).getMaxTemp();
                    float value = Float.parseFloat(s.toString());
                    if ( value <= maxVal)
                        getGlobalVars().getSettings().getBrewStep(mRastNr).setMinTemp(value);
                    else
                        mETRastMin.setText(oldValue);
                } catch (NumberFormatException e) {
                    mETRastMin.setText(oldValue);
                }
            }
        });
        mETRastMax.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastMax.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float minVal = getGlobalVars().getSettings().getBrewStep(mRastNr).getMinTemp();
                    float value = Float.parseFloat(s.toString());
                    if ( minVal <= value )
                        getGlobalVars().getSettings().getBrewStep(mRastNr).setMaxTemp(value);
                    else
                        mETRastMax.setText(oldValue);
                } catch (NumberFormatException e) {
                    mETRastMax.setText(oldValue);
                }
            }
        });
        mETRastKP.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastKP.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float value = Float.parseFloat(s.toString());
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setKp(value);
                } catch (NumberFormatException e) {
                    mETRastKP.setText(oldValue);
                }
            }
        });
        mETRastKI.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastKI.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float value = Float.parseFloat(s.toString());
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setKi(value);
                } catch (NumberFormatException e) {
                    mETRastKI.setText(oldValue);
                }
            }
        });
        mETRastKD.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastKD.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float value = Float.parseFloat(s.toString());
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setKd(value);
                } catch (NumberFormatException e) {
                    mETRastKD.setText(oldValue);
                }
            }
        });
        mETRastPulseOn.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastPulseOn.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setOnTimePuls(value);
                } catch (NumberFormatException e) {
                    mETRastPulseOn.setText(oldValue);
                }
            }
        });
        mETRastPulseOff.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastPulseOff.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setOffTimePuls(value);
                } catch (NumberFormatException e) {
                    mETRastPulseOff.setText(oldValue);
                }
            }
        });
        mETRastMaxGradient.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastMaxGradient.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Float value = Float.parseFloat(s.toString());
                    getGlobalVars().getSettings().getBrewStep(mRastNr).setMaxGradient(value);
                } catch (NumberFormatException e) {
                    mETRastMaxGradient.setText(oldValue);
                }
            }
        });
        mETRastInfo.addTextChangedListener(new TextWatcher() {
            String oldValue;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldValue = mETRastInfo.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                getGlobalVars().getSettings().getBrewStep(mRastNr).setInfoField(s.toString());
            }
        });
    }

    public void readSettingValues() {

        enablePid();

        mChHalt.setChecked(getGlobalVars().getSettings().getBrewStep(mRastNr).isHalt());
        mChCall.setChecked(getGlobalVars().getSettings().getBrewStep(mRastNr).isCall());
        mChEnabled.setChecked(
                getGlobalVars().getSettings().getBrewStep(mRastNr).isOn() ||
                getGlobalVars().getSettings().getBrewStep(mRastNr).isCall()
        );

        mETRastName.setText(
                getGlobalVars().getSettings().getBrewStep(mRastNr).getName(),
                TextView.BufferType.EDITABLE);

        mETRastTemp.setText(
                Float.toString(getGlobalVars().getSettings().getBrewStep(mRastNr).getSollTemp()),
                TextView.BufferType.EDITABLE);
        mETRastTime.setText(
                Integer.toString(getGlobalVars().getSettings().getBrewStep(mRastNr).getTime()),
                TextView.BufferType.EDITABLE);
        mETRastMin.setText(
                Float.toString(getGlobalVars().getSettings().getBrewStep(mRastNr).getMinTemp()),
                TextView.BufferType.EDITABLE);
        mETRastMax.setText(
                Float.toString(getGlobalVars().getSettings().getBrewStep(mRastNr).getMaxTemp()),
                TextView.BufferType.EDITABLE);
        mETRastKP.setText(
                Float.toString(getGlobalVars().getSettings().getBrewStep(mRastNr).getKp()),
                TextView.BufferType.EDITABLE);
        mETRastKI.setText(
                Float.toString(getGlobalVars().getSettings().getBrewStep(mRastNr).getKi()),
                TextView.BufferType.EDITABLE);
        mETRastKD.setText(
                Float.toString(getGlobalVars().getSettings().getBrewStep(mRastNr).getKd()),
                TextView.BufferType.EDITABLE);
        mETRastPulseOn.setText(
                Integer.toString(getGlobalVars().getSettings().getBrewStep(mRastNr).getOnTimePuls()),
                TextView.BufferType.EDITABLE);
        mETRastPulseOff.setText(
                Integer.toString(getGlobalVars().getSettings().getBrewStep(mRastNr).getOffTimePuls()),
                TextView.BufferType.EDITABLE);
        mETRastMaxGradient.setText(
                Float.toString(getGlobalVars().getSettings().getBrewStep(mRastNr).getMaxGradient()),
                TextView.BufferType.EDITABLE);
        mETRastInfo.setText(
                getGlobalVars().getSettings().getBrewStep(mRastNr).getInfoField(),
                TextView.BufferType.EDITABLE);

    }

    private void enablePid() {
        if (getGlobalVars().getSettings().isPid()) {
            enableEditText(mETRastKP, true);
            enableEditText(mETRastKI, true);
            enableEditText(mETRastKD, true);
            enableEditText(mETRastMin, false);
            enableEditText(mETRastMax, false);
            enableEditText(mETRastPulseOn, false);
            enableEditText(mETRastPulseOff, false);
            enableEditText(mETRastMaxGradient, false);
            enableEditText(mETRastInfo, false);
            mTVRastKP.setText(getString(R.string.rast_str_textView_kp));
            mTVRastKD.setText(getString(R.string.rast_str_textView_kd));
        } else {
            enableEditText(mETRastKP, true);
            enableEditText(mETRastKI, false);
            enableEditText(mETRastKD, false);
            enableEditText(mETRastMin, true);
            enableEditText(mETRastMax, true);
            enableEditText(mETRastPulseOn, true);
            enableEditText(mETRastPulseOff, true);
            enableEditText(mETRastMaxGradient, true);
            enableEditText(mETRastInfo, true);
            mTVRastKP.setText(getString(R.string.rast_str_textView_gradient_cost));
            mTVRastKD.setText(getString(R.string.rast_str_textView_kd));
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Button reset=(Button)findViewById(R.id.reset);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        readSettingValues();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
