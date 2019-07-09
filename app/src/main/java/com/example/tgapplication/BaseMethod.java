package com.example.tgapplication;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tgapplication.fragment.trip.module.FavList;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripData;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public abstract class BaseMethod extends AppCompatActivity {

    String fUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<String> visitArray = new ArrayList<>();
  public List<String> favArray = new ArrayList<>();
    public int fav_int;
    List<User> myDetail = new ArrayList<>();
   public List<TripList> tripList = new ArrayList<>();
    final long now = System.currentTimeMillis();
    List<Date> dates = new ArrayList<>();
    private Date closest;
    List<PlanTrip> from_to_dates = new ArrayList<>();
    String str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_look, str_from, str_to, str_visit;
    String tripNote = "";

    private List<String> getAllVisit() {

        DatabaseReference visitRef = FirebaseDatabase.getInstance().getReference("ProfileVisitor")
                .child(fUserId);
//        Log.i("Fav",visitorRef.getKey());

        visitRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                visitArray.clear();
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
        return visitArray;
    }

    public List<TripList> findClosestDate(List<Date> dates, User user) {

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
        Log.i("closest Date", " " + closest + " " + dateOutput + " " + dateOutput1);

        for (int i = 0; i < from_to_dates.size(); i++) {
            Log.i("This data", from_to_dates.get(i).getDate_from() + " " + dateOutput1);
            int fav_id= getFav(favArray,user.getId());
            if (from_to_dates.get(i).getDate_from().contains(dateOutput1)) {
//                String ageValue= getBirthday(user.getDob());
                String dateFromTo = from_to_dates.get(i).getDate_from() + " - " + from_to_dates.get(i).getDate_to();
                TripList tripListClass = new TripList(user.getId(), user.getUsername(), user.getImageURL(), user.getAge(), user.getGender(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getLook(), user.getVisit(), from_to_dates.get(i).getLocation(), tripNote, dateFromTo,fav_id);
                tripList.add(tripListClass);
            }
        }
        return tripList;
    }


    private int getFav(List<String> favArray, String id) {
        for(int i=0;i<favArray.size();i++)
        {
            if(favArray.get(i).equalsIgnoreCase(id))
            {
                fav_int=1;
                return fav_int;
            }
            else {
                fav_int=0;
            }
        }
        return fav_int;
    }

    private void filterTripList(final String str_city, final String str_lang, final String str_eyes, final String str_hairs, final String str_height, final String str_bodytype, final String str_look,
                                final String str_from, final String str_to, final String str_visit) {
        tripList = new ArrayList<>();
        myDetail = new ArrayList<>();
        // any way you managed to go the node that has the 'grp_key'
        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");
        MembersRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tripList.clear();
                        myDetail.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final User user = snapshot.getValue(User.class);
                            if (!user.getId().equalsIgnoreCase(fUserId)) {

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
//                                                        tripAdapter = new TripAdapter(getActivity(), fuser.getUid(), favArray, tripList);
//                                                        recyclerview.setAdapter(tripAdapter);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                            } else {
                                myDetail.add(user);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void getDataForDisplay(List<Date> dates, User user) {

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
        Log.i("closest Date", " " + closest + " " + dateOutput + " " + dateOutput1);

        for (int i = 0; i < from_to_dates.size(); i++) {
            if (from_to_dates.get(i).getDate_from().contains(dateOutput1)) {

                String dateFromTo = from_to_dates.get(i).getDate_from() + " - " + from_to_dates.get(i).getDate_to();
//                String ageValue= getBirthday(user.getDob());
                String ageValue = user.getAge();
                Log.i("Age Range", str_from + " <= " + ageValue + " <= " + str_to);
                int fav_id= getFav(favArray,user.getId());
                if (Integer.parseInt(str_from) <= Integer.parseInt(ageValue) && Integer.parseInt(ageValue) <= Integer.parseInt(str_to)) {
                    TripList tripListClass = new TripList(user.getId(), user.getUsername(), user.getImageURL(), ageValue, user.getGender(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getLook(), user.getVisit(), from_to_dates.get(i).getLocation(), tripNote, dateFromTo,fav_id);
                    tripList.add(tripListClass);
                }

            }
        }
    }

    private void getDataToFilter() {
        if (str_lang == "All") {
            str_lang = "Arabic,Danish,German,Belorussian,Dutch,Greek,Japanese,Portuguese,Italian,Polish,Spanish,Swedish,Bulgarian,English,Hebrew,Korean,Romanian,Thai,Catalan,Estonian,Hindi,Latvian,Russian,Turkish,Chinese,Filipino,Hungarian,Lithuanian,Serbian,Ukrainian,Croatian,Finnish,Icelandic,Norwegian,Slovak,Urdu,Czech,French,Indonesian,Persian,Slovenian,Vietnamese,Nepali,Armenian,Kurdish";
        }

        if (str_look == "All") {
            str_look = "Girls,Male";
        }
        filterTripList(str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_look, str_from, str_to, str_visit);
    }
}
