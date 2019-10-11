package com.example.tgapplication.fragment.visitor;

import com.example.tgapplication.fragment.trip.module.User;

import java.io.Serializable;

public class UserImg implements Serializable {
    User user;
    String pictureUrl;

    public UserImg(User user, String pictureUrl) {
        this.user=user;
        this.pictureUrl=pictureUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
