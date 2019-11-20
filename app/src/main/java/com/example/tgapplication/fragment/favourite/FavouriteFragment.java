package com.example.tgapplication.fragment.favourite;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.module.Upload;
import com.example.tgapplication.fragment.account.profile.ui.ProfileActivity;
import com.example.tgapplication.fragment.favourite.adapter.FavouriteAdapter;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.tgapplication.Constants.FavoritesInstance;
import static com.example.tgapplication.Constants.PicturesInstance;
import static com.example.tgapplication.Constants.ProfileVisitorInstance;
import static com.example.tgapplication.Constants.TripsInstance;
import static com.example.tgapplication.Constants.UsersInstance;

public class FavouriteFragment extends BaseFragment {


    TextView txtNoData;
    ProgressBar progressBar;
    private RecyclerView myFavRV;
    private FirebaseUser fuser;
    View view;
    String pictureUrl;
    private List<UserImg> myFavArray = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        progressBar=view.findViewById(R.id.progressBar);
        txtNoData=view.findViewById(R.id.txtNoData);
        myFavRV = view.findViewById(R.id.myFavRV);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        myFavRV.setLayoutManager(mGridLayoutManager);

        showProgressDialog();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        favList(Objects.requireNonNull(fuser));


        return view;
    }


    public void favList(FirebaseUser fuser) {


        FavoritesInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myFavArray.clear();
                if (snapshot.getChildrenCount() > 0) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        String userKey = dataSnapshot.getKey();
                        fav = 1;
                        UsersInstance.child(Objects.requireNonNull(userKey)).addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);


                                        PicturesInstance.child(Objects.requireNonNull(user).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                pictureUrl = "";
                                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                                                    Upload mainPhoto = snapshot1.getValue(Upload.class);
                                                    if (Objects.requireNonNull(mainPhoto).type == 1)
                                                        pictureUrl = mainPhoto.getUrl();

                                                }


                                                myFavArray.add(new UserImg(user, pictureUrl, fav));


                                                FavouriteAdapter tripAdapter = new FavouriteAdapter(getActivity(), fuser.getUid(), myFavArray, new FavouriteAdapter.FavouriteInterface() {
                                                    @Override
                                                    public void sendFavourite(String id) {
                                                        getData(id);
                                                    }

                                                    @Override
                                                    public void setProfileVisit(String uid, String id) {

                                                        ProfileVisitorInstance.child(id)
                                                                .child(uid).child("id").setValue(uid);

                                                    }

                                                    @Override
                                                    public void setData(User tList, int position) {
                                                        if (tList.getAccount_type() == 1) {
                                                            Intent mIntent = new Intent(getActivity(), ProfileActivity.class);
                                                            mIntent.putExtra("MyUserObj", myFavArray.get(position));
                                                            startActivityForResult(mIntent, 1);
                                                        } else {
                                                            hiddenProfileDialog();
                                                        }
                                                    }

                                                });
                                                myFavRV.setAdapter(tripAdapter);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
//                                }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                    }
                    progressBar.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dismissProgressDialog();
    }


    int fav;

    private void getData(String id) {

        UsersInstance.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tripList.clear();
                        myDetail.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final User user = snapshot.getValue(User.class);


                            if (Objects.requireNonNull(user).getId().equalsIgnoreCase(id)) {
                                FavoritesInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.hasChild(user.getId())) {

                                            fav = 1;
                                        } else {
                                            fav = 0;
                                        }

                                        PicturesInstance.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                pictureUrl = "";
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    Upload upload = ds.getValue(Upload.class);
                                                    if (Objects.requireNonNull(upload).getType() == 1) {
                                                        pictureUrl = upload.getUrl();
                                                    }
                                                }

                                                TripsInstance.orderByKey().equalTo(user.getId())
                                                        .addValueEventListener(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                from_to_dates.clear();
                                                                dates.clear();

                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                    String city = "";
                                                                    String tripNote = "";
                                                                    String date = "";

                                                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                                                        TripData tripData = snapshot1.getValue(TripData.class);

                                                                        city += tripData.getLocation();
                                                                        tripNote += tripData.getTrip_note();
                                                                        date += tripData.getFrom_date() + " - " + tripData.getTo_date();

                                                                        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                                                        try {
                                                                            Date date1 = format.parse(tripData.getFrom_date());
                                                                            dates.add(date1);
                                                                            PlanTrip planTrip = new PlanTrip(tripData.getLocation(), tripData.getFrom_date(), tripData.getTo_date());
                                                                            from_to_dates.add(planTrip);

                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }


                                                                    tripList = findClosestDate(dates, new UserImg(user, pictureUrl, fav));

                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

    }


}
