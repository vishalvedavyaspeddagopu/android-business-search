package com.example.androidbusinesssearch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {
    Context context;
    List<BusinessRow> businessRowList;

    public TableAdapter(Context context, List<BusinessRow> businessRowList) {
        this.context = context;
        this.businessRowList = businessRowList;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_row, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        if (businessRowList != null && businessRowList.size() > 0) {
            BusinessRow row = businessRowList.get(position);
            holder.id.setText(String.valueOf(position + 1));

            //Tag for referencing the row later
            holder.id.setTag(row.getId());

            holder.rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("id-tag-new", row.getId());
                    Log.d("id-name-new", row.getName());

                    Intent intent = new Intent(context, DetailTabs.class);
                    intent.putExtra("businessId", row.getId());
                    intent.putExtra("businessName", row.getName());
                    context.startActivity(intent);
                }
            });

            if(row.getImageUrl() != null && !row.getImageUrl().isEmpty()) {
                Picasso.get().setLoggingEnabled(true);
                Picasso.get().load(row.getImageUrl()).resize(120, 120)
                        .into(holder.image, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d("image issue", row.getImageUrl());
                                e.printStackTrace();
                            }
                        });
            }

            holder.name.setText(row.getName());
            holder.rating.setText(String.valueOf(row.getRating()));
            holder.dist.setText(String.valueOf(row.getDistanceInMiles()));
        }
    }

    @Override
    public int getItemCount() {
        return businessRowList.size();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder {
        View rowView;
        TextView id, name, rating, dist;
        ImageView image;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);

            rowView = itemView;
            id = itemView.findViewById(R.id.id);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.rating);
            dist = itemView.findViewById(R.id.dist);
        }
    }
}
