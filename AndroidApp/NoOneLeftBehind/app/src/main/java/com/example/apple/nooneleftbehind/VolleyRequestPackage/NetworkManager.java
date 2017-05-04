package com.example.apple.nooneleftbehind.VolleyRequestPackage;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by apple on 4/29/17.
 * "Singleton" class for multiple calls to the
 * network
 */

public class NetworkManager {

    private static NetworkManager mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private NetworkManager(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new NetworkManager(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

}
