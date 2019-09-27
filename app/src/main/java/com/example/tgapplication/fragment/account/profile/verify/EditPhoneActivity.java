package com.example.tgapplication.fragment.account.profile.verify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tgapplication.R;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPhoneActivity extends AppCompatActivity {


    @BindView(R.id.ed_number)
    EditText edNumber;
    @BindView(R.id.btn_sub)
    Button btnSub;
    private String mVerificationId, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        ButterKnife.bind(this);



    }




    @OnClick(R.id.btn_sub)
    public void onViewClicked() {
        String mobile = edNumber.getText().toString().trim();

                if (mobile.isEmpty()) {
                    edNumber.setError("Enter a valid mobile Number");
                    edNumber.requestFocus();
                    return;
                }

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
            code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code

          /*  if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }*/


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(EditPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
            Intent intent = new Intent(EditPhoneActivity.this, VerifyPhoneActivity.class);
        intent.putExtra("mVerificationId", mVerificationId);
        intent.putExtra("code",code);
        startActivity(intent);

        }
    };


}
