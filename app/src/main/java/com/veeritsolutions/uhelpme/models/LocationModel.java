package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hitesh on 22-09-2017.
 */

public class LocationModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId = 0;
    @SerializedName("Id")
    @Expose
    private int id = 0;
    @SerializedName("Latitude")
    @Expose
    private double latitude = 0;
    @SerializedName("Longitude")
    @Expose
    private double longitude = 0;
    @SerializedName("Altitude")
    @Expose
    private int altitude = 0;
    //private final static long serialVersionUID = 3499849539043123267L;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }
}
