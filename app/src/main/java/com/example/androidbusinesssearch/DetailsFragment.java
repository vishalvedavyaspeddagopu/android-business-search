package com.example.androidbusinesssearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment implements ReservationForm.ReservationFormInterface {
    private String businessId;
    private String businessName;
    private JSONObject response;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            businessId = getArguments().getString("businessId");
            try {
                response = new JSONObject(getArguments().getString("details"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        Button button = (Button) view.findViewById(R.id.reserveNow);
        button.setOnClickListener(v -> showModal());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setBusinessDetails(response);
    }

    public static DetailsFragment newInstance(String businessId, JSONObject details) {
        DetailsFragment f = new DetailsFragment();

        // Supply business id as an argument.
        Bundle args = new Bundle();
        args.putString("businessId", businessId);
        args.putString("details", details.toString());
        f.setArguments(args);

        return f;
    }

    public void setBusinessDetails(JSONObject response) {
        try {
            //Set business name
            businessName = response.getString("name");

            //Set address
            if (response.optJSONObject("location") != null && response.optJSONObject("location").optJSONArray("display_address") != null) {
                TextView addressView = (TextView) getView().findViewById(R.id.addressValue);
                addressView.setText(response.getJSONObject("location").getJSONArray("display_address").join(" ").replace("\"", ""));
            }
            else {
                TextView addressView = (TextView) getView().findViewById(R.id.addressValue);
                addressView.setText("N/A");
            }

            //Set phone number
            TextView phoneNumberView = (TextView) getView().findViewById(R.id.phoneNumberValue);
            phoneNumberView.setText(response.optString("display_phone"));
            if(response.optString("display_phone").equals(""))
                phoneNumberView.setText("N/A");

            //Set open close status
            JSONArray hours = response.optJSONArray("hours");
            if (hours != null && hours.length() >= 1) {
                JSONObject hoursObject = hours.getJSONObject(0);
                if (hoursObject.optBoolean("is_open_now")) {
                    TextView statusView = (TextView) getView().findViewById(R.id.statusValue);
                    statusView.setText("Open Now");
                    statusView.setTextColor(Color.GREEN);
                } else {
                    TextView statusView = (TextView) getView().findViewById(R.id.statusValue);
                    statusView.setText("Closed");
                    statusView.setTextColor(Color.RED);
                }
            }

            //Set category
            JSONArray categories = response.optJSONArray("categories");
            if (categories != null) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < categories.length(); i++) {
                    JSONObject obj = categories.getJSONObject(i);
                    list.add(obj.getString("title"));
                }
                TextView categoryView = (TextView) getView().findViewById(R.id.categoryValue);
                categoryView.setText(String.join(" | ", list));
            }

            //Set price range
            TextView priceView = (TextView) getView().findViewById(R.id.priceRangeValue);
            priceView.setText(response.optString("price"));

            //Set YELP link
            TextView yelpLinkView = (TextView) getView().findViewById(R.id.yelpLinkValue);
            yelpLinkView.setText(Html.fromHtml("<a href=\"" + response.optString("url") + "\">Business Link</a>"));
            yelpLinkView.setMovementMethod(LinkMovementMethod.getInstance());

            //Set Image Carousel
            JSONArray photos = response.getJSONArray("photos");
            for (int i = 0; i < Math.min(3, photos.length()); i++) {
                String imgUrl = photos.getString(i);
                ImageView imageView = (ImageView) getView().findViewById(getResources().getIdentifier("image_" + i, "id", getActivity().getPackageName()));
                Picasso.get().load(imgUrl).into(imageView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("info", response.toString());
    }

//    public void setBusinessDetails(String businessId) {
//        String url = "https://search-business-vive97.wl.r.appspot.com/api/getBusinessDetail?id=" + businessId;
//
//        Log.d("details-url", url);
//
//        RequestQueue requestQueue = VolleyRequestQueue.getInstance(getActivity().getApplicationContext()).getRequestQueue(getActivity().getApplicationContext());
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                response -> {
//                    try {
//                        //Set business name
//                        businessName = response.getString("name");
//
//                        //Set address
//                        if (response.optJSONObject("location") != null && response.optJSONObject("location").optJSONArray("display_address") != null) {
//                            TextView addressView = (TextView) getView().findViewById(R.id.addressValue);
//                            addressView.setText(response.getJSONObject("location").getJSONArray("display_address").join(" ").replace("\"", ""));
//                        }
//
//                        //Set phone number
//                        TextView phoneNumberView = (TextView) getView().findViewById(R.id.phoneNumberValue);
//                        phoneNumberView.setText(response.optString("display_phone"));
//
//                        //Set open close status
//                        JSONArray hours = response.optJSONArray("hours");
//                        if (hours != null && hours.length() >= 1) {
//                            JSONObject hoursObject = hours.getJSONObject(0);
//                            if (hoursObject.optBoolean("is_open_now")) {
//                                TextView statusView = (TextView) getView().findViewById(R.id.statusValue);
//                                statusView.setText("Open Now");
//                                statusView.setTextColor(Color.GREEN);
//                            } else {
//                                TextView statusView = (TextView) getView().findViewById(R.id.statusValue);
//                                statusView.setText("Closed");
//                                statusView.setTextColor(Color.RED);
//                            }
//                        }
//
//                        //Set category
//                        JSONArray categories = response.optJSONArray("categories");
//                        if (categories != null) {
//                            List<String> list = new ArrayList<>();
//                            for (int i = 0; i < categories.length(); i++) {
//                                JSONObject obj = categories.getJSONObject(i);
//                                list.add(obj.getString("title"));
//                            }
//                            TextView categoryView = (TextView) getView().findViewById(R.id.categoryValue);
//                            categoryView.setText(String.join(" | ", list));
//                        }
//
//                        //Set price range
//                        TextView priceView = (TextView) getView().findViewById(R.id.priceRangeValue);
//                        priceView.setText(response.optString("price"));
//
//                        //Set YELP link
//                        TextView yelpLinkView = (TextView) getView().findViewById(R.id.yelpLinkValue);
//                        yelpLinkView.setText(Html.fromHtml("<a href=\"" + response.optString("url") + "\">Business Link</a>"));
//                        yelpLinkView.setMovementMethod(LinkMovementMethod.getInstance());
//
//                        //Set Image Carousel
//                        JSONArray photos = response.getJSONArray("photos");
//                        for (int i = 0; i < Math.min(2, photos.length()); i++) {
//                            String imgUrl = photos.getString(i);
//                            ImageView imageView = (ImageView) getView().findViewById(getResources().getIdentifier("image_" + i, "id", getActivity().getPackageName()));
//                            Picasso.get().load(imgUrl).into(imageView);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("info", response.toString());
//                }, error -> Log.e("error", "Network call failed"));
//
//        requestQueue.add(jsonObjectRequest);
//    }

    public void showModal() {
        ReservationForm reservationForm = ReservationForm.newInstance(businessName);
        reservationForm.show(getChildFragmentManager(), "Reservation dialog");
    }

    @Override
    public void sendFragmentData(String email, String date, String time) {
        Log.d("reservation-data", email + date + time);

        if (email == null || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity().getApplicationContext(), "InValid Email Address", Toast.LENGTH_LONG).show();
            return;
        }

        if (isInvalidTime(time)) {
            Toast.makeText(getActivity().getApplicationContext(), "Time should be between 10AM AND 5PM", Toast.LENGTH_LONG).show();
            return;
        }

        // Get current bookings
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("Bookings", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("bookings", "[]");
        List<Bookings> obj = gson.fromJson(json, ArrayList.class);

        System.out.println("Bookings size " + obj.size());

        // Add booking
        Bookings booking = new Bookings(businessId, businessName, date, time, email);
        obj.add(booking);

        // Save booking
        String bookingsJson = gson.toJson(obj);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("bookings", bookingsJson);
        editor.commit();

        Toast.makeText(getActivity().getApplicationContext(), "Reservation Booked", Toast.LENGTH_LONG).show();
    }

    private boolean isInvalidTime(String time) {
        try {
            String[] tmp = time.split(":");
            int hrs = Integer.parseInt(tmp[0]);
            int mins = Integer.parseInt(tmp[1]);

            if (hrs < 10)
                return true;

            return hrs >= 17 && mins > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}