package com.example.tgapplication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tgapplication.fragment.account.AccountFragment;
import com.example.tgapplication.fragment.chat.ChatFragment;
import com.example.tgapplication.fragment.favourite.FavouriteFragment;
import com.example.tgapplication.fragment.trip.TripFragment;
import com.example.tgapplication.fragment.visitor.VisitorFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

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
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_account:
                fragment = new AccountFragment();
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
}
