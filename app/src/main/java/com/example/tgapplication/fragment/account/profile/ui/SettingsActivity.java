package com.example.tgapplication.fragment.account.profile.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.switchDisplayNumber)
    Switch switchDisplayNumber;
    Boolean displayPhone, emailAllNotify , emailMsgNotify , smsMsgNotify;
    @BindView(R.id.switchEmailAllNotify)
    Switch switchEmailAllNotify;
    @BindView(R.id.switchEmailMsgNotify)
    Switch switchEmailMsgNotify;
    @BindView(R.id.switchSmsMsgNotify)
    Switch switchSmsMsgNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        switchDisplayNumber.setOnCheckedChangeListener(this);
        switchEmailAllNotify.setOnCheckedChangeListener(this);
        switchEmailMsgNotify.setOnCheckedChangeListener(this);
        switchSmsMsgNotify.setOnCheckedChangeListener(this);


        SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("DisplayPhone")) {
            displayPhone = (sharedPreferences.getBoolean("DisplayPhone", false));
            Log.i(TAG, "onCreate: Checking Phone " + displayPhone);
            if (displayPhone) {
                switchDisplayNumber.setChecked(true);
            }
        }

        if (sharedPreferences.contains("EmailAllNotify")) {
            emailAllNotify = (sharedPreferences.getBoolean("EmailAllNotify", false));
            Log.i(TAG, "onCreate: Checking Phone " + emailAllNotify);
            if (emailAllNotify) {
                switchEmailAllNotify.setChecked(true);
            }
        }

        if (sharedPreferences.contains("EmailMsgNotify")) {
            emailMsgNotify = (sharedPreferences.getBoolean("EmailMsgNotify", false));
            Log.i(TAG, "onCreate: Checking Phone " + emailMsgNotify);
            if (emailMsgNotify) {
                switchEmailMsgNotify.setChecked(true);
            }
        }

        if (sharedPreferences.contains("SmsMsgNotify")) {
            smsMsgNotify = (sharedPreferences.getBoolean("SmsMsgNotify", false));
            Log.i(TAG, "onCreate: Checking Phone " + smsMsgNotify);
            if (smsMsgNotify) {
                switchSmsMsgNotify.setChecked(true);
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.switchDisplayNumber:
               saveSettingInfo("DisplayPhone",isChecked);
               break;

            case R.id.switchEmailMsgNotify:
                saveSettingInfo("EmailMsgNotify",isChecked);
            break;

            case R.id.switchSmsMsgNotify:
                saveSettingInfo("SmsMsgNotify",isChecked);
                break;

            case R.id.switchEmailAllNotify:
                saveSettingInfo("EmailAllNotify",isChecked);
                break;
        }
    }
}
