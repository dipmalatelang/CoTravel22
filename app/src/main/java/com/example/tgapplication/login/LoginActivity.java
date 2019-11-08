package com.example.tgapplication.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.account.profile.module.Upload;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.ButterKnife;

import static com.example.tgapplication.Constants.PicturesInstance;
import static com.example.tgapplication.Constants.UsersInstance;

//import com.example.tgapplication.trips.TripActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {

    LoginButton loginButton;
    Button btn_login;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    String value;
    TextView tv_register, link_signup;
    EditText input_email, input_password;
    ConstraintLayout constrainlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.login_button);
        tv_register = findViewById(R.id.tv_register);


        constrainlayout = findViewById(R.id.constrainlayout);


        tv_register.setPaintFlags(tv_register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_register.setText(getResources().getString(R.string.register));

        link_signup = findViewById(R.id.link_signup);
        link_signup.setOnClickListener(this);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);


        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);

        input_password.setOnTouchListener((view, motionEvent) -> showOrHidePwd(motionEvent,input_password));
        input_password.setOnKeyListener(this);

        tv_register.setOnClickListener(this);


//        value = getIntent().getExtras().getString("nextActivity");

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
    }


    private void handleFacebookAccessToken(String token) {
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        Log.d("Tiger", "" + credential);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Tiger", "handleFacebookAccessToken:" + task.isSuccessful());
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tiger", "signInWithCredential:success");
                            dismissProgressDialog();

                            UsersInstance.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(!dataSnapshot.exists())
                                    {
//                                        Toast.makeText(LoginActivity.this, "First Register", Toast.LENGTH_SHORT).show();
                                        registerFromLogin();
                                    }
                                    else {
                                        updateUI(mAuth.getCurrentUser());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        }

                    }
                });
    }

    private void registerFromLogin() {

        FirebaseUser user = mAuth.getCurrentUser();
        ArrayList<String> travel_with = new ArrayList<>();
        ArrayList<String> looking_for = new ArrayList<>();
        ArrayList<String> range_age=new ArrayList<>();

        User userClass=new User(Objects.requireNonNull(user).getUid(), user.getDisplayName(), "offline", Objects.requireNonNull(user.getDisplayName()).toLowerCase(), "", "",  user.getEmail(), user.getProviderId(), "", "", "", "", "", "",travel_with,looking_for, range_age, "",  user.getDisplayName().toLowerCase(), user.getPhoneNumber(), "", "",1,"");
        UsersInstance.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(userClass);

        String uploadId = PicturesInstance.child(user.getUid()).push().getKey();
        PicturesInstance.child(user.getUid()).child(Objects.requireNonNull(uploadId)).setValue(new Upload(uploadId,"Image", Objects.requireNonNull(user.getPhotoUrl()).toString()+"?type=large",1));

        updateUI(mAuth.getCurrentUser());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


/*    private void updateUI(FirebaseUser account) {
        if (account != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link_signup:
                Intent resetIntent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                resetIntent.putExtra("nextActivity", value);
                startActivity(resetIntent);
                break;
            case R.id.btn_login:
                showProgressDialog();
                String txt_email = input_email.getText().toString().trim();
                String txt_password = input_password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    dismissProgressDialog();
                    snackBar(constrainlayout,"All fileds are required !");

                }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(txt_email).matches())
                {
                    dismissProgressDialog();
                    snackBar(constrainlayout, "please enter valid email address");
                }
               else {
                    Log.d(TAG, "onComplete1: "+txt_email+" "+txt_password);
//                    hideKeyboard(this);
                    mAuth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        dismissProgressDialog();
                                        updateUI(mAuth.getCurrentUser());

                                        Log.d(TAG, "onComplete: "+txt_email+" "+txt_password);
                                        saveLoginDetails(txt_email,txt_password);

//                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
//

                                    } else {
                                        snackBar(constrainlayout, Objects.requireNonNull(task.getException()).getMessage());
                                        dismissProgressDialog();
                                    }
                                }
                            });
                }

           /*     if(CheckNetwork.isInternetAvailable(this)) //returns true if internet available
                {

                    showProgressDialog();
                }
                else
                {
                    dismissProgressDialog();
                    snackBar(constrainlayout, "no internet connection");
                }*/
                break;
            case R.id.tv_register:
//                Log.i("Send while Login", value);
                Intent loginIntent = new Intent(LoginActivity.this, RegisterActivity.class);
//                loginIntent.putExtra("nextActivity", value);
                startActivity(loginIntent);
                break;
        }

    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            btn_login.performClick();
            return true;
        }
        return false;
    }
//    private boolean isNetworkConnected() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        return cm.getActiveNetworkInfo() != null;
//    }
}


