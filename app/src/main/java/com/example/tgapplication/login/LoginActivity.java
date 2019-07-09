package com.example.tgapplication.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.chat.ChatActivity;
import com.example.tgapplication.chat.MessageActivity;
import com.example.tgapplication.trips.AddTripActivity;
import com.example.tgapplication.trips.ProfileActivity;
import com.example.tgapplication.trips.TripActivity;
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

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    LoginButton loginButton;
    Button btn_login;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    String value;
    TextView tv_register, link_signup;
    EditText input_email, input_password;

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

        tv_register.setPaintFlags(tv_register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_register.setText(getResources().getString(R.string.register));

        link_signup = findViewById(R.id.link_signup);
        link_signup.setOnClickListener(this);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);

        input_password.setOnTouchListener(this);

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

   /* private void updateUI(FirebaseUser account) {
        if (account != null) {
            Log.i("FBData", "" + account.getIdToken(true) + " " + account.getMetadata() + " " + account.getProviderData());
            Log.i("NextNow", value);
            if (value.equalsIgnoreCase("Chat")) {
                Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
                Toast.makeText(this, "in" + account, Toast.LENGTH_SHORT).show();
            } else if (value.equalsIgnoreCase("AddTrips")) {
                Intent intent = new Intent(LoginActivity.this, AddTripActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
                Toast.makeText(this, "in" + account, Toast.LENGTH_SHORT).show();
            } else if (value.equalsIgnoreCase("Trips")) {
                Intent intent = new Intent(LoginActivity.this, TripActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
                Toast.makeText(this, "in" + account, Toast.LENGTH_SHORT).show();
            } else if (value.equalsIgnoreCase("TripsMsg")) {
                String user = getIntent().getExtras().getString("nextActivityUser");
                Intent intent = new Intent(LoginActivity.this, MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userid", user);
                startActivity(intent);
                finish();
                Toast.makeText(this, "in" + account, Toast.LENGTH_SHORT).show();
            } else if (value.equalsIgnoreCase("profileEdit")) {
                Intent msgIntent=new Intent(LoginActivity.this, ProfileActivity.class);
//                msgIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                msgIntent.putExtra("nextActivity","profileEdit");
                startActivity(msgIntent);
                Toast.makeText(this, "Profile edited Successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "out", Toast.LENGTH_SHORT).show();
        }
    }*/

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
                String txt_email = input_email.getText().toString();
                String txt_password = input_password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                } else {

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
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            case R.id.tv_register:
                Log.i("Send while Login", value);
                Intent loginIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                loginIntent.putExtra("nextActivity", value);
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
                    input_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_eye_off, 0);

                } else {
                    input_password.setTransformationMethod(new HideReturnsTransformationMethod());
                    input_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_eye, 0);
                }
                return true;
            }
        }
        return false;
    }
}


