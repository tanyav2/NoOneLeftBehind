package com.example.apple.nooneleftbehind.PersonPackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apple.nooneleftbehind.FaceRecognitionPackage.FaceRecognition;
import com.example.apple.nooneleftbehind.MainActivity;
import com.example.apple.nooneleftbehind.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.apple.nooneleftbehind.CameraPackage.FaceTrackingPackage.FaceTrackerActivity.anonymousPeople;

/**
 * Allows editing of person details, addition of person, and deletion
 * of person from the Firebase database
 */
public class EditPersonDetailsActivity extends AppCompatActivity {

    @BindView(R.id.editText2) EditText mFirstNameTextView;
    @BindView(R.id.editText3) EditText mLastNameTextView;
    @BindView(R.id.imageView2) ImageView mImageView;

    private int numAnonPeople;
    private int indexAnon;

    private DatabaseReference mDatabaseRef;

    private FaceRecognition faceRecognition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person_details);
        ButterKnife.bind(this);

        if(anonymousPeople.size() == 0) {
            Toast.makeText(this, "No people to add", Toast.LENGTH_SHORT).show();
            goToMainActivity();
        }

        Picasso.with(getApplicationContext()).load(anonymousPeople.get(0)).into(mImageView);

        numAnonPeople = anonymousPeople.size();
        indexAnon = 0;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("People");
        faceRecognition = new FaceRecognition(getApplicationContext());
    }

    public void saveAndNext(View view) {

        String subjectIdKey = pushPersonToFirebase();
        if(subjectIdKey.equals(""))
            return;

        Log.d("subjectIdKey", subjectIdKey);
        pushPersonToGallery(subjectIdKey);

        numAnonPeople--;
        indexAnon++;

        if(numAnonPeople > 0) {
            clearFieldsAndLoadNext();
        } else {
            Toast.makeText(this, "People saved!", Toast.LENGTH_SHORT).show();
            goToMainActivity();
        }

    }

    private String pushPersonToFirebase() {

        Person newPerson = new Person();
        String firstName = mFirstNameTextView.getText().toString();
        String lastName = mLastNameTextView.getText().toString();
        String key;

        if(!firstName.equals("") && !lastName.equals("")) {
            key = mDatabaseRef.push().getKey();
            newPerson.setFirstName(firstName);
            newPerson.setLastName(lastName);
            newPerson.setImgPath(anonymousPeople.get(indexAnon).toString());
            mDatabaseRef.child(key).setValue(newPerson);
            return key;
        } else {
            Toast.makeText(this, "You didn't enter all fields", Toast.LENGTH_SHORT).show();
            return "";
        }

    }

    private void pushPersonToGallery(String subjectId) {
        faceRecognition.enrollPersonInGallery(anonymousPeople.get(indexAnon), subjectId);
    }

    private void clearFieldsAndLoadNext() {
        Picasso.with(getApplicationContext()).load(anonymousPeople.get(indexAnon)).into(mImageView);
        mFirstNameTextView.setText("");
        mLastNameTextView.setText("");
    }

    public void discardAndNext(View view) {
        //finish this activity or clear this activity
        if(numAnonPeople > 0) {
            clearFieldsAndLoadNext();
            indexAnon++;
            numAnonPeople--;
        } else {
            Toast.makeText(this, "Person discarded", Toast.LENGTH_SHORT).show();
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}
