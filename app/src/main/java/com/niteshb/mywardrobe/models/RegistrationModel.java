package com.niteshb.mywardrobe.models;

import java.util.Date;

public class RegistrationModel {
    private String fname;
    private String lName;
    private Date dob;
    private String email;
    private String uid;

    public RegistrationModel(String fname, String lName, Date dob, String email, String uid) {
        this.fname = fname;
        this.lName = lName;
        this.dob = dob;
        this.email = email;
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public String getlName() {
        return lName;
    }

    public Date getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }
}
