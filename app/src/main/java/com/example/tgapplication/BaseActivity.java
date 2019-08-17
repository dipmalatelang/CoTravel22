package com.example.tgapplication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity {

    public String TAG="Activity";
    //Global Method and Variable
//    String fUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

/*
    public List<TripList> getMyFav() {
        for (int i = 0; i < tripList.size(); i++) {
            for (int j = 0; j < favArray.size(); j++) {
                Log.i("Compare", tripList.get(i).getId() + " ==> " + favArray.get(j));
                if (tripList.get(i).getId().equalsIgnoreCase(favArray.get(j))) {
                    myFavArray.add(tripList.get(i));
                    Log.i("Got In Here ", tripList.get(i).getId());
                }
            }

        }
//        Log.i("Checking Size",myFavArray.size()+" "+favArray.size());
//        Intent mIntent = new Intent(this, ProfileVisitorActivity.class);
//        mIntent.putExtra("myFav", (Serializable) myFavArray);
//        mIntent.putExtra("ListFav",(Serializable) favArray);
//        startActivity(mIntent);
        return myFavArray;
    }*/



   /* private void filterTripList(final String str_city, final String str_lang, final String str_eyes, final String str_hairs, final String str_height, final String str_bodytype, final String str_look,
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
                                                                    *//* && user.getLook().contains(str_look)*//*) {
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
    }*/


    public void snackBar(View constrainlayout, String s){
        Snackbar snackbar = Snackbar.make(constrainlayout,s, Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Action Button", "onClick triggered");
            }
        });
        View snackbarLayout = snackbar.getView();
        TextView textView = (TextView)snackbarLayout.findViewById(R.id.snackbar_text);
//        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_android_green_24dp, 0, 0, 0);
//        textView.setCompoundDrawablePadding(20);
        snackbar.show();
    }

    //Progress Bar
    public void showProgressDialog(){
        if (!isFinishing()) {
            ProgressActivity.showDialog(this);
        }
    }

    public void dismissProgressDialog(){
        if (!isFinishing()) {
            ProgressActivity.dismissDialog();
        }
    }


    public static class CheckNetwork {


        private  final String TAG = CheckNetwork.class.getSimpleName();



      public static boolean isInternetAvailable(Context context)
        {
            NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

            if (info == null)
            {
                Log.d("","no internet connection");
                return false;
            }
            else
            {
                if(info.isConnected())
                {
                    Log.d(""," internet connection available...");
                    return true;
                }
                else
                {
                    Log.d(""," internet connection");
                    return true;
                }

            }
        }
    }

/*    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }*/

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



/*    private void getDataToFilter() {
        if (str_lang == "All") {
            str_lang = "Arabic,Danish,German,Belorussian,Dutch,Greek,Japanese,Portuguese,Italian,Polish,Spanish,Swedish,Bulgarian,English,Hebrew,Korean,Romanian,Thai,Catalan,Estonian,Hindi,Latvian,Russian,Turkish,Chinese,Filipino,Hungarian,Lithuanian,Serbian,Ukrainian,Croatian,Finnish,Icelandic,Norwegian,Slovak,Urdu,Czech,French,Indonesian,Persian,Slovenian,Vietnamese,Nepali,Armenian,Kurdish";
        }

        if (str_look == "All") {
            str_look = "Girls,Male";
        }
        filterTripList(str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_look, str_from, str_to, str_visit);
    }*/
}
