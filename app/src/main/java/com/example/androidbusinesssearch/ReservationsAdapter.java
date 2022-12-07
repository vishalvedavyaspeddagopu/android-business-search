package com.example.androidbusinesssearch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationsViewHolder> {
    Context context;
    List<Bookings> bookingsList;

    public ReservationsAdapter(Context context, List<Bookings> bookingsList) {
        this.context = context;
        this.bookingsList = bookingsList;
    }

    @NonNull
    @Override
    public ReservationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_row, parent, false);
        return new ReservationsAdapter.ReservationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationsViewHolder holder, int position) {
        if (bookingsList != null && bookingsList.size() > 0) {
            Bookings row = bookingsList.get(position);

            holder.id.setText(String.valueOf(position + 1));
            holder.name.setText(row.getName());
            holder.date.setText(row.getDate());
            holder.time.setText(row.getTime());
            holder.email.setText(row.getEmail());

            //Tag for referencing the row later
            holder.id.setTag(row.getId());
        }
    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }

    public void removeItem(int position) {
        bookingsList.remove(position);
        notifyItemRemoved(position);
    }

    public List<Bookings> getBookingsList() {
        return bookingsList;
    }

    public class ReservationsViewHolder extends RecyclerView.ViewHolder {
        TextView id, name, date, time, email;

        public ReservationsViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            email = itemView.findViewById(R.id.email);
        }
    }
}
