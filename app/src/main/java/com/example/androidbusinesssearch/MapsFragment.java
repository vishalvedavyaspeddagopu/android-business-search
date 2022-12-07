package com.example.androidbusinesssearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {
    private Double longitude;
    private Double latitude;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng location = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(location).title("Marker"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));

//            //Map is now ready. Add marker when you receive a response from the detail query
//            String url = "https://search-business-vive97.wl.r.appspot.com/api/getBusinessDetail?id=" + businessId;
//
//            Log.d("detail-url-maps", url);
//            //Remove network call to improve map marker updates
//            RequestQueue requestQueue = VolleyRequestQueue.getInstance(getActivity().getApplicationContext()).getRequestQueue(getActivity().getApplicationContext());
//
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                    response -> {
//                        try {
//                            JSONObject obj = response.getJSONObject("coordinates");
//                            //add a marker here
//                            LatLng location = new LatLng(obj.getDouble("latitude"), obj.getDouble("longitude"));
//                            googleMap.addMarker(new MarkerOptions().position(location).title("Marker"));
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        Log.d("info", response.toString());
//                    }, error -> Log.e("error", "Network call failed"));
//
//            requestQueue.add(jsonObjectRequest);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble("latitude");
            longitude = getArguments().getDouble("longitude");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public static MapsFragment newInstance(Double longitude, Double latitude) {
        MapsFragment f = new MapsFragment();

        // Supply business id as an argument.
        Bundle args = new Bundle();
        args.putDouble("longitude", longitude);
        args.putDouble("latitude", latitude);
        f.setArguments(args);

        return f;
    }
}