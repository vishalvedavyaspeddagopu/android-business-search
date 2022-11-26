package com.example.androidbusinesssearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.custom_tool_bar));

        //Categories dropdown
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

        String url = "https://search-business-vive97.wl.r.appspot.com/api/getNearbyBusinesses?term=" + "Chicken" +
                "&radius=" + String.valueOf((int) Math.ceil(1609.34 * Double.parseDouble("10"))) + "&categories=all&latitude=34.0030&longitude=-118.2863";

        Log.d("url", url);

        RequestQueue requestQueue = VolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue(this.getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("businesses");
                            List<BusinessRow> results = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                results.add(new BusinessRow(obj.getString("id"),
                                        obj.getString("name"),
                                        obj.getDouble("rating"),
                                        obj.getDouble("distance"),
                                        obj.getString("image_url")));
                                ;
                                // Do you fancy stuff
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

//    private boolean validateInputs(String keyword) {
//        if (etFirstName.length() == 0) {
//            etFirstName.setError("This field is required");
//            return false;
//        }
//
//        if (etLastName.length() == 0) {
//            etLastName.setError("This field is required");
//            return false;
//        }
//
//        if (etEmail.length() == 0) {
//            etEmail.setError("Email is required");
//            return false;
//        }
//
//        if (etPassword.length() == 0) {
//            etPassword.setError("Password is required");
//            return false;
//        } else if (etPassword.length() < 8) {
//            etPassword.setError("Password must be minimum 8 characters");
//            return false;
//        }
//
//        // after all validation return true.
//        return true;
//    }
}