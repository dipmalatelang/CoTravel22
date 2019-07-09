package com.example.tgapplication.fragment.trip;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseMethod;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.adapter.TripAdapter;
import com.example.tgapplication.fragment.trip.module.FavList;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.login.LoginActivity;
import com.example.tgapplication.trips.DetailActivity;
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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import android.widget.Toast;


public class TripFragment extends Fragment {

    private final List<TripList> tripList;
    TextView tripFilter;
    RecyclerView recyclerview;
    private View view;
    SharedPreferences prefs;
    private TripAdapter tripAdapter;


    private FirebaseUser fuser;
    String str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_look, str_from, str_to, str_visit;
    BaseMethod baseMethod;

    public TripFragment(List<TripList> tripList) {
        this.tripList=tripList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_trip, container, false);
        tripFilter=view.findViewById(R.id.trip_filter);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerview=view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(mGridLayoutManager);

        setHasOptionsMenu(true);

        fuser=FirebaseAuth.getInstance().getCurrentUser();

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


            tripFilter.setText("Filter");

        } else {
            tripFilter.setText("Clear Filter");
//            Toast.makeText(this, "Data: " + str_city + " " + str_lang + " " + str_look + " " + str_from + " " + str_to + " " + str_visit, Toast.LENGTH_SHORT).show();
//            getDataToFilter();

        }

//        Log.i("Fav Array",""+favArray.size());
        Log.i("Fav Array",""+ tripList.size());
        tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), tripList);
        recyclerview.setAdapter(tripAdapter);
//        getFav();

        return view;
    }














}
