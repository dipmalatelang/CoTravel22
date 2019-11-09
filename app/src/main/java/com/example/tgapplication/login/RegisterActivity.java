package com.example.tgapplication.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.BuildConfig;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.module.Upload;
import com.example.tgapplication.fragment.account.profile.ui.ChangePrefActivity;
import com.example.tgapplication.fragment.trip.module.User;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.tgapplication.Constants.PicturesInstance;
import static com.example.tgapplication.Constants.UsersInstance;


public class RegisterActivity extends BaseActivity implements View.OnTouchListener {

    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.regi_et_name)
    EditText regiEtName;
    @BindView(R.id.regi_et_email)
    EditText regiEtEmail;
    @BindView(R.id.regi_et_pass)
    EditText regiEtPass;
    @BindView(R.id.regi_et_location)
    EditText regiEtLocation;
    @BindView(R.id.sp_age)
    Spinner spAge;
//    @BindView(R.id.regi_rg)
    RadioGroup regiRg;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.relativelayout)
    RelativeLayout relativelayout;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    ArrayList<String> travel_with = new ArrayList<>();
    ArrayList<String> looking_for = new ArrayList<>();
    ArrayList<String> range_age = new ArrayList<>();

    RadioButton rb_gender;

    ArrayList<String> array_age;
    ArrayAdapter<String> adapter_age;
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Places.initialize(getApplicationContext(), BuildConfig.map_api_key);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();


        regiEtPass.setOnTouchListener(this);
        tvTitleText.setPaintFlags(tvTitleText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvTitleText.setText(getResources().getString(R.string.register));

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        initAge();

        regiRg=findViewById(R.id.regi_rg);


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

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Tiger", "facebook:onError", error);

            }
        });

        setPopup();

        assert getSupportActionBar() != null; //null check
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initAge() {

        array_age = new ArrayList<>(Arrays.asList("18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
                "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70",
                "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90",
                "91", "92", "93", "94", "95", "96", "97", "98", "99"));

        adapter_age = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, array_age);
        adapter_age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spAge.setAdapter(adapter_age);

        spAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            ListPopupWindow AgepopupWindow = (ListPopupWindow) popup.get(spAge);

            // Set popupWindow height to 500px
            Objects.requireNonNull(AgepopupWindow).setHeight(500);


        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void handleFacebookAccessToken(String token) {
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        Log.d("Tiger", "" + credential);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Tiger", "handleFacebookAccessToken:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tiger", "signInWithCredential:success");
                            dismissProgressDialog();

                            FirebaseUser user = mAuth.getCurrentUser();

                            travel_with.add("Female");
                            travel_with.add("Male");

                            range_age.add("18");
                            range_age.add("55");

                            User userClass = new User(Objects.requireNonNull(user).getUid(), user.getDisplayName(), "offline", Objects.requireNonNull(user.getDisplayName()).toLowerCase(), "", "18", user.getEmail(), user.getProviderId(), "", "", "", "", "", "", travel_with, looking_for, range_age, "", user.getDisplayName().toLowerCase(), user.getPhoneNumber(), "", "", 1, "");
                            UsersInstance.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(userClass);

                            String uploadId = PicturesInstance.child(user.getUid()).push().getKey();
                            PicturesInstance.child(user.getUid()).child(Objects.requireNonNull(uploadId)).setValue(new Upload(uploadId, "Image", Objects.requireNonNull(user.getPhotoUrl()).toString() + "?type=large", 1));

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

    private void register(final String username, final String email, String password, final String str_gender, final String str_age, String location, final ArrayList<String> travel_with, final ArrayList<String> range_age) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            snackBar(relativelayout, "Success");

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            Log.i("Done", "gothere");
                            User userClass = new User(userid, username, "offline", username.toLowerCase(), str_gender, str_age, email, firebaseUser.getProviderId(), "", "", "",
                                    "", "", "", travel_with, looking_for, range_age, location, username, "", "", "", 1, "");
                            UsersInstance.child(userid).setValue(userClass);
                            Log.i("Done", "gotin");

                            range_age.clear();
                            travel_with.clear();
                            startActivity(new Intent(RegisterActivity.this, ChangePrefActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure" + Objects.requireNonNull(task.getException()).getMessage());
                            snackBar(relativelayout, task.getException().getMessage());
                            dismissProgressDialog();
                            updateUI(null);
                        }

                    }

                });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (regiEtPass.getRight() - regiEtPass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                // your action here
                if (!regiEtPass.getTransformationMethod().toString().contains("Password")) {
                    regiEtPass.setTransformationMethod(new PasswordTransformationMethod());
                    regiEtPass.setSelection(regiEtPass.getText().length());
                    regiEtPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_white_24dp, 0, R.drawable.ic_action_eye_off, 0);

                } else {
                    regiEtPass.setTransformationMethod(new HideReturnsTransformationMethod());
                    regiEtPass.setSelection(regiEtPass.getText().length());
                    regiEtPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_white_24dp, 0, R.drawable.ic_action_eye, 0);
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
                regiEtLocation.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, Objects.requireNonNull(status.getStatusMessage()));
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else {
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick({R.id.regi_et_location, R.id.textInput_location, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.regi_et_location:
            case R.id.textInput_location:
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;
            case R.id.btn_register:

                int selectedId = regiRg.getCheckedRadioButtonId();
                Log.i(TAG, "onClick: " + selectedId);
                rb_gender = findViewById(selectedId);

                String str_gender = null;
                String txt_username = regiEtName.getText().toString();
                String txt_email = regiEtEmail.getText().toString();
                String txt_password = regiEtPass.getText().toString();
                String str_age = spAge.getSelectedItem().toString();
                String txt_location = regiEtLocation.getText().toString();


                int age_value = Integer.parseInt(str_age);
                if (age_value <= 25) {
                    range_age.add("18");
                    range_age.add("" + (age_value + 7));
                } else {
                    range_age.add("" + (age_value - 7));
                    range_age.add("" + (age_value + 7));
                }

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_location)) {
                    snackBar(relativelayout, "All fileds are required");

                } else if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                    snackBar(relativelayout, "please enter valid email address");
                } else if (txt_password.length() < 6) {

                    snackBar(relativelayout, "password must be at least 6 characters");
                } else if (rb_gender == null) {
                    snackBar(relativelayout, "Select gender");
                } else {

                    if (rb_gender.getText().toString().equalsIgnoreCase("Girl")) {
                        str_gender = "Female";
                    } else if (rb_gender.getText().toString().equalsIgnoreCase("Boy")) {
                        str_gender = "Male";
                    }

                    if (str_gender.equalsIgnoreCase("Female")) {
                        travel_with.add("Male");
                    } else if (str_gender.equalsIgnoreCase("Male")) {
                        travel_with.add("Female");
                    } else {
                        travel_with.add("Female");
                        travel_with.add("Male");
                    }

                    Log.i(TAG, "onClick: " + txt_location);
                    register(txt_username, txt_email, txt_password, str_gender, str_age, txt_location, travel_with, range_age);
                    snackBar(relativelayout, "Register Successfully..!");
                }
                break;
        }
    }
}

