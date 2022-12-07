package com.example.androidbusinesssearch;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.example.androidbusinesssearch.databinding.ActivityDetailTabsBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailTabs extends AppCompatActivity {

    private ActivityDetailTabsBinding binding;

    private String businessName;
    private String businessUrl;

    private static String[] tabNames = {"BUSINESS DETAILS", "MAP LOCATION", "REVIEWS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get business id clicked
        Intent intent = getIntent();
        String value = intent.getStringExtra("businessId");
        String name = intent.getStringExtra("businessName");
        businessName = name;

        //get business details
        getBusinessDetails(value);

        //set up home buttons
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.twitter:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/intent/tweet?text=Check " + businessName + " on Yelp. " + businessUrl)));
                return true;
            case R.id.facebook:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + businessUrl)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getBusinessDetails(String businessId) {
        String url = "https://search-business-vive97.wl.r.appspot.com/api/getBusinessDetail?id=" + businessId;

        Log.d("details-url", url);

        RequestQueue requestQueue = VolleyRequestQueue.getInstance(getApplicationContext()).getRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        //Set business name
                        businessUrl = response.getString("url");

                        binding = ActivityDetailTabsBinding.inflate(getLayoutInflater());
                        setContentView(binding.getRoot());

                        DetailsAdapter detailsAdapter = new DetailsAdapter(getSupportFragmentManager(), getLifecycle(), businessId, response);
                        ViewPager2 viewPager2 = binding.viewPager;
                        viewPager2.setAdapter(detailsAdapter);

                        TabLayout tabs = binding.tabLayout;
                        new TabLayoutMediator(tabs, viewPager2,
                                (tab, position) -> tab.setText(tabNames[position])
                        ).attach();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("info", response.toString());
                }, error -> Log.e("error", "Network call failed"));

        requestQueue.add(jsonObjectRequest);
    }
}