package com.example.tgapplication.fragment.account.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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

    @OnClick({R.id.btnSaveNow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSaveNow:
                currentpasword =etCurrentPassword.getText().toString();
                newPass = etNewpassword.getText().toString();
                Confirmpassword =etConfirmpassword.getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
// Get auth credentials from the user for re-authentication. The example below shows
                sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                if (sharedPreferences.contains("Email")) {
                    email_id=(sharedPreferences.getString("Email", ""));
                }
                if (sharedPreferences.contains("Password"))
                {
                    txt_password=(sharedPreferences.getString("Password", ""));

                }
//                currentpasword.equals(txt_password);

                Log.d(TAG, "onViewClicked: "+txt_password+" "+currentpasword+" "+newPass+" "+Confirmpassword+" "+email_id);
                if (currentpasword.equalsIgnoreCase("") || currentpasword == null) {
                    etCurrentPassword.setError("Enter Current Password");
                } else if (newPass.equalsIgnoreCase("") || newPass == null ) {
                    etNewpassword.setError("Enter new Password");
                } else if (Confirmpassword.equalsIgnoreCase("") || Confirmpassword == null) {
                    etConfirmpassword.setError("Enter Confirm Password");
                } else if(!newPass.equals(Confirmpassword)) {
                    snackBar(clChangepwd,"Current and Confirm Password Not Match");
//                    DataHolder.alertDialog("Error","",Settings_ChangePassword_Activity.this);
                }else if(newPass.length() < 8){
                    snackBar(clChangepwd, "password must be at least 8 characters");
                }

                else
                {
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email_id, txt_password);
                    Log.d(TAG, "onViewClicked:"+txt_password);
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
                                            new Handler().postDelayed(new Runnable() {

                                                @Override
                                                public void run() {
                                                    // change image
                                                    FirebaseAuth.getInstance().signOut();
                                                    LoginManager.getInstance().logOut();
                                                    finish();
                                                    startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                                                }

                                            }, 900);

                                        } else {
                                            Log.d(TAG, "Error password not updated");
                                            snackBar(clChangepwd, "Error password not updated");
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "Error auth failed");
                                snackBar(clChangepwd, "Enter valid Password");
                            }

                        }
                    });

                }



        }
// Prompt the user to re-provide their sign-in credentials



    }
}


