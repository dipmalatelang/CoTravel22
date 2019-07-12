package com.example.tgapplication.fragment.trip;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.BaseMethod;
import com.example.tgapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditProfileActivity extends BaseMethod implements View.OnClickListener {

    private FirebaseAuth mAuth;
    Button btn_regi;
    EditText et_name, et_location, et_visit;
    RadioGroup rg_gender;
    RadioButton rb_gender;
    TextView TV_dob;
    int day, month, year;
    CheckBox cb_girl,cb_men;
    RelativeLayout activity_profile_relativelayout;

    String value;
    ArrayList<String> str_look = new ArrayList<>();

    DatabaseReference databaseReference;


    AutoCompleteTextView suggestion_nationality, suggestion_height;
    MultiAutoCompleteTextView suggestion_lang;

    Spinner Sp_bodytype, Sp_hairs, Sp_eyes;

    Calendar mcalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btn_regi=findViewById(R.id.btn_regi);
        et_name=findViewById(R.id.et_name);
        et_location=findViewById(R.id.et_location);
        et_visit=findViewById(R.id.et_visit);
        rg_gender=findViewById(R.id.rg_gender);
        TV_dob = findViewById(R.id.tv_dob);
        activity_profile_relativelayout= findViewById(R.id.activity_profile_relativelayout);

        Sp_bodytype = findViewById(R.id.sp_body_type);
        Sp_hairs = findViewById(R.id.sp_hair);
        Sp_eyes = findViewById(R.id.sp_eyes);

        suggestion_nationality = findViewById(R.id.Nationality_suggestion);
        suggestion_lang = findViewById(R.id.Lang_suggestion);
        suggestion_height = findViewById(R.id.Height_suggestion);

        cb_girl=findViewById(R.id.cb_girl);
        cb_men=findViewById(R.id.cb_men);
        cb_girl.setOnClickListener(this);
        cb_men.setOnClickListener(this);

        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        month = mcalendar.get(Calendar.MONTH);
        year = mcalendar.get(Calendar.YEAR);

        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateTostr = simpleDateFormat.format(today);
        TV_dob.setText(dateTostr);

        btn_regi.setOnClickListener(this);

        mcalendar.add(Calendar.YEAR, -18);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        String[] nationalitySpinner = new String[]{
                "Afghan", "Albanian", "Algerian", "American", "Andorran", "Angolan", "Antiguans", "Argentinean", "Armenian", "Australian", "Austrian", "Azerbaijani", "Bahamian", "Bahraini", "Bangladeshi", "Barbadian", "Barbudans", "Batswana", "Belarusian", "Belgian", "Belizean", "Beninese", "Bhutanese", "Bolivian", "Bosnian", "Brazilian", "British", "Bruneian", "Bulgarian", "Burkinabe", "Burmese", "Burundian", "Cambodian", "Cameroonian", "Canadian", "Cape Verdean", "Central African", "Chadian", "Chilean", "Chinese", "Colombian", "Comoran", "Congolese", "Costa Rican", "Croatian", "Cuban", "Cypriot", "Czech", "Danish", "Djibouti", "Dominican", "Dutch", "East Timorese", "Ecuadorean", "Egyptian", "Emirian", "Equatorial Guinean", "Eritrean", "Estonian", "Ethiopian", "Fijian", "Filipino", "Finnish", "French", "Gabonese", "Gambian", "Georgian", "German", "Ghanaian", "Greek", "Grenadian", "Guatemalan", "Guinea-Bissauan", "Guinean", "Guyanese", "Haitian", "Herzegovinian", "Honduran", "Hungarian", "Icelander", "Indian", "Indonesian", "Iranian", "Iraqi", "Irish", "Israeli", "Italian", "Ivorian", "Jamaican", "Japanese", "Jordanian", "Kazakhstani", "Kenyan", "Kittian and Nevisian", "Kuwaiti", "Kyrgyz", "Laotian", "Latvian", "Lebanese", "Liberian", "Libyan", "Liechtensteiner", "Lithuanian", "Luxembourger", "Macedonian", "Malagasy", "Malawian", "Malaysian", "Maldivan", "Malian", "Maltese", "Marshallese", "Mauritanian", "Mauritian", "Mexican", "Micronesian", "Moldovan", "Monacan", "Mongolian", "Moroccan", "Mosotho", "Motswana", "Mozambican", "Namibian", "Nauruan", "Nepalese", "New Zealander", "Ni-Vanuatu", "Nicaraguan", "Nigerien", "North Korean", "Northern Irish", "Norwegian", "Omani", "Pakistani", "Palauan", "Panamanian", "Papua New Guinean", "Paraguayan", "Peruvian", "Polish", "Portuguese", "Qatari", "Romanian", "Russian", "Rwandan", "Saint Lucian", "Salvadoran", "Samoan", "San Marinese", "Sao Tomean", "Saudi", "Scottish", "Senegalese", "Serbian", "Seychellois", "Sierra Leonean", "Singaporean", "Slovakian", "Slovenian", "Solomon Islander", "Somali", "South African", "South Korean", "Spanish", "Sri Lankan", "Sudanese", "Surinamer", "Swazi", "Swedish", "Swiss", "Syrian", "Taiwanese", "Tajik", "Tanzanian", "Thai", "Togolese", "Tongan", "Trinidadian or Tobagonian", "Tunisian", "Turkish", "Tuvaluan", "Ugandan", "Ukrainian", "Uruguayan", "Uzbekistani", "Venezuelan", "Vietnamese", "Welsh", "Yemenite", "Zambian", "Zimbabwean"
        };

        String[] langSpinner = new String[]{
                "English", "French", "Spanish", "Arabic", "German", "Italian", "Armenian", "Azerbaijani", "Belorussian", "Bengali", "Bulgarian", "Catalan", "Chinese", "Croatian", "Czech", "Danish", "Dutch", "Esperanto", "Estonian", "Filipino", "Finnish", "Georgian", "Greek", "Hebrew", "Hindi", "Hungarian", "Icelandic", "Indonesian", "Japanese", "Korean", "Kurdish", "Latvian", "Lithuanian", "Maltese", "Nepali", "Norwegian", "Persian", "Polish", "Portuguese", "Romanian", "Russian", "Serbian", "Slovak", "Slovenian", "Swedish", "Thai", "Turkish", "Ukrainian", "Urdu", "Vietnamese"
        };

        String[] heightSpinner = new String[]{
                "150 cm (4'11\")", "151 cm (4'11\")", "152 cm (5'00\")", "153 cm (5'00\")", "154 cm (5'00\")", "155 cm (5'01\")", "156 cm (5'01\")", "157 cm (5'02\")", "158 cm (5'02\")", "159 cm (5'02\")", "160 cm (5'03\")", "161 cm (5'03\")", "162 cm (5'03\")", "163 cm (5'04\")", "164 cm (5'04\")", "165 cm (5'05\")", "166 cm (5'05\")", "167 cm (5'05\")", "168 cm (5'06\")", "169 cm (5'06\")", "170 cm (5'07\")", "171 cm (5'07\")", "172 cm (5'07\")", "173 cm (5'08\")", "174 cm (5'08\")", "175 cm (5'09\")", "176 cm (5'09\")", "177 cm (5'09\")", "178 cm (5'10\")", "179 cm (5'10\")", "180 cm (5'11\")", "181 cm (5'11\")", "182 cm (5'11\")", "183 cm (6'00\")", "184 cm (6'00\")", "185 cm (6'01\")", "186 cm (6'01\")", "187 cm (6'01\")", "188 cm (6'02\")", "189 cm (6'02\")", "190 cm (6'02\")", "191 cm (6'03\")", "192 cm (6'03\")", "193 cm (6'04\")", "194 cm (6'04\")", "195 cm (6'04\")", "196 cm (6'05\")", "197 cm (6'05\")", "198 cm (6'06\")", "199 cm (6'06\")", "200 cm (6'06\")", "201 cm (6'07\")", "202 cm (6'07\")", "203 cm (6'08\")", "204 cm (6'08\")", "205 cm (6'08\")", "206 cm (6'09\")", "207 cm (6'09\")", "208 cm (6'10\")", "209 cm (6'10\")", "210 cm (6'10\")", "211 cm (6'11\")", "212 cm (6'11\")", "213 cm (7'00\")", "214 cm (7'00\")", "215 cm (7'00\")", "216 cm (7'01\")", "217 cm (7'01\")", "218 cm (7'02\")", "219 cm (7'02\")", "220 cm (7'02\")"
        };

        String[] BodyTypeSpinner = new String[]{
                "Slim", "Athletic", "Average", "Curvy", "Heavy"
        };

        String[] HairSpinner = new String[]{
                "Blond", "Brown", "Black", "Red", "Auburn", "Grey", "Other"
        };

        String[] EyeSpinner = new String[]{
                "Brown", "Blue", "Green", "Hazel", "Gray", "Amber", "Other"
        };

        ArrayAdapter<String> Nationalityadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nationalitySpinner);
        Nationalityadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        suggestion_nationality.setAdapter(Nationalityadapter);

        ArrayAdapter<String>Languageadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, langSpinner);
        Languageadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        suggestion_lang.setAdapter(Languageadapter);
        suggestion_lang.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        ArrayAdapter<String>Heightadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, heightSpinner);
        Heightadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        suggestion_height.setAdapter(Heightadapter);

        ArrayAdapter<String>Bodyadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, BodyTypeSpinner);
        Bodyadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Sp_bodytype.setAdapter(Bodyadapter);

        ArrayAdapter<String>Hairadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, HairSpinner);
        Hairadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Sp_hairs.setAdapter(Hairadapter);

        ArrayAdapter<String>Eyesadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EyeSpinner);
        Eyesadapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Sp_eyes.setAdapter(Eyesadapter);

        TV_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dob = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String sMonth,sDay;
                        monthOfYear=monthOfYear+1;

                        if (monthOfYear<10){
                            sMonth = "0"+monthOfYear;
                        }else {
                            sMonth = String.valueOf(monthOfYear);
                        }

                        if (day<10){
                            sDay = "0"+day;
                        }else {
                            sDay = String.valueOf(day);
                        }

//                        view.setMinDate(System.currentTimeMillis());
                        TV_dob.setText(new StringBuilder().append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));
                    }
                };

                DatePickerDialog dobDialog = new DatePickerDialog(EditProfileActivity.this, dob, year, month, day);
//               Date datenew= new DateTime().minusYears(14);
                dobDialog.getDatePicker().setMaxDate(mcalendar.getTimeInMillis());
                dobDialog.show();
            }
        });

        setPopup();

        setPopup1();

    }

    private void setPopup() {
        Field popup = null;
        try {
            popup = AutoCompleteTextView.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow NationalitypopupWindow = (android.widget.ListPopupWindow) popup.get(suggestion_nationality);
            android.widget.ListPopupWindow LanguagepopupWindow = (android.widget.ListPopupWindow) popup.get(suggestion_lang);
            android.widget.ListPopupWindow HeightpopupWindow = (android.widget.ListPopupWindow) popup.get(suggestion_height);

            // Set popupWindow height to 500px
            NationalitypopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            LanguagepopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            HeightpopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }

    private void setPopup1() {
        Field popup1 = null;
        try {
            popup1 = Spinner.class.getDeclaredField("mPopup");
            popup1.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow BodypopupWindow = (android.widget.ListPopupWindow) popup1.get(Sp_bodytype);
            android.widget.ListPopupWindow HairpopupWindow = (android.widget.ListPopupWindow) popup1.get(Sp_hairs);
            android.widget.ListPopupWindow EyespopupWindow = (android.widget.ListPopupWindow) popup1.get(Sp_eyes);

            // Set popupWindow height to 500px
            BodypopupWindow.setHeight(300);
            HairpopupWindow.setHeight(400);
            EyespopupWindow.setHeight(400);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }



    private void register(final FirebaseUser user, String str_name, String str_dob, String str_gender, String age, String str_location, String str_nationality, String str_lang, ArrayList<String> str_look, String str_height, String str_body_type, String str_eyes, String str_hair, String str_visit) {
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        //uncomment this below lines later don't forget

//        User userClass=new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString()+"?type=large", "offline",user.getDisplayName().toLowerCase(), str_gender, age, user.getEmail(),
//                user.getProviderId(),str_body_type, str_dob, str_eyes, str_hair, str_height, str_lang,
//                str_look, str_location, str_name, user.getPhoneNumber(), str_nationality, str_visit);
//        databaseReference.setValue(userClass);

//        updateUI(user);

//            hashMap=new HashMap<>();
//        hashMap.put("id",user.getUid());
//        hashMap.put("username",user.getDisplayName());
//        hashMap.put("name",);
//        hashMap.put("dob",);
//        hashMap.put("gender",);
//        hashMap.put("location",);
//        hashMap.put("nationality",);
//        hashMap.put("lang",);
////        hashMap.put("look",);
//        hashMap.put("height",);
//        hashMap.put("body_type",);
//        hashMap.put("eyes",);
//        hashMap.put("hair",);
//        hashMap.put("visit",);
//        hashMap.put("email",);
//        hashMap.put("social_media",);
//        hashMap.put("phone_number",);
//        hashMap.put("imageURL",.toString());
//        hashMap.put("status", );
//        hashMap.put("search", );
//
//        hashMap1 = new HashMap<>();
//        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Addis_Ababa/Users").child(user.getUid());
//
//        databaseReference1.child("look");
//        for (int i = 0; i < str_look.size(); i++) {
//            hashMap1.put( databaseReference1.push().getKey(),str_look.get(i));
//        }
//
////        databaseReference1.updateChildren(hashMap1);
//
//        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful())
//                {
//                    databaseReference1.setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            updateUI(user);
//                        }
//                    });
//
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.cb_girl:
                boolean checked = ((CheckBox) v).isChecked();
                if (checked)
                {
                    str_look.add("female");
                }
                else
                {
                    str_look.remove("female");
                }

                break;
            case R.id.cb_men:
                boolean checkedmen = ((CheckBox) v).isChecked();
                if (checkedmen)
                {
                    str_look.add("male");
                }
                else {
                    str_look.remove("male");
                }
                break;

            case R.id.btn_regi:
                final FirebaseUser user = mAuth.getCurrentUser();

                if (et_name.getText().toString().length() <=0){

                    snackBar(activity_profile_relativelayout,"Please enter your name");
                }
                else if (et_location.getText().toString().length()<=0){
                    snackBar(activity_profile_relativelayout,"Enter a city you want to visit");
                }
                else if (et_visit.getText().toString().length()<=0){
                    snackBar(activity_profile_relativelayout,"Please enter your location");

                }
                else
                {
                    int selectedGender=rg_gender.getCheckedRadioButtonId();
                    rb_gender=findViewById(selectedGender);

                    String str_name=et_name.getText().toString();
                    String str_dob=TV_dob.getText().toString();
                    String str_location= et_location.getText().toString();
                    String str_nationality= suggestion_nationality.getText().toString();
                    String str_lang=suggestion_lang.getText().toString();
                    str_lang = str_lang.replaceAll(", $", "");
                    String str_height= suggestion_height.getText().toString();
                    String str_body_type=Sp_bodytype.getSelectedItem().toString();
                    String str_eyes=Sp_eyes.getSelectedItem().toString();
                    String str_hair=Sp_hairs.getSelectedItem().toString();
                    String str_visit=et_visit.getText().toString();
                    String str_gender=rb_gender.getText().toString();
                    String age="18";


                    Log.i("Simu"," "+str_height);
                }


                break;
        }
    }
}
