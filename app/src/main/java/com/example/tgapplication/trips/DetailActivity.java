package com.example.tgapplication.trips;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.tgapplication.R;
import com.example.tgapplication.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    ImageView mTrip;
    TextView tvName, tvAge, mCity, tvSex, tvNatioanlity, tvLanguage, tvHeight, tvBodyType, tvEyes, tvHairs,
            tvLooking, tvWantToVisit, tvPlannedtrip, tvDate;
    ImageView iv_send_msg, iv_fav, iv_profile_edit, iv_profile_visitor, iv_my_fav;
    FirebaseUser fuser;
    LinearLayout detail_list, image_list;
    Button btn_details, btn_images;
    List<TripList> myFavArray = new ArrayList<>();
    List<TripList> myVisitArray = new ArrayList<>();


    //    Gallery simpleGallery;
//    CustomGalleryAdapter customGalleryAdapter;
    TripList tripL;
    List<User> userList;
    List<TripList> listTrip;
    List<String> favArray;
    List<String> visitArray;

    int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5,
            R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10};
    @BindView(R.id.labelCity)
    LinearLayout labelCity;
    @BindView(R.id.labelSex)
    LinearLayout labelSex;
    @BindView(R.id.labelNationality)
    LinearLayout labelNationality;
    @BindView(R.id.labelLanguage)
    LinearLayout labelLanguage;
    @BindView(R.id.labelHeight)
    LinearLayout labelHeight;
    @BindView(R.id.labelBodyType)
    LinearLayout labelBodyType;
    @BindView(R.id.labelEyes)
    LinearLayout labelEyes;
    @BindView(R.id.labelHairs)
    LinearLayout labelHairs;
    @BindView(R.id.labelLooking)
    LinearLayout labelLooking;
    @BindView(R.id.labelWantToVisit)
    LinearLayout labelWantToVisit;
    @BindView(R.id.labelPlannedtrip)
    LinearLayout labelPlannedtrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        SharedPreferences prefs = getSharedPreferences("Filter_TripList", MODE_PRIVATE);

        String str_city = prefs.getString("str_city", "not_defined");//"No name defined" is the default value.
        String str_lang = prefs.getString("str_lang", "not_defined"); //0 is the default value.

        String str_look = prefs.getString("str_look", "not_defined");
        String str_from = prefs.getString("str_from", "not_defined");
        String str_to = prefs.getString("str_to", "not_defined");
        String str_visit = prefs.getString("str_visit", "not_defined");

        Toast.makeText(this, "" + str_city, Toast.LENGTH_SHORT).show();

//        if(str_city.equalsIgnoreCase("not_defined"))
//        {
//            tripList();
//        }
//        else{
//            Toast.makeText(this, "Data: "+str_city+" "+str_lang+" "+str_look+" "+str_from+" "+str_to+" "+str_visit, Toast.LENGTH_SHORT).show();
//            filterTripList(str_city);
//        }
        mToolbar = findViewById(R.id.toolbar);
//        simpleGallery = (Gallery) findViewById(R.id.simpleGallery);
        mTrip = findViewById(R.id.ivImage);
        tvName = findViewById(R.id.tvName);
        tvAge = findViewById(R.id.tvAge);
        mCity = findViewById(R.id.tvCity);
        detail_list = findViewById(R.id.detail_list);
        image_list = findViewById(R.id.image_list);

        btn_details = findViewById(R.id.btn_details);
        btn_images = findViewById(R.id.btn_images);

        iv_send_msg = findViewById(R.id.iv_send_msg);
        iv_profile_edit = findViewById(R.id.iv_profile_edit);
        iv_profile_visitor = findViewById(R.id.iv_profile_visitor);
        iv_my_fav = findViewById(R.id.iv_my_fav);
        iv_fav = findViewById(R.id.iv_fav);
        tvSex = findViewById(R.id.tvSex);
        tvNatioanlity = findViewById(R.id.tvNatioanlity);
        tvLanguage = findViewById(R.id.tvLanguage);
        tvHeight = findViewById(R.id.tvHeight);
        tvBodyType = findViewById(R.id.tvBodyType);
        tvEyes = findViewById(R.id.tvEyes);
        tvHairs = findViewById(R.id.tvHairs);
        tvLooking = findViewById(R.id.tvLooking);
        tvWantToVisit = findViewById(R.id.tvWantToVisit);
        tvPlannedtrip = findViewById(R.id.tvPlannedtrip);
        tvDate = findViewById(R.id.tvDate);


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        iv_send_msg.setOnClickListener(this);
        iv_fav.setOnClickListener(this);
        iv_profile_edit.setOnClickListener(this);
        iv_my_fav.setOnClickListener(this);
        iv_profile_visitor.setOnClickListener(this);

        btn_details.setOnClickListener(this);
        btn_images.setOnClickListener(this);


//        Log.i("Got Data back ",""+fav_int);
//        if(fav_int)
//        {
//            iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_remove));
//            iv_fav.setTag("ic_action_fav_remove");
//        }
//        else {
//            iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_add));
//            iv_fav.setTag("ic_action_fav_add");
//        }


        if (getIntent() != null) {
            if (getIntent().getSerializableExtra("MyObj") == null) {
                userList = (List<User>) getIntent().getSerializableExtra("MyDataObj");
                Log.i("My Name", userList.get(0).getName());
                listTrip = (List<TripList>) getIntent().getSerializableExtra("ListTrip");
                favArray = (List<String>) getIntent().getSerializableExtra("ListFav");
                visitArray = (List<String>) getIntent().getSerializableExtra("ListVisit");

                iv_send_msg.setVisibility(View.GONE);
                iv_fav.setVisibility(View.GONE);
                iv_profile_edit.setVisibility(View.VISIBLE);
                iv_my_fav.setVisibility(View.VISIBLE);
                iv_profile_visitor.setVisibility(View.VISIBLE);


                setDetails(userList.get(0).getName(), userList.get(0).getGender(), userList.get(0).getAge(), userList.get(0).getLook(), tripL.getUserLocation(), tripL.getNationality());
            } else {
                tripL = (TripList) getIntent().getSerializableExtra("MyObj");
                int faValue = getIntent().getIntExtra("FavId", 0);

                iv_send_msg.setVisibility(View.VISIBLE);
                iv_fav.setVisibility(View.VISIBLE);
                iv_profile_edit.setVisibility(View.GONE);
                iv_my_fav.setVisibility(View.GONE);
                iv_profile_visitor.setVisibility(View.GONE);

                Log.i("Fav Value", " " + faValue);
                if (faValue == 1) {
                    iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_remove));
                    iv_fav.setTag("ic_action_fav_remove");
                } else {
                    iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_add));
                    iv_fav.setTag("ic_action_fav_add");
                }
                setDetails(tripL.getName(), tripL.getGender(), tripL.getAge(), tripL.getLook(),tripL.getUserLocation(),tripL.getNationality());
//            if(tripL==null)
//            {
//                ArrayList<User> userL=getIntent().getSerializableExtra()
//            }

                Glide.with(DetailActivity.this).load(tripL.getImageUrl()).into(mTrip);


//            mDate.setText(tripL.getFrom_to_date());
            }
        }

    }

    private void setDetails(String name, String gender, String age, ArrayList<String> look, String userLocation, String nationality) {

        String str_look = null;
        mToolbar.setTitle(name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName.setText(name);

        tvSex.setText(gender);
        tvAge.setText(age + " years");

        for (int j = 0; j < look.size(); j++) {
            if (str_look != null) {
                str_look += ", " + look.get(j);
            } else {
                str_look = look.get(j);
            }

        }
        tvLooking.setText(str_look);

        mCity.setText(userLocation);
        tvNatioanlity.setText(nationality);
        tvLanguage.setText(tripL.getLang());
        tvHeight.setText(tripL.getHeight());
        tvBodyType.setText(tripL.getBody_type());
        tvEyes.setText(tripL.getEyes());
        tvHairs.setText(tripL.getHair());


        tvWantToVisit.setText(tripL.getVisit());
        tvPlannedtrip.setText(tripL.getPlanLocation());
        tvDate.setText(tripL.getFrom_to_date());

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

   /* private void tripList()
    {
        tripList = new ArrayList<>();

        // any way you managed to go the node that has the 'grp_key'
        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Addis_Ababa")
                .child("Users");
        MembersRef.addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        tripList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {

                            final User user = snapshot.getValue(User.class);

                            // HERE WHAT CORRESPONDS TO JOIN
                            DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("Addis_Ababa")
                                    .child("Trips");
                            reference1.orderByKey().equalTo(user.getId())
                                    .addValueEventListener(
                                            new ValueEventListener()
                                            {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot)
                                                {

                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                                    {
                                                        String city="";
                                                        String tripNote="";
                                                        String date="";

                                                        for(DataSnapshot snapshot1: snapshot.getChildren())
                                                        {
                                                            TripData tripData  = snapshot1.getValue(TripData.class);
                                                            Log.i("VishalD",""+user.getUsername()+" , "+tripData.getLocation());

                                                            city +=tripData.getLocation();
                                                            tripNote +=tripData.getTrip_note();
                                                            date += tripData.getFrom_date()+" - "+tripData.getTo_date();

                                                        }
                                                        TripList tripListClass= new TripList(user.getId(),user.getUsername(),user.getImageURL(),user.getDob(),user.getGender(),user.getLocation(),user.getNationality(),user.getLang(),user.getHeight(),user.getBody_type(),user.getEyes(),user.getHair(),user.getLook(),user.getVisit(),city,tripNote,date);
                                                        tripList.add(tripListClass);
                                                    }
                                                    customGalleryAdapter = new CustomGalleryAdapter(DetailActivity.this, tripList); // initialize the adapter
//                                                    tripAdapter = new TripAdapter(TripActivity.this, tripList);
//                                                    mRecyclerView.setAdapter(tripAdapter);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError)
                                                {

                                                }
                                            });
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                }
        );
    }*/


    /*  private void filterTripList(final String s)
      {
          tripList = new ArrayList<>();

          // any way you managed to go the node that has the 'grp_key'
          DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                  .getReference()
                  .child("Addis_Ababa")
                  .child("Users");
          MembersRef.addValueEventListener(
                  new ValueEventListener()
                  {
                      @Override
                      public void onDataChange(DataSnapshot dataSnapshot)
                      {
                          tripList.clear();
                          for (DataSnapshot snapshot : dataSnapshot.getChildren())
                          {

                              final User user = snapshot.getValue(User.class);

                              // HERE WHAT CORRESPONDS TO JOIN
                              DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                      .getReference()
                                      .child("Addis_Ababa")
                                      .child("Trips");
                              reference1.orderByKey().equalTo(user.getId())
                                      .addValueEventListener(
                                              new ValueEventListener()
                                              {
                                                  @Override
                                                  public void onDataChange(DataSnapshot dataSnapshot)
                                                  {

                                                      for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                                      {
                                                          String city="";
                                                          String tripNote="";
                                                          String date="";

                                                          for(DataSnapshot snapshot1: snapshot.getChildren())
                                                          {
                                                              TripData tripData  = snapshot1.getValue(TripData.class);
                                                              Log.i("VishalD",""+user.getUsername()+" , "+tripData.getLocation());

                                                              if(s.equalsIgnoreCase(tripData.getLocation()))
                                                              {
                                                                  city +=tripData.getLocation();
                                                                  tripNote +=tripData.getTrip_note();
                                                                  date += tripData.getFrom_date()+" - "+tripData.getTo_date();
                                                                  TripList tripListClass= new TripList(user.getId(),user.getUsername(),user.getImageURL(),user.getDob(),user.getGender(),user.getLocation(),user.getNationality(),user.getLang(),user.getHeight(),user.getBody_type(),user.getEyes(),user.getHair(),user.getLook(),user.getVisit(),city,tripNote,date);
                                                                  tripList.add(tripListClass);
                                                              }
                                                          }

                                                      }
  //                                                    tripAdapter = new TripAdapter(TripActivity.this, tripList);
  //                                                    mRecyclerView.setAdapter(tripAdapter);
                                                  }

                                                  @Override
                                                  public void onCancelled(DatabaseError databaseError)
                                                  {

                                                  }
                                              });
                          }
                      }


                      @Override
                      public void onCancelled(DatabaseError databaseError)
                      {

                      }
                  }
          );
      }*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_details:
                detail_list.setVisibility(View.VISIBLE);
                image_list.setVisibility(View.GONE);
                break;

            case R.id.btn_images:
                detail_list.setVisibility(View.GONE);
                image_list.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_send_msg:
//              startActivity(new Intent(this,ChatActivity.class));
                Intent chatIntent = new Intent(this, LoginActivity.class);
                chatIntent.putExtra("nextActivityUser", tripL.getId());
                chatIntent.putExtra("nextActivity", "TripsMsg");
                startActivity(chatIntent);
                break;

            case R.id.iv_profile_edit:
                Intent msgIntent = new Intent(this, ProfileActivity.class);
                msgIntent.putExtra("nextActivity", "profileEdit");
                startActivity(msgIntent);
                break;

            case R.id.iv_profile_visitor:
                getMyVisit();
                break;

            case R.id.iv_my_fav:
                getMyFav();
                break;

            case R.id.iv_fav:
                Log.i("GotTag", iv_fav.getTag().toString());
                if (iv_fav.getTag().toString().equalsIgnoreCase("ic_action_fav_add")) {
                    setFav(fuser.getUid(), tripL.getId());
                    iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_remove));
                    iv_fav.setTag("ic_action_fav_remove");
                } else if (iv_fav.getTag().toString().equalsIgnoreCase("ic_action_fav_remove")) {
                    removeFav(fuser.getUid(), tripL.getId());
                    iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_add));
                    iv_fav.setTag("ic_action_fav_add");
                }

                break;
        }
    }

    private void getMyVisit() {
        myVisitArray.clear();
        Log.i("VisitArray", "" + visitArray.size());
        for (int i = 0; i < listTrip.size(); i++) {
            for (int j = 0; j < visitArray.size(); j++) {
                Log.i("Compare", listTrip.get(i).getId() + " ==> " + visitArray.get(j));
                if (listTrip.get(i).getId().equalsIgnoreCase(visitArray.get(j))) {
                    myVisitArray.add(listTrip.get(i));
                    Log.i("Got In Here ", listTrip.get(i).getId());
                }
            }

        }
        Intent mIntent = new Intent(this, ProfileVisitorActivity.class);
        mIntent.putExtra("myVisit", (Serializable) myVisitArray);
        startActivity(mIntent);
    }

    private void getMyFav() {
        myFavArray.clear();
        for (int i = 0; i < listTrip.size(); i++) {
            for (int j = 0; j < favArray.size(); j++) {
                Log.i("Compare", listTrip.get(i).getId() + " ==> " + favArray.get(j));
                if (listTrip.get(i).getId().equalsIgnoreCase(favArray.get(j))) {
                    myFavArray.add(listTrip.get(i));
                    Log.i("Got In Here ", listTrip.get(i).getId());
                }
            }

        }
        Intent mIntent = new Intent(this, ProfileVisitorActivity.class);
        mIntent.putExtra("myFav", (Serializable) myFavArray);
        startActivity(mIntent);
    }


    private void setFav(String uid, String id) {

        final DatabaseReference visitorRef = FirebaseDatabase.getInstance().getReference("Favorites")
                .child(uid)
                .child(id);
        visitorRef.child("id").setValue(id);

    }


    private void removeFav(String uid, String id) {

        final DatabaseReference visitorRef = FirebaseDatabase.getInstance().getReference("Favorites")
                .child(uid);
        visitorRef.child(id).removeValue();

    }
}
