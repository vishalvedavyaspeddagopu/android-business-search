package com.example.androidbusinesssearch;

public class Bookings {
    String id;
    String name;
    String date;
    String time;
    String email;

    public Bookings(String id, String name, String date, String time, String email) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getEmail() {
        return email;
    }
}
