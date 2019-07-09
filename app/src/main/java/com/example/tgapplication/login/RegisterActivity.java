package com.example.tgapplication.login;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.R;
import com.example.tgapplication.chat.ChatActivity;
import com.example.tgapplication.chat.MessageActivity;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.trips.AddTripActivity;
import com.example.tgapplication.trips.TripActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    String value;
    LoginButton loginButton;
    Spinner sp_age, sp_age_from, sp_age_to;
    Button btn_register, btn_save_register;
    ArrayList<String> look = new ArrayList<>();
    ArrayList<String> range_age=new ArrayList<>();
    EditText regi_et_name, regi_et_email, regi_et_pass;
    DatabaseReference databaseReference;

    LinearLayout travelprefer_form;
    ConstraintLayout regi_form;
    RadioGroup regi_rg;
    RadioButton rb_gender;
    CheckBox cb_regi_girl,cb_regi_men;

    ArrayList<String> array_age;
    ArrayAdapter<String> adapter_age, adapter_age_from, adapter_age_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_register);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        sp_age=findViewById(R.id.sp_age);
        sp_age_from=findViewById(R.id.sp_age_from);
        sp_age_to=findViewById(R.id.sp_age_to);
        regi_et_name=findViewById(R.id.regi_et_name);
        regi_et_email=findViewById(R.id.regi_et_email);
        regi_et_pass=findViewById(R.id.regi_et_pass);
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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        value = getIntent().getExtras().getString("nextActivity");

        Log.i("Got Value in Register",value);


        initAge();

        regi_et_pass.setOnTouchListener(this);

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //   0
    private void updateUI(FirebaseUser account) {
        if (account != null){
            Log.i("FBData",""+account.getIdToken(true)+" "+account.getMetadata()+" "+account.getProviderData());
            Log.i("NextNow",value);
            if(value.equalsIgnoreCase("Chat"))
            {
                Intent intent=new Intent(RegisterActivity.this, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
                Toast.makeText(RegisterActivity.this, "in"+account, Toast.LENGTH_SHORT).show();
            }
            else if(value.equalsIgnoreCase("AddTrips"))
            {
                Intent intent=new Intent(RegisterActivity.this, AddTripActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
                Toast.makeText(RegisterActivity.this, "in"+account, Toast.LENGTH_SHORT).show();
            }
            else if(value.equalsIgnoreCase("Trips"))
            {
                Intent intent=new Intent(RegisterActivity.this, TripActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
                Toast.makeText(RegisterActivity.this, "in"+account, Toast.LENGTH_SHORT).show();
            }
            else if(value.equalsIgnoreCase("TripsMsg"))
            {
                String user = getIntent().getExtras().getString("nextActivityUser");
                Intent intent=new Intent(RegisterActivity.this, MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userid", user);
                startActivity(intent);
                finish();
                Toast.makeText(RegisterActivity.this, "in"+account, Toast.LENGTH_SHORT).show();
            }
            else if(value.equalsIgnoreCase("profileEdit"))
            {
                Toast.makeText(RegisterActivity.this, "Profile edited Successfully", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(RegisterActivity.this, "out", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFacebookAccessToken(String token) {

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
                            Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            Log.i("Done","gothere");
                            User userClass=new User(userid, username, "default", "offline",username.toLowerCase(),str_gender,str_age,email, firebaseUser.getProviderId(),"","","",
                                    "","","", look, range_age,"",username,"","","");
                            databaseReference.setValue(userClass);
                            Log.i("Done","gotin");
//                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            regi_form.setVisibility(View.GONE);
                            travelprefer_form.setVisibility(View.VISIBLE);
                            range_age.clear();
                            look.clear();

                    }
                }
    });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cb_regi_girl:
                boolean checked = ((CheckBox) v).isChecked();
                if (checked)
                {
                    look.add("female");
                }
                else
                {
                    look.remove("female");
                }

                break;
            case R.id.cb_regi_men:
                boolean checkedmen = ((CheckBox) v).isChecked();
                if (checkedmen)
                {
                    look.add("male");
                }
                else {
                    look.remove("male");
                }
                break;

            case R.id.btn_save_register:
//                look;

                String str_age_from = sp_age_from.getSelectedItem().toString();
                String str_age_to = sp_age_to.getSelectedItem().toString();
                range_age.add(str_age_from);
                range_age.add(str_age_to);
                updateRegister(look,range_age);
                updateUI(mAuth.getCurrentUser());
                break;

            case R.id.btn_register:
                int selectedId= regi_rg.getCheckedRadioButtonId();
                rb_gender=findViewById(selectedId);

                String str_gender= rb_gender.getText().toString();
                String txt_username = regi_et_name.getText().toString();
                String txt_email = regi_et_email.getText().toString();
                String txt_password = regi_et_pass.getText().toString();
                String str_age = sp_age.getSelectedItem().toString();

                int age_value=Integer.parseInt(str_age);
                if(age_value<=25)
                {
                    range_age.add("18");
                    range_age.add(""+(age_value+7));
                }
                else if(age_value>=25)
                {
                    range_age.add(""+(age_value-7));
                    range_age.add(""+(age_value+7));
                }


                if(str_gender.equalsIgnoreCase("girl"))
                {
                    look.add("male");
                }
                else if(str_gender.equalsIgnoreCase("boy")) {
                    look.add("female");
                }
                else {
                    look.add("female");
                    look.add("male");
                }

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6 ){
                    Toast.makeText(RegisterActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, " "+txt_username+" "+txt_email+" "+txt_password+" "+str_gender+" "+str_age+" "+look, Toast.LENGTH_SHORT).show();
                    register(txt_username, txt_email, txt_password, str_gender,str_age,look,range_age);
                }

                break;

        }
    }

    private void updateRegister(final ArrayList<String> look, ArrayList<String> age) {

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());

//        User userClass=new User(look,age);
        databaseReference.child("look").setValue(look);
        databaseReference.child("range_age").setValue(age);

//        User userClass=new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString()+"?type=large", "offline",user.getDisplayName().toLowerCase(), str_gender, age, user.getEmail(),
//                user.getProviderId(),str_body_type, str_dob, str_eyes, str_hair, str_height, str_lang,
//                str_look, str_location, str_name, user.getPhoneNumber(), str_nationality, str_visit);
//        databaseReference.setValue(userClass);
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
                        regi_et_pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_eye_off, 0);

                    }
                    else
                    {
                        regi_et_pass.setTransformationMethod(new HideReturnsTransformationMethod());
                        regi_et_pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_eye, 0);
                    }
                    return true;
                }
            }
            return false;
    }
}

