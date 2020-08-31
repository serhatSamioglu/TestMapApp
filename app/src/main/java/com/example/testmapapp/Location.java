package com.example.testmapapp;

public class Location {
    double latitude;
    double longitude;
    Integer id;
    String locationName;

    public Location(double latitude, double longitude, Integer id, String locationName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.locationName = locationName;
    }

    public Location(){

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
