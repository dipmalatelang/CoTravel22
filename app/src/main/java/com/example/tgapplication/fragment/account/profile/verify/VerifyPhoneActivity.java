package com.example.tgapplication.fragment.account.profile.verify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaos.view.PinView;
import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VerifyPhoneActivity extends BaseActivity {


    PinView pinView;
    Button buttonSignIn;
    @BindView(R.id.cl_verify)
    ConstraintLayout clVerify;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        ButterKnife.bind(this);

        pinView = findViewById(R.id.pinView);

        buttonSignIn = findViewById(R.id.buttonSignIn);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String mVerificationId = intent.getStringExtra("mVerificationId");

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String code = pinView.getText().toString().trim();
                progressBar.setVisibility(View.GONE);
                if (code.isEmpty() || code.length() < 6) {
                    pinView.setError("Enter valid code");
                    pinView.requestFocus();
                    return;
                }

                verifyVerificationCode(mVerificationId, code);

            }
        });

    }


    private void verifyVerificationCode(String mVerificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {


                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            snackBar(clVerify,""+message);

                        }
                    }
                });
    }

}
