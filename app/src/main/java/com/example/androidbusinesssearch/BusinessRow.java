package com.example.androidbusinesssearch;

public class BusinessRow {
    String id;
    String name;
    Double rating;
    Integer distanceInMiles;
    String imageUrl;

    public BusinessRow(String id, String name, Double rating, Double distanceInMetres, String imageUrl) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.distanceInMiles = (int)Math.round(distanceInMetres/1609.34);
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getRating() {
        return rating;
    }

    public Integer getDistanceInMiles() {
        return distanceInMiles;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
