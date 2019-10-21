package com.example.tgapplication;

import com.example.tgapplication.fragment.trip.module.User;

public class UserProfileData {

    User user;
    String profilePhoto;

    public UserProfileData(User user, String profilePhoto) {
        this.user = user;
        this.profilePhoto = profilePhoto;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
