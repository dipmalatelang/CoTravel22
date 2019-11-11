package com.example.tgapplication.fragment.account.profile.verify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPhoneActivity extends BaseActivity {


    @BindView(R.id.ed_number)
    EditText edNumber;
    @BindView(R.id.btn_sub)
    Button btnSub;
    @BindView(R.id.cl_phone)
    ConstraintLayout clPhone;
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

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            code = phoneAuthCredential.getSmsCode();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            snackBar(clPhone,""+e.getMessage());

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mVerificationId = s;
            Intent intent = new Intent(EditPhoneActivity.this, VerifyPhoneActivity.class);
            intent.putExtra("mVerificationId", mVerificationId);
            intent.putExtra("code", code);
            startActivity(intent);

        }
    };

}
