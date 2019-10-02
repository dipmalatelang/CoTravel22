package com.example.tgapplication.photo;

public class Upload {
    public String name;
    public String url;


    public int type;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String url, int type) {
        this.name = name;
        this.url= url;
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

}
