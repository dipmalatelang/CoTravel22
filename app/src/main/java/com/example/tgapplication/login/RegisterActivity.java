package com.example.tgapplication.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.BuildConfig;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.photo.Upload;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class RegisterActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    String value;
    LoginButton loginButton;
    Spinner sp_age, sp_age_from, sp_age_to;
    Button btn_register, btn_save_register;
    ArrayList<String> look = new ArrayList<>();
    ArrayList<String> range_age=new ArrayList<>();
    EditText regi_et_name, regi_et_email, regi_et_pass,regi_et_location;
    TextInputLayout textInput_location;

    LinearLayout travelprefer_form;
    ConstraintLayout regi_form;
    RadioGroup regi_rg;
    RadioButton rb_gender;
    CheckBox cb_regi_girl,cb_regi_men;
    RelativeLayout relativelayout;

    ArrayList<String> array_age;
    ArrayAdapter<String> adapter_age, adapter_age_from, adapter_age_to;
    private String TAG="RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_register);
        Places.initialize(getApplicationContext(), BuildConfig.map_api_key);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        sp_age=findViewById(R.id.sp_age);
        sp_age_from=findViewById(R.id.sp_age_from);
        sp_age_to=findViewById(R.id.sp_age_to);
        regi_et_name=findViewById(R.id.regi_et_name);
        regi_et_email=findViewById(R.id.regi_et_email);
        regi_et_pass=findViewById(R.id.regi_et_pass);
        regi_et_location=findViewById(R.id.regi_et_location);
        textInput_location=findViewById(R.id.textInput_location);
        regi_rg=findViewById(R.id.regi_rg);

        cb_regi_girl=findViewById(R.id.cb_regi_girl);
        cb_regi_men=findViewById(R.id.cb_regi_men);

        cb_regi_girl.setOnClickListener(this);
        cb_regi_men.setOnClickListener(this);

        regi_form=findViewById(R.id.cl_register_form);
        travelprefer_form=findViewById(R.id.travelprefer_form);

        btn_register=findViewById(R.id.btn_register);
        btn_save_register=findViewById(R.id.btn_save_register);

        btn_register.setOnClickListener(this);
        btn_save_register.setOnClickListener(this);

        loginButton = findViewById(R.id.login_button);

        relativelayout = findViewById(R.id.relativelayout);

        TextView cl_register_form = findViewById(R.id.tv_title_text);
        cl_register_form.setPaintFlags(cl_register_form.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        cl_register_form.setText(getResources().getString(R.string.register));

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
//        value = getIntent().getExtras().getString("nextActivity");

//        Log.i("Got Value in Register",value);


        initAge();

        regi_et_pass.setOnTouchListener(this);
        regi_et_location.setOnClickListener(this);
        textInput_location.setOnClickListener(this);

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Tiger", "facebook:onSuccess:" + loginResult);
                Log.d("Tiger", "facebook:token:" + loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Log.d("Tiger", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Tiger", "facebook:onError", error);
                // ...
            }
        });

        setPopup();

        assert getSupportActionBar() != null; //null check
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initAge() {

        array_age = new ArrayList<>(Arrays.asList("18","19","20","21","22","23","24","25","26","27","28","29","30",
                "31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50",
                "51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70",
                "71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90",
                "91","92","93","94","95","96","97","98","99"));

        adapter_age = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, array_age);
        adapter_age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter_age_from = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, array_age);
        adapter_age_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter_age_to = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, array_age);
        adapter_age_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_age.setAdapter(adapter_age);
        sp_age_from.setAdapter(adapter_age_from);
        sp_age_to.setAdapter(adapter_age_to);

        sp_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                hideKeyboard();
                ((TextView) view).setTextColor(Color.WHITE); //Change selected text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_age_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) view).setTextColor(Color.WHITE); //Change selected text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_age_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            android.widget.ListPopupWindow AgepopupWindow = (android.widget.ListPopupWindow) popup.get(sp_age);
            android.widget.ListPopupWindow AgeFrompopupWindow = (android.widget.ListPopupWindow) popup.get(sp_age_from);
            android.widget.ListPopupWindow AgeTopopupWindow = (android.widget.ListPopupWindow) popup.get(sp_age_to);
            // Set popupWindow height to 500px
            AgepopupWindow.setHeight(500);
            AgeFrompopupWindow.setHeight(500);
            AgeTopopupWindow.setHeight(500);

        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void updateUI(FirebaseUser account) {
        if (account != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
    private void handleFacebookAccessToken(String token) {
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        Log.d("Tiger",""+credential);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Tiger", "handleFacebookAccessToken:" +task.isSuccessful());
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tiger", "signInWithCredential:success");
                            dismissProgressDialog();
                            FirebaseUser user = mAuth.getCurrentUser();

                            User userClass=new User(user.getUid(), user.getDisplayName(), "offline", user.getDisplayName().toLowerCase(), "", "",  user.getEmail(), user.getProviderId(), "", "", "", "", "", "", look, range_age, "",  user.getDisplayName().toLowerCase(), user.getPhoneNumber(), "", "");
                            UsersInstance.child(mAuth.getCurrentUser().getUid()).setValue(userClass);

                            String uploadId = PicturesInstance.child(user.getUid()).push().getKey();
                            PicturesInstance.child(user.getUid()).child(uploadId).setValue(new Upload(uploadId,"Image", user.getPhotoUrl().toString()+"?type=large",1));

                            updateUI(mAuth.getCurrentUser());

                        }

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void register(final String username, final String email, String password, final String str_gender, final String str_age, final ArrayList<String> look, final ArrayList<String> range_age){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            snackBar(relativelayout,"Success");

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            Log.i("Done","gothere");
                            User userClass=new User(userid, username,"offline",username.toLowerCase(),str_gender,str_age,email, firebaseUser.getProviderId(),"","","",
                                    "","","", look, range_age,"",username,"","","");
                            UsersInstance.child(userid).setValue(userClass);
                            Log.i("Done","gotin");
//                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            regi_form.setVisibility(View.GONE);
                            travelprefer_form.setVisibility(View.VISIBLE);
                            range_age.clear();
                            look.clear();

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure"+ Objects.requireNonNull(task.getException()).getMessage());
                            snackBar(relativelayout,task.getException().getMessage());
                            showProgressDialog();
                            updateUI(null);
                        }

                    }

                });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_regi_girl:
                boolean checked = ((CheckBox) v).isChecked();
                if (checked) {
                    look.add("female");
                } else {
                    look.remove("female");
                }

                break;
            case R.id.cb_regi_men:
                boolean checkedmen = ((CheckBox) v).isChecked();
                if (checkedmen) {
                    look.add("male");
                } else {
                    look.remove("male");
                }
                break;

            case R.id.btn_save_register:
//                look;

                String str_age_from = sp_age_from.getSelectedItem().toString();
                String str_age_to = sp_age_to.getSelectedItem().toString();
                range_age.add(str_age_from);
                range_age.add(str_age_to);
                updateRegister(look, range_age);
                updateUI(mAuth.getCurrentUser());
                break;

            case R.id.btn_register:
                int selectedId = regi_rg.getCheckedRadioButtonId();
                rb_gender = findViewById(selectedId);

                String str_gender = rb_gender.getText().toString();
                String txt_username = regi_et_name.getText().toString();
                String txt_email = regi_et_email.getText().toString();
                String txt_password = regi_et_pass.getText().toString();
                String str_age = sp_age.getSelectedItem().toString();



                int age_value = Integer.parseInt(str_age);
                if (age_value <= 25) {
                    range_age.add("18");
                    range_age.add("" + (age_value + 7));
                } else if (age_value >= 25) {
                    range_age.add("" + (age_value - 7));
                    range_age.add("" + (age_value + 7));
                }


                if (str_gender.equalsIgnoreCase("girl")) {
                    look.add("female");
                } else if (str_gender.equalsIgnoreCase("boy")) {
                    look.add("male");
                } else {
                    look.add("female");
                    look.add("male");
                }
                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password )) {
                    snackBar(relativelayout, "All fileds are required");

                } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(txt_email).matches())
                {
                    snackBar(relativelayout, "please enter valid email address");}

                else if (txt_password.length() < 6 ) {

                    snackBar(relativelayout, "password must be at least 6 characters");


                }
                else {

//                    snackBar(relativelayout, "Register Successfully..!");
                    register(txt_username, txt_email, txt_password, str_gender, str_age, look, range_age);
                }

                break;

            case R.id.textInput_location: case R.id.regi_et_location:
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;

        }
    }




    private void updateRegister(final ArrayList<String> look, ArrayList<String> age) {

//        User userClass=new User(look,age);
        UsersInstance.child(mAuth.getCurrentUser().getUid()).child("look").setValue(look);
        UsersInstance.child(mAuth.getCurrentUser().getUid()).child("range_age").setValue(age);

    }


    private String validateDOB(String str_dob)
    {
        Date today = new Date();
        Date birth = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            birth = sdf.parse(str_dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int age = today.getYear() - birth.getYear();

        if(age>18)
        {
            return "valid";
        }
        else {
            return "notValid";
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(event.getRawX() >= (regi_et_pass.getRight() - regi_et_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                // your action here
                if(!regi_et_pass.getTransformationMethod().toString().contains("Password"))
                {
                    regi_et_pass.setTransformationMethod(new PasswordTransformationMethod());
                    regi_et_pass.setSelection(regi_et_pass.getText().length());
                    regi_et_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_white_24dp, 0, R.drawable.ic_action_eye_off, 0);

                }
                else
                {
                    regi_et_pass.setTransformationMethod(new HideReturnsTransformationMethod());
                    regi_et_pass.setSelection(regi_et_pass.getText().length());
                    regi_et_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_white_24dp, 0, R.drawable.ic_action_eye, 0);
                }
                return true;
            }
        }
        return false;
    }

    int AUTOCOMPLETE_REQUEST_CODE = 110;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Placeq: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                regi_et_location.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else {
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}

