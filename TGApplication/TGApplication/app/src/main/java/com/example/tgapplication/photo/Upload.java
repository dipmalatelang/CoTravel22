package com.example.tgapplication.photo;

public class Upload {
    public String name;
    public String url;
    public String image;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String url, String image) {
        this.name = name;
        this.url= url;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getimage() {
        return image;
    }
}
