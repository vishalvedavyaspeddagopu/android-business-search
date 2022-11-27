package com.example.androidbusinesssearch;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {
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
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setBusinessDetails(businessId);
    }

    public static DetailsFragment newInstance(String businessId) {
        DetailsFragment f = new DetailsFragment();

        // Supply business id as an argument.
        Bundle args = new Bundle();
        args.putString("businessId", businessId);
        f.setArguments(args);

        return f;
    }

    public void setBusinessDetails(String businessId) {
        String url = "https://search-business-vive97.wl.r.appspot.com/api/getBusinessDetail?id=" + businessId;

        Log.d("details-url", url);

        RequestQueue requestQueue = VolleyRequestQueue.getInstance(getActivity().getApplicationContext()).getRequestQueue(getActivity().getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        //Set address
                        if(response.optJSONObject("location") != null && response.optJSONObject("location").optJSONArray("display_address") != null)
                        {
                            TextView addressView = (TextView)getView().findViewById(R.id.addressValue);
                            addressView.setText(response.getJSONObject("location").getJSONArray("display_address").join(" ").replace("\"", ""));
                        }

                        //Set phone number
                        TextView phoneNumberView = (TextView)getView().findViewById(R.id.phoneNumberValue);
                        phoneNumberView.setText(response.optString("display_phone"));

                        //Set open close status
                        JSONArray hours = response.optJSONArray("hours");
                        if(hours != null && hours.length() >= 1)
                        {
                            JSONObject hoursObject = hours.getJSONObject(0);
                            if(hoursObject.optBoolean("is_open_now"))
                            {
                                TextView statusView = (TextView) getView().findViewById(R.id.statusValue);
                                statusView.setText("Open Now");
                                statusView.setTextColor(Color.GREEN);
                            }
                            else
                            {
                                TextView statusView = (TextView) getView().findViewById(R.id.statusValue);
                                statusView.setText("Closed");
                                statusView.setTextColor(Color.RED);
                            }
                        }

                        //Set category
                        JSONArray categories = response.optJSONArray("categories");
                        if(categories != null)
                        {
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
                        for (int i = 0; i < Math.min(2, photos.length()); i++) {
                            String imgUrl = photos.getString(i);
                            ImageView imageView = (ImageView) getView().findViewById(getResources().getIdentifier("image_" + i, "id", getActivity().getPackageName()));
                            Picasso.get().load(imgUrl).into(imageView);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("info", response.toString());
                }, error -> Log.e("error", "Network call failed"));

        requestQueue.add(jsonObjectRequest);
    }
}