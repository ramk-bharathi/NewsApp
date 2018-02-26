package me.ramk.newsapp.helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Ram K Bharathi on 2/25/2018.
 */

public class VolleyHelper {
    private static VolleyHelper volleyHelper;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleyHelper(Context cntxt) {
        context = cntxt;

    }

    public static VolleyHelper getInstance(Context cntxt) {
        if(volleyHelper == null) {
            volleyHelper = new VolleyHelper(cntxt);
        }
        return volleyHelper;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
