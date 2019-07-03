package com.example.tgapplication.trips;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tgapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileVisitorActivity extends AppCompatActivity {

    List<TripList> favList=new ArrayList<>();
    TextView testIt;
    String strTest="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_visitor);

        testIt=findViewById(R.id.testIt);

        if (getIntent() != null) {
            favList.clear();
            if (getIntent().getSerializableExtra("myFav") == null) {
                favList = (List<TripList>) getIntent().getSerializableExtra("myVisit");
                Log.i("Visit",""+favList.size());
                for (int i = 0; i < favList.size(); i++) {
                    strTest += favList.get(i).getName();
                }
                testIt.setText(strTest);
            } else {
                favList = (List<TripList>) getIntent().getSerializableExtra("myFav");
                Log.i("Fav",""+favList.size());

                for (int i = 0; i < favList.size(); i++) {
                    strTest += favList.get(i).getName();
                }
                testIt.setText(strTest);
            }
        }
    }
}
