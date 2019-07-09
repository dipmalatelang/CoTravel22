package com.example.tgapplication.fragment.trip;


import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.adapter.TripAdapter;
import com.example.tgapplication.fragment.trip.module.FavList;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import android.widget.Toast;


public class TripFragment extends Fragment {

    TextView tripFilter;
    RecyclerView recyclerview;
    private View view;
    SharedPreferences prefs;
    FirebaseUser fuser;
    String str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_look, str_from, str_to, str_visit;
    private TripAdapter tripAdapter;
    private Date closest;
    List<PlanTrip> from_to_dates = new ArrayList<>();
    final long now = System.currentTimeMillis();
    List<Date> dates = new ArrayList<>();
    List<User> myDetail;
    List<String> favArray = new ArrayList<>();
    List<String> visitArray = new ArrayList<>();
    List<TripList> tripList;
    String tripNote = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_trip, container, false);

        tripFilter=view.findViewById(R.id.trip_filter);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerview=view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(mGridLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

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

//        Toast.makeText(, "", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "" + str_city, Toast.LENGTH_SHORT).show();

//        tripList();
        if (str_city.equalsIgnoreCase("not_defined")) {
            tripList();
            tripFilter.setText("Filter");
        } else {
            tripFilter.setText("Clear Filter");
//            Toast.makeText(this, "Data: " + str_city + " " + str_lang + " " + str_look + " " + str_from + " " + str_to + " " + str_visit, Toast.LENGTH_SHORT).show();
            getDataToFilter();

        }

//        getFav();

        getAllFav();
        getAllVisit();

        return view;
    }

    private void tripList() {
        tripList = new ArrayList<>();
        myDetail = new ArrayList<>();

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
                            if (!user.getId().equalsIgnoreCase(fuser.getUid())) {
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
                                                    findClosestDate(dates, user);
                                                }
                                                tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), favArray, tripList);
                                                recyclerview.setAdapter(tripAdapter);
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

    private void getDataToFilter() {
        if (str_lang == "All") {
            str_lang = "Arabic,Danish,German,Belorussian,Dutch,Greek,Japanese,Portuguese,Italian,Polish,Spanish,Swedish,Bulgarian,English,Hebrew,Korean,Romanian,Thai,Catalan,Estonian,Hindi,Latvian,Russian,Turkish,Chinese,Filipino,Hungarian,Lithuanian,Serbian,Ukrainian,Croatian,Finnish,Icelandic,Norwegian,Slovak,Urdu,Czech,French,Indonesian,Persian,Slovenian,Vietnamese,Nepali,Armenian,Kurdish";
        }

        if (str_look == "All") {
            str_look = "Girls,Male";
        }
        filterTripList(str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_look, str_from, str_to, str_visit);
    }

    private void filterTripList(final String str_city, final String str_lang, final String str_eyes, final String str_hairs, final String str_height, final String str_bodytype, final String str_look,
                                final String str_from, final String str_to, final String str_visit) {
        tripList = new ArrayList<>();
        myDetail = new ArrayList<>();
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
                            if (!user.getId().equalsIgnoreCase(fuser.getUid())) {

                                // HERE WHAT CORRESPONDS TO JOIN
                                DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child("Trips");
                                reference1.orderByKey().equalTo(user.getId())
                                        .addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
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
//                                                                from_to_dates.add(tripData.getFrom_date()+" - "+tripData.getTo_date());
                                                                    Log.i("Dates", tripData.getFrom_date() + " " + date1);
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Log.i("Janu", str_city + " " + str_lang + " " + str_eyes + " " + str_hairs + " " + str_height + " " + str_bodytype + " " + str_look + " " + str_visit);
                                                                Log.i("Komu", city + " " + user.getLang() + " " + user.getEyes() + " " + user.getHair() + " " + user.getHeight() + " " + user.getBody_type() + " " + user.getLook()
                                                                        + user.getVisit());

                                                                if (city.toLowerCase().contains(str_city.toLowerCase()) && user.getEyes().contains(str_eyes) && user.getHair().contains(str_hairs) && user.getHeight().contains(str_height) && user.getBody_type().contains(str_bodytype)
                                                                    /* && user.getLook().contains(str_look)*/) {
                                                                    Log.i("FilterFromTo", "" + from_to_dates.size());
                                                                    getDataForDisplay(dates, user);
                                                                    List<String> lang_item = Arrays.asList(user.getLang().split("\\s*,\\s*"));
                                                                    Log.i("Getting Count", lang_item.size() + " ");
                                                                    int count = 0;
//                                                            for(int j=0;j<lang_item.size();j++)
//                                                            {
//                                                                Log.i("Got data"," "+lang_item.get(j)+" " +user.getLang());
//                                                                if(lang_item.get(j).contains(str_lang))
//                                                                {
//                                                                    count++;
//                                                                    Log.i("Got Count",lang_item.size()+" "+count);
//                                                                }
//                                                            }
//                                                            if(lang_item.size()==count)
//                                                            {
//                                                                getDataForDisplay(dateOutput1,user);
//                                                            }
                                                                }
                                                            }
                                                        }
                                                        tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), favArray, tripList);
                                                        recyclerview.setAdapter(tripAdapter);
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

    private void getAllFav() {

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("Favorites")
                .child(fuser.getUid());
//        Log.i("Fav",visitorRef.getKey());

        favRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favArray.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

//                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    FavList favData = snapshot.getValue(FavList.class);
                    if (favData != null) {
                        favArray.add(favData.getId());
                    }
//                            }
                }
                Log.i("Checking Size in Trip",""+dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void findClosestDate(List<Date> dates, User user) {

        closest = Collections.min(dates, new Comparator<Date>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public int compare(Date d1, Date d2) {
                long diff1 = Math.abs(d1.getTime() - now);
                long diff2 = Math.abs(d2.getTime() - now);
                return Long.compare(diff1, diff2);
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        String dateOutput = simpleDateFormat.format(closest);
        String dateOutput1 = simpleDateFormat1.format(closest);
        Log.i("closest Date", " " + closest + " " + dateOutput + " " + dateOutput1);

        for (int i = 0; i < from_to_dates.size(); i++) {
            Log.i("This data", from_to_dates.get(i).getDate_from() + " " + dateOutput1);
            if (from_to_dates.get(i).getDate_from().contains(dateOutput1)) {
//                String ageValue= getBirthday(user.getDob());
                String dateFromTo = from_to_dates.get(i).getDate_from() + " - " + from_to_dates.get(i).getDate_to();
                TripList tripListClass = new TripList(user.getId(), user.getUsername(), user.getImageURL(), user.getAge(), user.getGender(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getLook(), user.getVisit(), from_to_dates.get(i).getLocation(), tripNote, dateFromTo);
                tripList.add(tripListClass);
            }
        }
    }

    private void getDataForDisplay(List<Date> dates, User user) {

        closest = Collections.min(dates, new Comparator<Date>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public int compare(Date d1, Date d2) {
                long diff1 = Math.abs(d1.getTime() - now);
                long diff2 = Math.abs(d2.getTime() - now);
                return Long.compare(diff1, diff2);
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        String dateOutput = simpleDateFormat.format(closest);
        String dateOutput1 = simpleDateFormat1.format(closest);
        Log.i("closest Date", " " + closest + " " + dateOutput + " " + dateOutput1);

        for (int i = 0; i < from_to_dates.size(); i++) {
            if (from_to_dates.get(i).getDate_from().contains(dateOutput1)) {

                String dateFromTo = from_to_dates.get(i).getDate_from() + " - " + from_to_dates.get(i).getDate_to();
//                String ageValue= getBirthday(user.getDob());
                String ageValue = user.getAge();
                Log.i("Age Range", str_from + " <= " + ageValue + " <= " + str_to);
                if (Integer.parseInt(str_from) <= Integer.parseInt(ageValue) && Integer.parseInt(ageValue) <= Integer.parseInt(str_to)) {
                    TripList tripListClass = new TripList(user.getId(), user.getUsername(), user.getImageURL(), ageValue, user.getGender(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getLook(), user.getVisit(), from_to_dates.get(i).getLocation(), tripNote, dateFromTo);
                    tripList.add(tripListClass);
                }

            }
        }
    }
    private void getAllVisit() {

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
}
