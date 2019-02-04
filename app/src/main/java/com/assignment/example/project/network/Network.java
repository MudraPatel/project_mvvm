package com.assignment.example.project.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Whiteseed on 12/13/2017.
 */

public class Network {


    public static String serverError = "SERVER ERROR";
    public static String decline = "Declined";
    public static String accept = "Accepted";
    public static String not = "Not";


    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void getListData(Context context, final DataCallback dataCallback) {

        String url = "https://randomuser.me/api/?results=10";
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE: NETWOrK :", response + "ACtivity");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    dataCallback.onSuccess(jsonObject.getString("results"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataCallback.onSuccess(serverError);
            }
        });

        mRequestQueue.add(stringRequest);
    }

}

