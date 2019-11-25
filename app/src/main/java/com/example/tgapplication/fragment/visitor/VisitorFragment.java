package com.example.tgapplication.fragment.visitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.ui.AppSettings;
import com.example.tgapplication.fragment.account.profile.ui.ProfileActivity;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.adapter.VisitorAdapter;
import com.example.tgapplication.fragment.account.profile.module.Upload;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.tgapplication.Constants.FavoritesInstance;
import static com.example.tgapplication.Constants.PicturesInstance;
import static com.example.tgapplication.Constants.ProfileVisitorInstance;
import static com.example.tgapplication.Constants.UsersInstance;


public class VisitorFragment extends BaseFragment {


    private RecyclerView myVisitRV;
    private FirebaseUser fuser;
    View view;
    TextView txtNoData;
    ProgressBar progressBar;
    String pictureUrl;
    private List<UserImg> myFavArray=new ArrayList<>();
    SharedPreferences sharedPreferences;
    String fusername;
    AdView mAdmobView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_visitor, container, false);

        progressBar=view.findViewById(R.id.progressBar);
        txtNoData=view.findViewById(R.id.txtNoData);
        myVisitRV = view.findViewById(R.id.myVisitRV);
        mAdmobView=view.findViewById(R.id.home_admob);
        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getActivity());
        myVisitRV.setLayoutManager(nLayoutManager);

        showProgressDialog();
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        sharedPreferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("Name")) {
            fusername = (sharedPreferences.getString("Name", ""));
        }

        revVisitList(Objects.requireNonNull(fuser));

        initAdmob();
        return view;
    }
    protected void initAdmob() {
        MobileAds.initialize(getContext(), getString(R.string.app_id));

        if (AppSettings.ENABLE_ADMOB) {
            mAdmobView.setVisibility(View.VISIBLE);
            AdRequest.Builder builder = new AdRequest.Builder();
            AdRequest adRequest = builder.build();
            // Start loading the ad in the background.
            mAdmobView.loadAd(adRequest);
        } else {
            mAdmobView.setVisibility(View.GONE);
        }
    }




    public void revVisitList(FirebaseUser fuser) {


        ProfileVisitorInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                            myFavArray.clear();
                                                                                            if(snapshot.getChildrenCount()>0) {
                                                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                                                                                    String userKey = dataSnapshot.getKey();

                                                                                                    UsersInstance.child(Objects.requireNonNull(userKey)).addValueEventListener(
                                                                                                            new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                    User user = dataSnapshot.getValue(User.class);

//
                                                                                                                    FavoritesInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                        @Override
                                                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                                                            if (snapshot.hasChild(Objects.requireNonNull(user).getId())) {

                                                                                                                                fav = 1;
                                                                                                                            } else {
                                                                                                                                fav = 0;
                                                                                                                            }
                                                                                                                            PicturesInstance.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                @Override
                                                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                                                    pictureUrl = "";
                                                                                                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                                                                                                                                        Upload mainPhoto = snapshot1.getValue(Upload.class);
                                                                                                                                        if (Objects.requireNonNull(mainPhoto).type == 1)
                                                                                                                                            pictureUrl = mainPhoto.getUrl();

                                                                                                                                    }


                                                                                                                                    myFavArray.add(new UserImg(user, pictureUrl, fav));

                                                                                                                                    VisitorAdapter tripAdapter = new VisitorAdapter(getActivity(), fuser.getUid(), myFavArray, new VisitorAdapter.VisitorInterface() {
                                                                                                                                        @Override
                                                                                                                                        public void setProfileVisit(String uid, String id) {

                                                                                                                                            setProfile(uid,id,fusername);
                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public void setData(UserImg mTrip, int position) {
                                                                                                                                            if (mTrip.getUser().getAccount_type() == 1) {
                                                                                                                                                Intent mIntent = new Intent(getActivity(), ProfileActivity.class);
                                                                                                                                                mIntent.putExtra("MyUserObj", myFavArray.get(position));
                                                                                                                                                startActivityForResult(mIntent, 1);
                                                                                                                                            } else {
                                                                                                                                                hiddenProfileDialog();
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                    myVisitRV.setAdapter(tripAdapter);

                                                                                                                                }

                                                                                                                                @Override
                                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                                }

                                                                                                                            });

                                                                                                                        }

                                                                                                                        @Override
                                                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                        }
                                                                                                                    });
                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                }

                                                                                                            });
                                                                                                }
                                                                                                progressBar.setVisibility(View.GONE);
                                                                                                txtNoData.setVisibility(View.GONE);
                                                                                            }
                                                                                            else {
                                                                                                progressBar.setVisibility(View.GONE);
                                                                                                txtNoData.setVisibility(View.VISIBLE);
                                                                                            }
                                                                                        }
                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                        }
                                                                                    }
        );
        dismissProgressDialog();
    }



    int fav;

}
