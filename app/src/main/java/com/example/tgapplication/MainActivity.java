package com.example.tgapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tgapplication.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.trip)
    Button trip;
    @BindView(R.id.chat)
    Button chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.trip, R.id.chat})
    public void onViewClicked(View v) {
        switch (v.getId())
        {
            case R.id.chat:
                Intent chatIntent= new Intent(this, LoginActivity.class);
                chatIntent.putExtra("nextActivity","Chat");
                startActivity(chatIntent);

                break;

            case R.id.trip:
                Intent tripIntent = new Intent(this, LoginActivity.class);
                tripIntent.putExtra("nextActivity", "Trips");
                startActivity(tripIntent);
                break;
        }

    }

}
