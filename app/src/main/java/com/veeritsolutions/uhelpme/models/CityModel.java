package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 3/8/2017.
 */

public class CityModel implements Serializable {
    //    Position variable is common for all class
    private int position=0;

    @SerializedName("CityId")
    @Expose
    private int cityId=0;
    @SerializedName("CityName")
    @Expose
    private String cityName="";

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
