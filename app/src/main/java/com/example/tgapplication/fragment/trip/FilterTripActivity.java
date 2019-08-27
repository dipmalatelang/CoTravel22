package com.example.tgapplication.fragment.trip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class FilterTripActivity extends BaseActivity implements View.OnClickListener {

    Spinner spinner_lang, spinner_look,spinner_from,spinner_to, spinner_eyes, spinner_hairs, spinner_height, spinner_bodytype;
    ArrayList<String> array_lang,array_look,array_from,array_to, array_eyes, array_hairs, array_height, array_bodytype;
    ArrayAdapter<String> adapter_lang, adapter_look, adapter_from, adapter_to, adapter_eyes, adapter_hairs, adapter_height, adapter_bodytype;
    EditText et_city;
    Button btn_add_trip;
    RadioButton rb_visit;
    RadioGroup rg_trip;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    CoordinatorLayout activity_filter_trip_coodinatelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_trip);
        activity_filter_trip_coodinatelayout = findViewById(R.id.activity_filter_trip_coodinatelayout);

        initComponent();

         prefs = getSharedPreferences("Filter_TripList", MODE_PRIVATE);

        if (prefs.contains("str_city")) {
            String str_city = prefs.getString("str_city", "not_defined");//"No name defined" is the default value.
            String str_lang = prefs.getString("str_lang", "not_defined"); //0 is the default value.
            String str_eyes = prefs.getString("str_eyes","not_defined");
            String str_hairs = prefs.getString("str_hairs","not_defined");
            String str_height=prefs.getString("str_height","not_defined");
            String str_bodytype=prefs.getString("str_bodytype","not_defined");

            String str_look = prefs.getString("str_look","not_defined");
            String str_from= prefs.getString("str_from","not_defined");
            String str_to= prefs.getString("str_to","not_defined");
            String str_visit= prefs.getString("str_visit","not_defined");

            et_city.setText(str_city);
            spinner_lang.setSelection(adapter_lang.getPosition(str_lang));
            spinner_eyes.setSelection(adapter_eyes.getPosition(str_eyes));
            spinner_hairs.setSelection(adapter_hairs.getPosition(str_hairs));
            spinner_height.setSelection(adapter_height.getPosition(str_height));
            spinner_bodytype.setSelection(adapter_bodytype.getPosition(str_bodytype));

            spinner_look.setSelection(adapter_look.getPosition(str_look));
            spinner_from.setSelection(adapter_from.getPosition(str_from));
            spinner_to.setSelection(adapter_to.getPosition(str_to));

            if(str_visit.equalsIgnoreCase("From"))
                rg_trip.check(R.id.rb_from);
            else
                rg_trip.check(R.id.rb_visit);
//           int selectedId= rg_trip.getCheckedRadioButtonId();
//            rb_visit=findViewById(selectedId);

        }

        assert getSupportActionBar() != null; //null check
    //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initComponent() {

        array_lang = new ArrayList<String>(
                Arrays.asList("All","Arabic","Danish","German","Belorussian","Dutch","Greek","Japanese","Portuguese","Italian","Polish","Spanish","Swedish","Bulgarian","English","Hebrew","Korean","Romanian","Thai","Catalan",
                        "Estonian","Hindi","Latvian","Russian","Turkish","Chinese","Filipino","Hungarian","Lithuanian","Serbian","Ukrainian","Croatian","Finnish","Icelandic","Norwegian","Slovak","Urdu","Czech","French",
                        "Indonesian","Persian","Slovenian","Vietnamese","Nepali","Armenian","Kurdish"));

        array_look = new ArrayList<>(Arrays.asList("All","Girls","Male"));

        array_from = new ArrayList<>(Arrays.asList("18","19","20","21","22","23","24","25","26","27","28","29","30",
                "31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50",
                "51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70",
                "71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90",
                "91","92","93","94","95","96","97","98","99"));


        array_to = new ArrayList<>(Arrays.asList("18","19","20","21","22","23","24","25","26","27","28","29","30",
                "31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50",
                "51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70",
                "71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90",
                "91","92","93","94","95","96","97","98","99"));

        array_eyes = new ArrayList<>(Arrays.asList("All","Brown", "Blue", "Green", "Hazel", "Gray", "Amber", "Other"));

        array_hairs = new ArrayList<>(Arrays.asList("All","Blond", "Brown", "Black", "Red", "Auburn", "Grey", "Other"));

        array_height = new ArrayList<>(Arrays.asList("All","150 cm (4'11\")", "151 cm (4'11\")", "152 cm (5'00\")",
                "153 cm (5'00\")", "154 cm (5'00\")", "155 cm (5'01\")", "156 cm (5'01\")", "157 cm (5'02\")",
                "158 cm (5'02\")", "159 cm (5'02\")", "160 cm (5'03\")", "161 cm (5'03\")", "162 cm (5'03\")",
                "163 cm (5'04\")", "164 cm (5'04\")", "165 cm (5'05\")", "166 cm (5'05\")", "167 cm (5'05\")",
                "168 cm (5'06\")", "169 cm (5'06\")", "170 cm (5'07\")", "171 cm (5'07\")", "172 cm (5'07\")",
                "173 cm (5'08\")", "174 cm (5'08\")", "175 cm (5'09\")", "176 cm (5'09\")", "177 cm (5'09\")",
                "178 cm (5'10\")", "179 cm (5'10\")", "180 cm (5'11\")", "181 cm (5'11\")", "182 cm (5'11\")",
                "183 cm (6'00\")", "184 cm (6'00\")", "185 cm (6'01\")", "186 cm (6'01\")", "187 cm (6'01\")",
                "188 cm (6'02\")", "189 cm (6'02\")", "190 cm (6'02\")", "191 cm (6'03\")", "192 cm (6'03\")",
                "193 cm (6'04\")", "194 cm (6'04\")", "195 cm (6'04\")", "196 cm (6'05\")", "197 cm (6'05\")",
                "198 cm (6'06\")", "199 cm (6'06\")", "200 cm (6'06\")", "201 cm (6'07\")", "202 cm (6'07\")",
                "203 cm (6'08\")", "204 cm (6'08\")", "205 cm (6'08\")", "206 cm (6'09\")", "207 cm (6'09\")",
                "208 cm (6'10\")", "209 cm (6'10\")", "210 cm (6'10\")", "211 cm (6'11\")", "212 cm (6'11\")",
                "213 cm (7'00\")", "214 cm (7'00\")", "215 cm (7'00\")", "216 cm (7'01\")", "217 cm (7'01\")",
                "218 cm (7'02\")", "219 cm (7'02\")", "220 cm (7'02\")"));

        array_bodytype = new ArrayList<>(Arrays.asList("All","Slim", "Athletic", "Average", "Curvy", "Heavy"));

        spinner_lang=findViewById(R.id.spinner_lang);
        spinner_look=findViewById(R.id.spinner_look);
        spinner_from=findViewById(R.id.spinner_from);
        spinner_to=findViewById(R.id.spinner_to);
        spinner_eyes = findViewById(R.id.spinner_eyes);
        spinner_hairs = findViewById(R.id.spinner_hairs);
        spinner_height = findViewById(R.id.spinner_height);
        spinner_bodytype = findViewById(R.id.spinner_bodytype);
        btn_add_trip=findViewById(R.id.btn_add_trip);

        rg_trip=findViewById(R.id.rg_trip);
//        rb_visit=findViewById(R.id.rb_visit);
        et_city=findViewById(R.id.et_city);

        adapter_lang = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array_lang);
        adapter_lang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lang.setAdapter(adapter_lang);

        adapter_look = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array_look);
        adapter_look.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_look.setAdapter(adapter_look);

        adapter_eyes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array_eyes);
        adapter_eyes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_eyes.setAdapter(adapter_eyes);

        adapter_hairs = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array_hairs);
        adapter_hairs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_hairs.setAdapter(adapter_hairs);

        adapter_height = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array_height);
        adapter_height.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_height.setAdapter(adapter_height);

        adapter_bodytype = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array_bodytype);
        adapter_bodytype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bodytype.setAdapter(adapter_bodytype);

        adapter_from = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array_from);
        adapter_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_from.setAdapter(adapter_from);

        adapter_to = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array_to);
        adapter_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_to.setAdapter(adapter_to);

        spinner_to.setSelection(adapter_to.getPosition("99"));

        btn_add_trip.setOnClickListener(this);

        setPopup();
    }

    private void setPopup() {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow TopopupWindow = (android.widget.ListPopupWindow) popup.get(spinner_to);
            android.widget.ListPopupWindow FrompopupWindow = (android.widget.ListPopupWindow) popup.get(spinner_from);
            android.widget.ListPopupWindow LanguagepopupWindow = (android.widget.ListPopupWindow) popup.get(spinner_lang);
            android.widget.ListPopupWindow HeightpopupWindow = (android.widget.ListPopupWindow) popup.get(spinner_height);
            android.widget.ListPopupWindow BodypopupWindow = (android.widget.ListPopupWindow) popup.get(spinner_bodytype);
            android.widget.ListPopupWindow HairpopupWindow = (android.widget.ListPopupWindow) popup.get(spinner_hairs);
            android.widget.ListPopupWindow EyespopupWindow = (android.widget.ListPopupWindow) popup.get(spinner_eyes);

            // Set popupWindow height to 500px
            TopopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            FrompopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            LanguagepopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            HeightpopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            BodypopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            HairpopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            EyespopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_add_trip:

                if (et_city.getText().toString().length()<=0){
                    snackBar(activity_filter_trip_coodinatelayout,"Please enter city name");
                }
                else
                {

                    int selectedId= rg_trip.getCheckedRadioButtonId();
                    rb_visit=findViewById(selectedId);

                    String str_city=et_city.getText().toString();
                    String str_lang = spinner_lang.getSelectedItem().toString();
                    String str_eyes=spinner_eyes.getSelectedItem().toString();
                    String str_hairs=spinner_hairs.getSelectedItem().toString();
                    String str_height=spinner_height.getSelectedItem().toString();
                    String str_bodytype=spinner_bodytype.getSelectedItem().toString();
                    String str_look = spinner_look.getSelectedItem().toString();
                    String str_from = spinner_from.getSelectedItem().toString();
                    String str_to = spinner_to.getSelectedItem().toString();
                    String str_visit= rb_visit.getText().toString();

                    editor = prefs.edit();
                    editor.putString("str_city", str_city);
                    editor.putString("str_lang", str_lang);
                    editor.putString("str_eyes",str_eyes);
                    editor.putString("str_hairs",str_hairs);
                    editor.putString("str_height",str_height);
                    editor.putString("str_bodytype",str_bodytype);
                    editor.putString("str_look",str_look);
                    editor.putString("str_from",str_from);
                    editor.putString("str_to",str_to);
                    editor.putString("str_visit",str_visit);
                    editor.apply();

                    startActivity(new Intent(FilterTripActivity.this, MainActivity.class));
                }


                break;
        }
    }
}
