package com.example.tgapplication;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.tgapplication.chat.Chat;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public abstract class BaseFragment extends Fragment {

    public List<String> visitArray = new ArrayList<>();
    public List<String> favArray = new ArrayList<>();
    public int fav_int;
    public List<User> myDetail = new ArrayList<>();
    public List<TripList> tripList = new ArrayList<>();
    public long now = System.currentTimeMillis();
    public List<Date> dates = new ArrayList<>();
    private Date closest;
    public List<PlanTrip> from_to_dates = new ArrayList<>();
    String str_city, str_lang, str_eyes, str_hairs, str_height, str_bodytype, str_look, str_from, str_to, str_visit;
    String tripNote = "";
    public List<TripList> myFavArray = new ArrayList<>();
    String theLastMessage;

    public DatabaseReference PicturesInstance = FirebaseDatabase.getInstance().getReference("Pictures");
    public DatabaseReference ChatlistInstance = FirebaseDatabase.getInstance().getReference("Chatlist");
    public DatabaseReference ChatsInstance = FirebaseDatabase.getInstance().getReference("Chats");
    public DatabaseReference FavoritesInstance = FirebaseDatabase.getInstance().getReference("Favorites");
    public DatabaseReference ProfileVisitorInstance = FirebaseDatabase.getInstance().getReference("ProfileVisitor");
    public DatabaseReference TokensInstance = FirebaseDatabase.getInstance().getReference("Tokens");
    public DatabaseReference TripsInstance = FirebaseDatabase.getInstance().getReference("Trips");
    public DatabaseReference UsersInstance = FirebaseDatabase.getInstance().getReference("Users");


    public List<TripList> findClosestDate(List<Date> dates, User user, int fav_id) {

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
//            int fav_id= getFav(favArray,user.getId());
            int visit_id=getVisit(visitArray,user.getId());
            if (from_to_dates.get(i).getDate_from().contains(dateOutput1)) {
//                String ageValue= getBirthday(user.getDob());
                String dateFromTo = from_to_dates.get(i).getDate_from() + " - " + from_to_dates.get(i).getDate_to();
                TripList tripListClass = new TripList(user.getId(), user.getUsername(), user.getImageURL(), user.getAge(), user.getGender(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getLook(), user.getVisit(), from_to_dates.get(i).getLocation(), tripNote, dateFromTo,fav_id,visit_id);
                tripList.add(tripListClass);
            }
        }

        return tripList;
    }

    private int getVisit(List<String> favArray, String id) {
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
    public void snackBar(View constrainlayout, String s){
        Snackbar snackbar = Snackbar.make(constrainlayout,s, Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Action Button", "onClick triggered");
//                new MainActivity().loadFragment()
            }
        });
        View snackbarLayout = snackbar.getView();
        TextView textView = snackbarLayout.findViewById(R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_android_green_24dp, 0, 0, 0);
        textView.setCompoundDrawablePadding(20);
        snackbar.show();
    }

    //Progress Bar
    public void showProgressDialog(){
        if (!getActivity().isFinishing()) {
            ProgressActivity.showDialog(getActivity());
        }
    }

    public void dismissProgressDialog(){
        if (!getActivity().isFinishing()) {
            ProgressActivity.dismissDialog();
        }
    }

    //check for last message
    public void checkForLastMsg(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ChatsInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    Log.i("Snap"," "+snapshot);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }

                switch (theLastMessage){
                    case  "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
