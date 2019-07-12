package com.example.tgapplication;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public abstract class BaseFragment extends Fragment {
    public void snackBar(View constrainlayout, String s){
        Snackbar snackbar = Snackbar.make(constrainlayout,s, Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Action Button", "onClick triggered");
            }
        });
        View snackbarLayout = snackbar.getView();
        TextView textView = (TextView)snackbarLayout.findViewById(R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_android_green_24dp, 0, 0, 0);
        textView.setCompoundDrawablePadding(20);
        snackbar.show();
    }

}
