package com.example.androidbusinesssearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReservationsPage extends AppCompatActivity {
    ReservationsAdapter tableAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations_page);

        //set up home buttons
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Yelp");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Get current bookings
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Bookings", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("bookings", "[]");

        Type bookingsType = new TypeToken<List<Bookings>>() {}.getType();
        List<Bookings> bookings = gson.fromJson(json, bookingsType);

        displayBookings(bookings);

        deleteOnSwipe();
    }

    private void displayBookings(List<Bookings> rows)
    {
        TextView textView = (TextView) findViewById(R.id.noBookingLabel);
        if(rows == null || rows.size() == 0)
            textView.setVisibility(View.VISIBLE);

        //Table
        recyclerView = findViewById(R.id.reservations_table);

        tableAdapter = new ReservationsAdapter(this, rows);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tableAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void deleteOnSwipe() {
        DeleteReservation deleteReservation = new DeleteReservation(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();

//                final Bookings deletedBooking = tableAdapter.getBookingsList().get(position);

                SharedPreferences preferences = getApplicationContext().getSharedPreferences("Bookings", Context.MODE_PRIVATE);
                Gson gson = new Gson();
//                String json = preferences.getString("bookings", "[]");

                Type bookingsType = new TypeToken<List<Bookings>>() {}.getType();
                List<Bookings> bookings = tableAdapter.getBookingsList(); //gson.fromJson(json, bookingsType);

                List<Bookings> newBookings = new ArrayList<>();
                for (int k = 0; k < bookings.size(); k++) {
                    if(k == position)
                        continue;
                    newBookings.add(bookings.get(k));
                }

                // Save bookings
                String bookingsJson = gson.toJson(newBookings, bookingsType);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("bookings", bookingsJson);
                editor.commit();

                tableAdapter.removeItem(position);

                displayBookings(newBookings);

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.reservations_page), "Removing Existing Reservation", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(deleteReservation);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}