package com.example.tgapplication.fragment.account.profile.verify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.google.firebase.auth.FirebaseUser;

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
    private FirebaseUser fuser;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_sub)
    public void onViewClicked() {
        String mobile = edNumber.getText().toString().trim();

        if (mobile.isEmpty() || mobile.length() < 10) {
            edNumber.setError("Enter a valid mobile");
            edNumber.requestFocus();
            return;
        }

        Intent intent = new Intent(EditPhoneActivity.this, VerifyPhoneActivity.class);
        intent.putExtra("mobile", mobile);
        startActivity(intent);
    }
}