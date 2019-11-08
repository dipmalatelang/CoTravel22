package com.example.tgapplication.fragment.account.profile.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.adapter.CustomAdapter;
import com.example.tgapplication.fragment.account.profile.adapter.PlanTripsAdapter;
import com.example.tgapplication.fragment.account.profile.module.Permit;
import com.example.tgapplication.fragment.account.profile.module.Upload;
import com.example.tgapplication.fragment.chat.MessageActivity;
import com.example.tgapplication.fragment.trip.EditProfileActivity;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;
import com.example.tgapplication.login.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.tgapplication.Constants.PhotoRequestInstance;
import static com.example.tgapplication.Constants.PicturesInstance;
import static com.example.tgapplication.Constants.TripsInstance;
import static com.example.tgapplication.Constants.UsersInstance;

public class ProfileActivity extends BaseActivity {

    CustomAdapter adapter;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.profile_details)
    ConstraintLayout profileDetails;
    @BindView(R.id.tvCount)
    Chip tvCount;
    @BindView(R.id.textProfile)
    Chip textProfile;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
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
    @BindView(R.id.card_trip)
    CardView cardTrip;
    @BindView(R.id.floatingActionButton2)
    FloatingActionButton floatingActionButton2;
    @BindView(R.id.iv_fav_user)
    ImageView ivFavUser;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvUser)
    TextView tvUser;
    @BindView(R.id.tv_looking_for_value)
    TextView tvLookingForValue;
    @BindView(R.id.tv_want_to_visit_value)
    TextView tvWantToVisitValue;
    private ArrayList<Upload> upload1 = new ArrayList<>();
    private ArrayList<Upload> upload2 = new ArrayList<>();
    private ArrayList<Upload> upload3 = new ArrayList<>();

    private ArrayList<Upload> uploads = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>();
    int account_type = 1;

    private FirebaseUser fuser;
    TripList tripL;
    UserImg userL;
    String profileId;

    @BindView(R.id.rv_trip_value)
    RecyclerView rvTripValue;

    ArrayList<TripData> planTripsList = new ArrayList<>();
    int privateValue = 0;

    //    @BindView(R.id.bottomNav)
//    ConstraintLayout bottomNav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        ButterKnife.bind(this);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        LinearLayoutManager ll_manager = new LinearLayoutManager(ProfileActivity.this);
        rvTripValue.setLayoutManager(ll_manager);

        if (getIntent().getSerializableExtra("MyObj") == null && getIntent().getSerializableExtra("MyUserObj") == null) {
            ivEditProfile.setVisibility(View.VISIBLE);
            textProfile.setVisibility(View.VISIBLE);
            ivMenu.setVisibility(View.VISIBLE);
            ivFavUser.setVisibility(View.GONE);
            floatingActionButton2.hide();
            profileId = fuser.getUid();
            SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);

            if (sharedPreferences.contains("Gender")) {
                String gender = (sharedPreferences.getString("Gender", ""));
                getAllImages(profileId, gender);
            }

            getAllTrips(profileId);
            getProfileData(profileId);

        } else if (getIntent().getSerializableExtra("MyObj") != null) {
            ivEditProfile.setVisibility(View.GONE);
//            textProfile.setVisibility(View.GONE);
            textProfile.setText("");
            textProfile.setChipIconResource(R.drawable.ic_action_eye);
            ivMenu.setVisibility(View.GONE);
            ivFavUser.setVisibility(View.VISIBLE);
            floatingActionButton2.show();
            tripL = (TripList) getIntent().getSerializableExtra("MyObj");
            profileId = Objects.requireNonNull(tripL).getId();
            getAllImages(profileId, tripL.getGender());
            getAllTrips(profileId);
            if (tripL.getFavid() == 1) {
                ivFavUser.setImageResource(R.drawable.ic_action_fav_remove);
            } else {
                ivFavUser.setImageResource(R.drawable.ic_action_fav_add);
            }


            Log.i(TAG, "onCreate: " + tripL.getName() + " " + tripL.getFavid());
            setDetails(tripL.getName(), tripL.getGender(), tripL.getAbout_me(), tripL.getAge(), tripL.getLooking_for(), tripL.getTravel_with(), tripL.getUserLocation(), tripL.getNationality(),
                    tripL.getLang(), tripL.getHeight(), tripL.getBody_type(), tripL.getEyes(), tripL.getHair(), tripL.getVisit(), tripL.getPlanLocation(), tripL.getFrom_to_date(), tripL.getImageUrl());
//            if(tripL==null)
//            setDetails(userList.get(i).getName(), userList.get(i).getGender(), userList.get(i).getAge(), userList.get(i).getLook(), userList.get(i).getLocation(), userList.get(i).getNationality(), userList.get(i).getLang(), userList.get(i).getHeight(), userList.get(i).getBody_type(), userList.get(i).getEyes(), userList.get(i).getHair(), userList.get(i).getVisit(), "", "", userList.get(i).getImageURL());


        } else if (getIntent().getSerializableExtra("MyUserObj") != null) {
            ivEditProfile.setVisibility(View.GONE);
//            textProfile.setVisibility(View.GONE);
            textProfile.setText("");
            textProfile.setChipIconResource(R.drawable.ic_action_eye);
            ivMenu.setVisibility(View.GONE);
            ivFavUser.setVisibility(View.VISIBLE);
            floatingActionButton2.show();
            userL = (UserImg) getIntent().getSerializableExtra("MyUserObj");
            profileId = Objects.requireNonNull(userL).getUser().getId();
            getAllImages(profileId, userL.getUser().getGender());
            getAllTrips(profileId);

           /* if (userL.getUser().getFavid() == 1) {
                ivFavUser.setImageResource(R.drawable.ic_action_fav_remove);
            } else {
                ivFavUser.setImageResource(R.drawable.ic_action_fav_add);
            }*/

            Log.i(TAG, "MyUserObj : " + userL.getUser().getName() + " ");
            setDetails(userL.getUser().getName(), userL.getUser().getGender(), userL.getUser().getAbout_me(), userL.getUser().getAge(), userL.getUser().getLooking_for(), userL.getUser().getTravel_with(), "", userL.getUser().getNationality(),
                    userL.getUser().getLang(), userL.getUser().getHeight(), userL.getUser().getBody_type(), userL.getUser().getEyes(), userL.getUser().getHair(), userL.getUser().getVisit(), "", "", "");

        }


    }


    private void setDetails(String name, String gender, String about_me, String age, ArrayList<String> looking_for, ArrayList<String> travel_with, String userLocation, String nationality, String lang, String height, String body_type, String eyes, String hair, String visit, String planLocation, String from_to_date, String imageUrl) {

        String str_travel_with = null;

        if (name != null && !name.equalsIgnoreCase("") || age != null && !age.equalsIgnoreCase("")) {
            tvUser.setText(name + " , " + age);
        }

   /*     if (gender != null && !gender.equalsIgnoreCase("")) {
            tvSex.setText(gender);
        }*/

        if (about_me != null && !about_me.equalsIgnoreCase("")) {
            tvAboutMeValue.setText(about_me);
        }


        if (travel_with != null) {
            for (int j = 0; j < travel_with.size(); j++) {
                if (str_travel_with != null) {
                    str_travel_with += ", " + travel_with.get(j);
                } else {
                    str_travel_with = travel_with.get(j);
                }

            }

           /* if (str_travel_with != null && !str_travel_with.equalsIgnoreCase("")) {
                tvLooking.setText(str_travel_with);
            }*/
        }

        if (height != null && !height.equalsIgnoreCase("")) {
            tvHeightValue.setText(height);
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
            tvCountry.setText(userLocation);
        }

        if (visit != null && !visit.equalsIgnoreCase("")) {
            tvWantToVisitValue.setText(visit);
        }

        String strlookingFor = "";
        if (looking_for != null) {
            if (looking_for.size() > 0) {
                for (int i = 0; i < looking_for.size(); i++) {
                    Log.i(TAG, "setDetails: " + looking_for.get(i));
                    strlookingFor += looking_for.get(i);
                }
                tvLookingForValue.setText(strlookingFor);
            }
        }


        /*
        if (nationality != null && !nationality.equalsIgnoreCase("")) {
            tvNatioanlity.setText(nationality);
        }

        if (lang != null && !lang.equalsIgnoreCase("")) {
            tvLanguage.setText(lang);
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


    public void getProfileData(String id) {
        // any way you managed to go the node that has the 'grp_key'

        UsersInstance.child(id).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userList.clear();
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        User user = dataSnapshot.getValue(User.class);
//                            if (user != null && user.getId().equalsIgnoreCase(fuser.getUid())) {
                        account_type = Objects.requireNonNull(user).getAccount_type();
                        setDetails(user.getName(), user.getGender(), user.getAbout_me(), user.getAge(), user.getLooking_for(), user.getTravel_with(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getVisit(), "", "", "default");

                 /*           userList.add(user);
//                            }
                        }
                        if (userList.size() > 0) {
                            for (int i = 0; i < userList.size(); i++)
                        }*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void getAllTrips(String id) {
        TripsInstance.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    TripData tripData = dataSnapshot1.getValue(TripData.class);
                    planTripsList.add(tripData);
                    Log.i(TAG, "onDataChange: Trips " + Objects.requireNonNull(dataSnapshot1.getValue(TripData.class)).getLocation());
                }

                if (planTripsList.size() <= 0)
                    cardTrip.setVisibility(View.GONE);

                PlanTripsAdapter planTripsAdapter = new PlanTripsAdapter(ProfileActivity.this, planTripsList);
                rvTripValue.setAdapter(planTripsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getAllImages(String uid, String gender) {

        PhotoRequestInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                upload1 = new ArrayList<>();
                upload2 = new ArrayList<>();
                upload3 = new ArrayList<>();
                uploads = new ArrayList<>();

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        Permit permit = ds.getValue(Permit.class);
                        Log.i(TAG, "Sarita : Sender " + permit.getSender() + " Status " + permit.getStatus());
                        Log.i(TAG, "Sarita : Receiver " + permit.getReceiver());
                        if (permit.getSender().equals(fuser.getUid()) && permit.getReceiver().equals(uid) && permit.getStatus() == 1) {
                            PicturesInstance.child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {


                                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                        Upload upload = postSnapshot.getValue(Upload.class);
                                        if (Objects.requireNonNull(upload).getType() == 1) {
                                            upload1.add(upload);
                                        } else if (upload.getType() == 2) {
                                            upload2.add(upload);
                                        } else if (upload.getType() == 3) {
                                            upload3.add(upload);
                                        }
                                    }

                                    Log.i(TAG, "onDataChange: Upload1 " + upload1.size());
                                    if (upload1.size() > 0) {
                                        uploads.addAll(upload1);
                                    }
                                    Log.i(TAG, "onDataChange: Upload2 " + upload2.size());
                                    if (upload2.size() > 0) {
                                        uploads.addAll(upload2);
                                    }
                                    Log.i(TAG, "onDataChange: Upload3 " + upload3.size());
                                    if (upload3.size() > 0) {
                                        uploads.addAll(upload3);
                                    }

                                    privateValue = 1;


                                    if (uploads.size() > 0) {
                                        adapter = new CustomAdapter(ProfileActivity.this, uid, uploads, gender);
                                        viewPager.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();

                                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                            @Override
                                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                                int i = position + 1;
                                                tvCount.setText(i + " / " + uploads.size());
                                            }

                                            @Override
                                            public void onPageSelected(int position) {

                                            }

                                            @Override
                                            public void onPageScrollStateChanged(int state) {

                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            PicturesInstance.child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    upload1 = new ArrayList<>();
                                    upload2 = new ArrayList<>();
                                    uploads = new ArrayList<>();

                                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                        Upload upload = postSnapshot.getValue(Upload.class);
                                        if (Objects.requireNonNull(upload).getType() == 1) {
                                            upload1.add(upload);
                                        } else if (upload.getType() == 2) {
                                            upload2.add(upload);
                                        }
                                    }

                                    Log.i(TAG, "onDataChange: Uploads" + upload1.size());
                                    if (upload1.size() > 0) {
                                        uploads.addAll(upload1);
                                    }
                                    Log.i(TAG, "onDataChange: Uploads" + upload2.size());
                                    if (upload2.size() > 0) {
                                        uploads.addAll(upload2);
                                    }

                                    privateValue = 0;

                                    Log.i(TAG, "onDataChange: Uploads" + uploads.size());
                                    if (uploads.size() > 0) {
                                        adapter = new CustomAdapter(ProfileActivity.this, uid, uploads, gender);
                                        viewPager.setAdapter(adapter);

                                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                            @Override
                                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                                int i = position + 1;
                                                tvCount.setText(i + " / " + uploads.size());
                                            }

                                            @Override
                                            public void onPageSelected(int position) {

                                            }

                                            @Override
                                            public void onPageScrollStateChanged(int state) {

                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        if (permit.getReceiver().equals(uid)) {
                            break;
                        }
                    }
                       /* else {
                            PicturesInstance.child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    upload1 = new ArrayList<>();
                                    upload2 = new ArrayList<>();
                                    uploads = new ArrayList<>();

                                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                        Upload upload = postSnapshot.getValue(Upload.class);
                                        if (Objects.requireNonNull(upload).getType() == 1) {
                                            upload1.add(upload);
                                        } else if (upload.getType() == 2) {
                                            upload2.add(upload);
                                        }
                                    }

                                    Log.i(TAG, "onDataChange: Uploads" + upload1.size());
                                    if (upload1.size() > 0) {
                                        uploads.addAll(upload1);
                                    }
                                    Log.i(TAG, "onDataChange: Uploads" + upload2.size());
                                    if (upload2.size() > 0) {
                                        uploads.addAll(upload2);
                                    }


                                    Log.i(TAG, "onDataChange: Uploads" + uploads.size());
                                    if (uploads.size() > 0) {
                                        adapter = new CustomAdapter(ProfileActivity.this, uid, uploads);
                                        viewPager.setAdapter(adapter);

                                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                            @Override
                                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                                int i = position + 1;
                                                tvCount.setText(i + " / " + uploads.size());
                                            }

                                            @Override
                                            public void onPageSelected(int position) {

                                            }

                                            @Override
                                            public void onPageScrollStateChanged(int state) {

                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }*/
//                    }
                } else {
                    PicturesInstance.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            upload1 = new ArrayList<>();
                            upload2 = new ArrayList<>();
                            uploads = new ArrayList<>();

                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Upload upload = postSnapshot.getValue(Upload.class);
                                if (Objects.requireNonNull(upload).getType() == 1) {
                                    upload1.add(upload);
                                } else if (upload.getType() == 2) {
                                    upload2.add(upload);
                                }
                            }

                            Log.i(TAG, "onDataChange: Uploads" + upload1.size());
                            if (upload1.size() > 0) {
                                uploads.addAll(upload1);
                            }
                            Log.i(TAG, "onDataChange: Uploads" + upload2.size());
                            if (upload2.size() > 0) {
                                uploads.addAll(upload2);
                            }

                            privateValue = 0;


                            Log.i(TAG, "onDataChange: Uploads" + uploads.size());
                            if (uploads.size() > 0) {
                                adapter = new CustomAdapter(ProfileActivity.this, uid, uploads, gender);
                                viewPager.setAdapter(adapter);

                                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                        int i = position + 1;
                                        tvCount.setText(i + " / " + uploads.size());
                                    }

                                    @Override
                                    public void onPageSelected(int position) {

                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

//

    }

  /*  public void getAllImages(String uid) {
        PicturesInstance.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                uploads = new ArrayList<>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (upload.getType() != 3) {
                        uploads.add(upload);
                    }
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
    }*/

    public void showMenu(View view) {
        PopupMenu popup = new PopupMenu(ProfileActivity.this, view);
        popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());

        MenuItem bedMenuItem = popup.getMenu().findItem(R.id.one);
        if (account_type == 1) {
            bedMenuItem.setTitle("Hide profile");
            account_type = 2;
        } else {
            bedMenuItem.setTitle("Unhide profile");
            account_type = 1;
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.one) {
                    if (account_type == 2) {
                        alertDialogHideProfile();
                    } else {
                        alertDialogHideProfile2();
                    }
//                    alertDialogHideProfile(account_type);
//                    Toast.makeText(ProfileActivity.this, "Add to fav", Toast.LENGTH_SHORT).show();
                    //  holder.ic_action_fav_remove.setVisibility(View.VISIBLE);
//                            listener.chatFavorite(user.getId());
                    return true;
                }
                if (id == R.id.two) {
                    alertDialogAccountRemove();
//                    Toast.makeText(ProfileActivity.this, "add to delete", Toast.LENGTH_LONG).show();
                    return true;
                }


//                        Toast.makeText(mContext,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();//showing popup menu
    }

    private void alertDialogAccountRemove() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to remove your account?");
        dialog.setTitle("Account removal");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        active_hide_delete_Profile(fuser.getUid(), 3);
                        fuser.delete();

                        Toast.makeText(ProfileActivity.this, "Account remove successfully", Toast.LENGTH_LONG).show();

                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        finish();
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    }
                });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProfileActivity.this, "cancel is clicked", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void alertDialogHideProfile() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to hide your profile? Users won't be able to find you through the site in any way."
        );
        dialog.setTitle("Profile visibility");
        dialog.setPositiveButton("Hide My Profile",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        active_hide_delete_Profile(fuser.getUid(), account_type);
                        Toast.makeText(ProfileActivity.this, "Hide My Profile is clicked", Toast.LENGTH_LONG).show();
                    }
                });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void alertDialogHideProfile2() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Your profile is hidden. Do you want to make it public again?"
        );
        dialog.setTitle("Profile visibility");
        dialog.setPositiveButton("Yes ,make it public...",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        active_hide_delete_Profile(fuser.getUid(), account_type);

                        Toast.makeText(ProfileActivity.this, "unhide My Profile is clicked", Toast.LENGTH_LONG).show();
                    }
                });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    /*private void alertDialogHideProfile(int account_type) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Are you sure you want to hide your profile? Users won't be able to find you through the site in any way."
        );
        dialog.setTitle("Profile visibility");
        dialog.setPositiveButton("Hide My Profile",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        active_hide_delete_Profile(fuser.getUid(), account_type);
                        Toast.makeText(ProfileActivity.this, "Hide My Profile is clicked", Toast.LENGTH_LONG).show();
                    }
                });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }*/

    private void alertDialogRequestPermission() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to request permission to see private photo?");
        dialog.setTitle("Request Permission");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        PhotoRequestInstance.push().setValue(new Permit(fuser.getUid(), tripL.getId(), 0));
                        alertDialogRP();
                    }
                });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProfileActivity.this, "cancel is clicked", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void alertDialogAlreadyRequest() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Already requested");
        dialog.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void alertDialogRP() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Request permission..!");
        dialog.setPositiveButton("Request sent",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                    }
                });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    @OnClick({R.id.textProfile, R.id.iv_edit_profile, R.id.floatingActionButton2, R.id.iv_fav_user, R.id.fab_backFromProfile, R.id.iv_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.textProfile:
                if (textProfile.getText().toString().equalsIgnoreCase("") && privateValue == 1) {
                    alertDialogAlreadyRequest();
                } else if (textProfile.getText().toString().equalsIgnoreCase("")) {
                    alertDialogRequestPermission();
                } else {
                    startActivity(new Intent(this, EditPhotoActivity.class));
                }
                break;

            case R.id.iv_edit_profile:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;

            case R.id.floatingActionButton2:

                Intent intent = new Intent(this, MessageActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userid", profileId);
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
                if (tripL != null) {
                    if (tripL.getFavid() == 1) {
                        removeFav(fuser.getUid(), tripL.getId());
                        tripL.setFavid(0);
                        ivFavUser.setImageResource(R.drawable.ic_action_fav_add);
                    } else {
                        setFav(fuser.getUid(), tripL.getId());
                        tripL.setFavid(1);
                        ivFavUser.setImageResource(R.drawable.ic_action_fav_remove);
                    }
                } else if (userL != null) {
                    if (userL.getFav() == 1) {
                        removeFav(fuser.getUid(), userL.getUser().getId());
                        userL.setFav(0);
                        ivFavUser.setImageResource(R.drawable.ic_action_fav_add);
                    } else {
                        setFav(fuser.getUid(), userL.getUser().getId());
                        userL.setFav(1);
                        ivFavUser.setImageResource(R.drawable.ic_action_fav_remove);
                    }
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

            case R.id.iv_menu:
                showMenu(view);
                break;


        }
    }
}