package com.example.tgapplication.trips;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileVisitorActivity extends AppCompatActivity {

    List<TripList> favList = new ArrayList<>();
    String strTest = "";
    @BindView(R.id.myProfileRV)
    RecyclerView myProfileRV;
    FirebaseUser fuser;
    private List<String> favArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_visitor);
        ButterKnife.bind(this);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(ProfileVisitorActivity.this, 2);
        myProfileRV.setLayoutManager(mGridLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if (getIntent() != null) {
            favList.clear();
            if (getIntent().getSerializableExtra("myFav") == null) {
                favList = (List<TripList>) getIntent().getSerializableExtra("myVisit");
                favArray = (List<String>) getIntent().getSerializableExtra("ListFav");
                Log.i("Visit", "" + favList.size());
                ProfileVisitorAdapter tripAdapter = new ProfileVisitorAdapter(this, fuser.getUid(), favArray, favList,"visit");
                myProfileRV.setAdapter(tripAdapter);

            } else {
                favList = (List<TripList>) getIntent().getSerializableExtra("myFav");
                Log.i("Fav", "" + favList.size());

                favArray = (List<String>) getIntent().getSerializableExtra("ListFav");

                ProfileVisitorAdapter tripAdapter = new ProfileVisitorAdapter(this, fuser.getUid(), favArray, favList,"fav");
                myProfileRV.setAdapter(tripAdapter);
            }
        }
    }
}
