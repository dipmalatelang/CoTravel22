package com.example.tgapplication.fragment.account.profile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.account.profile.module.Permit;
import com.example.tgapplication.fragment.account.profile.module.Upload;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.UserImg;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.tgapplication.Constants.FavoritesInstance;
import static com.example.tgapplication.Constants.PhotoRequestInstance;
import static com.example.tgapplication.Constants.PicturesInstance;
import static com.example.tgapplication.Constants.UsersInstance;

public class PhotoRequestActivity extends BaseActivity {
    @BindView(R.id.cl_Photo_Request)
    ConstraintLayout clPhotoRequest;
    @BindView(R.id.photo_rqst_count)
    TextView photoRqstCount;
    @BindView(R.id.photo_permits_count)
    TextView photoPermitsCount;
    @BindView(R.id.given_permits_count)
    TextView givenPermitsCount;
    private FirebaseUser fuser;
    String pictureUrl;
    ArrayList<UserImg> viewPhotoRequestList = new ArrayList<>();
    ArrayList<UserImg> photoPermitsList = new ArrayList<>();
    ArrayList<UserImg> givenPermitsList = new ArrayList<>();
    int fav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_requests);
        ButterKnife.bind(this);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        viewPhotoRequest();
        photoPermits();
        givenPermits();
    }

    @OnClick({R.id.btn_photo_rqst, R.id.btn_photo_permits, R.id.btn_given_permits})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_photo_rqst:
                if (viewPhotoRequestList.size() > 0) {
                    Intent intent1 = new Intent(PhotoRequestActivity.this, ViewPhotoRequestActivity.class);
                    intent1.putExtra("intentType", 1);
                    intent1.putExtra("userList", new Gson().toJson(viewPhotoRequestList));
                    startActivity(intent1);
                } else {
                    snackBar(clPhotoRequest, "No Data");
                }

                break;
            case R.id.btn_photo_permits:
                if (photoPermitsList.size() > 0) {
                    Intent intent2 = new Intent(PhotoRequestActivity.this, ViewPhotoRequestActivity.class);
                    intent2.putExtra("intentType", 2);
                    intent2.putExtra("userList", new Gson().toJson(photoPermitsList));
                    startActivity(intent2);
                } else {
                    snackBar(clPhotoRequest, "No Data");
                }
                break;
            case R.id.btn_given_permits:
                if (givenPermitsList.size() > 0) {
                    Intent intent3 = new Intent(PhotoRequestActivity.this, ViewPhotoRequestActivity.class);
                    intent3.putExtra("intentType", 3);
                    intent3.putExtra("userList", new Gson().toJson(givenPermitsList));
                    startActivity(intent3);
                } else {
                    snackBar(clPhotoRequest, "No Data");
                }
                break;
        }
    }

    private void viewPhotoRequest() {
        PhotoRequestInstance.orderByChild("receiver").equalTo(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Permit permit = ds.getValue(Permit.class);
                    if (Objects.requireNonNull(permit).getStatus() == 0) {
                        UsersInstance.child(permit.getSender()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
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
                                                    fav = 1;
                                                } else {
                                                    fav = 0;
                                                }
                                                viewPhotoRequestList.add(new UserImg(user, pictureUrl, fav));

                                                String prcount=""+viewPhotoRequestList.size();
                                                Log.i(TAG, "onCompleted: givenPermitsList " + prcount);

                                                photoRqstCount.setVisibility(View.VISIBLE);
                                                photoRqstCount.setText(prcount);

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

    private void photoPermits() {
        PhotoRequestInstance.orderByChild("sender").equalTo(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Permit permit = ds.getValue(Permit.class);
                    if (Objects.requireNonNull(permit).getStatus() == 1) {
                        UsersInstance.child(permit.getReceiver()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);

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
                                                    fav = 1;
                                                } else {
                                                    fav = 0;
                                                }
                                                photoPermitsList.add(new UserImg(user, pictureUrl, fav));
                                                String ppcount=""+photoPermitsList.size();
                                                Log.i(TAG, "onCompleted: photoPermitsList " + ppcount);

                                                photoPermitsCount.setVisibility(View.VISIBLE);
                                                photoPermitsCount.setText(ppcount);

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
                Log.i(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void givenPermits() {
        PhotoRequestInstance.orderByChild("receiver").equalTo(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Permit permit = ds.getValue(Permit.class);
                    if (Objects.requireNonNull(permit).getStatus() == 1) {
                        UsersInstance.child(permit.getSender()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);

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
                                                    fav = 1;
                                                } else {
                                                    fav = 0;
                                                }
                                                givenPermitsList.add(new UserImg(user, pictureUrl, fav));

                                                String gpcount=""+givenPermitsList.size();
                                                Log.i(TAG, "onCompleted: givenPermitsList " + gpcount);

                                                givenPermitsCount.setVisibility(View.VISIBLE);
                                                givenPermitsCount.setText(gpcount);

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
