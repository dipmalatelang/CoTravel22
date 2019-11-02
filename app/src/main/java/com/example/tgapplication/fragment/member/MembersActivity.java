package com.example.tgapplication.fragment.member;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.ProfileActivity;
import com.example.tgapplication.fragment.member.adapter.MembersAdapter;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;
import com.example.tgapplication.photo.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class MembersActivity extends BaseActivity {
    private RecyclerView recyclerView;

    private MembersAdapter membersAdapter;
    int fav=0;
    String pictureUrl;
    final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);

        tripList(fuser);
    }

    public void tripList(FirebaseUser fuser) {
        // any way you managed to go the node that has the 'grp_key'

        UsersInstance.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tripList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            User user = snapshot.getValue(User.class);
                            if (!Objects.requireNonNull(user).getId().equalsIgnoreCase(fuser.getUid()) && user.getAccount_type()==1) {
//                                getFav(fuser.getUid(),user.getId());
                                // HERE WHAT CORRESPONDS TO JOIN
                                FavoritesInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        fav=0;
                                        if (snapshot.hasChild(user.getId()))
                                            fav = 1;

                                        Log.i(TAG, "onDataChange: "+user.getName()+" "+fav);

                                            PicturesInstance.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    pictureUrl="";
                                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                                                        Upload mainPhoto = snapshot1.getValue(Upload.class);
                                                        if (Objects.requireNonNull(mainPhoto).type == 1)
                                                            pictureUrl = mainPhoto.getUrl();

                                                        Log.i(TAG, "onDataChangeMy: "+fav+" ==> "+pictureUrl);
                                                    }


                                                    tripList=findAllMembers(new UserImg(user,pictureUrl,fav));



                                                    for(int k=0;k<tripList.size();k++)
                                                    {
                                                        Log.i(TAG, "onDataChange: TripList "+tripList.get(k).getFavid());
                                                    }
//                                                tripAdapter = new MembersAdapter(this, fuser.getUid(), favArray, tripList);
//                                                recyclerview.setAdapter(tripAdapter);
                                                        membersAdapter = new MembersAdapter(MembersActivity.this, fuser.getUid(), tripList, new MembersAdapter.ProfileData() {
                                                            @Override
                                                            public void setData(TripList tList, int position) {
                                                                Intent mIntent = new Intent(MembersActivity.this, ProfileActivity.class);
                                                                mIntent.putExtra("MyObj", tripList.get(position));
                                                                startActivityForResult(mIntent,1);
                                                            }

                                                            @Override
                                                            public void setProfileVisit(String uid, String id) {
                                                                ProfileVisitorInstance.child(id)
                                                                            .child(uid).child("id").setValue(uid);

                                                            }

                                                        });
                                                        recyclerView.setAdapter(membersAdapter);
                                                        membersAdapter.notifyDataSetChanged();

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }

                                            });

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.i(TAG, "DatabaseError2: "+databaseError);
                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i(TAG, "DatabaseError3: "+databaseError);
                    }
                }
        );
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                TripList strEditText = (TripList) Objects.requireNonNull(data.getExtras()).getSerializable("MyObj");

            }
        }
    }*/
}
