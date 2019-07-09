package com.example.tgapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tgapplication.fragment.account.AccountFragment;
import com.example.tgapplication.fragment.chat.ChatFragment;
import com.example.tgapplication.fragment.favourite.FavouriteFragment;
import com.example.tgapplication.fragment.trip.TripFragment;
import com.example.tgapplication.fragment.visitor.VisitorFragment;
import com.example.tgapplication.login.LoginActivity;
import com.example.tgapplication.trips.AddTripActivity;
import com.example.tgapplication.trips.DetailActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trip_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.trip_add) {
            Intent msgIntent = new Intent(this, AddTripActivity.class);
//            msgIntent.putExtra("nextActivity", "AddTrips");
            startActivity(msgIntent);
//            startActivity(new Intent(this, AddTripActivity.class));
            return true;
        }
//        else if (item.getItemId() == R.id.trip_filter) {
//            startActivity(new Intent(this, FilterTripActivity.class));
//            return true;
//        }
        else if (item.getItemId() == R.id.trip_edit) {
            Intent msgIntent = new Intent(this, LoginActivity.class);
            msgIntent.putExtra("nextActivity", "profileEdit");
            startActivity(msgIntent);

            return true;
        } else if (item.getItemId() == R.id.trip_info) {
            Intent mIntent = new Intent(this, DetailActivity.class);
//            mIntent.putExtra("MyDataObj", (Serializable) myDetail);
//            mIntent.putExtra("ListTrip", (Serializable) tripList);
//            mIntent.putExtra("ListFav", (Serializable) favArray);
//            mIntent.putExtra("ListVisit", (Serializable) visitArray);
            startActivity(mIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
