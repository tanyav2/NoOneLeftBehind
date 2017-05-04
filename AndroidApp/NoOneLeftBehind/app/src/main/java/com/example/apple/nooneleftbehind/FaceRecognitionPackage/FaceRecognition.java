package com.example.apple.nooneleftbehind.FaceRecognitionPackage;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.apple.nooneleftbehind.PersonPackage.Person;
import com.example.apple.nooneleftbehind.VolleyRequestPackage.NetworkManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.apple.nooneleftbehind.CameraPackage.FaceTrackingPackage.FaceTrackerActivity.anonymousPeople;
import static com.example.apple.nooneleftbehind.ControlsPackage.StartCountConfirmActivity.newRecord;

/**
 * Created by apple on 4/29/17.
 */

public class FaceRecognition {

    private String mainUrl;
    private String appId;
    private String appKey;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private static String returnVal;
    private String subject_id;


    public FaceRecognition(Context context) {
        mainUrl = "https://api.kairos.com/";
        appId = "3e12830b";
        appKey = "156e06fd782a3304f085f8c9db3ea5f0";
        mContext = context.getApplicationContext();
        mRequestQueue = NetworkManager.getInstance(mContext).getRequestQueue();
        returnVal = null;
    }

    /**
     * View gallery, mostly for debugging purposes
     */
    public void viewGallery() {
        String urlViewGallery = mainUrl + "gallery/view";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlViewGallery, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("app_id", appId);
                params.put("app_key", appKey);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject params = new JSONObject();
                try {
                    params.put("gallery_name", "FirstGallery");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        mRequestQueue.add(stringRequest);
    }


    /**
     * Enroll person in gallery
     */
    public void enrollPersonInGallery(Uri pictureUrl, String subjectId) {

        String enrollRequestUrl = "enroll";

        Map<String, String> params = new HashMap<>();
        params.put("image", pictureUrl.toString());
        params.put("subject_id", subjectId);
        params.put("gallery_name", "FirstGallery");

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mainUrl + enrollRequestUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Volley", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("app_id", appId);
                params.put("app_key", appKey);
                return params;
            }


            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        mRequestQueue.add(jsonObjectRequest);
    }


    /**
     * Recognize image that was detected and if recognized,
     * add person to record, else add to list of anonymous
     * people to be dealt by the user at the end of the counting
     * @param uri
     * @return
     */
    public String recognizeImage(final Uri uri) {
        Log.d("Recognize", "you made it");
        String enrollRequestUrl = "recognize";

        Map<String, String> params = new HashMap<>();
        params.put("image", uri.toString());
        params.put("gallery_name", "FirstGallery");

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, mainUrl + enrollRequestUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject firstItr = (JSONObject) response.getJSONArray("images").get(0);
                    Log.d("firstItr", firstItr.toString());
                    JSONObject secondItr = firstItr.getJSONObject("transaction");
                    Log.d("secondItr", secondItr.toString());
                    String thirdItr = secondItr.getString("status");
                    Log.d("thirdItr", thirdItr);
                    if(firstItr.getJSONObject("transaction").getString("status").equals("success")) {
                        Log.d("Recognize", "img recognized");
                        subject_id = secondItr.getString("subject_id");
                        Log.d("PictureRecognized", subject_id);
                        returnVal = subject_id;
                        Log.d("recognize: ", returnVal);
                        newRecord.addPersonToRecord(returnVal);

                    } else {
                        Log.d("Recognize", "Unable to recognize");
                        anonymousPeople.add(uri);
                    }
                } catch (JSONException e) {
                    Log.d("TheurlIsent", response.toString());

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("app_id", appId);
                params.put("app_key", appKey);
                return params;
            }


            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        mRequestQueue.add(jsonObjectRequest);
        return returnVal;
    }

}
