package com.example.tgapplication;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tgapplication.fragment.account.profile.ChangePasswordActivity;
import com.example.tgapplication.fragment.account.profile.ProfileActivity;
import com.example.tgapplication.fragment.account.profile.verify.EditPhoneActivity;
import com.example.tgapplication.login.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileFragment extends Fragment {

    @BindView(R.id.iv_Image)
    ImageView ivImage;
    @BindView(R.id.tv_Profile_Name)
    TextView tvProfileName;
    @BindView(R.id.tv_Profile_Age)
    TextView tvProfileAge;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_my_profile)
    TextView tvMyProfile;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.tv_Private_Photo)
    TextView tvPrivatePhoto;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.tv_Change_Password)
    TextView tvChangePassword;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.tv_Logout)
    TextView tvLogout;
    @BindView(R.id.view5)
    View view5;
    private SharedPreferences sharedPreferences;
    String name,imageUrl,age;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, view);

        sharedPreferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("Name")) {
            name=(sharedPreferences.getString("Name", ""));
        }
        if (sharedPreferences.contains("Age")) {
            age=(sharedPreferences.getString("Age", ""));

        }
        if (sharedPreferences.contains("ImageUrl")) {
            imageUrl=(sharedPreferences.getString("ImageUrl", ""));

        }

        setProfileValue(name,age,imageUrl);

        return view;
    }

    private void setProfileValue(String name, String age, String imageUrl) {
        tvProfileName.setText(name);
        tvProfileAge.setText(age);
        Log.i("TAG", "setProfileValue: "+imageUrl);
        Glide.with(Objects.requireNonNull(getActivity())).load(imageUrl).placeholder(R.drawable.ic_broken_image_primary_24dp).into(ivImage);
    }


    @OnClick({R.id.tv_my_profile, R.id.tv_Logout, R.id.iv_Image,R.id.tv_Change_Password, R.id.tv_verify_acc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //

            case R.id.tv_verify_acc:
                startActivity(new Intent(getActivity(), EditPhoneActivity.class));
                break;
            case R.id.tv_my_profile:
            case R.id.iv_Image:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.tv_Change_Password:
            startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            break;
            case R.id.tv_Logout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
//                snackBar(container,"Logout");
                break;

        }
    }


}
