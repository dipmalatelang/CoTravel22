package com.example.tgapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tgapplication.fragment.account.profile.ProfileActivity;
import com.example.tgapplication.login.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @OnClick({R.id.tv_my_profile, R.id.tv_Logout, R.id.iv_Image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //
            case R.id.tv_my_profile: case R.id.iv_Image:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.tv_Logout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
//                snackBar(container,"Logout");
                break;

        }
    }
}
