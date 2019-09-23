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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

public class MembersActivity extends BaseActivity {
    private RecyclerView recyclerView;

    private MembersAdapter membersAdapter;
    private List<User> mUsers;
    int fav;
    EditText search_users;
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
        DatabaseReference MembersRef = FirebaseDatabase.getInstance()
                .getReference().child("Users");
        MembersRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tripList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final User user = snapshot.getValue(User.class);
                            if (!user.getId().equalsIgnoreCase(fuser.getUid())) {
//                                getFav(fuser.getUid(),user.getId());
                                // HERE WHAT CORRESPONDS TO JOIN
                                DatabaseReference visitorRef = FirebaseDatabase.getInstance().getReference("Favorites")
                                        .child(fuser.getUid());

                                visitorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        if (snapshot.hasChild(user.getId())) {
                                            // run some code
                                            fav = 1;
                                        } else {
                                            fav = 0;
                                        }

                                        tripList=findAllMembers(user,fav);

//                                                tripAdapter = new MembersAdapter(this, fuser.getUid(), favArray, tripList);
//                                                recyclerview.setAdapter(tripAdapter);
                                                        membersAdapter = new MembersAdapter(MembersActivity.this, fuser.getUid(), tripList, new MembersAdapter.ProfileData() {
                                                            @Override
                                                            public void setData(TripList tList, int position) {
                                                                Intent mIntent = new Intent(MembersActivity.this, ProfileActivity.class);
                                                                mIntent.putExtra("MyObj", (Serializable) tripList.get(position));
                                                                startActivity(mIntent);
                                                            }
                                                        });
                                                        recyclerView.setAdapter(membersAdapter);
                                                        membersAdapter.notifyDataSetChanged();


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
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i(TAG, "DatabaseError3: "+databaseError);
                    }
                }
        );
    }
}
