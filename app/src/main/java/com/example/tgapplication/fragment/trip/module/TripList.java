package com.example.tgapplication.fragment.trip.module;

import java.io.Serializable;
import java.util.ArrayList;

public class TripList implements Serializable {

    private int visit_id;
    private int favid;
    private String id;
    private String name;
    private String imageUrl;
    private String planLocation;
    private String trip_note;
    private String from_to_date;
    private String userLocation;
    private String gender,age;
    private String nationality, lang, height,  body_type,  eyes,  hair, visit;
    private ArrayList<String> look;


    public TripList() {

    }

    public TripList(String id, String name, String imageUrl, String age, String gender, String userLocation, String nationality, String lang, String height, String body_type, String eyes, String hair, ArrayList<String> look, String visit, String planLocation, String trip_note, String from_to_date, int favid, int visit_id) {        this.id=id;
        this.name=name;
        this.imageUrl=imageUrl;
        this.planLocation = planLocation;
        this.trip_note = trip_note;
        this.from_to_date = from_to_date;
        this.age=age;
        this.gender=gender;
        this.userLocation=userLocation;
        this.nationality=nationality;
        this.lang= lang;
        this.height=height;
        this.body_type= body_type;
        this.eyes=eyes;
        this.hair=hair;
        this.look=look;
        this.visit= visit;
        this.favid=favid;
        this.visit_id=visit_id;
    }

    public int getVisit_id() {
        return visit_id;
    }

    public void setVisit_id(int visit_id) {
        this.visit_id = visit_id;
    }

    public int getFavid() {
        return favid;
    }

    public void setFavid(int favid) {
        this.favid = favid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
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

    public ArrayList<String> getLook() {
        return look;
    }

    public void setLook(ArrayList<String> look) {
        this.look = look;
    }

    public String getVisit() {
        return visit;
    }

    public void setVisit(String visit) {
        this.visit = visit;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPlanLocation() {
        return planLocation;
    }

    public void setPlanLocation(String planLocation) {
        this.planLocation = planLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrip_note() {
        return trip_note;
    }

    public void setTrip_note(String trip_note) {
        this.trip_note = trip_note;
    }

    public String getFrom_to_date() {
        return from_to_date;
    }

    public void setFrom_to_date(String from_to_date) {
        this.from_to_date = from_to_date;
    }
}
