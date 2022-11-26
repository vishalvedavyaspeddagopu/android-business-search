package com.example.androidbusinesssearch;

public class ReviewRow {
    String name;
    Integer rating;
    String review;
    String date;

    public ReviewRow(String name, Integer rating, String review, String date) {
        this.name = name;
        this.rating = rating;
        this.review = review;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public Integer getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public String getDate() {
        return date;
    }
}
