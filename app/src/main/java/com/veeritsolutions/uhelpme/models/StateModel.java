package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ${hitesh} on 3/8/2017.
 */

public class StateModel implements Serializable {

    //    Position variable is common for all class
    private int position = 0;

    @SerializedName("StateId")
    @Expose
    private int stateId = 0;
    @SerializedName("StateName")
    @Expose
    private String stateName = "";

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}