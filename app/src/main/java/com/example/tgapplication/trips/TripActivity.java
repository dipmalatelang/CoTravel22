package com.example.tgapplication.trips;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.R;
import com.example.tgapplication.login.LoginActivity;
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

public class TripActivity extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    List<TripData> mTripList;
    List<TripList> tripList;
    List<User> myDetail;
    FirebaseUser fuser;
    TripAdapter tripAdapter;
    private ArrayList<Object> mTripList1;
    String city="";
    String tripNote="";
    String date="";
    final long now = System.currentTimeMillis();
    List<Date> dates = new ArrayList<>();
    List<PlanTrip> from_to_dates = new ArrayList<>();
    List<String> favArray=new ArrayList<>();
    List<String> visitArray=new ArrayList<>();
    Date closest;
    String str_city,str_lang,str_eyes, str_hairs, str_height, str_bodytype, str_look, str_from, str_to, str_visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        mToolbar = findViewById(R.id.trip_toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setTitle(getResources().getString(R.string.trips));

        mRecyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(TripActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        mTripList = new ArrayList<>();
        mTripList1= new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("Filter_TripList", MODE_PRIVATE);

        str_city = prefs.getString("str_city", "not_defined");//"No name defined" is the default value.
        str_lang = prefs.getString("str_lang", "not_defined"); //0 is the default value.

        str_eyes = prefs.getString("str_eyes","not_defined");
        str_hairs  = prefs.getString("str_hairs","not_defined");
        str_height=prefs.getString("str_height","not_defined");
        str_bodytype=prefs.getString("str_bodytype","not_defined");

        str_look = prefs.getString("str_look","not_defined");
        str_from= prefs.getString("str_from","not_defined");
         str_to= prefs.getString("str_to","not_defined");
        str_visit= prefs.getString("str_visit","not_defined");

        Toast.makeText(this, ""+str_city, Toast.LENGTH_SHORT).show();

        tripList();
//        if(str_city.equalsIgnoreCase("not_defined"))
//        {
//            tripList();
//        }
//        else{
//            Toast.makeText(this, "Data: "+str_city+" "+str_lang+" "+str_look+" "+str_from+" "+str_to+" "+str_visit, Toast.LENGTH_SHORT).show();
//            getDataToFilter();
//        }

//        getFav();

        getAllFav();
        getAllVisit();

        assert getSupportActionBar() != null; //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onResume() {
        getAllFav();
        super.onResume();
    }

    private void getAllVisit() {
        visitArray.clear();
        final DatabaseReference visitRef = FirebaseDatabase.getInstance().getReference("ProfileVisitor")
                .child(fuser.getUid());
//        Log.i("Fav",visitorRef.getKey());

        visitRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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

    private void getAllFav() {
        favArray.clear();
        final DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("Favorites")
                .child(fuser.getUid());
//        Log.i("Fav",visitorRef.getKey());

        favRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

//                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    FavList favData = snapshot.getValue(FavList.class);
                    favArray.add(favData.getId());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getDataToFilter() {
        if(str_lang=="All")
        {
            str_lang="Arabic,Danish,German,Belorussian,Dutch,Greek,Japanese,Portuguese,Italian,Polish,Spanish,Swedish,Bulgarian,English,Hebrew,Korean,Romanian,Thai,Catalan,Estonian,Hindi,Latvian,Russian,Turkish,Chinese,Filipino,Hungarian,Lithuanian,Serbian,Ukrainian,Croatian,Finnish,Icelandic,Norwegian,Slovak,Urdu,Czech,French,Indonesian,Persian,Slovenian,Vietnamese,Nepali,Armenian,Kurdish";
        }

        if(str_look=="All")
        {
            str_look="Girls,Male";
        }
        filterTripList(str_city,str_lang,str_eyes,str_hairs,str_height,str_bodytype,str_look,str_from, str_to, str_visit);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void tripList()
    {
        tripList = new ArrayList<>();
        myDetail=new ArrayList<>();

        // any way you managed to go the node that has the 'grp_key'
        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");
        MembersRef.addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        tripList.clear();
                        myDetail.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {

                            final User user = snapshot.getValue(User.class);
                            if(!user.getId().equalsIgnoreCase(fuser.getUid())) {
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

                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                                        {
                                                            String city = "";
                                                            String tripNote = "";
                                                            String date = "";

                                                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                                                            {

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
                                                                }
                                                                catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                            Log.i("TripFromTo", "" + from_to_dates.size());
                                                            findClosestDate(dates, user);
                                                        }
                                                        tripAdapter = new TripAdapter(TripActivity.this, fuser.getUid(),favArray,tripList);
                                                        mRecyclerView.setAdapter(tripAdapter);
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                            }
                            else {
                                myDetail.add(user);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                }
        );
    }

    private void filterTripList(final String str_city, final String str_lang, final String str_eyes, final String str_hairs, final String str_height, final String str_bodytype, final String str_look,
                                final String str_from, final String str_to, final String str_visit)
    {
        tripList = new ArrayList<>();
        myDetail=new ArrayList<>();
        // any way you managed to go the node that has the 'grp_key'
        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");
        MembersRef.addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        tripList.clear();
                        myDetail.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final User user = snapshot.getValue(User.class);
                            if(!user.getId().equalsIgnoreCase(fuser.getUid())) {

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
                                                    tripAdapter = new TripAdapter(TripActivity.this, fuser.getUid(),favArray, tripList);
                                                    mRecyclerView.setAdapter(tripAdapter);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                        }
                        else {
                                myDetail.add(user);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                }
        );
    }

    private void findClosestDate(List<Date> dates,User user) {

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
        Log.i("closest Date"," "+closest+" "+dateOutput+" "+dateOutput1);

        for(int i=0;i<from_to_dates.size();i++) {
            Log.i("This data",from_to_dates.get(i).date_from+" "+dateOutput1);
            if (from_to_dates.get(i).date_from.contains(dateOutput1)) {
//                String ageValue= getBirthday(user.getDob());
                String dateFromTo= from_to_dates.get(i).getDate_from()+ " - "+from_to_dates.get(i).getDate_to();
                TripList tripListClass = new TripList(user.getId(), user.getUsername(), user.getImageURL(), user.getAge(), user.getGender(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getLook(), user.getVisit(), from_to_dates.get(i).getLocation(), tripNote,dateFromTo);
                tripList.add(tripListClass);
            }
        }
    }


    private void getDataForDisplay(List<Date> dates,User user) {

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
        Log.i("closest Date"," "+closest+" "+dateOutput+" "+dateOutput1);

        for(int i=0;i<from_to_dates.size();i++) {
            if (from_to_dates.get(i).date_from.contains(dateOutput1)) {

                String dateFromTo= from_to_dates.get(i).getDate_from()+ " - "+from_to_dates.get(i).getDate_to();
//                String ageValue= getBirthday(user.getDob());
                String ageValue=user.getAge();
                Log.i("Age Range",str_from+" <= "+ageValue+" <= "+str_to);
                if(Integer.parseInt(str_from)<=Integer.parseInt(ageValue) && Integer.parseInt(ageValue)<=Integer.parseInt(str_to))
                {
                    TripList tripListClass = new TripList(user.getId(), user.getUsername(), user.getImageURL(), ageValue, user.getGender(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getLook(), user.getVisit(), from_to_dates.get(i).getLocation(), tripNote,dateFromTo);
                    tripList.add(tripListClass);
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trip_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.trip_add) {
            Intent msgIntent= new Intent(this, LoginActivity.class);
            msgIntent.putExtra("nextActivity","AddTrips");
            startActivity(msgIntent);
//            startActivity(new Intent(this, AddTripActivity.class));
            return true;
        } else if (item.getItemId() == R.id.trip_filter) {
            startActivity(new Intent(this, FilterTripActivity.class));
            return true;
        }
        else if(item.getItemId() ==R.id.trip_edit)
        {
            Intent msgIntent=new Intent(this,LoginActivity.class);
            msgIntent.putExtra("nextActivity","profileEdit");
            startActivity(msgIntent);

            return true;
        }
        else if(item.getItemId() ==R.id.trip_info)
        {
            Intent mIntent = new Intent(this, DetailActivity.class);
            mIntent.putExtra("MyDataObj", (Serializable) myDetail);
            mIntent.putExtra("ListTrip", (Serializable) tripList);
            mIntent.putExtra("ListFav", (Serializable) favArray);
            mIntent.putExtra("ListVisit", (Serializable) visitArray);
            startActivity(mIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public String getBirthday(String dob){
//        Date today = new Date();
//        Date birth = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            birth = sdf.parse(dob);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        int age = today.getYear() - birth.getYear();
//
//        return String.valueOf(age);
//    }


    private class PlanTrip {
        private String location;
        private String date_from;
        private String date_to;

        public PlanTrip() {

        }

        public PlanTrip(String location, String date_from, String date_to) {
            this.location=location;
            this.date_from=date_from;
            this.date_to=date_to;

        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDate_from() {
            return date_from;
        }

        public void setDate_from(String date_from) {
            this.date_from = date_from;
        }

        public String getDate_to() {
            return date_to;
        }

        public void setDate_to(String date_to) {
            this.date_to = date_to;
        }
    }
}
