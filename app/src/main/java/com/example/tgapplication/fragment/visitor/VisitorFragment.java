package com.example.tgapplication.fragment.visitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.ui.ProfileActivity;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.adapter.VisitorAdapter;
import com.example.tgapplication.fragment.account.profile.module.Upload;
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
    String pictureUrl;
    private List<UserImg> myFavArray=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_visitor, container, false);

        myVisitRV = view.findViewById(R.id.myVisitRV);
        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getActivity());
        myVisitRV.setLayoutManager(nLayoutManager);

        showProgressDialog();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        revVisitList(Objects.requireNonNull(fuser));


        return view;
    }




    public void revVisitList(FirebaseUser fuser) {


        ProfileVisitorInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                            myFavArray.clear();
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

                                                                                                                                        ProfileVisitorInstance.child(id)
                                                                                                                                                .child(uid).child("id").setValue(uid);
                                                                                                                                    }

                                                                                                                                    @Override
                                                                                                                                    public void setData(UserImg mTrip, int position) {
                                                                                                                                        if (mTrip.getUser().getAccount_type() == 1) {
                                                                                                                                            Intent mIntent = new Intent(getActivity(), ProfileActivity.class);
                                                                                                                                            mIntent.putExtra("MyUserObj", myFavArray.get(position));
                                                                                                                                            startActivityForResult(mIntent, 1);
                                                                                                                                        }
                                                                                                                                        else {
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
