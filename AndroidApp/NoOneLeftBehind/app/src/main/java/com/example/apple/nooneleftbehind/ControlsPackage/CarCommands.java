package com.example.apple.nooneleftbehind.ControlsPackage;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.apple.nooneleftbehind.VolleyRequestPackage.NetworkManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by apple on 4/11/17.
 * These are commands that will be given to the car
 * to command it to move in a certain way
 */
public class CarCommands {

    private String mainUrl;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private int returnVal;
    ArrayList<Integer> retArrList;

    public CarCommands(Context context) {
        mainUrl = "http://192.168.43.33/";
        mContext = context.getApplicationContext();
        mRequestQueue = NetworkManager.getInstance(mContext).getRequestQueue();
        retArrList = new ArrayList<>(2);
    }


    /**
     * The speed of the vehicle is defined as a value from -8 to 8.
     * Positive or negative denotes going forward or backward.
     * @return value of current speed
     */
    public int getSpeed() {
        String speedUrl = mainUrl + "speed";
        final String speedTAG = "CarCommands-GetSpeed";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, speedUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("ok")) {
                        Log.d(speedTAG, response.getString("speed"));
                        returnVal = response.getInt("speed");
                    }
                } catch (JSONException e) {
                    Log.d(speedTAG, "failed to set speed");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(speedTAG, "failed to set speed");
            }
        });

        mRequestQueue.add(jsonObjectRequest);
        return returnVal;
    }


    /**
     * Sets speed to specified value.
     * Speed can range between 0 to 8.
     */
    public void setSpeed(final int speed) {

        String speedUrl = mainUrl + "speed/set-absolute?value=" + Integer.toString(speed);
        final String speedTAG = "CarCommands-SetSpeed";


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, speedUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("ok")) {
                        Log.d(speedTAG, response.getString("speed"));
                        if(response.getInt("speed") == -1) {
                            setSpeed(speed);
                        }
                    }
                } catch (JSONException e) {
                    Log.d(speedTAG, "failed to set speed");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(speedTAG, "failed to set speed");
            }
        });

        mRequestQueue.add(jsonObjectRequest);

    }


    /**
     * Sets the heading of the car relative to previous value
     * Number can be between -180 to +180
     * @param heading
     */
    public void setHeading(int heading) {

        String headingUrl = mainUrl + "heading/set-relative?value=" + Integer.toString(heading);
        final String headingTAG = "CarCommands-SetHeading";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, headingUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("ok")) {
                        Log.d(headingTAG, "Set heading successfully");
                    }
                } catch (JSONException e) {
                    Log.d(headingTAG, "failed to set heading");
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(headingTAG, "failed to set heading");
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }

    public void stopCar() {
        setSpeed(0);
    }

    public void startCar(int speed) {
        setSpeed(speed);
    }

    /**
     * Gets an ArrayList of the steps traversed and obstacle
     * @return arraylist
     */
    public int getStatus() {

        String statusUrl = mainUrl + "status";
        final String statusTAG = "CarCommands-status";


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, statusUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //              Log.d(TAG, "Response: " + response.toString());
                try {
                    if(response.getString("status").equals("ok")) {
                        Log.d(statusTAG, response.getString("sys-status"));
                        Log.d(statusTAG, response.getString("steps-traversed"));
                       // retArrList.add(response.getInt("steps-traversed"));
                        Log.d(statusTAG, response.getString("obstacle"));
                        returnVal = response.getInt("obstacle");
                        if(returnVal == -1) {
                            getStatus();
                        }
                        Log.d(statusTAG+"obstacle", Integer.toString(response.getInt("obstacle")));
                    }
                } catch (JSONException e) {
                    Log.d(statusTAG, "Couldn't get status");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(statusTAG, "Couldn't get status");
            }
        });

        mRequestQueue.add(jsonObjectRequest);
        return returnVal;
    }

    /**
     * Same as getStatus but does not clear the steps traversed every time status
     * is called
     * @return arraylist containing cumulative steps traversed and obstacles present
     */
    public ArrayList getStatusNC() {

        String statusUrl = mainUrl + "status-nc";
        final String statusTAG = "CarCommands-status";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, statusUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //              Log.d(TAG, "Response: " + response.toString());
                try {
                    if(response.getString("status").equals("ok")) {
                        Log.d(statusTAG, response.getString("sys-status"));
                        Log.d(statusTAG, response.getString("steps-traversed"));
                        retArrList.add(response.getInt("steps-traversed"));
                        Log.d(statusTAG, response.getString("obstacle"));
                        retArrList.add(response.getInt("obstacle"));
                    }
                } catch (JSONException e) {
                    Log.d(statusTAG, "Couldn't get status");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(statusTAG, "Couldn't get status");
            }
        });

        mRequestQueue.add(jsonObjectRequest);
        return retArrList;
    }

}
