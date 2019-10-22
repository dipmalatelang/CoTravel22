package com.example.tgapplication.fragment.chat;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.chat.Chatlist;
import com.example.tgapplication.chat.Token;
import com.example.tgapplication.chat.UserAdapter;
import com.example.tgapplication.fragment.member.MembersActivity;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;
import com.example.tgapplication.photo.Upload;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment {


    private RecyclerView recyclerView;

    private UserAdapter userAdapter;

    String pictureUrl="";

    FirebaseUser fuser;
    FloatingActionButton floatingActionButton;


//    private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        setHasOptionsMenu(false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();


        floatingActionButton=view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MembersActivity.class));
            }
        });

        ChatlistInstance.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Chatlist> usersList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
//                Log.i("ChatFrag",""+dataSnapshot.getChildren());
                Log.i("Size", "onDataChange: "+usersList.size());
                chatList(usersList);
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

    private void chatList(List<Chatlist> usersList) {

        UsersInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserImg> mUsers;  mUsers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList){
                        if (user.getId().equals(chatlist.getId())){

                            PicturesInstance.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds: dataSnapshot.getChildren())
                                    {

                                        Upload upload=ds.getValue(Upload.class);

                                        if(Objects.requireNonNull(upload).getType()==1)
                                        {
                                            pictureUrl=upload.getUrl();
                                        }
                                    }

                                    mUsers.add(new UserImg(user,pictureUrl));

                                    Log.i("TAG", "onDataChange: chat"+mUsers.size());
                                    userAdapter = new UserAdapter(getContext(), mUsers, true, new UserAdapter.UserInterface() {
                                        @Override
                                        public void lastMessage(Context mContext, String userid, TextView last_msg) {
                                            checkForLastMsg(mContext,userid,last_msg);
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
