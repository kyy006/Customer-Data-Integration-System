package com.example.customersystem.embeddable;

import jakarta.persistence.Embeddable;

@Embeddable
public class Geo {
    private String lat;
    private String lng;

    // Getters and Setters
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
