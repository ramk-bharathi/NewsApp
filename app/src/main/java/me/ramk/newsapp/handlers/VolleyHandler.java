package me.ramk.newsapp.handlers;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ram K Bharathi on 2/26/2018.
 */

public class VolleyHandler<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> responseClass;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Response.Listener<T> listener;

    public VolleyHandler(int requestMethod, String url, Class<T> responseClass, Map<String, String> params, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(requestMethod, url, errorListener);
        this.responseClass = responseClass;
        this.headers = headers;
        this.listener = listener;
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>(this.params);
        Log.i(VolleyHandler.class.getName(), "getParams: " + params.toString());
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>(this.headers);
        Log.i(VolleyHandler.class.getName(), "getHeaders: " + headers.toString());
        return headers;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, responseClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
