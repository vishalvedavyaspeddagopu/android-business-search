package com.example.androidbusinesssearch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailsAdapter extends FragmentStateAdapter {
    private String businessId;
    private JSONObject details;

    public DetailsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String businessId, JSONObject details) {
        super(fragmentManager, lifecycle);
        this.businessId = businessId;
        this.details = details;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0: return DetailsFragment.newInstance(businessId, details);
            case 1:
                try {
                    return MapsFragment.newInstance(details.getJSONObject("coordinates").getDouble("longitude"), details.getJSONObject("coordinates").getDouble("latitude"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case 2: return ReviewsFragment.newInstance(businessId);
            default: return (businessId == null ? new DetailsFragment() : DetailsFragment.newInstance(businessId, details));
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}