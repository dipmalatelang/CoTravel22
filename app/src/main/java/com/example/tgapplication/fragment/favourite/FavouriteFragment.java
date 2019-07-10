package com.example.tgapplication.fragment.favourite;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.R;
import com.example.tgapplication.fragment.favourite.adapter.FavouriteAdapter;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {


    private final List<TripList> tripList;
    private RecyclerView myFavRV;
    private FirebaseUser fuser;
    View view;
    private List<TripList> myFavArray=new ArrayList<>();

    public FavouriteFragment(List<TripList> tripList) {
        // Required empty public constructor
        this.tripList=tripList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        myFavRV = view.findViewById(R.id.myFavRV);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        myFavRV.setLayoutManager(mGridLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

//
//        favList = (List<TripList>) getIntent().getSerializableExtra("myFav");
//        favArray = (List<String>) getIntent().getSerializableExtra("ListFav");
        Log.i("myFavArray",""+tripList.size());
        for(int i=0;i<tripList.size();i++)
        {
        if(tripList.get(i).getFavid()==1)
        {
            myFavArray.add(tripList.get(i));
        }}

        FavouriteAdapter tripAdapter = new FavouriteAdapter(getActivity(), fuser.getUid(), myFavArray);
        myFavRV.setAdapter(tripAdapter);

        return view;
    }


}
