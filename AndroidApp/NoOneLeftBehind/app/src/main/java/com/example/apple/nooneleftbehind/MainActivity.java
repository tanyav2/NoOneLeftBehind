package com.example.apple.nooneleftbehind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.apple.nooneleftbehind.ControlsPackage.StartCountConfirmActivity;
import com.example.apple.nooneleftbehind.PersonPackage.AllPeopleActivity;
import com.example.apple.nooneleftbehind.RecordPackage.AllRecordsActivity;
import com.example.apple.nooneleftbehind.VolleyRequestPackage.NetworkManager;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Home screen of the App
 * Has four buttons for the four main functions
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkManager.getInstance(getApplicationContext());
    }

    //Methods for on click of all 3 main buttons
    public void startCounting(View view) {
        Toast.makeText(this, "Start counting", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), StartCountConfirmActivity.class);
        startActivity(intent);
    }

    public void viewPreviousRecords(View view) {
        Toast.makeText(this, "Viewing prev records", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AllRecordsActivity.class);
        startActivity(intent);
    }

    public void viewPeople(View view) {
        Toast.makeText(this, "Viewing all people", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AllPeopleActivity.class);
        startActivity(intent);
    }

}
