package com.example.androidbusinesssearch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DetailsAdapter extends FragmentStateAdapter {
    private String businessId;

    public DetailsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String businessId) {
        super(fragmentManager, lifecycle);
        this.businessId = businessId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0: return DetailsFragment.newInstance(businessId);
            case 1: return MapsFragment.newInstance(businessId);
            case 2: return ReviewsFragment.newInstance(businessId);
            default: return (businessId == null ? new DetailsFragment() : DetailsFragment.newInstance(businessId));
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}