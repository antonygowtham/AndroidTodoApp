package com.example.todolist.api;
import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ApiRequestHelper {
    private static final String TAG = "ApiRequestHelper";
    private final Context context;

    public ApiRequestHelper(Context context) {
        this.context = context;
    }

    public void checkUserAuthorization(final VolleyCallback callback) {
        String url = "http://localhost:3000/isAuthorized";
        makeRequest(url, callback);
    }

    public void fetchUserData(final VolleyCallback callback) {
        String url = "http://localhost:3000/userData";
        makeRequest(url, callback);
    }

    private void makeRequest(String url, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });

        queue.add(stringRequest);
    }

    public interface VolleyCallback {
        void onSuccess(String response);
        void onError(String errorMessage);
    }
}

