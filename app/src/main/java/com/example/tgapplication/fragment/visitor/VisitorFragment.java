package com.example.tgapplication.fragment.visitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.ProfileActivity;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.adapter.VisitorAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class VisitorFragment extends BaseFragment {


    private RecyclerView myVisitRV;
    private FirebaseUser fuser;
    View view;
    private List<User> myFavArray=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_visitor, container, false);

        myVisitRV = view.findViewById(R.id.myVisitRV);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getActivity());
        myVisitRV.setLayoutManager(nLayoutManager);

        showProgressDialog();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        revVisitList(fuser);
//
//        favList = (List<TripList>) getIntent().getSerializableExtra("myFav");
//        favArray = (List<String>) getIntent().getSerializableExtra("ListFav");
      /*  Log.i("myFavArray",""+tripList.size());
        for(int i=0;i<tripList.size();i++)
        {
            if(tripList.get(i).getVisit_id()==1)
            {
                myFavArray.add(tripList.get(i));
            }}

        VisitorAdapter tripAdapter = new VisitorAdapter(getActivity(), fuser.getUid(), myFavArray);
        myVisitRV.setAdapter(tripAdapter);*/

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

//    public void visitList(FirebaseUser fuser) {
//        // any way you managed to go the node that has the 'grp_key'
//        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Users");
//        MembersRef.addValueEventListener(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        myFavArray.clear();
//
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                            final User user = snapshot.getValue(User.class);
//                            if (!user.getId().equalsIgnoreCase(fuser.getUid())) {
////                                getFav(fuser.getUid(),user.getId());
//                                // HERE WHAT CORRESPONDS TO JOIN
//                              ProfileVisitorInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot snapshot) {
//
//                                        if (snapshot.hasChild(user.getId())) {
//                                            // run some code
//                                            myFavArray.add(user);
//                                        }
//
//                                        VisitorAdapter tripAdapter = new VisitorAdapter(getActivity(), fuser.getUid(), myFavArray, new VisitorAdapter.VisitorInterface() {
//                                            @Override
//                                            public void sendVisitor(String id) {
//                                                getData(id);
//                                            }
//                                        });
//                                        myVisitRV.setAdapter(tripAdapter);
//                                    }
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                }
//        );
//    }

    public void revVisitList(FirebaseUser fuser) {
        // any way you managed to go the node that has the 'grp_key'

        ProfileVisitorInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        myFavArray.clear();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                            String userKey = dataSnapshot.getKey();

                                            UsersInstance.child(userKey).addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        User user = dataSnapshot.getValue(User.class);
                                                                myFavArray.add(user);

                                                                VisitorAdapter tripAdapter = new VisitorAdapter(getActivity(), fuser.getUid(), myFavArray, new VisitorAdapter.VisitorInterface() {
                                                                    @Override
                                                                    public void setProfileVisit(String uid, String id) {

                                                                        ProfileVisitorInstance.child(id)
                                                                                    .child(uid).child("id").setValue(uid);


                                                                    }

                                                                    @Override
                                                                    public void setData(User mTrip, int position) {
                                                                        Intent mIntent = new Intent(getActivity(), ProfileActivity.class);
                                                                        mIntent.putExtra("MyUserObj", myFavArray.get(position));
                                                                        startActivityForResult(mIntent,1);
                                                                    }
                                                                });
                                                                myVisitRV.setAdapter(tripAdapter);

                                                            }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                        });
                                                    }
                                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
                                dismissProgressDialog();
    }

    int fav;

    private void getData(String id){
        // any way you managed to go the node that has the 'grp_key'

        UsersInstance.addValueEventListener(
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
                                FavoritesInstance
                                        .child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        if (snapshot.hasChild(user.getId())) {
                                            // run some code
                                            fav = 1;
                                        } else {
                                            fav = 0;
                                        }


                                        TripsInstance.orderByKey().equalTo(user.getId())
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

                                                        Intent mIntent = new Intent(getActivity(), ProfileActivity.class);
                                                        mIntent.putExtra("MyObj", tripList.get(0));
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

}
