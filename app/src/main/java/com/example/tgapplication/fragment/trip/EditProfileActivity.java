package com.example.tgapplication.fragment.trip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.BuildConfig;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.User;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.tgapplication.Constants.UsersInstance;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {

    /* Button btn_regi;
     EditText et_name, et_location, et_visit;
     RadioGroup rg_gender;

     TextView TV_dob;*/
    int day, month, year;
    RadioButton rb_gender;

    /* CheckBox cb_girl, cb_men;
     CoordinatorLayout activity_profile_coordinatelayout;*/
    FirebaseUser fuser;
    User prevUser;
    ArrayList<String> str_look = new ArrayList<>();

/*
    AutoCompleteTextView suggestion_nationality, suggestion_height;
    MultiAutoCompleteTextView suggestion_lang;*/

    /*Spinner Sp_bodytype, Sp_hairs, Sp_eyes;*/

    Calendar mcalendar = Calendar.getInstance();

    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_dob)
    TextView tvDob;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.Nationality_suggestion)
    AutoCompleteTextView NationalitySuggestion;
    @BindView(R.id.Lang_suggestion)
    MultiAutoCompleteTextView LangSuggestion;
    @BindView(R.id.Height_suggestion)
    AutoCompleteTextView HeightSuggestion;
    @BindView(R.id.sp_body_type)
    Spinner spBodyType;
    @BindView(R.id.sp_eyes)
    Spinner spEyes;
    @BindView(R.id.sp_hair)
    Spinner spHair;
    @BindView(R.id.et_visit)
    EditText etVisit;
    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
    @BindView(R.id.cb_men)
    CheckBox cbMen;
    @BindView(R.id.cb_girl)
    CheckBox cbGirl;
    @BindView(R.id.btn_regi)
    Button btnRegi;
    @BindView(R.id.activity_profile_coordinatelayout)
    CoordinatorLayout activityProfileCoordinatelayout;
    @BindView(R.id.et_about_me)
    EditText etAboutMe;
    private ArrayList<User> prevUserList = new ArrayList<>();
    ArrayAdapter<String> Nationalityadapter, Languageadapter, Heightadapter, Bodyadapter, Hairadapter, Eyesadapter;

    @BindView(R.id.imgv_location)
    ImageView imgv_location;
    @BindView(R.id.imgv_dream_location)
    ImageView imgv_dream_location;


    int AUTOCOMPLETE_REQUEST_CODE = 108;
    int AUTOCOMPLETE_REQUEST_CODE_DREAM = 109;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        Places.initialize(getApplicationContext(), BuildConfig.map_api_key);

       /* btn_regi = findViewById(R.id.btn_regi);
        et_name = findViewById(R.id.et_name);
        et_location = findViewById(R.id.et_location);
        et_visit = findViewById(R.id.et_visit);
        rg_gender = findViewById(R.id.rg_gender);
        TV_dob = findViewById(R.id.tv_dob);
        activity_profile_coordinatelayout = findViewById(R.id.activity_profile_coordinatelayout);*/

     /*   Sp_bodytype = findViewById(R.id.sp_body_type);
        Sp_hairs = findViewById(R.id.sp_hair);
        Sp_eyes = findViewById(R.id.sp_eyes);

        suggestion_nationality = findViewById(R.id.Nationality_suggestion);
        suggestion_lang = findViewById(R.id.Lang_suggestion);
        suggestion_height = findViewById(R.id.Height_suggestion);*/

     /*   cb_girl = findViewById(R.id.cb_girl);
        cb_men = findViewById(R.id.cb_men);*/
        cbGirl.setOnClickListener(this);
        cbMen.setOnClickListener(this);


        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        month = mcalendar.get(Calendar.MONTH);
        year = mcalendar.get(Calendar.YEAR);

        mcalendar.add(Calendar.YEAR, -18);
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateTostr = simpleDateFormat.format(today);
        tvDob.setText(dateTostr);

        // Initialize Firebase Auth

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        myList(fuser);

        btnRegi.setOnClickListener(this);


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
                "Select", "Slim", "Athletic", "Average", "Curvy", "Heavy"
        };

        String[] HairSpinner = new String[]{
                "Select", "Blond", "Brown", "Black", "Red", "Auburn", "Grey", "Other"
        };

        String[] EyeSpinner = new String[]{
                "Select", "Brown", "Blue", "Green", "Hazel", "Gray", "Amber", "Other"
        };

        Nationalityadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nationalitySpinner);
        Nationalityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NationalitySuggestion.setAdapter(Nationalityadapter);

        Languageadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, langSpinner);
        Languageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LangSuggestion.setAdapter(Languageadapter);
        LangSuggestion.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        Heightadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, heightSpinner);
        Heightadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        HeightSuggestion.setAdapter(Heightadapter);

        Bodyadapter = new ArrayAdapter<>(this, R.layout.spinner_text, BodyTypeSpinner);
        Bodyadapter.setDropDownViewResource(R.layout.spinner_text);
        spBodyType.setAdapter(Bodyadapter);

        Hairadapter = new ArrayAdapter<>(this, R.layout.spinner_text, HairSpinner);
        Hairadapter.setDropDownViewResource(R.layout.spinner_text);
        spHair.setAdapter(Hairadapter);

        Eyesadapter = new ArrayAdapter<>(this, R.layout.spinner_text, EyeSpinner);
        Eyesadapter.setDropDownViewResource(R.layout.spinner_text);
        spEyes.setAdapter(Eyesadapter);

        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dob = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String sMonth, sDay;
                        monthOfYear = monthOfYear + 1;

                        if (monthOfYear < 10) {
                            sMonth = "0" + monthOfYear;
                        } else {
                            sMonth = String.valueOf(monthOfYear);
                        }

                        if (dayOfMonth < 10) {
                            sDay = "0" + dayOfMonth;
                        } else {
                            sDay = String.valueOf(dayOfMonth);
                        }

//                        view.setMinDate(System.currentTimeMillis());
                        tvDob.setText(new StringBuilder().append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));
                    }
                };

                DatePickerDialog dobDialog = new DatePickerDialog(EditProfileActivity.this, dob, year, month, day);
//               Date datenew= new DateTime().minusYears(14);
                dobDialog.getDatePicker().setMaxDate(mcalendar.getTimeInMillis());
                dobDialog.show();
            }
        });
        manageBlinkEffect();
        setPopup();

        setPopup1();

    }

    public void myList(FirebaseUser fuser) {
        // any way you managed to go the node that has the 'grp_key'

        UsersInstance.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        prevUserList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            prevUser = snapshot.getValue(User.class);
                            if (prevUser != null && prevUser.getId().equalsIgnoreCase(fuser.getUid())) {
                                prevUserList.add(prevUser);
                            }
                        }
                        if (prevUserList.size() > 0) {
                            for (int i = 0; i < prevUserList.size(); i++)
                                setDefaultVal(prevUserList.get(i).getName(), prevUserList.get(i).getDob(), prevUserList.get(i).getGender(), prevUserList.get(i).getAbout_me(), prevUserList.get(i).getAge(), prevUserList.get(i).getLook(), prevUserList.get(i).getLocation(), prevUserList.get(i).getNationality(), prevUserList.get(i).getLang(), prevUserList.get(i).getHeight(), prevUserList.get(i).getBody_type(), prevUserList.get(i).getEyes(), prevUserList.get(i).getHair(), prevUserList.get(i).getVisit());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void setDefaultVal(String name, String dob, String gender, String about_me,String age, ArrayList<String> look, String location, String nationality, String lang, String height, String body_type, String eyes, String hair, String visit) {
        etName.setText(name);
        etLocation.setText(location);
        etVisit.setText(visit);
        etAboutMe.setText(about_me);
        NationalitySuggestion.setText(nationality);
        if (lang != null && !lang.equalsIgnoreCase("")) {
            LangSuggestion.setText(lang + ", ");
        } else {
            LangSuggestion.setText(lang);
        }
        HeightSuggestion.setText(height);
        tvDob.setText(dob);
        str_look = look;
        spHair.setSelection(Hairadapter.getPosition(hair));
        spEyes.setSelection(Eyesadapter.getPosition(eyes));
        spBodyType.setSelection(Bodyadapter.getPosition(body_type));

        Log.i(TAG, "setDefaultVal: Gender" + gender);
        if (gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("Girl")) {
            rbFemale.setChecked(true);
        } else if (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("Boy")) {
            rbMale.setChecked(true);
        }


        for (int i = 0; i < look.size(); i++) {
            Log.i("TAG", "setDefaultVal: " + look.get(i));
            if (look.get(i).equalsIgnoreCase("female")) {
                cbGirl.setChecked(true);
            } else if (look.get(i).equalsIgnoreCase("male")) {
                cbMen.setChecked(true);
            }
        }


    }

    private void setPopup() {
        Field popup;
        try {
            popup = AutoCompleteTextView.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            ListPopupWindow NationalitypopupWindow = (ListPopupWindow) popup.get(NationalitySuggestion);
            ListPopupWindow LanguagepopupWindow = (ListPopupWindow) popup.get(LangSuggestion);
            ListPopupWindow HeightpopupWindow = (ListPopupWindow) popup.get(HeightSuggestion);

            // Set popupWindow height to 500px
            Objects.requireNonNull(NationalitypopupWindow).setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            Objects.requireNonNull(LanguagepopupWindow).setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            Objects.requireNonNull(HeightpopupWindow).setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }

    private void setPopup1() {
        Field popup1;
        try {
            popup1 = Spinner.class.getDeclaredField("mPopup");
            popup1.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            ListPopupWindow BodypopupWindow = (ListPopupWindow) popup1.get(spBodyType);
            ListPopupWindow HairpopupWindow = (ListPopupWindow) popup1.get(spHair);
            ListPopupWindow EyespopupWindow = (ListPopupWindow) popup1.get(spEyes);

            // Set popupWindow height to 500px
            Objects.requireNonNull(BodypopupWindow).setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            Objects.requireNonNull(HairpopupWindow).setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            Objects.requireNonNull(EyespopupWindow).setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }


    private void register(FirebaseUser fuser, String str_name, String str_dob, String str_gender,String str_about_me, String age, String str_location, String str_nationality, String str_lang, ArrayList<String> str_look, String str_height, String str_body_type, String str_eyes, String str_hair, String str_visit) {

        //uncomment this below lines later don't forget

        User userClass = new User(fuser.getUid(), prevUserList.get(0).getUsername(), "offline", prevUserList.get(0).getSearch(), str_gender, age, prevUserList.get(0).getEmail(),
                fuser.getProviderId(), str_body_type, str_dob, str_eyes, str_hair, str_height, str_lang,
                str_look, prevUserList.get(0).getRange_age(), str_location, str_name, fuser.getPhoneNumber(), str_nationality, str_visit, prevUserList.get(0).getAccount_type(),str_about_me);

        UsersInstance.child(fuser.getUid()).setValue(userClass);

        snackBar(activityProfileCoordinatelayout, "Your profile has been successfully updated");
        finish();
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
        switch (v.getId()) {

            case R.id.cb_girl:
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    str_look.add("female");
                } else {
                    str_look.remove("female");
                }
                Log.i("TAG", "onClick: 1" + str_look.size());
                break;
            case R.id.cb_men:
                boolean checkedmen = ((CheckBox) v).isChecked();
                if (checkedmen) {
                    str_look.add("male");
                } else {
                    str_look.remove("male");
                }
                Log.i("TAG", "onClick: 2" + str_look.size());
                break;

            case R.id.btn_regi:

                if (etName.getText().toString().length() <= 0) {
                    etName.requestFocus();
                    snackBar(activityProfileCoordinatelayout, "Please enter your name");
                } else if (etLocation.getText().toString().length() <= 0) {
                    etLocation.requestFocus();
                    snackBar(activityProfileCoordinatelayout, "Please enter your location");
                } else if (etVisit.getText().toString().length() <= 0) {
                    etVisit.requestFocus();
                    snackBar(activityProfileCoordinatelayout, "Enter a city you want to visit");

                } else {
                    int selectedGender = rgGender.getCheckedRadioButtonId();
                    if (selectedGender < 0) {
                        snackBar(activityProfileCoordinatelayout, "Select Gender");
                    } else {
                        Log.i(TAG, "onClick: " + selectedGender);
                        rb_gender = findViewById(selectedGender);

                        String str_name = etName.getText().toString();
                        String str_dob = tvDob.getText().toString();
                        String str_location = etLocation.getText().toString();
                        String str_about_me=etAboutMe.getText().toString();
                        String str_nationality = NationalitySuggestion.getText().toString();
                        String str_lang = LangSuggestion.getText().toString();
                        str_lang = str_lang.replaceAll(", $", "");
                        String str_height = HeightSuggestion.getText().toString();
                        String str_body_type = spBodyType.getSelectedItem().toString();
                        String str_eyes = spEyes.getSelectedItem().toString();
                        String str_hair = spHair.getSelectedItem().toString();
                        String str_visit = etVisit.getText().toString();
                        String str_gender = rb_gender.getText().toString();
                        String age = "18";


                        Log.i("Simu", " " + str_look.size());

                        register(fuser, str_name, str_dob, str_gender,str_about_me, age, str_location, str_nationality, str_lang, str_look, str_height, str_body_type, str_eyes, str_hair, str_visit);

                    }

                }
                break;
        }
    }


    @OnClick({R.id.imgv_location, R.id.imgv_dream_location})
    public void onClickBind(View v) {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);

        switch (v.getId()) {
            case R.id.imgv_location:

                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;

            case R.id.imgv_dream_location:
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_DREAM);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Placeq: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                etLocation.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, Objects.requireNonNull(status.getStatusMessage()));
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_DREAM) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Placeq: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                etVisit.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, Objects.requireNonNull(status.getStatusMessage()));
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void manageBlinkEffect() {
        Animation animation = new AlphaAnimation((float) 1.0, (float) 0.1); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter
        // animation
        // rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation
        // infinitely
        animation.setRepeatMode(Animation.REVERSE);
        imgv_location.startAnimation(animation);
        imgv_dream_location.startAnimation(animation);
    }

}
