package com.example.tgapplication.fragment.trip;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.adapter.TripListAdapter;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.google.android.material.textfield.TextInputLayout;
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
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTripActivity extends BaseActivity implements View.OnClickListener {

    DatabaseReference reference;
    FirebaseUser fuser;
    Button btn_add_trip;
    TextView tv_from_date1, tv_to_date1;
    EditText tv_to_date,tv_from_date;
    EditText et_location, et_note;
    Calendar mcalendar = Calendar.getInstance();
    int day, month, year;
    RecyclerView recyclerView;
    TripListAdapter mtripAdapter;
    Toolbar toolbar;
    ArrayList<TripData> trips = new ArrayList<>();
    @BindView(R.id.trip_relativelayout)
    RelativeLayout tripRelativelayout;
    String edit_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        btn_add_trip = findViewById(R.id.btn_add_trip);
        et_location = findViewById(R.id.et_location);
        tv_from_date = findViewById(R.id.tv_from_date1);
        tv_to_date = findViewById(R.id.tv_to_date1);
        et_note = findViewById(R.id.et_note);
//        toolbar = findViewById(R.id.toolbar);


        tv_from_date.setOnClickListener(this);
        tv_to_date.setOnClickListener(this);

        btn_add_trip.setOnClickListener(this);

        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        month = mcalendar.get(Calendar.MONTH);
        year = mcalendar.get(Calendar.YEAR);

        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateTostr = simpleDateFormat.format(today);
        //tv_from_date.setText(dateTostr);
        //tv_to_date.setText(dateTostr);

        recyclerView = findViewById(R.id.recyclerview_trips);

        LinearLayoutManager ll_manager = new LinearLayoutManager(AddTripActivity.this);
        recyclerView.setLayoutManager(ll_manager);

//        assert getSupportActionBar() != null; //null check
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        displayTripList(fuser.getUid());

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void displayTripList(String uid) {

        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReference()
                .child("Trips");
        reference1.orderByKey().equalTo(uid)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                trips.clear();
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
                                mtripAdapter = new TripListAdapter(AddTripActivity.this, uid, trips, new TripListAdapter.TripListInterface() {
                                    @Override
                                    public void sendTripLiist(List<TripData> tripDataList, int position) {
                                        et_location.setText(tripDataList.get(position).getLocation());
                                        et_note.setText(tripDataList.get(position).getTrip_note());
                                        tv_from_date.setText(tripDataList.get(position).getFrom_date());
                                        tv_to_date.setText(tripDataList.get(position).getTo_date());
                                        edit_id=tripDataList.get(position).getId();
                                    }
                                });
                                recyclerView.setAdapter(mtripAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
    }

    private void Trips(String edit_id) {
        reference = FirebaseDatabase.getInstance().getReference("Trips").child(fuser.getUid());
        String userId;
        if(!edit_id.equalsIgnoreCase(""))
        {
            userId=edit_id;
            snackBar(tripRelativelayout, "Trip edited Successfully..!");
            dismissProgressDialog();
        }
        else {
            userId = reference.push().getKey();
            snackBar(tripRelativelayout, "Trip added Successfully..!");
            dismissProgressDialog();
        }
        TripData tripData = new TripData(userId, et_location.getText().toString(), et_note.getText().toString(),
                tv_from_date.getText().toString(), tv_to_date.getText().toString());

        reference.child(userId).setValue(tripData);


        displayTripList(fuser.getUid());
        clearText();
//        trips.add(tripData);
//        mtripAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(mtripAdapter);
//                    Intent intent = new Intent(AddTripActivity.this, TripActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();

    }

    private void clearText() {
        et_note.setText("");
        et_location.setText("");
        et_location.clearFocus();
        et_note.clearFocus();
        edit_id="";
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_trip:
                hideKeyboard();
                showProgressDialog();
                String et_locations = et_location.getText().toString().trim();
                String et_notes = et_note.getText().toString().trim();
                String tv_from_dates = tv_from_date.getText().toString().trim();

                if (TextUtils.isEmpty(et_locations) || TextUtils.isEmpty(et_notes) || TextUtils.isEmpty(tv_from_dates) || TextUtils.isEmpty(tv_from_dates)) {
                    snackBar(tripRelativelayout, "All fileds are required !");
                    dismissProgressDialog();
                } else {
                    Trips(edit_id);

                }
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
