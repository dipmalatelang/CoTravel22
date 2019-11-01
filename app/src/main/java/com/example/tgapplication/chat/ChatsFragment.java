package com.example.tgapplication.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;
import com.example.tgapplication.photo.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatsFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<UserImg> mUsers;
int fav;
    FirebaseUser fuser;
    String pictureUrl;

    private List<Chatlist> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();



        ChatlistInstance.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                Log.i("ChatFrag",""+dataSnapshot.getChildren());
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());


        return view;
    }

    private void updateToken(String token){
        Token token1 = new Token(token);
        TokensInstance.child(fuser.getUid()).setValue(token1);
    }

    private void chatList() {
        mUsers = new ArrayList<>();
        UsersInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList) {
                        if (user.getId().equals(chatlist.getId())) {
                            PicturesInstance.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    pictureUrl = "";
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                                        Upload mainPhoto = snapshot1.getValue(Upload.class);
                                        if (Objects.requireNonNull(mainPhoto).type == 1)
                                            pictureUrl = mainPhoto.getUrl();

                                    }
                                    Log.i("TAG", "onDataChangeMy: " + pictureUrl);
                                    FavoritesInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {

                                            if (snapshot.hasChild(user.getId())) {
                                                // run some code
                                                fav = 1;
                                            } else {
                                                fav = 0;
                                            }
                                            mUsers.add(new UserImg(user, pictureUrl,fav));

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }

                                    });

                                    userAdapter = new UserAdapter(getContext(), mUsers, true, new UserAdapter.UserInterface() {
                                        @Override
                                        public void lastMessage(Context mContext, String userid, TextView last_msg) {
                                            checkForLastMsg(mContext, userid, last_msg);
                                        }

                                        @Override
                                        public void addToFav(String userid, int position) {

                                        }

                                        @Override
                                        public void addToTrash(String userid, int position) {

                                        }

                                        @Override
                                        public void restoreFromTrash(String userid, int position) {

                                        }


                                        @Override
                                        public void removeFromFav(String userid) {

                                        }


                                    });
                                    recyclerView.setAdapter(userAdapter);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}