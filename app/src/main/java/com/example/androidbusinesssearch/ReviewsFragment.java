package com.example.androidbusinesssearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {
    private String businessId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            businessId = getArguments().getString("businessId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    public static ReviewsFragment newInstance(String businessId) {
        ReviewsFragment f = new ReviewsFragment();

        // Supply business id as an argument.
        Bundle args = new Bundle();
        args.putString("businessId", businessId);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //remove this function
        getReviews(businessId);
    }

    public void getReviews(String businessId) {
        String url = "https://search-business-vive97.wl.r.appspot.com/api/getReviews?id=" + businessId;

        Log.d("review-url", url);

        RequestQueue requestQueue = VolleyRequestQueue.getInstance(getActivity().getApplicationContext()).getRequestQueue(getActivity().getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("reviews");
                            List<ReviewRow> results = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                results.add(new ReviewRow(obj.getJSONObject("user").getString("name"),
                                        obj.getInt("rating"),
                                        obj.getString("text"),
                                        obj.getString("time_created").split(" ")[0]));
                            }
                            setUpTable(results);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("info", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", "Network call failed");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void setUpTable(List<ReviewRow> rows)
    {
        //Table
        RecyclerView recyclerView = getView().findViewById(R.id.reviews);

        ReviewsAdapter tableAdapter = new ReviewsAdapter(getContext(), rows);
        recyclerView.setAdapter(tableAdapter);
        recyclerView.setHasFixedSize(true);
    }
}