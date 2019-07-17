package com.example.tgapplication.fragment.trip;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.adapter.TripAdapter;
import com.example.tgapplication.fragment.trip.module.FavList;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TripFragment extends BaseFragment {

    TextView tripFilter;
    RecyclerView recyclerview;
    private View view;
    SharedPreferences prefs;
    private TripAdapter tripAdapter;
    String TAG="TripFragment";

    private FirebaseUser fuser;
    String str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_look, str_from, str_to, str_visit;
    FloatingActionButton floatingActionButton;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_trip, container, false);
        fuser=FirebaseAuth.getInstance().getCurrentUser();

//        getAllFav(fuser);



        tripFilter=view.findViewById(R.id.trip_filter);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerview=view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(mGridLayoutManager);

        setHasOptionsMenu(true);


        floatingActionButton=view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddTripActivity.class));
            }
        });

//        mTripList = new ArrayList<>();
//        mTripList1 = new ArrayList<>();

        prefs = getActivity().getSharedPreferences("Filter_TripList", 0);

        str_city = prefs.getString("str_city", "not_defined");//"No name defined" is the default value.
        str_lang = prefs.getString("str_lang", "not_defined"); //0 is the default value.

        str_eyes = prefs.getString("str_eyes", "not_defined");
        str_hairs = prefs.getString("str_hairs", "not_defined");
        str_height = prefs.getString("str_height", "not_defined");
        str_bodytype = prefs.getString("str_bodytype", "not_defined");

        str_look = prefs.getString("str_look", "not_defined");
        str_from = prefs.getString("str_from", "not_defined");
        str_to = prefs.getString("str_to", "not_defined");
        str_visit = prefs.getString("str_visit", "not_defined");


        getAllVisit(fuser);
//        tripList();
        if (str_city.equalsIgnoreCase("not_defined")) {

            tripList(fuser);
            tripFilter.setText("Filter");

        } else {


//            if (str_lang.equalsIgnoreCase("All")) {
//                str_lang = "Arabic, Armenian, Belorussian, Bulgarian, Catalan, Chinese, Croatian, Czech, Danish, Dutch, English, Estonian, Filipino, Finnish, French, German, Greek, Hebrew, Hindi, Hungarian, Icelandic, Indonesian, Italian, Japanese, Korean, Kurdish, Latvian, Lithuanian, Nepali, Norwegian, Persian, Polish, Portuguese, Romanian, Russian, Serbian, Slovak, Slovenian, Spanish, Swedish, Thai, Turkish, Ukrainian, Urdu, Vietnamese";
//            }
            if (str_look.equalsIgnoreCase("All")) {
                str_look = "Female,Male";
            }
            filterTripList(str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_look, str_from, str_to, str_visit);
            tripFilter.setText("Clear Filter");
        }

//        Log.i("Fav Array",""+favArray.size());
        Log.i("Fav Array",""+ tripList.size());


//        getFav();

        tripFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String city=prefs.getString("str_city", "not_defined");
                if (!city.equalsIgnoreCase("not_defined")) {
                    tripFilter.setText("Clear Filter");
                    editor = prefs.edit();
                    editor.clear();
                    editor.apply();
                    tripFilter.setText("Filter");
                }
                else {
                    tripFilter.setText("Filter");
                    startActivity(new Intent(getActivity(), FilterTripActivity.class));
                }
            }
        });

        return view;
    }

    private void filterTripList(final String str_city, final String str_lang, final String str_eyes, final String str_hairs, final String str_height, final String str_bodytype, final String str_look,
                                final String str_from, final String str_to, final String str_visit) {


        // any way you managed to go the node that has the 'grp_key'
        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                .getReference().child("Users");
        MembersRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tripList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final User user = snapshot.getValue(User.class);
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
//                                                                    Log.i("VishalD", "" + user.getUsername() + " , " + tripData.getLocation());

                                                                city += tripData.getLocation();
                                                                tripNote += tripData.getTrip_note();
                                                                date += tripData.getFrom_date() + " - " + tripData.getTo_date();

                                                                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                                                try {
                                                                    Date date1 = format.parse(tripData.getFrom_date());
                                                                    dates.add(date1);
                                                                    PlanTrip planTrip = new PlanTrip(tripData.getLocation(), tripData.getFrom_date(), tripData.getTo_date());
                                                                    from_to_dates.add(planTrip);
//                                                                        Log.i("Dates", tripData.getFrom_date() + " " + date1);
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
//                                                                Log.i("TripFromTo", "" + from_to_dates.size());
                                                            List<String> lang_item = Arrays.asList(user.getLang().split("\\s*,\\s*"));
                                                            Collections.sort(lang_item);
                                                            String lang="";
                                                            for(int i=0;i<lang_item.size();i++)
                                                            {

                                                                if(!lang.equalsIgnoreCase(""))
                                                                {
                                                                    lang+=", "+lang_item.get(i);
                                                                }
                                                                else {
                                                                    lang=lang_item.get(i);
                                                                }
                                                            }

                                                            Log.i("Tag", "onDataChange: 123" +user.getLook().size());
                                                            Collections.sort(user.getLook());
                                                            String look="";
                                                            for(int i=0;i<user.getLook().size();i++)
                                                            {
                                                                if(look.equalsIgnoreCase(""))
                                                                {
                                                                    look=user.getLook().get(i);
                                                                }
                                                                else {
                                                                    look+=","+user.getLook().get(i);
                                                                }
                                                            }
                                                            Log.i(TAG, "onDataChange: look "+look);

                                                            if(city.contains(str_city) && user.getEyes().contains(str_eyes) && user.getHair().contains(str_hairs) && user.getHeight().contains(str_height) && user.getBody_type().contains(str_bodytype) && (Integer.parseInt(str_from)<=Integer.parseInt(user.getAge())) && (Integer.parseInt(user.getAge())<=Integer.parseInt(str_to)) && !str_lang.equalsIgnoreCase("All") && lang.contains(str_lang) && !str_look.equalsIgnoreCase("All") && look.contains(str_look))
                                                            {
                                                                  tripList = findClosestDate(dates, user, fav);
                                                            }
                                                            else {
                                                                Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
//                                                tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), favArray, tripList);
//                                                recyclerview.setAdapter(tripAdapter);
                                                        tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), tripList, new TripAdapter.ProfileData() {
                                                            @Override
                                                            public void setData(TripList tList, int position) {
                                                                Intent mIntent = new Intent(getActivity(), DetailActivity.class);
                                                                mIntent.putExtra("MyObj", (Serializable) tripList.get(position));
                                                                startActivity(mIntent);
                                                            }
                                                        });
                                                        recyclerview.setAdapter(tripAdapter);
                                                        tripAdapter.notifyDataSetChanged();
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

    int fav;
    private void getFav(String uid,String id) {

        final DatabaseReference visitorRef = FirebaseDatabase.getInstance().getReference("Favorites")
                .child(uid);

        visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChild(id)) {
                    // run some code
                    fav=1;
                }
                else {
                    fav=0;
                }
                Log.i("GetFav",""+fav+" "+id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //    private int getFav(List<String> favArray, String id) {
//        for(int i=0;i<favArray.size();i++)
//        {
//            if(favArray.get(i).equalsIgnoreCase(id))
//            {
//                fav_int=1;
//                return fav_int;
//            }
//            else {
//                fav_int=0;
//            }
//        }
//        return fav_int;
//    }
    public void getAllFav(FirebaseUser fuser, String id) {

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("Favorites")
                .child(fuser.getUid());
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

    @Override
    public void onStart() {
        super.onStart();


    }

    public void getAllVisit(FirebaseUser fuser) {

        DatabaseReference visitRef = FirebaseDatabase.getInstance().getReference("ProfileVisitor")
                .child(fuser.getUid());
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

    public void tripList(FirebaseUser fuser) {
        // any way you managed to go the node that has the 'grp_key'
        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                .getReference().child("Users");
        MembersRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tripList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final User user = snapshot.getValue(User.class);
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
                                                            Log.i("Tag", "onDataChange: "+fav);
                                                            tripList = findClosestDate(dates, user,fav);

                                                        }
//                                                tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), favArray, tripList);
//                                                recyclerview.setAdapter(tripAdapter);
                                                        tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), tripList, new TripAdapter.ProfileData() {
                                                            @Override
                                                            public void setData(TripList tList, int position) {
                                                                Intent mIntent = new Intent(getActivity(), DetailActivity.class);
                                                                mIntent.putExtra("MyObj", (Serializable) tripList.get(position));
                                                                startActivity(mIntent);
                                                            }
                                                        });
                                                        recyclerview.setAdapter(tripAdapter);
                                                        tripAdapter.notifyDataSetChanged();
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
