package com.example.tgapplication.fragment.account.profile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoRequestActivity extends BaseActivity {
    @BindView(R.id.cl_Photo_Request)
    ConstraintLayout clPhotoRequest;

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
                Intent intent = new Intent(this, ViewPhotoRequestActivity.class);
                intent.putExtra("intentType", "request");
                startActivity(intent);
                break;
            case R.id.btn_photo_permits:
                Intent intent1 = new Intent(this, ViewPhotoRequestActivity.class);
                intent1.putExtra("intentType", "photoPermits");
                startActivity(intent1);
                snackBar(clPhotoRequest,"PhotoPermitsActivity");
                break;
            case R.id.btn_given_permits:
                Intent intent2 = new Intent(this, ViewPhotoRequestActivity.class);
                intent2.putExtra("intentType", "givenPermits");
                startActivity(intent2);
                snackBar(clPhotoRequest,"GivenPermitsActivity");
                break;
        }
    }
}
