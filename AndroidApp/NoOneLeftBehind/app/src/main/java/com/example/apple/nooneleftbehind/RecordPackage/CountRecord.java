package com.example.apple.nooneleftbehind.RecordPackage;

import android.util.Log;

import com.example.apple.nooneleftbehind.PersonPackage.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.apple.nooneleftbehind.ControlsPackage.StartCountConfirmActivity.newRecord;

/**
 * Created by apple on 4/8/17.
 * Contains details of a record
 */

public class CountRecord {

    private String dateAndTimeString;
    public int count;
    private ArrayList<Person> peoplePresent;

    public CountRecord() {
    }

    public CountRecord(String hey) {
        Date dateAndTime = new Date();
        dateAndTimeString = dateAndTime.toString();
        count = 0;
        peoplePresent = new ArrayList<>();
    }

    public String getDateAndTimeString() {
        return dateAndTimeString;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<Person> getPeoplePresent() {
        return peoplePresent;
    }

    public Person getPersonFromId(String id) {
        for (Person person : peoplePresent) {
            if (person.getId().equals(id)) {
                return person;
            }
        }
        return null;
    }

    public void addPerson(Person personToAdd) {
        peoplePresent.add(personToAdd);
        count++;
    }

    public String getPeoplePresentString() {
        String peoplePresentString = "";
        if((peoplePresent == null) || (peoplePresent.size() == 0))
            return "No known people present";
        int i = 0;
        for (Person p : peoplePresent) {
            i++;
            if(i == peoplePresent.size()) {
                peoplePresentString += p.getName();
            } else {
                peoplePresentString += p.getName() + ", ";
            }
        }
        return peoplePresentString;
    }

    public void deletePerson(Person personToDelete) {
        for (Person p : peoplePresent) {
            if (p.getId().equals(personToDelete.getId())) {
                peoplePresent.remove(personToDelete);
                count--;
                break;
            }
        }
    }

    public void addPersonToRecord(final String subjectId) {

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("People");
        Query query = mDatabaseRef.orderByKey().equalTo(subjectId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot personSnapshot : dataSnapshot.getChildren()) {
                    Person person = personSnapshot.getValue(Person.class);
                    String key = personSnapshot.getKey();
                    Log.d("addPersonToRecordKey", key);
                    if(key.equals(subjectId)) {
                        newRecord.addPerson(person);
                        Log.d("personAdded", "insideNewRecord");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
