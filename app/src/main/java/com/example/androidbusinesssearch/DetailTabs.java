package com.example.androidbusinesssearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.example.androidbusinesssearch.databinding.ActivityDetailTabsBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class DetailTabs extends AppCompatActivity {

    private ActivityDetailTabsBinding binding;

    private static String[] tabNames = {"BUSINESS DETAILS", "MAP LOCATION", "REVIEWS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get business id clicked
        Intent intent = getIntent();
        String value = intent.getStringExtra("businessId");

        binding = ActivityDetailTabsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DetailsAdapter detailsAdapter = new DetailsAdapter(getSupportFragmentManager(), getLifecycle(), value);
        ViewPager2 viewPager2 = binding.viewPager;
        viewPager2.setAdapter(detailsAdapter);

        TabLayout tabs = binding.tabLayout;
        new TabLayoutMediator(tabs, viewPager2,
                (tab, position) -> tab.setText(tabNames[position])
        ).attach();
    }
}