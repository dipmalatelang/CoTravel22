package com.example.tgapplication.fragment.account;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.ui.ChangePasswordActivity;
import com.example.tgapplication.fragment.account.profile.ui.ChangePrefActivity;
import com.example.tgapplication.fragment.account.profile.ui.PhotoRequestActivity;
import com.example.tgapplication.fragment.account.profile.ui.ProfileActivity;
import com.example.tgapplication.fragment.account.profile.ui.SettingsActivity;
import com.example.tgapplication.fragment.account.profile.ui.TrashActivity;
import com.example.tgapplication.fragment.account.profile.verify.EditPhoneActivity;
import com.example.tgapplication.login.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.pkmmte.view.CircularImageView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileFragment extends BaseFragment {


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
    @BindView(R.id.tv_Trash)
    TextView tvTrash;
    @BindView(R.id.tv_photo_request)
    TextView tvPhotoRequest;
    @BindView(R.id.iv_Image)
    ImageView ivImage;
    @BindView(R.id.tv_Change_Preferences)
    TextView tvChangePreferences;
    @BindView(R.id.tv_verify_acc)
    TextView tvVerifyAcc;
    @BindView(R.id.view6)
    View view6;
    @BindView(R.id.tv_Setting)
    TextView tvSetting;
    @BindView(R.id.view7)
    View view7;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private SharedPreferences sharedPreferences;
    private String name, imageUrl, age, gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, view);

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("Name")) {
            name = (sharedPreferences.getString("Name", ""));
            tvProfileName.setText(name);
        }
        if (sharedPreferences.contains("Age")) {
            age = (sharedPreferences.getString("Age", ""));
            tvProfileAge.setText(age);

        }
        if (sharedPreferences.contains("ImageUrl")) {
            imageUrl = (sharedPreferences.getString("ImageUrl", ""));
            Glide.with(getActivity()).load(imageUrl).placeholder(R.drawable.whitewallpapar).into(ivImage);

        }

        if (sharedPreferences.contains("Gender")) {
            gender = (sharedPreferences.getString("Gender", "Male"));

            if(imageUrl==null || imageUrl.equalsIgnoreCase(""))
            {
                if (gender.equalsIgnoreCase("Female")) {
                    ivImage.setImageResource(R.drawable.no_photo_female);

                } else {
                    ivImage.setImageResource(R.drawable.no_photo_male);
                }
            }

        }

        return view;
    }

    @OnClick({R.id.tv_Setting, R.id.tv_my_profile, R.id.tv_Logout, R.id.iv_Image, R.id.tv_Change_Password, R.id.tv_verify_acc, R.id.tv_Change_Preferences, R.id.tv_Trash, R.id.tv_photo_request})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //

            case R.id.tv_Setting:
                Intent sIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(sIntent);
                break;

            case R.id.tv_photo_request:
                startActivity(new Intent(getActivity(), PhotoRequestActivity.class));
                break;
            case R.id.tv_Trash:
                startActivity(new Intent(getActivity(), TrashActivity.class));
                break;

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
                clearSharedPref();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();

                Objects.requireNonNull(getActivity()).finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));

                break;

            case R.id.tv_Change_Preferences:
                Intent mIntent = new Intent(getActivity(), ChangePrefActivity.class);
                startActivity(mIntent);

                break;

        }
    }


}
