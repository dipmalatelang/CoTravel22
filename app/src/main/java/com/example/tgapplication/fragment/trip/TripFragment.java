package com.example.tgapplication.fragment.trip;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseMethod;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.adapter.TripAdapter;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

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
    FloatingActionButton floatingActionButton;
    SharedPreferences.Editor editor;

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

        tripFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        String city=prefs.getString("str_city", "not_defined");
        if (!city.equalsIgnoreCase("not_defined")) {
            tripFilter.setText("Clear Filter");
            editor = prefs.edit();
            editor.clear();
            editor.apply();
//          tripList();
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














}
