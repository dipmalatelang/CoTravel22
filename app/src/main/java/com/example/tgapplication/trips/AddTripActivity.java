package com.example.tgapplication.trips;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTripActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference reference;
    FirebaseUser fuser;
    Button btn_add_trip;
    TextView tv_from_date, tv_to_date;
    EditText et_location, et_note;
    Calendar mcalendar = Calendar.getInstance();
    int day, month, year;
    ArrayList tripList;
    RecyclerView recyclerView;
    TripListAdapter mtripAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        btn_add_trip = findViewById(R.id.btn_add_trip);
        et_location = findViewById(R.id.et_location);
        tv_from_date = findViewById(R.id.tv_from_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        et_note = findViewById(R.id.et_note);

        tv_from_date.setOnClickListener(this);
        tv_to_date.setOnClickListener(this);

        btn_add_trip.setOnClickListener(this);

        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        month = mcalendar.get(Calendar.MONTH);
        year = mcalendar.get(Calendar.YEAR);

        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateTostr = simpleDateFormat.format(today);
        tv_from_date.setText(dateTostr);
        tv_to_date.setText(dateTostr);

        recyclerView = findViewById(R.id.recyclerview_trips);

        LinearLayoutManager ll_manager = new LinearLayoutManager(AddTripActivity.this);
        recyclerView.setLayoutManager(ll_manager);

//        assert getSupportActionBar() != null; //null check
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        displayTripList();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void displayTripList() {
        tripList = new ArrayList<>();

        final ArrayList<TripData> trips = new ArrayList<>();

        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReference()
                .child("Trips");
        reference1.orderByKey().equalTo(fuser.getUid())
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
                                        //Log.i("VishalD",""+fuser.getUsername()+" , "+tripData.getLocation());

                                        trips.add(tripData);
                                    }
                                }
                                for (int j = 0; j < trips.size(); j++) {
                                    Log.i("List now", "" + trips.get(j).getLocation());
                                }
                                // add code here of adapter
                                mtripAdapter = new TripListAdapter(AddTripActivity.this, trips);
                                recyclerView.setAdapter(mtripAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
    }

    private void Trips() {
        reference = FirebaseDatabase.getInstance().getReference("Trips").child(fuser.getUid());

        String userId = reference.push().getKey();
        TripData tripData = new TripData(fuser.getUid(), et_location.getText().toString(), et_note.getText().toString(),
                tv_from_date.getText().toString(), tv_to_date.getText().toString());

        reference.child(userId).setValue(tripData);

        displayTripList();
//                    Intent intent = new Intent(AddTripActivity.this, TripActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_trip:
                Trips();
                break;

            case R.id.tv_from_date:
                DatePickerDialog.OnDateSetListener fromlistener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String sMonth, sDay;
                        month = monthOfYear + 1;
                        day = dayOfMonth;

                        if (month < 10) {
                            sMonth = "0" + month;
                        } else {
                            sMonth = String.valueOf(month);
                        }

                        if (day < 10) {
                            sDay = "0" + day;
                        } else {
                            sDay = String.valueOf(day);
                        }

                        tv_from_date.setText(new StringBuilder().append(sDay)
                                .append("/").append(sMonth).append("/").append(year)
                                .append(" "));

                    }
                };

                DatePickerDialog fromdpDialog = new DatePickerDialog(this, fromlistener, year, month, day);
                fromdpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                fromdpDialog.show();
                break;

            case R.id.tv_to_date:
                DatePickerDialog.OnDateSetListener tolistener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String sMonth, sDay;
                        month = monthOfYear + 1;
                        day = dayOfMonth;

                        if (month < 10) {
                            sMonth = "0" + month;
                        } else {
                            sMonth = String.valueOf(month);
                        }

                        if (day < 10) {
                            sDay = "0" + day;
                        } else {
                            sDay = String.valueOf(day);
                        }

                        tv_to_date.setText(new StringBuilder().append(sDay)
                                .append("/").append(sMonth).append("/").append(year)
                                .append(" "));

                    }
                };
                DatePickerDialog todpDialog = new DatePickerDialog(this, tolistener, year, month, day);
                todpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                todpDialog.show();
                break;
        }
    }
}
