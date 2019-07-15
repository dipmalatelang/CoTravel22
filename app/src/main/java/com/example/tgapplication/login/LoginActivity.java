package com.example.tgapplication.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
//import com.example.tgapplication.trips.TripActivity;
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
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, View.OnKeyListener {

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

        input_password.setOnTouchListener(this);
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


    private void updateUI(FirebaseUser account) {
        if (account != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

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
                hideKeyboard();
                String txt_email = input_email.getText().toString().trim();
                String txt_password = input_password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    snackBar(constrainlayout,"All fileds are required !");


                }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(txt_email).matches())
                {
                    snackBar(constrainlayout, "please enter valid email address");
                    dismissProgressDialog();
                }

               else {
                    hideKeyboard();
                    mAuth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        updateUI(mAuth.getCurrentUser());
//                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
//

                                    } else {
                                        snackBar(constrainlayout,"Authentication failed !");
                                        dismissProgressDialog();
                                    }
                                }
                            });
                    dismissProgressDialog();
                }

                if(CheckNetwork.isInternetAvailable(this)) //returns true if internet available
                {

                    showProgressDialog();
                }
                else
                {
                    dismissProgressDialog();
                    snackBar(constrainlayout, "no internet connection");
                }
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
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_LEFT = 0;
        final int DRAWABLE_TOP = 1;
        final int DRAWABLE_RIGHT = 2;
        final int DRAWABLE_BOTTOM = 3;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (input_password.getRight() - input_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                // your action here

                if (!input_password.getTransformationMethod().toString().contains("Password")) {
                    input_password.setTransformationMethod(new PasswordTransformationMethod());
                    input_password.setSelection(input_password.getText().length());
                    input_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_eye_off, 0);

                } else {
                    input_password.setTransformationMethod(new HideReturnsTransformationMethod());
                    input_password.setSelection(input_password.getText().length());
                    input_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_eye, 0);
                }
                return true;
            }
        }
        return false;
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


