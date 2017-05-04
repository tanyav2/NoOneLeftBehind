package com.example.apple.nooneleftbehind.ControlsPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.apple.nooneleftbehind.CameraPackage.FaceTrackingPackage.FaceTrackerActivity;
import com.example.apple.nooneleftbehind.R;
import com.example.apple.nooneleftbehind.RecordPackage.CountRecord;

/**
 * Displays a page asking the user to make sure that the robot
 * is in a valid position and the camera is placed on the robot
 */
public class StartCountConfirmActivity extends AppCompatActivity {

    public static CountRecord newRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_count_confirm);
    }

    /**
     * Initializes new record onClick of Start! button
     * @param view - a button
     */
    public void startCount(View view) {
        Toast.makeText(this, "Started count", Toast.LENGTH_SHORT).show();

        //Need the ("") because an empty constructor is used by Firebase
        //So to use a custom constructor which doesnt require any parameters,
        //my custom constructor takes in a useless string as a parameter
        newRecord = new CountRecord("");
        startCarIntent();
        Intent intent = new Intent(getApplicationContext(), FaceTrackerActivity.class);
        startActivity(intent);
    }

    /**
     * Starts a background thread that is responsible for controlling
     * the car's movement using IntentService
     */
    private void startCarIntent() {
        Intent intent = new Intent(this, CarControl.class);
        startService(intent);
    }

}
