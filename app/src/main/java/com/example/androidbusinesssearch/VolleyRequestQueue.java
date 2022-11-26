package com.example.androidbusinesssearch;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestQueue {
    private static VolleyRequestQueue object;

    private RequestQueue requestQueue;

    public RequestQueue getRequestQueue(Context context)
    {
        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    private VolleyRequestQueue(Context context)
    {
        requestQueue = getRequestQueue(context);
    }

    public static VolleyRequestQueue getInstance(Context context)
    {
        if(object == null)
        {
            object = new VolleyRequestQueue(context);
        }
        return object;
    }
}
