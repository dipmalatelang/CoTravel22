package com.example.tgapplication.fragment.account.profile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoRequestActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_requests);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_photo_rqst, R.id.btn_photo_permits, R.id.btn_given_permits})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_photo_rqst:
                Intent intent=new Intent(this,ViewPhotoRequestActivity.class);
                intent.putExtra("intentType","request");
                startActivity(intent);
                break;
            case R.id.btn_photo_permits:
                Intent intent1=new Intent(this,ViewPhotoRequestActivity.class);
                intent1.putExtra("intentType","photoPermits");
                startActivity(intent1);
                Toast.makeText(this, "PhotoPermitsActivity", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(this,PhotoPermitsActivity.class));
                break;
            case R.id.btn_given_permits:
                Intent intent2=new Intent(this,ViewPhotoRequestActivity.class);
                intent2.putExtra("intentType","givenPermits");
                startActivity(intent2);
                Toast.makeText(this, "GivenPermitsActivity", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(this,GivenPermitsActivity.class));
                break;
        }
    }
}
