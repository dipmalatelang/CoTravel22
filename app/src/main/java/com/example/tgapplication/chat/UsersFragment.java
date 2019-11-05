package com.example.tgapplication.chat;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.tgapplication.Constants.FavoritesInstance;
import static com.example.tgapplication.Constants.PicturesInstance;
import static com.example.tgapplication.Constants.UsersInstance;


public class UsersFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<UserImg> mUsers;
    String pictureUrl;
int fav;
    EditText search_users;
//    final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        readUsers();

        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchUsers(String s)
    {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = UsersInstance.orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert fuser != null;
                    if (!user.getId().equals(fuser.getUid())) {

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
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

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
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });

                    }

                    userAdapter = new UserAdapter(getContext(), mUsers, false, new UserAdapter.UserInterface() {
                        @Override
                        public void lastMessage(Context mContext, String userid, TextView last_msg, RelativeLayout chat) {
                            checkForLastMsg(mContext, userid, last_msg,chat);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UsersInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_users.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        Log.i("hghghhgh",""+ Objects.requireNonNull(user).getId());
                        Log.i("hghghhgh1",""+ Objects.requireNonNull(firebaseUser).getUid());
                        try {
                            if (!user.getId().equals(firebaseUser.getUid())){
                                mUsers.add(new UserImg(user,pictureUrl,fav));
                                Log.i("hghghhgh2",""+mUsers);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), mUsers, false, new UserAdapter.UserInterface() {
                        @Override
                        public void lastMessage(Context mContext, String userid, TextView last_msg, RelativeLayout chat) {
                            checkForLastMsg(mContext, userid, last_msg,chat);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
