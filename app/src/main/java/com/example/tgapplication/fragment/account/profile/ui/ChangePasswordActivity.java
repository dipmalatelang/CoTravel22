package com.example.tgapplication.fragment.account.profile.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.login.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.etCurrentPassword)
    EditText etCurrentPassword;

    @BindView(R.id.etNewpassword)
    EditText etNewpassword;

    @BindView(R.id.etConfirmpassword)
    EditText etConfirmpassword;
    @BindView(R.id.btnSaveNow)
    Button btnSaveNow;

    String newPass,currentpasword,Confirmpassword;
    @BindView(R.id.cl_changepwd)
    ConstraintLayout clChangepwd;
    SharedPreferences sharedPreferences;
    String email_id,txt_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        ButterKnife.bind(this);

        etCurrentPassword.setOnTouchListener((view, motionEvent) -> showOrHidePwd(motionEvent, etCurrentPassword));
        etNewpassword.setOnTouchListener((view, motionEvent) -> showOrHidePwd(motionEvent, etNewpassword));
        etConfirmpassword.setOnTouchListener((view, motionEvent) -> showOrHidePwd(motionEvent, etConfirmpassword));
    }

    @OnClick({R.id.btnSaveNow})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btnSaveNow) {
            currentpasword = etCurrentPassword.getText().toString();
            newPass = etNewpassword.getText().toString();
            Confirmpassword = etConfirmpassword.getText().toString();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
            if (sharedPreferences.contains("Email")) {
                email_id = (sharedPreferences.getString("Email", ""));
            }
            if (sharedPreferences.contains("Password")) {
                txt_password = (sharedPreferences.getString("Password", ""));

            }

            if (currentpasword.equalsIgnoreCase("")) {
                etCurrentPassword.setError("Enter Current Password");
            } else if (newPass.equalsIgnoreCase("")) {
                etNewpassword.setError("Enter new Password");
            } else if (Confirmpassword.equalsIgnoreCase("")) {
                etConfirmpassword.setError("Enter Confirm Password");
            } else if (!newPass.equals(Confirmpassword)) {
                snackBar(clChangepwd, "Current and Confirm Password Not Match");
            } else if (newPass.length() < 8) {
                snackBar(clChangepwd, "password must be at least 8 characters");
            } else {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email_id, txt_password);
                Objects.requireNonNull(user).reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        snackBar(clChangepwd, "Password updated");
                                        new Handler().postDelayed(new Runnable() {

                                            @Override
                                            public void run() {

                                                FirebaseAuth.getInstance().signOut();
                                                LoginManager.getInstance().logOut();
                                                finish();
                                                startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                                            }

                                        }, 900);

                                    } else {
                                        snackBar(clChangepwd, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            snackBar(clChangepwd, "Enter valid Password");
                        }

                    }
                });

            }
        }

    }
}


