package com.example.apple.nooneleftbehind.ControlsPackage;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.apple.nooneleftbehind.RecordPackage.SingleRecordActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apple on 4/23/17.
 */

public class CarControl extends IntentService {

    private CarCommands carCommands;

    public CarControl() {
        super("My_Car's_Controls");

    }

//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//
//        //carCommands = new CarCommands(getApplicationContext());
//        Log.d("beforeSynchro", "beforeIt");
//
//        synchronized (this) {
//            int numObstaclesFaced = 0;
//            int count = 0;
//
//            while (numObstaclesFaced < 4) {
//                if(count == 1000000)
//                    stopCarForGood();
//                count++;
//            }
//            stopCarForGood();
//        }
//    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        carCommands = new CarCommands(getApplicationContext());
       // statusArr = new ArrayList<>(2);

        Log.d("beforeSynchro", "beforeIt");

        synchronized (this) {

            Log.d("insideSynchro", "apple");
            int numObstaclesFaced = 0;
            int obstacleFront;
            //carCommands.stopCar();
            // carCommands.startCar(4);
            int count = 0;
            Log.d("countOutside", Integer.toString(count));

            while (numObstaclesFaced < 4) {

                if(count == 100000) {
                    Log.d("count:", Integer.toString(count));
                    stopCarForGood();
                }

                count++;

//                while (statusArr.size() < 2) {
//                    statusArr = carCommands.getStatusNC();
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                statusArr.clear();
//                while (statusArr.size() < 2) {
//                    statusArr = carCommands.getStatus();
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                obstacleFront = statusArr.get(1);
//
//                if (obstacleFront == 1) {
//                    carCommands.setHeading(-105);
//                    carCommands.startCar(4);
//                    //carCommands.getStatus();
//                    numObstaclesFaced++;
//                }
            }
            //stopCarForGood();
        }
    }


    private void stopCarForGood() {
    //    carCommands.stopCar();
        Log.d("stopCarForGood", "wasCalled");
        stopFaceRecognition();
        displayRecord();
    }

    /**
     * Sends a broadcast to FaceTrackerActivity to destroy it
     */
    private void stopFaceRecognition() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.
                getInstance(CarControl.this);
        localBroadcastManager.sendBroadcast(new Intent("facerecognition.close"));
    }

    /**
     * Starts the SingleRecordActivity to display results and to ask
     * user if they want to add the anonymous people to the database
     */
    private void displayRecord() {

        Intent singleRecordIntent = new Intent(getBaseContext(), SingleRecordActivity.class);
        singleRecordIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        singleRecordIntent.putExtra("from", "carControls");
        getApplication().startActivity(singleRecordIntent);

    }

}
