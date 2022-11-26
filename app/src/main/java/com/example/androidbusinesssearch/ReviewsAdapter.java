package com.example.androidbusinesssearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    Context context;
    List<ReviewRow> reviewRowList;

    public ReviewsAdapter(Context context, List<ReviewRow> reviewRowList) {
        this.context = context;
        this.reviewRowList = reviewRowList;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_row, parent, false);
        //check and remove
//        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        if (reviewRowList != null && reviewRowList.size() > 0) {
            ReviewRow row = reviewRowList.get(position);
            holder.name.setText(row.getName());
            holder.rating.setText("Rating :" + row.getRating() + "/5");
            holder.review.setText(row.getReview());
            holder.date.setText(row.getDate());
            if(position >= reviewRowList.size() - 1)
            {
                holder.divider.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
            }
        }
    }

    @Override
    public int getItemCount() {
        return reviewRowList.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView name, rating, review, date;
        View divider;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.rating);
            review = itemView.findViewById(R.id.review);
            date = itemView.findViewById(R.id.date);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
