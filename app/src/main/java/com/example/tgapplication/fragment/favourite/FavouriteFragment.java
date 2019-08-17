package com.example.tgapplication.fragment.favourite;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.favourite.adapter.FavouriteAdapter;
import com.example.tgapplication.fragment.trip.DetailActivity;
import com.example.tgapplication.fragment.trip.adapter.TripAdapter;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends BaseFragment {


    //    private final List<TripList> tripList;
    private RecyclerView myFavRV;
    private FirebaseUser fuser;
    View view;
    private List<User> myFavArray=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        myFavRV = view.findViewById(R.id.myFavRV);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        myFavRV.setLayoutManager(mGridLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
//        favList(fuser);
//
//        favList = (List<TripList>) getIntent().getSerializableExtra("myFav");
//        favArray = (List<String>) getIntent().getSerializableExtra("ListFav");


     /*   Log.i("myFavArray",""+tripList.size());
        for(int i=0;i<tripList.size();i++)
        {
        if(tripList.get(i).getFavid()==1)
        {
            myFavArray.add(tripList.get(i));
        }}

        FavouriteAdapter tripAdapter = new FavouriteAdapter(getActivity(), fuser.getUid(), myFavArray);
        myFavRV.setAdapter(tripAdapter);*/

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        favList(fuser);
    }

    public void favList(FirebaseUser fuser) {
        // any way you managed to go the node that has the 'grp_key'
        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");
        MembersRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myFavArray.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                           User user = snapshot.getValue(User.class);
                            if (!user.getId().equalsIgnoreCase(fuser.getUid())) {
//                                getFav(fuser.getUid(),user.getId());
                                // HERE WHAT CORRESPONDS TO JOIN
                                DatabaseReference visitorRef = FirebaseDatabase.getInstance().getReference("Favorites")
                                        .child(fuser.getUid());

                                visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        if (snapshot.hasChild(user.getId())) {
                                            // run some code
                                            myFavArray.add(user);
                                        }
                                        FavouriteAdapter tripAdapter = new FavouriteAdapter(getActivity(), fuser.getUid(), myFavArray, new FavouriteAdapter.FavouriteInterface() {
                                            @Override
                                            public void sendFavourite(String id) {
                                                getData(id);
                                            }
                                        });
                                        myFavRV.setAdapter(tripAdapter);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    int fav;
    private void getData(String id){
            // any way you managed to go the node that has the 'grp_key'
            DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users");
            MembersRef.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            tripList.clear();
                            myDetail.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                final User user = snapshot.getValue(User.class);

//                                getFav(fuser.getUid(),user.getId());
                                // HERE WHAT CORRESPONDS TO JOIN
                                if (user.getId().equalsIgnoreCase(id)) {
                                    DatabaseReference visitorRef = FirebaseDatabase.getInstance().getReference("Favorites")
                                            .child(fuser.getUid());

                                    visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {

                                            if (snapshot.hasChild(user.getId())) {
                                                // run some code
                                                fav = 1;
                                            } else {
                                                fav = 0;
                                            }

                                            DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                    .getReference()
                                                    .child("Trips");
                                            reference1.orderByKey().equalTo(user.getId())
                                                    .addValueEventListener(new ValueEventListener() {

                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            from_to_dates.clear();
                                                            dates.clear();

                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                String city = "";
                                                                String tripNote = "";
                                                                String date = "";

                                                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                                                    TripData tripData = snapshot1.getValue(TripData.class);
                                                                    Log.i("VishalD", "" + user.getUsername() + " , " + tripData.getLocation());

                                                                    city += tripData.getLocation();
                                                                    tripNote += tripData.getTrip_note();
                                                                    date += tripData.getFrom_date() + " - " + tripData.getTo_date();

                                                                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                                                    try {
                                                                        Date date1 = format.parse(tripData.getFrom_date());
                                                                        dates.add(date1);
                                                                        PlanTrip planTrip = new PlanTrip(tripData.getLocation(), tripData.getFrom_date(), tripData.getTo_date());
                                                                        from_to_dates.add(planTrip);
                                                                        Log.i("Dates", tripData.getFrom_date() + " " + date1);
                                                                    } catch (ParseException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                                Log.i("TripFromTo", "" + from_to_dates.size());
                                                                Log.i("Tag", "onDataChange: " + fav);
                                                                tripList = findClosestDate(dates, user, fav);

                                                            }
//                                                tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), favArray, tripList);
//                                                recyclerview.setAdapter(tripAdapter);

                                                            Intent mIntent = new Intent(getActivity(), DetailActivity.class);
                                                            mIntent.putExtra("MyObj", (Serializable) tripList.get(0));
                                                            startActivity(mIntent);
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

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
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

    }
        //tList.getId()

}
