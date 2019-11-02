package com.example.tgapplication.fragment.account.profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.MyProfileFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.login.RegisterActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

public class ChangePrefActivity extends BaseActivity {

    ArrayList<String> look = new ArrayList<>();
    ArrayList<String> range_age = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @BindView(R.id.sp_age_from)
    Spinner spAgeFrom;
    @BindView(R.id.sp_age_to)
    Spinner spAgeTo;
    ArrayAdapter<String> adapter_age_from, adapter_age_to;
    ArrayList<String> array_age;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pref);
        ButterKnife.bind(this);
        relativeLayout = findViewById(R.id.relativelayout);


        setPopup();
        setSpinner();

    }

    private void setSpinner() {

        array_age = new ArrayList<>(Arrays.asList("18","19","20","21","22","23","24","25","26","27","28","29","30",
                "31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50",
                "51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70",
                "71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90",
                "91","92","93","94","95","96","97","98","99"));

        adapter_age_from = new ArrayAdapter<>(ChangePrefActivity.this, android.R.layout.simple_spinner_item, array_age);
        adapter_age_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter_age_to = new ArrayAdapter<>(ChangePrefActivity.this, android.R.layout.simple_spinner_item, array_age);
        adapter_age_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spAgeFrom.setAdapter(adapter_age_from);
        spAgeTo.setAdapter(adapter_age_to);

        spAgeFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(Color.WHITE); //Change selected text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAgeTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(Color.WHITE); //Change selected text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setPopup() {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow AgeFrompopupWindow = (android.widget.ListPopupWindow) popup.get(spAgeFrom);
            android.widget.ListPopupWindow AgeTopopupWindow = (android.widget.ListPopupWindow) popup.get(spAgeTo);
            // Set popupWindow height to 500px
            AgeFrompopupWindow.setHeight(500);
            AgeTopopupWindow.setHeight(500);

        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }


    @OnClick({R.id.cb_regi_girl, R.id.cb_regi_men, R.id.btn_save_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_regi_girl:
                boolean checked = ((CheckBox) view).isChecked();
                if (checked) {
                    look.add("female");
                } else {
                    look.remove("female");
                }

                break;
            case R.id.cb_regi_men:
                boolean checkedmen = ((CheckBox) view).isChecked();
                if (checkedmen) {
                    look.add("male");
                } else {
                    look.remove("male");
                }
                break;

            case R.id.btn_save_register:
//                look;

                String str_age_from = spAgeFrom.getSelectedItem().toString();
                String str_age_to = spAgeTo.getSelectedItem().toString();
                range_age.add(str_age_from);
                range_age.add(str_age_to);
                updateRegister(look, range_age);
                updateUI(mAuth.getCurrentUser());
                snackBar(relativeLayout, " Your Preferences Succssfully Changed..!");
                Toast.makeText(this, "Your Preferences Succssfully Changed..!", Toast.LENGTH_SHORT).show();
                finishActivity();
                
//                Intent showContent = new Intent(getApplication(),
//                        MyProfileFragment.class);
//                startActivity(showContent);

            break;


        }
    }

    private void finishActivity() {
    }


}
