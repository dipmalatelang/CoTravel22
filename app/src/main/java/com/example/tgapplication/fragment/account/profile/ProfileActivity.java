package com.example.tgapplication.fragment.account.profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.EditProfileActivity;
import com.example.tgapplication.photo.Upload;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {

    ViewPager viewPager;
    CustomAdapter adapter;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.profile_details)
    ConstraintLayout profileDetails;
    @BindView(R.id.textView)
    Chip textView;
    @BindView(R.id.textProfile)
    Chip textProfile;
    private DatabaseReference mDatabase;
    private ArrayList<Upload> uploads =new ArrayList<>();
    private FirebaseUser fuser;
    //    @BindView(R.id.bottomNav)
//    ConstraintLayout bottomNav;
    private int[] images = {R.drawable.image1, R.drawable.login_bg, R.drawable.image1, R.drawable.login_bg, R.drawable.image1};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        ButterKnife.bind(this);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        getAllImages();

//        getProfileData();

    }

    public void getAllImages()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("Pictures").child(fuser.getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
                Log.i(TAG, "onCreate: size"+uploads.size());
                adapter = new CustomAdapter(ProfileActivity.this, fuser.getUid(), uploads);
                viewPager.setAdapter(adapter);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        int i = position + 1;
                        Log.i(TAG, "onCreate: " + i);
                        textView.setText(i + " / " + uploads.size());
                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick({R.id.iv_info, R.id.iv_msg, R.id.iv_trip, R.id.textProfile, R.id.iv_edit_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_info:
                Log.i(TAG, "onViewClicked: " + constraintLayout.getHeight());
                if (constraintLayout.getTranslationY() != 0) {
                    constraintLayout.animate().translationY(0).alpha(1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            profileDetails.setVisibility(View.GONE);
                        }
                    });
//                    bottomNav.animate().translationY(0);


                } else {
                    constraintLayout.animate().translationY(180 - constraintLayout.getHeight()).alpha(0.5f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            profileDetails.setVisibility(View.VISIBLE);
                        }
                    });

//                    bottomNav.animate().translationY(100-constraintLayout.getHeight());
                }

              /*  Animation animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
                constraintLayout.startAnimation(animSlideUp);*/
//                Need to change

                break;
            case R.id.iv_msg:
                if (constraintLayout.getVisibility() == View.GONE) {
                    constraintLayout.setVisibility(View.VISIBLE);
                } else {
                    constraintLayout.setVisibility(View.GONE);
                }

                break;
            case R.id.textProfile:
                startActivity(new Intent(this, EditPhotoActivity.class));
                break;

            case R.id.iv_edit_profile:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;

            case R.id.iv_trip:

                break;
        }
    }

}
