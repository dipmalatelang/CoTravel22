package com.example.tgapplication.fragment.account;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.ChangePasswordActivity;
import com.example.tgapplication.fragment.account.profile.ChangePrefActivity;
import com.example.tgapplication.fragment.account.profile.PhotoRequestActivity;
import com.example.tgapplication.fragment.account.profile.ProfileActivity;
import com.example.tgapplication.fragment.account.profile.TrashActivity;
import com.example.tgapplication.fragment.account.profile.verify.EditPhoneActivity;
import com.example.tgapplication.login.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileFragment extends BaseFragment {

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
    private SharedPreferences sharedPreferences;
    String name, imageUrl, age,gender;

    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, view);

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("Name")) {
            name = (sharedPreferences.getString("Name", ""));
        }
        if (sharedPreferences.contains("Age")) {
            age = (sharedPreferences.getString("Age", ""));

        }
        if (sharedPreferences.contains("ImageUrl")) {
            imageUrl = (sharedPreferences.getString("ImageUrl", ""));

        }

        if (sharedPreferences.contains("Gender")) {
            gender = (sharedPreferences.getString("Gender", ""));

        }

        Log.i("TAG", "onCreateView: " + imageUrl);
        setProfileValue(name, age, imageUrl,gender);

        return view;
    }

    private void setProfileValue(String name, String age, String imageUrl, String gender) {
        tvProfileName.setText(name);
        tvProfileAge.setText(age);
        Log.i("TAG", "setProfileValue: " + imageUrl);
        if(gender!=null && !gender.equalsIgnoreCase(""))
        {
            if(gender.equalsIgnoreCase("Female")||gender.equalsIgnoreCase("Girl"))
            {
                Glide.with(getActivity()).asBitmap().load(imageUrl)
                        .fitCenter()
                        .override(450,600)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                                ivImage.setImageResource(R.drawable.no_photo_female);
                           /* ClipDrawable mImageDrawable = (ClipDrawable) holder.profile_image.getDrawable();
                            mImageDrawable.setLevel(5000);*/
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                ivImage.setImageBitmap(resource);
                            }
                        });
            }
        }
        else {
            Glide.with(getActivity()).asBitmap().load(imageUrl)
                    .centerCrop()
                    .override(450,600)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                            ivImage.setImageResource(R.drawable.no_photo_male);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            ivImage.setImageBitmap(resource);
                        }
                    });
        }
//        Glide.with(Objects.requireNonNull(getActivity())).load(imageUrl).placeholder(R.drawable.ic_broken_image_primary_24dp).centerCrop().into(ivImage);
    }


    @OnClick({R.id.tv_my_profile, R.id.tv_Logout, R.id.iv_Image, R.id.tv_Change_Password, R.id.tv_verify_acc, R.id.tv_Change_Preferences, R.id.tv_Trash, R.id.tv_photo_request})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //

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
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                clearSharedPref();
                Objects.requireNonNull(getActivity()).finish();

                startActivity(new Intent(getActivity(), LoginActivity.class));
//                snackBar(container,"Logout");
                break;

            case R.id.tv_Change_Preferences:
                Intent mIntent = new Intent(getActivity(), ChangePrefActivity.class);
                startActivity(mIntent);
//              Toast.makeText(getActivity(), "Preferrences", Toast.LENGTH_SHORT).show();

                break;

        }
    }


}
