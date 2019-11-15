package com.example.tgapplication.fragment.trip;


import android.content.Context;
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
import com.example.tgapplication.fragment.account.profile.ui.ProfileActivity;
import com.example.tgapplication.fragment.trip.adapter.TripAdapter;
import com.example.tgapplication.fragment.trip.module.FavList;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;
import com.example.tgapplication.fragment.account.profile.module.Upload;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.tgapplication.Constants.FavoritesInstance;
import static com.example.tgapplication.Constants.PicturesInstance;
import static com.example.tgapplication.Constants.ProfileVisitorInstance;
import static com.example.tgapplication.Constants.TripsInstance;
import static com.example.tgapplication.Constants.UsersInstance;


public class TripFragment extends BaseFragment {

    TextView tripFilter;
    RecyclerView recyclerview;
    private View view;
    SharedPreferences prefs;
    private TripAdapter tripAdapter;
    String TAG = "TripFragment";
    int ageTo, ageFrom;

    private FirebaseUser fuser;
    String str_name;
    String str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_looking_for, str_visit, str_trip;
    int num_from, num_to;
    String travel_with_user;
    FloatingActionButton floatingActionButton;
    SharedPreferences.Editor editor;
    int fav;
    String pictureUrl="";
    SharedPreferences sharedPreferences;
    ArrayList<String> travel_with = new ArrayList<>();
    ArrayList<String> ageRange = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_trip, container, false);
        fuser = FirebaseAuth.getInstance().getCurrentUser();


        tripFilter = view.findViewById(R.id.trip_filter);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(mGridLayoutManager);

        setHasOptionsMenu(true);


        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddTripActivity.class));
            }
        });

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);

        Map<String, ?> sharedData = sharedPreferences.getAll();
        for (Map.Entry entry : sharedData.entrySet()) {
        }

        if (sharedPreferences.contains("TravelWith")) {
            travel_with = new Gson().fromJson((sharedPreferences.getString("TravelWith", "")), new TypeToken<ArrayList<String>>() {
            }.getType());


            if (travel_with.size() > 0) {
                if (travel_with.size() > 1) {
                    travel_with_user = "Female,Male";

                } else {

                    travel_with_user = travel_with.get(0);

                }
            }
        }


        if (sharedPreferences.contains("AgeRange")) {
            ageRange = new Gson().fromJson((sharedPreferences.getString("AgeRange", "")), new TypeToken<ArrayList<String>>() {
            }.getType());

            ageFrom = Integer.parseInt(ageRange.get(0));
            ageTo = Integer.parseInt(ageRange.get(1));
        }
/*

        if (str_city.equalsIgnoreCase("not_defined")) {

            tripList(fuser,travel_with_user, ageFrom, ageTo);
            tripFilter.setText("Filter");

        } else {
            tripFilter.setText("Clear Filter");

            getDataToFilter();
        }
*/

        prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("Filter_TripList", 0);

        if (prefs.contains("FilterFlag")) {
            int flag = prefs.getInt("FilterFlag", 0);
            if (flag > 0) {
                if (prefs.contains("str_name")) {
                    str_name = prefs.getString("str_name", "");
                }

                if(prefs.contains("str_trip"))
                {
                    str_trip = prefs.getString("str_trip", "");
                }
                if (prefs.contains("str_city")) {
                    str_city = prefs.getString("str_city", "");
                }

                if (prefs.contains("str_eyes")) {
                    str_eyes = prefs.getString("str_eyes", "");

                }

                if (prefs.contains("str_hairs")) {
                    str_hairs = prefs.getString("str_hairs", "");

                }

                if (prefs.contains("str_height")) {
                    str_height = prefs.getString("str_height", "");

                }

                if (prefs.contains("str_bodytype")) {
                    str_bodytype = prefs.getString("str_bodytype", "");

                }

                if (prefs.contains("str_lang")) {
                    str_lang = prefs.getString("str_lang", "");

                }

                if (prefs.contains("str_looking_for")) {
                    str_looking_for = prefs.getString("str_looking_for", "");
                }

                if (prefs.contains("str_from")) {
                    num_from = prefs.getInt("str_from", 18);

                }

                if (prefs.contains("str_to")) {
                    num_to = prefs.getInt("str_to", 99);
                }
                tripFilter.setText("Clear Filter");

                filterTripList(str_name, str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_looking_for,
                        num_from, num_to, str_trip);
            }
        } else {
            tripList(fuser, travel_with_user, ageFrom, ageTo);
            tripFilter.setText("Filter");
        }

        getAllVisit(fuser);


        tripFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (prefs.contains("FilterFlag")) {
                    int flag = prefs.getInt("FilterFlag", 0);
                    if (flag > 0) {
                        tripFilter.setText("Clear Filter");
                        editor = prefs.edit();
                        editor.clear();
                        editor.apply();
                        tripList(fuser, travel_with_user, ageFrom, ageTo);
                        tripFilter.setText("Filter");
                    }
                    }
                else {
                    tripFilter.setText("Filter");
                    startActivity(new Intent(getActivity(), FilterTripActivity.class));
                }
            }
        });

        return view;
    }

 /*   private void getDataToFilter() {

           if (str_lang.equals("All")) {
                        str_lang = "Arabic,Danish,German,Belorussian,Dutch,Greek,Japanese,Portuguese,Italian,Polish,Spanish,Swedish,Bulgarian,English,Hebrew,Korean,Romanian,Thai,Catalan,Estonian,Hindi,Latvian,Russian,Turkish,Chinese,Filipino,Hungarian,Lithuanian,Serbian,Ukrainian,Croatian,Finnish,Icelandic,Norwegian,Slovak,Urdu,Czech,French,Indonesian,Persian,Slovenian,Vietnamese,Nepali,Armenian,Kurdish";
                    }

        if (str_travel_with.equals("All")) {
            str_travel_with = "Female,Male";
        }
        filterTripList(str_name,str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_travel_with, str_from, str_to, str_visit);
    }*/

    private void filterTripList(String str_name, String str_city, String str_lang, String str_eyes, String str_hairs, String str_height, String str_bodytype, String str_looking_for,
                                int num_from, int num_to, String str_trip) {

        UsersInstance.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tripList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final User user = snapshot.getValue(User.class);
                            if (!Objects.requireNonNull(user).getId().equalsIgnoreCase(fuser.getUid())) {

                                FavoritesInstance
                                        .child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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

                                                UserImg userImg = new UserImg(user, pictureUrl, fav);
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

                                                                        city += Objects.requireNonNull(tripData).getLocation();
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

                                                                    List<String> lang_item = Arrays.asList(user.getLang().split("\\s*,\\s*"));


                                                                    Log.i(TAG, "onDataChange: Got Data finally");
                                                                    tripList = findClosestDate(dates, userImg);

                                                                }

                                                                if (str_name != null && !str_name.equalsIgnoreCase("")) {
                                                                    if (tripList.size() > 0) {
                                                                        for (int i = 0; i < tripList.size(); i++) {
                                                                            if (!tripList.get(i).getName().equalsIgnoreCase(str_name)) {
                                                                                tripList.remove(i);
                                                                            }
                                                                        }
                                                                    }
                                                                }


                                                                if (str_trip.equalsIgnoreCase("Who wants to visit")) {
                                                                    if (str_city != null && !str_city.equalsIgnoreCase("")) {
                                                                        if (tripList.size() > 0) {
                                                                            for (int i = 0; i < tripList.size(); i++) {
                                                                                Log.i(TAG, "onDataChange: " + tripList.get(i).getPlanLocation());
                                                                                if (!tripList.get(i).getPlanLocation().contains(str_city)) {
                                                                                    tripList.remove(i);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    if (str_city != null && !str_city.equalsIgnoreCase("")) {
                                                                        if (tripList.size() > 0) {
                                                                            for (int i = 0; i < tripList.size(); i++) {
                                                                                if (!tripList.get(i).getUserLocation().equalsIgnoreCase(str_city)) {
                                                                                    tripList.remove(i);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }


                                                                if (str_eyes != null && !str_eyes.equalsIgnoreCase("") && !str_eyes.equalsIgnoreCase("All")) {
                                                                    if (tripList.size() > 0) {
                                                                        for (int i = 0; i < tripList.size(); i++) {
                                                                            if (!tripList.get(i).getEyes().contains(str_eyes)) {
                                                                                tripList.remove(i);
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                if (str_hairs != null && !str_hairs.equalsIgnoreCase("") && !str_hairs.equalsIgnoreCase("All")) {
                                                                    if (tripList.size() > 0) {
                                                                        for (int i = 0; i < tripList.size(); i++) {
                                                                            if (!tripList.get(i).getHair().contains(str_hairs)) {
                                                                                tripList.remove(i);
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                if (str_height != null && !str_height.equalsIgnoreCase("") && !str_height.equalsIgnoreCase("All")) {
                                                                    if (tripList.size() > 0) {
                                                                        for (int i = 0; i < tripList.size(); i++) {
                                                                            if (!tripList.get(i).getHeight().contains(str_height)) {
                                                                                tripList.remove(i);
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                if (str_bodytype != null && !str_bodytype.equalsIgnoreCase("") && !str_bodytype.equalsIgnoreCase("All")) {
                                                                    if (tripList.size() > 0) {
                                                                        for (int i = 0; i < tripList.size(); i++) {
                                                                            if (!tripList.get(i).getBody_type().contains(str_bodytype)) {
                                                                                tripList.remove(i);
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                if (str_lang != null && !str_lang.equalsIgnoreCase("") && !str_lang.equalsIgnoreCase("All")) {
                                                                    if (tripList.size() > 0) {
                                                                        for (int i = 0; i < tripList.size(); i++) {
                                                                            if (!tripList.get(i).getLang().contains(str_lang)) {
                                                                                tripList.remove(i);
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                if (str_looking_for != null && !str_looking_for.equalsIgnoreCase("") && !str_looking_for.equalsIgnoreCase("All")) {
                                                                    if (tripList.size() > 0) {
                                                                        for (int i = 0; i < tripList.size(); i++) {
                                                                            if (tripList.get(i).getLooking_for() != null) {
                                                                                if (tripList.get(i).getLooking_for().size() > 1) {
                                                                                    Log.i(TAG, "onDataChange: " + tripList.get(i).getLooking_for().size());
                                                                                    for (int j = 0; j < tripList.get(i).getLooking_for().size(); j++) {
                                                                                        Log.i(TAG, "onDataChange: Data " + tripList.get(i).getLooking_for().get(j));
                                                                                        if (!tripList.get(i).getLooking_for().get(j).equalsIgnoreCase(str_looking_for)) {
                                                                                            tripList.remove(i);
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    if (!tripList.get(i).getLooking_for().get(0).equalsIgnoreCase(str_looking_for)) {
                                                                                        tripList.remove(i);
                                                                                    }
                                                                                }
                                                                            }

                                                                        }
                                                                    }
                                                                }

                                                                if (tripList.size() > 0) {
                                                                    for (int i = 0; i < tripList.size(); i++) {
                                                                        Log.i(TAG, "onDataChange: " + num_from + " " + Integer.parseInt(tripList.get(i).getAge()) + " " + num_to);

                                                                        if (num_from > Integer.parseInt(tripList.get(i).getAge()) || Integer.parseInt(tripList.get(i).getAge()) > num_to) {
                                                                            tripList.remove(i);
                                                                        }
                                                                    }
                                                                }


                                                                Log.i(TAG, "onDataChange: Final trip List" + tripList.size());
                                                                    tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), tripList, new TripAdapter.ProfileData() {
                                                                        @Override
                                                                        public void setData(TripList tList, int position) {
                                                                            if (tList.getAccount_type() == 1) {
                                                                                Intent mIntent = new Intent(getActivity(), ProfileActivity.class);
                                                                                mIntent.putExtra("MyObj", tripList.get(position));
                                                                                startActivity(mIntent);
                                                                            } else {
                                                                                hiddenProfileDialog();
                                                                            }

                                                                        }

                                                                        @Override
                                                                        public void setProfileVisit(String uid, String id) {

                                                                            ProfileVisitorInstance
                                                                                    .child(id)
                                                                                    .child(uid).child("id").setValue(uid);
                                                                        }
                                                                    });
                                                                    recyclerview.setAdapter(tripAdapter);
                                                                    tripAdapter.notifyDataSetChanged();


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


    private void getFav(String uid, String id) {

        FavoritesInstance
                .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(id)) {
                    fav = 1;
                } else {
                    fav = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void getAllFav(FirebaseUser fuser, String id) {

        FavoritesInstance
                .child(fuser.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    FavList favData = snapshot.getValue(FavList.class);
                    if (favData != null) {
                        favArray.add(favData.getId());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void getAllVisit(FirebaseUser fuser) {

        ProfileVisitorInstance
                .child(fuser.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                visitArray.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    FavList favData = snapshot.getValue(FavList.class);
                    visitArray.add(Objects.requireNonNull(favData).getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void tripList(FirebaseUser fuser, String travel_with_user, int ageFrom, int ageTo) {

        UsersInstance.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tripList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final User user = snapshot.getValue(User.class);
                            if (!Objects.requireNonNull(user).getId().equalsIgnoreCase(fuser.getUid())) {

                                if (travel_with_user.contains(user.getGender()) && Integer.parseInt(user.getAge()) >= ageFrom && Integer.parseInt(user.getAge()) <= ageTo) {
                                    FavoritesInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            fav=0;
                                            if (snapshot.hasChild(user.getId()))
                                                fav = 1;

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

                                                    UserImg userImg = new UserImg(user, pictureUrl, fav);

                                                    TripsInstance.orderByKey().equalTo(user.getId())
                                                            .addValueEventListener(new ValueEventListener() {

                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                    from_to_dates.clear();
                                                                    dates.clear();

                                                                    Log.i(TAG, "tripList: Came working"+dataSnapshot.getChildrenCount());

                                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                        String city = "";
                                                                        String tripNote = "";
                                                                        String date = "";

                                                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                                                            TripData tripData = snapshot1.getValue(TripData.class);

                                                                            if(tripData!=null)
                                                                            {
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
                                                                        }


                                                                        tripList = findClosestDate(dates, userImg);

                                                                    }

                                                                    Log.i(TAG, "tripList: Came here"+tripList.size());

                                                                        tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), tripList, new TripAdapter.ProfileData() {
                                                                            @Override
                                                                            public void setData(TripList tList, int position) {
                                                                                if (tList.getAccount_type() == 1) {
                                                                                    Intent mIntent = new Intent(getActivity(), ProfileActivity.class);
                                                                                    mIntent.putExtra("MyObj", tripList.get(position));
                                                                                    startActivity(mIntent);
                                                                                } else {
                                                                                    hiddenProfileDialog();
                                                                                }

                                                                            }

                                                                            @Override
                                                                            public void setProfileVisit(String uid, String id) {
                                                                                ProfileVisitorInstance.child(id)
                                                                                        .child(uid).child("id").setValue(uid);
                                                                            }
                                                                        });
                                                                        recyclerview.setAdapter(tripAdapter);
                                                                        tripAdapter.notifyDataSetChanged();


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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

}
