package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 3/8/2017.
 */

public class CountryModel implements Serializable {

    //    Position variable is common for all class
    private int position=0;

    @SerializedName("CountryId")
    @Expose
    private int countryId=0;
    @SerializedName("CountryName")
    @Expose
    private String countryName="";

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
