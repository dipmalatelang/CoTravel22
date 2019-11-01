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
import android.widget.TextView;
import android.widget.Toast;

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

import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;

import com.hudomju.swipe.SwipeableItemClickListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class UsersFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<UserImg> mUsers;
    String pictureUrl;
    String TAG ;

    EditText search_users;
    final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

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


        final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new RecyclerViewAdapter(recyclerView),
                        new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }



                            @Override
                            public void onDismiss(RecyclerViewAdapter view, int position) {
                                UserAdapter.remove(position);
                                userAdapter.notifyItemRemoved(position);
                                userAdapter.notifyItemRangeChanged(position ,userAdapter.getItemCount());

                            }
                        });
// Dismiss the item automatically after 3 seconds
//        touchListener.setDismissDelay(3000);
//TextView tvdelete,tvfavourite;
//
//        tvfavourite = view.findViewById(R.id.tvfavourite);
//        tvdelete =view.findViewById(R.id.tvdelete);
//        recyclerView.setOnTouchListener(touchListener);
//        recyclerView.setOnScrollListener((RecyclerView.OnScrollListener)touchListener.makeScrollListener());
//        recyclerView.addOnItemTouchListener(new SwipeableItemClickListener(this, new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        }));


//        recyclerView.addOnItemTouchListener(new SwipeableItemClickListener(this, new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//
//            }



//        recyclerView.addOnItemTouchListener(new SwipeableItemClickListener(this,
//                new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        if (view.getId() == R.id.tvdelete) {
//                            touchListener.processPendingDismisses();
//                        } else if (view.getId() == R.id.tvfavourite) {
//                            touchListener.undoPendingDismiss();
//                        } else { // R.id.txt_data
//
//                            Log.d(TAG, "onItemClick: "+position);
//                        }
//                    }
//                }));


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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert fuser != null;
                    if (!user.getId().equals(fuser.getUid())){

                        PicturesInstance.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                pictureUrl="";
                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                                    Upload mainPhoto = snapshot1.getValue(Upload.class);
                                    if (Objects.requireNonNull(mainPhoto).type == 1)
                                        pictureUrl = mainPhoto.getUrl();

                                }
                                Log.i("TAG", "onDataChangeMy: "+pictureUrl);
                                mUsers.add(new UserImg(user,pictureUrl));

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                    }

                }

                userAdapter = new UserAdapter(getContext(), mUsers, false, new UserAdapter.UserInterface() {
                    @Override
                    public void lastMessage(Context mContext, String userid, TextView last_msg) {
                        checkForLastMsg(mContext, userid,last_msg);
                    }

                });
                recyclerView.setAdapter(userAdapter);
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

                        Log.i("hghghhgh",""+user.getId());
                        Log.i("hghghhgh1",""+firebaseUser.getUid());
                        try {
                            if (!user.getId().equals(firebaseUser.getUid())){
                                mUsers.add(new UserImg(user,pictureUrl));
                                Log.i("hghghhgh2",""+mUsers);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), mUsers, false, new UserAdapter.UserInterface() {
                        @Override
                        public void lastMessage(Context mContext, String userid, TextView last_msg) {
                            checkForLastMsg(mContext, userid,last_msg);
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
