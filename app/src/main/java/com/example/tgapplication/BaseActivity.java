package com.example.tgapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.fragment.account.profile.module.Upload;
import com.example.tgapplication.fragment.chat.module.APIService;
import com.example.tgapplication.fragment.chat.module.Chat;
import com.example.tgapplication.fragment.chat.module.Client;
import com.example.tgapplication.fragment.chat.module.Data;
import com.example.tgapplication.fragment.chat.module.MyResponse;
import com.example.tgapplication.fragment.chat.module.Sender;
import com.example.tgapplication.fragment.chat.module.Token;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.tgapplication.Constants.ChatsInstance;
import static com.example.tgapplication.Constants.FavoritesInstance;
import static com.example.tgapplication.Constants.PicturesInstance;
import static com.example.tgapplication.Constants.ProfileVisitorInstance;
import static com.example.tgapplication.Constants.TokensInstance;
import static com.example.tgapplication.Constants.TrashInstance;
import static com.example.tgapplication.Constants.UsersInstance;

public abstract class BaseActivity extends AppCompatActivity {

    public String TAG = "Activity";
    public List<String> visitArray = new ArrayList<>();
    public List<TripList> tripList = new ArrayList<>();
    String tripNote = "";
    public int fav_int;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String theLastMessage, theLastMsgTime;
    Boolean textType;
    boolean notify = false;

    APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService .class);



    public List<TripList> findAllMembers(UserImg userImg) {
        User user=userImg.getUser();
        int visit_id = getVisit(visitArray, user.getId());
//        TripList tripListClass = new TripList(user.getId(), user.getUsername(), userImg.getPictureUrl(), user.getPhone(),user.getAge(), user.getGender(), user.getAbout_me(), user.getLocation(), user.getNationality(), user.getLang(), user.getHeight(), user.getBody_type(), user.getEyes(), user.getHair(), user.getLooking_for(),user.getTravel_with(), user.getVisit(), tripNote, user.getAccount_type(), userImg.getFav(), visit_id);
        TripList tripListClass = new TripList(user, userImg, tripNote, visit_id);
        tripList.add(tripListClass);

        return tripList;
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

    public void appDetails(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getAppDetails(String key) {
        String name = "";
        SharedPreferences sharedPreferences = getSharedPreferences("AppDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains(key)) {
            name = sharedPreferences.getString(key, "");
        }
        return name;
    }




    public void snackBar(View constrainlayout, String s) {
        Snackbar snackbar = Snackbar.make(constrainlayout, s, Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        snackbar.show();
    }

    public void showProgressDialog() {
        if (!isFinishing()) {
            ProgressActivity.showDialog(this);
        }
    }
    public void saveDetailsLater(User user) {
        startActivity(new Intent(this, MainActivity.class));
        finish();

        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("Id", user.getId());
        editor.putString("Name",  user.getName());
        editor.putString("Age", user.getAge());
        editor.putString("Gender",user.getGender());
        editor.putString("Phone",user.getPhone());
        editor.putString("TravelWith",new Gson().toJson(user.getTravel_with()));
        editor.putString("AgeRange",new Gson().toJson(user.getRange_age()));

        editor.apply();


    }

    public void saveSettingInfo(String b, boolean isChecked)
    {
        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(b, isChecked);
        editor.apply();
    }

    public void saveDisplayName(String name)
    {
        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.apply();
    }
    public void saveLoginDetails(String email, String password) {
        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("Email", email);
        editor.putString("Password", password);

        editor.commit();
    }

    public void dismissProgressDialog() {
        if (!isFinishing()) {
            ProgressActivity.dismissDialog();
        }
    }

    public void removeVisit(String uid, String id) {
        ProfileVisitorInstance
                .child(uid).child(id).removeValue();
    }

    public void removeFav(String uid, String id) {

        FavoritesInstance.child(uid).child(id).removeValue();
    }

    public void removeTrash(String uid, String id)
    {
        TrashInstance.child(uid).child(id).removeValue();
    }

    public void setTrash(String uid, String id)
    {
        TrashInstance.child(uid).child(id).child("id").setValue(id);
    }

    public void saveDetailsLater(ArrayList<String> travel_with, ArrayList<String> range_age) {
        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("TravelWith",new Gson().toJson(travel_with));
        editor.putString("AgeRange",new Gson().toJson(range_age));
        editor.apply();
    }


    public void updateRegister(final ArrayList<String> travel_with, ArrayList<String> age) {

        UsersInstance.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("travel_with").setValue(travel_with);
        UsersInstance.child(mAuth.getCurrentUser().getUid()).child("range_age").setValue(age);

        saveDetailsLater(travel_with,age);

    }

    public void setProfile(String uid, String id, String name) {
        ProfileVisitorInstance.child(id).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("TAG", "onDataChange: "+dataSnapshot.getChildrenCount());
                if (!dataSnapshot.exists()) {
                    ProfileVisitorInstance.child(id).child(uid).child("id").setValue(uid);
                    notify = true;
                    if (notify) {
                        sendNotifiaction(uid, id, name , "has visited your profile");
                    }
                    notify=false;
//                    Toast.makeText(getActivity(), "First Visit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendNotifiaction(String uid, String userid, final String username, final String message) {
        Query query = TokensInstance.orderByKey().equalTo(userid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(uid, R.mipmap.ic_launcher, username + " " + message, "Notification",
                            userid);

                    Sender sender = new Sender(data, Objects.requireNonNull(token).getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (Objects.requireNonNull(response.body()).success != 1) {
//                                            snackBar(message_realtivelayout, "Failed!");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendMsgNotifiaction(String uid, String userid, String receiver, final String username, final String message) {
        Query query = TokensInstance.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(uid, R.mipmap.ic_launcher, username + ": " + message, "New Message",
                            userid);

                    Sender sender = new Sender(data, Objects.requireNonNull(token).getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (Objects.requireNonNull(response.body()).success != 1) {
//                                            snackBar(message_realtivelayout, "Failed!");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retrieveUserDetail(FirebaseUser fUser) {
        UsersInstance.child(fUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                PicturesInstance.child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Upload upload = ds.getValue(Upload.class);

                            if(Objects.requireNonNull(upload).getType()==1)
                                profilePhotoDetails(upload.getUrl());

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                if(user!=null)
                saveDetailsLater(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void profilePhotoDetails(String imageUrl) {

        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("ImageUrl", imageUrl);
        editor.apply();
    }

    public void active_hide_delete_Profile(String id, int account_type) {
        UsersInstance.child(id).child("account_type").setValue(account_type);
    }

    public void setFav(String uid, String id) {

        FavoritesInstance
                .child(uid)
                .child(id).child("id").setValue(id);
    }

    public void setPhoneNumber(String id, String mobile)
    {
        UsersInstance.child(id).child("phone").setValue(mobile);
    }

    public void setShowNumber(String id, boolean show_number)
    {
        UsersInstance.child(id).child("show_number").setValue(show_number);
    }

    public void checkForLastMsg(Context mContext, final String userid, TextView last_msg, TextView last_msg_time, ConstraintLayout rl_chat) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ChatsInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                theLastMessage = "default";
                theLastMsgTime="default";

                textType = true;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                            theLastMsgTime= chat.getMsg_time();
                        }
                    }

                    if (Objects.requireNonNull(chat).getSender().equals(userid) && !chat.isIsseen()) {

                        textType = chat.isIsseen();
                    }



                }


                if ("default".equals(theLastMessage)) {
                    last_msg.setText("No Message");
                    last_msg_time.setText("No Time");
                } else {
                    last_msg.setText(theLastMessage);
                    last_msg_time.setText(theLastMsgTime);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public boolean showOrHidePwd(MotionEvent event, EditText input_password) {
        final int DRAWABLE_RIGHT = 2;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (input_password.getRight() - input_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {


                if (!input_password.getTransformationMethod().toString().contains("Password")) {
                    input_password.setTransformationMethod(new PasswordTransformationMethod());
                    input_password.setSelection(input_password.getText().length());
                    input_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_white_24dp, 0, R.drawable.ic_action_eye_off, 0);

                } else {
                    input_password.setTransformationMethod(new HideReturnsTransformationMethod());
                    input_password.setSelection(input_password.getText().length());
                    input_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_white_24dp, 0, R.drawable.ic_action_eye, 0);
                }
                return true;
            }
        }
        return false;
    }




    public void hideKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(inputManager).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
