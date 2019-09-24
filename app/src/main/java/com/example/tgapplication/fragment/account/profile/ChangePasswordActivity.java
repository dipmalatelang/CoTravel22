package com.example.tgapplication.fragment.account.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    String newPass;
    @BindView(R.id.cl_changepwd)
    ConstraintLayout clChangepwd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String email_id,txt_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chnage);
        ButterKnife.bind(this);

        etCurrentPassword.setOnTouchListener((view, motionEvent) -> showOrHidePwd(motionEvent, etCurrentPassword));
        etNewpassword.setOnTouchListener((view, motionEvent) -> showOrHidePwd(motionEvent, etNewpassword));
        etConfirmpassword.setOnTouchListener((view, motionEvent) -> showOrHidePwd(motionEvent, etConfirmpassword));
    }

    @OnClick({R.id.etCurrentPassword, R.id.etNewpassword, R.id.etConfirmpassword, R.id.btnSaveNow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.etCurrentPassword:
                break;
            case R.id.etNewpassword:
                break;
            case R.id.etConfirmpassword:
                break;
            case R.id.btnSaveNow:

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
// Get auth credentials from the user for re-authentication. The example below shows
                sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                if (sharedPreferences.contains("Email")) {
                    email_id=(sharedPreferences.getString("Email", ""));
                }
                if (sharedPreferences.contains("Password")) {
                    txt_password=(sharedPreferences.getString("Password", ""));

                }
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email_id, txt_password);
                Log.d(TAG, "onViewClicked:"+txt_password);

                newPass = etNewpassword.getText().toString();
// Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: "+newPass);
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Password updated");
                                        snackBar(clChangepwd, "Password updated");
                                    } else {
                                        Log.d(TAG, "Error password not updated");
                                        snackBar(clChangepwd, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "Error auth failed");
                            snackBar(clChangepwd, "Error auth failed");
                        }

                    }
                });


        }
    }

}
