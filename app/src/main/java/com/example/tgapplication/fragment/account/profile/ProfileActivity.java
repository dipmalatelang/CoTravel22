package com.example.tgapplication.fragment.account.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.chat.MessageActivity;
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
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.iv_edit_profile)
    ImageView ivEditProfile;
    /*  @BindView(R.id.iv_info)
      ImageView ivInfo;
      @BindView(R.id.iv_msg)
      ImageView ivMsg;
      @BindView(R.id.iv_trip)
      ImageView ivTrip;*/
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
    @BindView(R.id.tv_trip)
    TextView tvTrip;
    @BindView(R.id.tv_trip_value)
    TextView tvTripValue;
    @BindView(R.id.card_trip)
    CardView cardTrip;
    @BindView(R.id.floatingActionButton2)
    FloatingActionButton floatingActionButton2;
    @BindView(R.id.iv_fav_user)
    ImageView ivFavUser;
    private ArrayList<Upload> uploads = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>();
    private FirebaseUser fuser;
    TripList tripL;
    User userL;
    //    @BindView(R.id.bottomNav)
//    ConstraintLayout bottomNav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        ButterKnife.bind(this);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if (getIntent().getSerializableExtra("MyObj") == null && getIntent().getSerializableExtra("MyUserObj") ==null) {
            textProfile.setVisibility(View.VISIBLE);
            ivFavUser.setVisibility(View.GONE);
            floatingActionButton2.hide();
            getAllImages(fuser.getUid());
            getAllTrips(fuser.getUid());
            getProfileData(fuser);
        } else if(getIntent().getSerializableExtra("MyObj") != null){
            textProfile.setVisibility(View.GONE);
            ivFavUser.setVisibility(View.VISIBLE);
            floatingActionButton2.show();
            tripL = (TripList) getIntent().getSerializableExtra("MyObj");
            getAllImages(tripL.getId());
            getAllTrips(tripL.getId());
            if (tripL.getFavid() == 1) {
                ivFavUser.setImageResource(R.drawable.ic_action_fav_remove);
            } else {
                ivFavUser.setImageResource(R.drawable.ic_action_fav_add);
            }

            Log.i(TAG, "onCreate: " + tripL.getName() + " " + tripL.getFavid());
            setDetails(tripL.getName(), tripL.getGender(), tripL.getAge(), tripL.getLook(), tripL.getUserLocation(), tripL.getNationality(),
                    tripL.getLang(), tripL.getHeight(), tripL.getBody_type(), tripL.getEyes(), tripL.getHair(), tripL.getVisit(), tripL.getPlanLocation(), tripL.getFrom_to_date(), tripL.getImageUrl());
//            if(tripL==null)
//            setDetails(userList.get(i).getName(), userList.get(i).getGender(), userList.get(i).getAge(), userList.get(i).getLook(), userList.get(i).getLocation(), userList.get(i).getNationality(), userList.get(i).getLang(), userList.get(i).getHeight(), userList.get(i).getBody_type(), userList.get(i).getEyes(), userList.get(i).getHair(), userList.get(i).getVisit(), "", "", userList.get(i).getImageURL());


        }
        else if(getIntent().getSerializableExtra("MyUserObj") !=null)
        {
            textProfile.setVisibility(View.GONE);
            ivFavUser.setVisibility(View.VISIBLE);
            floatingActionButton2.show();
            userL = (User) getIntent().getSerializableExtra("MyUserObj");
            getAllImages(userL.getId());
            getAllTrips(userL.getId());
         /*   if (userL.getFavid() == 1) {
                ivFavUser.setImageResource(R.drawable.ic_action_fav_remove);
            } else {
                ivFavUser.setImageResource(R.drawable.ic_action_fav_add);
            }*/

            Log.i(TAG, "MyUserObj : " + userL.getName() + " " );
            setDetails(userL.getName(), userL.getGender(), userL.getAge(), userL.getLook(), "", userL.getNationality(),
                    userL.getLang(), userL.getHeight(), userL.getBody_type(), userL.getEyes(), userL.getHair(), userL.getVisit(), "", "", "");

        }


    }



    private void setDetails(String name, String gender, String age, ArrayList<String> look, String userLocation, String nationality, String lang, String height, String body_type, String eyes, String hair, String visit, String planLocation, String from_to_date, String imageUrl) {

        String str_look = null;

        if (name != null && !name.equalsIgnoreCase("") || age != null && !age.equalsIgnoreCase("")) {
            textView2.setText(name + " , " + age);
        }

   /*     if (gender != null && !gender.equalsIgnoreCase("")) {
            tvSex.setText(gender);
        }*/

        if (look != null) {
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

        UsersInstance.addValueEventListener(
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
                                setDetails(userList.get(i).getName(), userList.get(i).getGender(), userList.get(i).getAge(), userList.get(i).getLook(), userList.get(i).getLocation(), userList.get(i).getNationality(), userList.get(i).getLang(), userList.get(i).getHeight(), userList.get(i).getBody_type(), userList.get(i).getEyes(), userList.get(i).getHair(), userList.get(i).getVisit(), "", "", "default");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void getAllTrips(String id) {
        TripsInstance.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    dataSnapshot1.getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllImages(String uid) {
        PicturesInstance.child(uid).addValueEventListener(new ValueEventListener() {
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

    @OnClick({R.id.textProfile, R.id.iv_edit_profile, R.id.floatingActionButton2, R.id.iv_fav_user, R.id.fab_backFromProfile})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.textProfile:
                startActivity(new Intent(this, EditPhotoActivity.class));
                break;

            case R.id.iv_edit_profile:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;

            case R.id.floatingActionButton2:
                Intent intent = new Intent(this, MessageActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userid", tripL.getId());
                startActivity(intent);

                break;

            case R.id.fab_backFromProfile:
                finish();
               /* Intent intent = new Intent();
                intent.putExtra("editTextValue", "value_here")
                setResult(RESULT_OK, intent);
                finish();*/
                break;

            case R.id.iv_fav_user:
                Log.i(TAG, "onViewClicked: " + fav_int);
                if (tripL.getFavid() == 1) {
                    removeFav(fuser.getUid(), tripL.getId());
                    tripL.setFavid(0);
                    ivFavUser.setImageResource(R.drawable.ic_action_fav_add);
                } else {
                    setFav(fuser.getUid(), tripL.getId());
                    tripL.setFavid(1);
                    ivFavUser.setImageResource(R.drawable.ic_action_fav_remove);
                }

               /* if (iv_fav.getTag().toString().equalsIgnoreCase("ic_action_fav_add")) {
                    setFav(fuser.getUid(), tripL.getId());
                    iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_remove));
                    iv_fav.setTag("ic_action_fav_remove");
                } else if (iv_fav.getTag().toString().equalsIgnoreCase("ic_action_fav_remove")) {
                    removeFav(fuser.getUid(), tripL.getId());
                    iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_add));
                    iv_fav.setTag("ic_action_fav_add");
                }*/
                break;


        }
    }
}
