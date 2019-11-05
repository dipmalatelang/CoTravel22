package com.example.tgapplication.fragment.trip.module;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{

    private String id;
    private String username;
    private String status;
    private String search;
    private String email;
    private String social_media;
    private String body_type;
    private String dob;
    private String gender;
    private String eyes;
    private String hair;
    private String height;
    private String lang;
    private ArrayList<String> look,range_age;
    private String location;
    private String name , phone;
    private String nationality;
    private String visit;
    private String age;
    private int account_type;
    private String about_me;

    public User() {
    }

    //    public User(String id, String username, String imageURL, String status, String search,String email, String social_media) {
//        this.id = id;
//        this.username = username;
//        this.imageURL = imageURL;
//        this.status = status;
//        this.search = search;
//        this.email=email;
//        this.social_media=social_media;
//    }



    public User(String id, String username, String status, String search, String gender, String age, String email, String social_media, String body_type, String dob, String eyes, String hair, String height, String lang, ArrayList<String> look, ArrayList<String> range_age, String location, String name, String phone, String nationality, String visit,int account_type, String about_me) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.dob = dob;
        this.gender=gender;
        this.age=age;
        this.location = location;
        this.nationality = nationality;
        this.lang = lang;
        this.look= look;
        this.range_age=range_age;
        this.height = height;
        this.body_type = body_type;
        this.eyes = eyes;
        this.hair = hair;
        this.visit = visit;
        this.email = email;
        this.social_media = social_media;
        this.phone=phone;
        this.status = status;
        this.search = search;
        this.account_type=account_type;
        this.about_me=about_me;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public ArrayList<String> getRange_age() {
        return range_age;
    }

    public void setRange_age(ArrayList<String> range_age) {
        this.range_age = range_age;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getLook() {
        return look;
    }

    public void setLook(ArrayList<String> look) {
        this.look = look;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocial_media() {
        return social_media;
    }

    public void setSocial_media(String social_media) {
        this.social_media = social_media;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
