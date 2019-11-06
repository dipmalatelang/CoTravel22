package com.example.tgapplication.fragment.account.profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.verify.ViewPhotoRequestAdapter;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;
import com.example.tgapplication.photo.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.tgapplication.Constants.FavoritesInstance;
import static com.example.tgapplication.Constants.PhotoRequestInstance;
import static com.example.tgapplication.Constants.PicturesInstance;
import static com.example.tgapplication.Constants.UsersInstance;

public class ViewPhotoRequestActivity extends BaseActivity {
    @BindView(R.id.rv_view_photo_request)
    RecyclerView rvViewPhotoRequest;
    private FirebaseUser fuser;
    String pictureUrl;
    int fav;
    String intentType;
    ViewPhotoRequestAdapter viewPhotoRequestAdapter;
    ArrayList<UserImg> userList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo_request);
        ButterKnife.bind(this);

        fuser=FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rvViewPhotoRequest.setLayoutManager(linearLayoutManager);

        String intentType=getIntent().getStringExtra("intentType");
        Log.i(TAG, "onCreate: "+intentType);
        if(intentType.equalsIgnoreCase("request"))
        {
            viewPhotoRequest();
        }
        else if(intentType.equalsIgnoreCase("givenPermits"))
        {
            givenPermits();
        }
        else if(intentType.equalsIgnoreCase("photoPermits"))
        {
            PhotoPermits();
        }

     /*  viewPhotoRequest();
       givenPermitsActivity();*/
    }

    private void PhotoPermits() {
        PhotoRequestInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Permit permit= ds.getValue(Permit.class);
                    if(Objects.requireNonNull(permit).getSender().equals(fuser.getUid()) && Objects.requireNonNull(permit).getStatus()==1)
                    {
                        UsersInstance.child(permit.getReceiver()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user= dataSnapshot.getValue(User.class);

                                PicturesInstance.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        pictureUrl = "";
                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                                            Upload mainPhoto = snapshot1.getValue(Upload.class);
                                            if (Objects.requireNonNull(mainPhoto).type == 1)
                                                pictureUrl = mainPhoto.getUrl();

                                        }
                                        FavoritesInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.hasChild(user.getId())) {
                                                    // run some code
                                                    fav = 1;
                                                } else {
                                                    fav = 0;
                                                }
                                                userList.add(new UserImg(user,pictureUrl,fav));
                                                Log.i(TAG, "onDataChange:  "+user.getName()+" "+user.getAge());

                                                viewPhotoRequestAdapter= new ViewPhotoRequestAdapter(ViewPhotoRequestActivity.this, userList,2, new ViewPhotoRequestAdapter.ViewPhotoRequestInterface() {
                                                    @Override
                                                    public void acceptRequest(String id, int pos) {
                                                        acceptPhotoRequest(id,1);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();

                                                       /* acceptPhotoRequest(fuser.getUid(),id);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();*/
                                                        Toast.makeText(ViewPhotoRequestActivity.this, "Accept", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void denyRequest(String id, int pos) {
                                                        acceptPhotoRequest(id,2);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();
                                                       /* PrivatePhotoAccessInstance.child(fuser.getUid()).child(id).child("status").setValue(2);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();*/
                                                        Toast.makeText(ViewPhotoRequestActivity.this, "Deny", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void hidePhotoRequest(String id, int pos) {
                                                        removePhotoRequest(id,0);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();
                                                       /* PrivatePhotoAccessInstance.child(fuser.getUid()).child(id).child("status").setValue(0);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();*/
                                                    }
                                                });

                                                rvViewPhotoRequest.setAdapter(viewPhotoRequestAdapter);
                                                viewPhotoRequestAdapter.notifyDataSetChanged();
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

  /*  private void PhotoPermits(){
        PrivatePhotoAccessInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: "+dataSnapshot);
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Permit permit= ds.getValue(Permit.class);
                   *//* if(Objects.requireNonNull(permit).getId().equalsIgnoreCase(fuser.getUid()))
                    {*//*
                        Log.i(TAG, "onDataChange: "+permit.getId());
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    private void acceptPhotoRequest(String uid, String id) {
        PrivatePhotoAccessInstance.child(uid).child(id).child("status").setValue(1);
    }*/

    private void givenPermits() {
        PhotoRequestInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Permit permit= ds.getValue(Permit.class);
                    if(Objects.requireNonNull(permit).getReceiver().equals(fuser.getUid()) && Objects.requireNonNull(permit).getStatus()==1)
                    {
                        UsersInstance.child(permit.getSender()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user= dataSnapshot.getValue(User.class);

                                PicturesInstance.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        pictureUrl = "";
                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                                            Upload mainPhoto = snapshot1.getValue(Upload.class);
                                            if (Objects.requireNonNull(mainPhoto).type == 1)
                                                pictureUrl = mainPhoto.getUrl();

                                        }
                                        FavoritesInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.hasChild(user.getId())) {
                                                    // run some code
                                                    fav = 1;
                                                } else {
                                                    fav = 0;
                                                }
                                                userList.add(new UserImg(user,pictureUrl,fav));
                                                Log.i(TAG, "onDataChange:  "+user.getName()+" "+user.getAge());

                                                viewPhotoRequestAdapter= new ViewPhotoRequestAdapter(ViewPhotoRequestActivity.this, userList,3, new ViewPhotoRequestAdapter.ViewPhotoRequestInterface() {
                                                    @Override
                                                    public void acceptRequest(String id, int pos) {
                                                        acceptPhotoRequest(id,1);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();

                                                       /* acceptPhotoRequest(fuser.getUid(),id);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();*/
                                                        Toast.makeText(ViewPhotoRequestActivity.this, "Accept", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void denyRequest(String id, int pos) {
                                                        acceptPhotoRequest(id,2);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();
                                                       /* PrivatePhotoAccessInstance.child(fuser.getUid()).child(id).child("status").setValue(2);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();*/
                                                        Toast.makeText(ViewPhotoRequestActivity.this, "Deny", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void hidePhotoRequest(String id, int pos) {
                                                        removePhotoRequest(id,0);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();
                                                       /* PrivatePhotoAccessInstance.child(fuser.getUid()).child(id).child("status").setValue(0);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();*/
                                                    }
                                                });

                                                rvViewPhotoRequest.setAdapter(viewPhotoRequestAdapter);
                                                viewPhotoRequestAdapter.notifyDataSetChanged();
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void acceptPhotoRequest(String userid, int i)
    {
        PhotoRequestInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Permit permit = snapshot.getValue(Permit.class);
                    if (Objects.requireNonNull(permit).getReceiver().equals(fuser.getUid()) && permit.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("status", i);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void removePhotoRequest(String userid, int i)
    {
        PhotoRequestInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Permit permit = snapshot.getValue(Permit.class);
                    if (Objects.requireNonNull(permit).getReceiver().equals(fuser.getUid()) && permit.getSender().equals(userid)) {
                       /* HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("status", i);
                        snapshot.getRef().updateChildren(hashMap);*/
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void viewPhotoRequest() {
        PhotoRequestInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Permit permit= ds.getValue(Permit.class);
                    if(Objects.requireNonNull(permit).getReceiver().equals(fuser.getUid()) && Objects.requireNonNull(permit).getStatus()==0)
                    {
                        UsersInstance.child(permit.getSender()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user= dataSnapshot.getValue(User.class);

                                PicturesInstance.child(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        pictureUrl = "";
                                        for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                                            Upload mainPhoto = snapshot1.getValue(Upload.class);
                                            if (Objects.requireNonNull(mainPhoto).type == 1)
                                                pictureUrl = mainPhoto.getUrl();

                                        }
                                        FavoritesInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if (snapshot.hasChild(user.getId())) {
                                                    // run some code
                                                    fav = 1;
                                                } else {
                                                    fav = 0;
                                                }
                                                userList.add(new UserImg(user,pictureUrl,fav));
                                                Log.i(TAG, "onDataChange:  "+user.getName()+" "+user.getAge());

                                                viewPhotoRequestAdapter= new ViewPhotoRequestAdapter(ViewPhotoRequestActivity.this, userList,1, new ViewPhotoRequestAdapter.ViewPhotoRequestInterface() {
                                                    @Override
                                                    public void acceptRequest(String id, int pos) {
                                                        acceptPhotoRequest(id,1);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();

                                                       /* acceptPhotoRequest(fuser.getUid(),id);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();*/
                                                        Toast.makeText(ViewPhotoRequestActivity.this, "Accept", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void denyRequest(String id, int pos) {
                                                        acceptPhotoRequest(id,2);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();
                                                       /* PrivatePhotoAccessInstance.child(fuser.getUid()).child(id).child("status").setValue(2);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();*/
                                                        Toast.makeText(ViewPhotoRequestActivity.this, "Deny", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void hidePhotoRequest(String id, int pos) {
                                                        removePhotoRequest(id,0);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();
                                                       /* PrivatePhotoAccessInstance.child(fuser.getUid()).child(id).child("status").setValue(0);
                                                        userList.remove(pos);
                                                        viewPhotoRequestAdapter.notifyDataSetChanged();*/
                                                    }
                                                });

                                                rvViewPhotoRequest.setAdapter(viewPhotoRequestAdapter);
                                                viewPhotoRequestAdapter.notifyDataSetChanged();
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
