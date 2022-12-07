package com.example.androidbusinesssearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;

    private static final Map<String, String> categoryMap = new HashMap<String, String>() {{
        put("Default", "all");
        put("Arts & Entertainment", "arts");
        put("Health & Medical", "health");
        put("Hotels & Travel", "hotelstravel");
        put("Food", "food");
        put("Professional Services", "professional");

    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.custom_tool_bar));

        ((TextView) findViewById(R.id.noResultsLabel)).setVisibility(View.INVISIBLE);

        requestQueue = VolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue(this.getApplicationContext());

        //Categories dropdown
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Set up checkbox
        EditText locationView = ((EditText) findViewById(R.id.location));
        CheckBox checkBox = ((CheckBox) findViewById(R.id.checkBox));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()) {
                    locationView.getText().clear();
                    locationView.setVisibility(View.INVISIBLE);
                }
                else {
                    locationView.setVisibility(View.VISIBLE);
                }
            }
        });

        //Get autocomplete suggestions
        AutoCompleteTextView autocomplete = (AutoCompleteTextView) findViewById(R.id.keyword);
        autocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchWord = s.toString();
                getAutocompleteSuggestions(searchWord);
            }
        });
        autocomplete.setThreshold(1);
    }

    private void setUpTable(List<BusinessRow> rows)
    {
        //Table
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TableAdapter tableAdapter = new TableAdapter(this, rows);
        recyclerView.setAdapter(tableAdapter);
        recyclerView.setHasFixedSize(true);
    }

    public void resetForm(View view) {
        ((EditText) findViewById(R.id.keyword)).getText().clear();
        ((EditText) findViewById(R.id.distance)).getText().clear();
        ((EditText) findViewById(R.id.location)).getText().clear();
        EditText locationView = ((EditText) findViewById(R.id.location));
        locationView.getText().clear();
        locationView.setVisibility(View.VISIBLE);

        CheckBox checkBox = ((CheckBox) findViewById(R.id.checkBox));
        if(checkBox.isChecked())
            checkBox.setChecked(false);
        Spinner spinner = ((Spinner) findViewById(R.id.spinner));
        spinner.setSelection(0);
        ((TextView) findViewById(R.id.noResultsLabel)).setVisibility(View.INVISIBLE);
        setUpTable(new ArrayList<>());
    }

    public void launchReservationsPage(View view)
    {
        Intent intent = new Intent(this, ReservationsPage.class);
        startActivity(intent);
    }

    public void getBusinessData(View view) {
        String keyword = ((EditText) findViewById(R.id.keyword)).getText().toString();
        String distance = ((EditText) findViewById(R.id.distance)).getText().toString();
        String location = ((EditText) findViewById(R.id.location)).getText().toString();
        Boolean autoDetect = ((CheckBox) findViewById(R.id.checkBox)).isChecked();
        String category = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
        Log.d("info", keyword);
        Log.d("distance", distance);
        Log.d("location", location);
        Log.d("autoDetect", String.valueOf(autoDetect));
        Log.d("category", category);

        //Add validation here
        if(!validateInputs(keyword, distance, category, autoDetect, location))
            return;

        requestQueue.add(autoDetect ? getIpInfoRequest(keyword, distance, category) : getGeoEncodingRequest(keyword, distance, category, location));
    }

    public String getURL(String keyword, String distance, String category, String latitude, String longitude) {
        Double distanceDoubleVal;
        try {
            distanceDoubleVal = Double.parseDouble(distance);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return "https://search-business-vive97.wl.r.appspot.com/api/getNearbyBusinesses?term=" + keyword +
                "&radius=" + String.valueOf((int) Math.ceil(1609.34 * distanceDoubleVal)) + "&categories=" + categoryMap.get(category) + "&latitude=" + latitude + "&longitude=" + longitude;
    }

    public JsonObjectRequest getBusinessSearchRequest(String url) {
        Log.d("url", url);

        return new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("businesses");
                            List<BusinessRow> results = new ArrayList<>();
                            for (int i = 0; i < Math.min(10, jsonArray.length()); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                results.add(new BusinessRow(obj.getString("id"),
                                        obj.getString("name"),
                                        obj.getDouble("rating"),
                                        obj.getDouble("distance"),
                                        obj.getString("image_url")));
                                ;
                            }
                            if(results.isEmpty())
                            {
                                ((TextView) findViewById(R.id.noResultsLabel)).setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                ((TextView) findViewById(R.id.noResultsLabel)).setVisibility(View.INVISIBLE);
                            }
                            setUpTable(results);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            setUpTable(new ArrayList<>());
                            ((TextView) findViewById(R.id.noResultsLabel)).setVisibility(View.VISIBLE);
                        }
                        Log.d("info", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Set error message
                Log.e("error", "Network call failed");
                error.printStackTrace();
                setUpTable(new ArrayList<>());
                ((TextView) findViewById(R.id.noResultsLabel)).setVisibility(View.VISIBLE);
            }
        });
    }

    public JsonObjectRequest getIpInfoRequest(String keyword, String distance, String category) {
        String ipInfoURL = "https://ipinfo.io/?token=33d28f55a75e60";

        return new JsonObjectRequest(Request.Method.GET, ipInfoURL, null,
                response -> {
                    try {
                        String[] locationObject = response.getString("loc").split(",");
                        requestQueue.add(getBusinessSearchRequest(getURL(keyword, distance, category, locationObject[0], locationObject[1])));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("info", response.toString());
                }, error -> {
                    //Set error message
                    Log.e("error", "Network call failed");
                });
    }

    public void getAutocompleteSuggestions(String searchKey) {
        String url = "https://search-business-vive97.wl.r.appspot.com/api/autocomplete?text=" + searchKey;

        requestQueue.add(new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray categories = response.getJSONArray("categories");
                        JSONArray terms = response.getJSONArray("terms");

                        List<String> suggestions = new ArrayList<>();
                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject obj = categories.getJSONObject(i);
                            suggestions.add(obj.getString("title"));
                        }
                        for (int i = 0; i < terms.length(); i++) {
                            JSONObject obj = terms.getJSONObject(i);
                            suggestions.add(obj.getString("text"));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (this, R.layout.dropdown_item, suggestions);

                        ((AutoCompleteTextView) findViewById(R.id.keyword)).setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("info", response.toString());
                }, error -> {
            //Set error message
            Log.e("error", "Network call failed");
        }));
    }

    public JsonObjectRequest getGeoEncodingRequest(String keyword, String distance, String category, String address) {
        String geoEncodingURL = "https://search-business-vive97.wl.r.appspot.com/api/geocoding?location=" + address;

        return new JsonObjectRequest(Request.Method.GET, geoEncodingURL, null,
                response -> {
                    try {
                        JSONObject locationObject = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        requestQueue.add(getBusinessSearchRequest(getURL(keyword, distance, category, locationObject.getString("lat"), locationObject.getString("lng"))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("info", response.toString());
                }, error -> {
                    //Set error message
                    Log.e("error", "Network call failed");
                });
    }

    private boolean validateInputs(String keyword, String distance, String category, Boolean autodetect, String location) {
        if (keyword == null || keyword.isEmpty()) {
            ((EditText) findViewById(R.id.keyword)).setError("This field is required");
            return false;
        }

        if (distance == null || distance.isEmpty()) {
            ((EditText) findViewById(R.id.distance)).setError("This field is required");
            return false;
        }

        if (category == null || category.isEmpty()) {
            return false;
        }

        if (autodetect) {
            return true;
        } else {
            if (location == null || location.isEmpty()) {
                ((EditText) findViewById(R.id.location)).setError("This field is required");
                return false;
            }
        }

        return true;
    }
}