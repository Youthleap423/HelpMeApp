package com.veeritsolutions.uhelpme.models;

import java.io.Serializable;

/**
 * Created by Admin on 8/10/2017.
 */

public class GenderModel implements Serializable {

    private int position = 0;
    private int id = 0;
    private String gender = "";

    public GenderModel(int id, String gender) {
        this.id = id;
        this.gender = gender;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
