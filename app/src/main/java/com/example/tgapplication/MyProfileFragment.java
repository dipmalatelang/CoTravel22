package com.example.tgapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tgapplication.fragment.account.profile.ProfileActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this,view);

        return view;
    }

    @OnClick(R.id.tv_my_profile)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), ProfileActivity.class));
    }
}
