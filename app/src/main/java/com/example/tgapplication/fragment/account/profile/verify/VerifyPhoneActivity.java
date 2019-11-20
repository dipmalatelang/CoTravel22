package com.example.tgapplication.fragment.account.profile.verify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaos.view.PinView;
import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VerifyPhoneActivity extends BaseActivity {


    Button buttonSignIn;
    @BindView(R.id.cl_verify)
    ConstraintLayout clVerify;
    @BindView(R.id.pinView)
    PinView pinView;
    private FirebaseAuth mAuth;
    private FirebaseUser fuser;
    private String mVerificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        ButterKnife.bind(this);
        //initializing objects
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                pinView.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            retrieveUserDetail(currentUser);

        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        if(fuser!=null)
        {
            fuser.linkWithCredential(credential)
                    .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //verification successful we will start the profile activity
                                FirebaseUser user = task.getResult().getUser();
                                snackBar(clVerify,user.getPhoneNumber()+" "+user.getUid());
                                setPhoneNumber(fuser.getUid(),user.getPhoneNumber());
                                updateUI(user);

                           /* Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/

                            } else {
                                //verification unsuccessful.. display an error message

                                String message = "Somthing is wrong, we will fix it soon...";

                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    message = "Invalid code entered...";
                                }
                                snackBar(clVerify,task.getException().getMessage());
                                updateUI(null);
                            }
                        }
                    });
        }
        else {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                snackBar(clVerify,user.getPhoneNumber()+" "+user.getUid());
                                setPhoneNumber(fuser.getUid(),user.getPhoneNumber());
                                updateUI(user);

                            } else {
                                //verification unsuccessful.. display an error message

                                String message = "Somthing is wrong, we will fix it soon...";

                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    message = "Invalid code entered...";
                                }
                                snackBar(clVerify,task.getException().getMessage());
                                updateUI(null);
                            }
                        }
                    });
        }

    }

    @OnClick(R.id.buttonSignIn)
    public void onViewClicked() {
        String code = pinView.getText().toString().trim();
        if (code.isEmpty() || code.length() < 6) {
            pinView.setError("Enter valid code");
            pinView.requestFocus();
            return;
        }

        //verifying the code entered manually
        verifyVerificationCode(code);
    }
}