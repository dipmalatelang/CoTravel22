package com.example.tgapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tgapplication.fragment.account.AccountFragment;
import com.example.tgapplication.fragment.chat.ChatFragment;
import com.example.tgapplication.fragment.favourite.FavouriteFragment;
import com.example.tgapplication.fragment.trip.TripFragment;
import com.example.tgapplication.fragment.trip.module.FavList;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.VisitorFragment;
import com.example.tgapplication.login.LoginActivity;
import com.example.tgapplication.trips.AddTripActivity;
import com.example.tgapplication.trips.DetailActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        getAllFav();
        tripList();

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
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_account:
                fragment = new AccountFragment();
                break;
            case R.id.nav_chat:
                fragment = new ChatFragment();
                break;
            case R.id.nav_favorites:
                fragment = new FavouriteFragment();
                break;
            case R.id.nav_trip:
                fragment = new TripFragment(tripList);
                break;
            case R.id.nav_vistor:
                fragment = new VisitorFragment();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trip_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.trip_add) {
            Intent msgIntent = new Intent(this, AddTripActivity.class);
//            msgIntent.putExtra("nextActivity", "AddTrips");
            startActivity(msgIntent);
//            startActivity(new Intent(this, AddTripActivity.class));
            return true;
        }
//        else if (item.getItemId() == R.id.trip_filter) {
//            startActivity(new Intent(this, FilterTripActivity.class));
//            return true;
//        }
        else if (item.getItemId() == R.id.trip_edit) {
            Intent msgIntent = new Intent(this, LoginActivity.class);
            msgIntent.putExtra("nextActivity", "profileEdit");
            startActivity(msgIntent);

            return true;
        } else if (item.getItemId() == R.id.trip_info) {
            Intent mIntent = new Intent(this, DetailActivity.class);
//            mIntent.putExtra("MyDataObj", (Serializable) myDetail);
//            mIntent.putExtra("ListTrip", (Serializable) tripList);
//            mIntent.putExtra("ListFav", (Serializable) favArray);
//            mIntent.putExtra("ListVisit", (Serializable) visitArray);
            startActivity(mIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
