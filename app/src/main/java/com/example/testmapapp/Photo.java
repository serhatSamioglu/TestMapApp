package com.example.testmapapp;

public class Photo {
    Integer id;
    String url;
    String information;

    public Photo(Integer id, String url, String information) {
        this.id = id;
        this.url = url;
        this.information = information;
    }

    public Photo() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
