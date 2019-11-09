package com.example.tgapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.tgapplication.fragment.chat.module.Chat;
import com.example.tgapplication.fragment.trip.module.PlanTrip;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.tgapplication.Constants.ChatsInstance;
import static com.example.tgapplication.Constants.FavoritesInstance;
import static com.example.tgapplication.Constants.TrashInstance;

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
    String tripNote = "";
    String theLastMessage, theLastMsgTime, theLastMsgDate;
    Boolean textType;





    public List<TripList> findClosestDate(List<Date> dates, UserImg userImg) {

        User user = userImg.getUser();

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
        int visit_id = getVisit(visitArray, user.getId());
        Log.i("closest Date", " " + closest + " " + dateOutput + " " + dateOutput1);
        for (int i = 0; i < from_to_dates.size(); i++) {
            Log.i("This data", from_to_dates.get(i).getDate_from() + " " + dateOutput1);
//            int fav_id= getFav(favArray,user.getId());
//            Log.i("TAG", "onBindViewHolder: Visitor "+user.getId()+" - "+userImg.getPictureUrl());
            if (from_to_dates.get(i).getDate_from().contains(dateOutput1)) {
//                String ageValue= getBirthday(user.getDob());
                String dateFromTo = from_to_dates.get(i).getDate_from() + " - " + from_to_dates.get(i).getDate_to();

                TripList tripListClass = new TripList(user.getId(), user.getUsername(), userImg.getPictureUrl(), user.getAge(), user.getGender(), user.getAbout_me(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getLooking_for(), user.getTravel_with(), user.getVisit(), from_to_dates.get(i).getLocation(), tripNote, dateFromTo, user.getAccount_type(),userImg.getFav(), visit_id);
                tripList.add(tripListClass);
            }
        }

        if (tripList.size() < 1) {

            TripList tripListClass = new TripList(user.getId(), user.getUsername(), userImg.getPictureUrl(), user.getAge(), user.getGender(), user.getAbout_me(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getLooking_for(), user.getTravel_with(), user.getVisit(), "", tripNote, "", user.getAccount_type(), userImg.getFav(), visit_id);
            tripList.add(tripListClass);
        }

        return tripList;
    }

    public void hiddenProfileDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage("You can't view hidden profile.");
        dialog.setTitle("Profile is hidden");
        dialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getActivity(), "ok is clicked", Toast.LENGTH_LONG).show();
                    }
                });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void clearSharedPref() {
        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private int getVisit(List<String> favArray, String id) {
        for (int i = 0; i < favArray.size(); i++) {
            if (favArray.get(i).equalsIgnoreCase(id)) {
                fav_int = 1;
                return fav_int;
            } else {
                fav_int = 0;
            }
        }
        return fav_int;
    }

    public void setFav(String uid, String id) {

        FavoritesInstance
                .child(uid)
                .child(id).child("id").setValue(id);
    }


    public void setTrash(String uid, String id)
    {
        TrashInstance.child(uid).child(id).child("id").setValue(id);
    }

    public void removeTrash(String uid, String id)
    {
        TrashInstance.child(uid).child(id).removeValue();
    }

    public void removeFav(String uid, String id) {

        FavoritesInstance
                .child(uid).child(id).removeValue();

    }


    public void snackBar(View constrainlayout, String s) {
        Snackbar snackbar = Snackbar.make(constrainlayout, s, Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
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
    public void showProgressDialog() {
        if (!Objects.requireNonNull(getActivity()).isFinishing()) {
            ProgressActivity.showDialog(getActivity());
        }
    }

    public void dismissProgressDialog() {
        if (!Objects.requireNonNull(getActivity()).isFinishing()) {
            ProgressActivity.dismissDialog();
        }
    }

 /*   //show new message arrival
    public void highlightNewMessage(String userid)
    {
        ChatsInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Chat chat=ds.getValue(Chat.class);
//                    chat.isIsseen();
                    if(chat.getSender().equals(userid) && !chat.isIsseen())
                    {
                        Log.i("TAG", "onDataChange: HighLight "+userid+" "+chat.isIsseen());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("TAG", "onDataChange: HighLight Not "+userid+" "+databaseError.getMessage());
            }
        });
    }*/

    //check for last message
    public void checkForLastMsg(Context mContext, final String userid, TextView last_msg, TextView last_msg_time, ConstraintLayout rl_chat) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ChatsInstance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                theLastMessage = "default";
                theLastMsgTime="default";
                theLastMsgDate="default";

                textType = true;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    Log.i("Snap", " " + snapshot);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                            theLastMsgTime= chat.getMsg_time();
                            theLastMsgDate=chat.getMsg_date();
                        }
                    }

                    if (Objects.requireNonNull(chat).getSender().equals(userid) && !chat.isIsseen()) {
                        Log.i("TAG", "onDataChange: HighLight " + userid + " " + chat.isIsseen());
                        textType = chat.isIsseen();
                    }

                  /*  if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }*/

                }

      /*          if (!textType)
                {
//                    rl_chat.setBackgroundColor(Color.GRAY);
                    last_msg.setTextColor(mContext.getResources().getColor(R.color.black));

                }
                else
                {
//                    rl_chat.setBackgroundColor(Color.LTGRAY);
                    last_msg.setTextColor(mContext.getResources().getColor(R.color.gray));
                }*/

            /*    DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
                String str_date = dateFormat2.format(new Date()).toString();*/

                if ("default".equals(theLastMessage)) {
                    last_msg.setText("No Message");
                    last_msg_time.setText("No Time");
                } else {
                    last_msg.setText(theLastMessage);

                    last_msg_time.setText(theLastMsgTime);
                }

//                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

/*    public void appDetails(String key, String value) {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("AppDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getAppDetails(String key) {
        String name = "";
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("AppDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            name = sharedPreferences.getString(key, "");
        }
        return name;
    }*/

}
