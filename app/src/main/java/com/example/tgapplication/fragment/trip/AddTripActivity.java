package com.example.tgapplication.fragment.trip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.BuildConfig;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.adapter.TripListAdapter;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTripActivity extends BaseActivity implements View.OnClickListener {

    FirebaseUser fuser;
    Button btn_add_trip;
    TextView tv_from_date1, tv_to_date1;
    EditText tv_to_date, tv_from_date;
    EditText et_note;
    Calendar mcalendar = Calendar.getInstance();
    int day, month, year;
    RecyclerView recyclerView;
    TripListAdapter mtripAdapter;
    Toolbar toolbar;
    ArrayList<TripData> trips = new ArrayList<>();
    @BindView(R.id.trip_relativelayout)
    CoordinatorLayout tripRelativelayout;
    String edit_id = "";
    LinearLayoutManager ll_manager;
    AppBarLayout appbar;
    @BindView(R.id.et_location)
    TextInputEditText et_location;
    @BindView(R.id.til_location)
    TextInputLayout til_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);
        Places.initialize(getApplicationContext(), BuildConfig.map_api_key);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        btn_add_trip = findViewById(R.id.btn_add_trip);
        tv_from_date = findViewById(R.id.tv_from_date1);
        tv_to_date = findViewById(R.id.tv_to_date1);
        et_note = findViewById(R.id.et_note);
//        toolbar = findViewById(R.id.toolbar);

        appbar = findViewById(R.id.appbar);

        tv_from_date.setOnClickListener(this);
        tv_to_date.setOnClickListener(this);

        btn_add_trip.setOnClickListener(this);
        til_location.setOnClickListener(this);
        et_location.setOnClickListener(this);

        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        month = mcalendar.get(Calendar.MONTH);
        year = mcalendar.get(Calendar.YEAR);

        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateTostr = simpleDateFormat.format(today);
        //tv_from_date.setText(dateTostr);
        //tv_to_date.setText(dateTostr);

        recyclerView = findViewById(R.id.recyclerview_trips);

        ll_manager = new LinearLayoutManager(AddTripActivity.this);
        recyclerView.setLayoutManager(ll_manager);

//        assert getSupportActionBar() != null; //null check
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        displayTripList(fuser.getUid());

        /*tv_from_date.setOnTouchListener((v, event) -> {
            v.onTouchEvent(event);   // handle the event first
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  // hide the soft keyboard
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

                DatePickerDialog fromdpDialog = new DatePickerDialog(AddTripActivity.this, fromlistener, year, month, day);
                fromdpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                fromdpDialog.show();
            }
            return true;
        });*/

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void displayTripList(String uid) {

        TripsInstance.orderByKey().equalTo(uid)
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

                                        trips.add(0, tripData);
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
                                        edit_id = tripDataList.get(position).getId();
                                        appbar.setExpanded(true);
                                    }

                                    @Override
                                    public void removeTrip(String uid, String id) {
                                        TripsInstance.child(uid).child(id).removeValue();
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
        String userId;
        if (!edit_id.equalsIgnoreCase("")) {
            userId = edit_id;
            snackBar(tripRelativelayout, "Trip edited Successfully..!");
            dismissProgressDialog();
        } else {
            userId = TripsInstance.child(fuser.getUid()).push().getKey();
            snackBar(tripRelativelayout, "Trip added Successfully..!");
            dismissProgressDialog();
        }
        TripData tripData = new TripData(userId, et_location.getText().toString(), et_note.getText().toString(),
                tv_from_date.getText().toString(), tv_to_date.getText().toString());

        TripsInstance.child(fuser.getUid()).child(userId).setValue(tripData);


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
        tv_to_date.setText("");
        tv_from_date.setText("");
        et_location.clearFocus();
        et_note.clearFocus();
        tv_to_date.clearFocus();
        tv_from_date.clearFocus();

        edit_id = "";
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

            case R.id.tv_from_date1:
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

            case R.id.tv_to_date1:
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

            case R.id.et_location: case R.id.til_location:
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                break;
        }
    }

    int AUTOCOMPLETE_REQUEST_CODE = 111;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Placeq: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                et_location.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.

            }
        }
    }
}
