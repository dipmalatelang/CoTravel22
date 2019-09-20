package com.example.tgapplication.fragment.account.profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.EditProfileActivity;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.photo.Upload;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    CustomAdapter adapter;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.profile_details)
    ConstraintLayout profileDetails;
    @BindView(R.id.textView)
    Chip textView;
    @BindView(R.id.textProfile)
    Chip textProfile;
    @BindView(R.id.imageView2)
    FloatingActionButton imageView2;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.iv_edit_profile)
    ImageView ivEditProfile;
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    @BindView(R.id.iv_trip)
    ImageView ivTrip;
    @BindView(R.id.tv_about_me)
    TextView tvAboutMe;
    @BindView(R.id.tv_about_me_value)
    TextView tvAboutMeValue;
    @BindView(R.id.tv_dream_place)
    TextView tvDreamPlace;
    @BindView(R.id.tv_tv_dream_place_value)
    TextView tvTvDreamPlaceValue;
    @BindView(R.id.card_summary)
    CardView cardSummary;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_height_value)
    TextView tvHeightValue;
    @BindView(R.id.tv_body_type)
    TextView tvBodyType;
    @BindView(R.id.tv_body_type_value)
    TextView tvBodyTypeValue;
    @BindView(R.id.tv_eye)
    TextView tvEye;
    @BindView(R.id.tv_eye_value)
    TextView tvEyeValue;
    @BindView(R.id.tv_hair)
    TextView tvHair;
    @BindView(R.id.tv_hair_value)
    TextView tvHairValue;
    @BindView(R.id.card_personal)
    CardView cardPersonal;
    private DatabaseReference mDatabase;
    private ArrayList<Upload> uploads = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>();
    private FirebaseUser fuser;
    TripList tripL;
    //    @BindView(R.id.bottomNav)
//    ConstraintLayout bottomNav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        ButterKnife.bind(this);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if (getIntent().getSerializableExtra("MyObj") == null)
        {
            getAllImages(fuser.getUid());

            getProfileData(fuser);
        }
        else{

            tripL = (TripList) getIntent().getSerializableExtra("MyObj");
            getAllImages(tripL.getId());
            setDetails(tripL.getName(), tripL.getGender(), tripL.getAge(), tripL.getLook(), tripL.getUserLocation(), tripL.getNationality(),
                    tripL.getLang(), tripL.getHeight(), tripL.getBody_type(), tripL.getEyes(), tripL.getHair(), tripL.getVisit(), tripL.getPlanLocation(), tripL.getFrom_to_date(), tripL.getImageUrl());
//            if(tripL==null)
//            setDetails(userList.get(i).getName(), userList.get(i).getGender(), userList.get(i).getAge(), userList.get(i).getLook(), userList.get(i).getLocation(), userList.get(i).getNationality(), userList.get(i).getLang(), userList.get(i).getHeight(), userList.get(i).getBody_type(), userList.get(i).getEyes(), userList.get(i).getHair(), userList.get(i).getVisit(), "", "", userList.get(i).getImageURL());


        }


    }

    private void setDetails(String name, String gender, String age, ArrayList<String> look, String userLocation, String nationality, String lang, String height, String body_type, String eyes, String hair, String visit, String planLocation, String from_to_date, String imageUrl) {

        String str_look = null;

        if (name != null && !name.equalsIgnoreCase("") && age != null && !age.equalsIgnoreCase("")) {
            textView2.setText(name+" , "+age);
        }

   /*     if (gender != null && !gender.equalsIgnoreCase("")) {
            tvSex.setText(gender);
        }*/

        if(look!=null)
        {
            for (int j = 0; j < look.size(); j++) {
                if (str_look != null) {
                    str_look += ", " + look.get(j);
                } else {
                    str_look = look.get(j);
                }

            }

           /* if (str_look != null && !str_look.equalsIgnoreCase("")) {
                tvLooking.setText(str_look);
            }*/
        }

        if (height != null && !height.equalsIgnoreCase("")) {
            tvHeightValue.setText(height);
            tvAboutMeValue.setText(height);
        }

        if (body_type != null && !body_type.equalsIgnoreCase("")) {
            tvBodyTypeValue.setText(body_type);
        }

        if (eyes != null && !eyes.equalsIgnoreCase("")) {
            tvEyeValue.setText(eyes);
        }

        if (hair != null && !hair.equalsIgnoreCase("")) {
            tvHairValue.setText(hair);
        }

        if (userLocation != null && !userLocation.equalsIgnoreCase("")) {
            tvTvDreamPlaceValue.setText(userLocation);
        }


        /*
        if (nationality != null && !nationality.equalsIgnoreCase("")) {
            tvNatioanlity.setText(nationality);
        }

        if (lang != null && !lang.equalsIgnoreCase("")) {
            tvLanguage.setText(lang);
        }

        if (visit != null && !visit.equalsIgnoreCase("")) {
            tvWantToVisit.setText(visit);
        }

        if (planLocation != null && !planLocation.equalsIgnoreCase("")) {
            tvPlannedtrip.setText(planLocation);
            tvDate.setText(from_to_date);
        }*/

    /*    if(getActivity()!=null)
        {
            Glide.with(getActivity()).load(imageUrl).placeholder(R.drawable.ic_services_ratings_user_pic).into(mTrip);
        }*/
    }


    public void getProfileData(FirebaseUser fuser) {
        // any way you managed to go the node that has the 'grp_key'
        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");
        MembersRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            User user = snapshot.getValue(User.class);
                            if (user != null && user.getId().equalsIgnoreCase(fuser.getUid())) {
                                userList.add(user);
                            }
                        }
                        if (userList.size() > 0) {
                            for (int i = 0; i < userList.size(); i++)
                                setDetails(userList.get(i).getName(), userList.get(i).getGender(), userList.get(i).getAge(), userList.get(i).getLook(), userList.get(i).getLocation(), userList.get(i).getNationality(), userList.get(i).getLang(), userList.get(i).getHeight(), userList.get(i).getBody_type(), userList.get(i).getEyes(), userList.get(i).getHair(), userList.get(i).getVisit(), "", "", userList.get(i).getImageURL());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    public void getAllImages(String uid) {
        mDatabase = FirebaseDatabase.getInstance().getReference("Pictures").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
              
                adapter = new CustomAdapter(ProfileActivity.this, uid, uploads);
                viewPager.setAdapter(adapter);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        int i = position + 1;
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
