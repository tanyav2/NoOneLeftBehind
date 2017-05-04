package com.example.apple.nooneleftbehind.PersonPackage;

import android.net.Uri;

import com.example.apple.nooneleftbehind.RecordPackage.CountRecord;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by apple on 4/8/17.
 * Details of each person
 */

public class Person {

    private String id;
    private String firstName;
    private String lastName;
    private String imgPath;

    public Person() {

    }

    public Person(String UID, String firstName, String lastName) {
        this.id = UID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}
