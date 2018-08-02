package com.example.lenovo.notebook.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.notebook.HomeActivity;

import org.json.JSONObject;


public class MyVolley {
    private Context context;
   RequestQueue mQueue = Volley.newRequestQueue(context.getApplicationContext());
    public int post(){
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://m.weather.com.cn/data/101010100.html", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    Log.d("TAG", response.toString());
                    }
                }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.e("TAG", error.getMessage(), error);
           }

        });
       mQueue.add(jsonObjectRequest);
        return 1;
    }
}
