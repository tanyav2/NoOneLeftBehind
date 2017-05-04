package com.example.apple.nooneleftbehind.RecordPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.nooneleftbehind.MainActivity;
import com.example.apple.nooneleftbehind.PersonPackage.EditPersonDetailsActivity;
import com.example.apple.nooneleftbehind.PersonPackage.Person;
import com.example.apple.nooneleftbehind.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import static com.example.apple.nooneleftbehind.ControlsPackage.StartCountConfirmActivity.newRecord;
import static com.example.apple.nooneleftbehind.CameraPackage.FaceTrackingPackage.FaceTrackerActivity.anonymousPeople;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Displays a record if not authenticated
 * If authenticated redirects to activity EditSingleRecordActivity
 * which allows editing of record details
 */
public class SingleRecordActivity extends AppCompatActivity {

    @BindView(R.id.textView5) TextView dateTimeTextView;
    @BindView(R.id.textView6) TextView countTextView;
    @BindView(R.id.textView8) TextView knownPeopleTextView;
    @BindView(R.id.textView9) TextView anonCountTextView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_record);
        ButterKnife.bind(this);
        Log.d("from", getIntent().getStringExtra("from"));
        fillOutActivity();
        addRecordToFB();
    }

    private void addRecordToFB() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Records");
        newRecord.count += anonymousPeople.size();
        mDatabase.push().setValue(newRecord);
    }

    private void fillOutActivity() {
        Log.d("dateTimeTextView", newRecord.getDateAndTimeString().toString());
        dateTimeTextView.setText(newRecord.getDateAndTimeString().toString());
        countTextView.setText(Integer.toString(newRecord.getCount()));
        anonCountTextView.setText(Integer.toString(anonymousPeople.size()));
        String personTextViewString = "";
        if(newRecord.getPeoplePresent().size() != 0) {
            for (Person person : newRecord.getPeoplePresent()) {
                personTextViewString += (person.getName() + "\n");
            }
        } else {
            personTextViewString = "No known people present";
        }

        knownPeopleTextView.setText(personTextViewString);
    }


    public void addAnonPeopleToDB(View view) {
        if(anonymousPeople.size() > 0) {
            Intent intent = new Intent(getApplicationContext(), EditPersonDetailsActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No anonymous people present", Toast.LENGTH_SHORT).show();
            goToMainActivity();
        }
    }


    public void discardAllPeople(View view) {

        Toast.makeText(this, "Your record has been saved", Toast.LENGTH_SHORT).show();

        if(anonymousPeople.size() > 0) {
            Intent intent = new Intent(getApplicationContext(), EditPersonDetailsActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No anonymous people present", Toast.LENGTH_SHORT).show();
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}
