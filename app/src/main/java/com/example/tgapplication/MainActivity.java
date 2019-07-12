package com.example.tgapplication;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tgapplication.fragment.account.AccountFragment;
import com.example.tgapplication.fragment.chat.ChatFragment;
import com.example.tgapplication.fragment.favourite.FavouriteFragment;
import com.example.tgapplication.fragment.trip.DetailActivity;
import com.example.tgapplication.fragment.trip.TripFragment;
import com.example.tgapplication.fragment.trip.module.FavList;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.VisitorFragment;
import com.example.tgapplication.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseMethod implements BottomNavigationView.OnNavigationItemSelectedListener {

    String fUserId;
    Fragment fragment;
    BottomNavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        getAllFav();
        getAllVisit();
        tripList();

        navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.nav_trip);
        navView.setOnNavigationItemSelectedListener(this);

        fragment=new TripFragment(tripList);
        loadFragment(fragment);
    }

    public void getAllFav() {

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("Favorites")
                .child(fUserId);
//        Log.i("Fav",visitorRef.getKey());

        favRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                favArray.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

//                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    FavList favData = snapshot.getValue(FavList.class);
                    if (favData != null) {
                        favArray.add(favData.getId());
                    }
//                            }
                }
                Log.i("Checking Size in Trip",""+favArray.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("Check Now",""+favArray.size());
    }

    public void getAllVisit() {

        DatabaseReference visitRef = FirebaseDatabase.getInstance().getReference("ProfileVisitor")
                .child(fUserId);
//        Log.i("Fav",visitorRef.getKey());

        visitRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                visitArray.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

//                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    FavList favData = snapshot.getValue(FavList.class);
                    visitArray.add(favData.getId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void tripList() {
        tripList.clear();
        myDetail.clear();


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
                            if (!user.getId().equalsIgnoreCase(fUserId)) {
                                // HERE WHAT CORRESPONDS TO JOIN
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
                                                    tripList= (List<TripList>) findClosestDate(dates, user);

                                                    }
//                                                tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), favArray, tripList);
//                                                recyclerview.setAdapter(tripAdapter);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                            } else {
                                myDetail.add(user);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_main_screen, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_account:
                fragment = new AccountFragment(myDetail);
                break;
            case R.id.nav_chat:
                fragment = new ChatFragment();
                break;
            case R.id.nav_favorites:
                fragment = new FavouriteFragment(tripList);
                break;
            case R.id.nav_trip:
                fragment = new TripFragment(tripList);
                break;
            case R.id.nav_vistor:
                fragment = new VisitorFragment(tripList);
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trip_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.home:
                navView.setSelectedItemId(R.id.nav_trip);
                fragment=new TripFragment(tripList);
                loadFragment(fragment);
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
