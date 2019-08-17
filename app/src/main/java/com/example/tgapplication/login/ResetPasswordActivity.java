package com.example.tgapplication.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    EditText send_email;
    Button btn_reset;
    String value;

    FirebaseAuth firebaseAuth;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Reset Password");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        send_email = findViewById(R.id.send_email);
        btn_reset = findViewById(R.id.btn_reset);

        value = getIntent().getExtras().getString("nextActivity");

        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(this);
        linearLayout = findViewById(R.id.linearLayout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_reset:
                showProgressDialog();
                        String email = send_email.getText().toString();

                        if (email.equals("")){
                            dismissProgressDialog();
                            snackBar(linearLayout,"All fileds are required!");
                        } else {
                            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        dismissProgressDialog();
                                       snackBar(linearLayout,"Please check you Email");
                                        Intent resetIntent= new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                        resetIntent.putExtra("nextActivity",value);
                                        startActivity(resetIntent);
                                    }
                                    else
                                    {
                                        dismissProgressDialog();
                                        String error = task.getException().getMessage();

                                        snackBar(linearLayout,error);

                                    }
                                }
                            });
                        }
                break;
        }
    }
}
