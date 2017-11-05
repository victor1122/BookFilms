package com.darkwinter.bookfilms;

import java.io.Serializable;

/**
 * Created by hieum on 11/1/2017.
 */

public class User implements Serializable {
    private String name;
    private String email;
    private String DOB;
    private String phone;
    private String zipcode;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public User(String name, String email, String DOB, String phone, String zipcode) {
        this.name = name;
        this.email = email;
        this.DOB = DOB;
        this.phone = phone;
        this.zipcode = zipcode;
    }
}
