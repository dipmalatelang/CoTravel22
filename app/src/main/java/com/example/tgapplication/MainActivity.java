package com.example.tgapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.tgapplication.fragment.chat.ChatFragment;
import com.example.tgapplication.fragment.favourite.FavouriteFragment;
import com.example.tgapplication.fragment.trip.TripFragment;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.VisitorFragment;
import com.example.tgapplication.login.LoginActivity;
import com.example.tgapplication.photo.Upload;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    FirebaseUser fUser;
    Fragment fragment;
    BottomNavigationView navView;
    ConstraintLayout container;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        retrieveUserDetail();

        navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.nav_trip);
        navView.setOnNavigationItemSelectedListener(this);

        container =findViewById(R.id.container);

        fragment=new TripFragment();
        loadFragment(fragment);
    }

    private void retrieveUserDetail() {
        UsersInstance.child(fUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Log.i(TAG, "onDataChange User: "+ Objects.requireNonNull(user).getName()+" "+user.getAge()+" "+user.getId());
        PicturesInstance.child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange Pictures: ");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Upload upload = ds.getValue(Upload.class);
                    if (Objects.requireNonNull(upload).getType() == 1) {
                        Log.i(TAG, "onDataChange Pictures: " + upload.getUrl());
                        saveDetailsLater(user.getId(),user.getName(),user.getAge(),upload.getUrl());
                    }
                }

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

    public void saveDetailsLater(String id, String email, String password, String imageUrl){
        sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("Id",id);
        editor.putString("Name", email);
        editor.putString("Age", password);
        editor.putString("ImageUrl",imageUrl);
        editor.apply();
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_main_screen, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_account:
                fragment = new MyProfileFragment();
                break;
            case R.id.nav_chat:
                fragment = new ChatFragment();
                break;
            case R.id.nav_favorites:
                fragment = new FavouriteFragment();
                break;
            case R.id.nav_trip:
                fragment = new TripFragment();
                break;
            case R.id.nav_vistor:
                fragment = new VisitorFragment();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trip_menu, menu);
        return true;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        snackBar(container,"Please click Back again to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.home:
                navView.setSelectedItemId(R.id.nav_trip);
                fragment=new TripFragment();
                loadFragment(fragment);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                snackBar(container,"Logout");
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
