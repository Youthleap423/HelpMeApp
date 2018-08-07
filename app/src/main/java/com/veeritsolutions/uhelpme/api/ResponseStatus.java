package com.veeritsolutions.uhelpme.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by VEER7 on 6/20/2017.
 */

public class ResponseStatus implements Serializable
{

    @SerializedName("DataId")
    @Expose
    private int dataId;
    @SerializedName("IsError")
    @Expose
    private boolean isError;
    @SerializedName("ErrorNumber")
    @Expose
    private int errorNumber;
    @SerializedName("Error")
    @Expose
    private String error;


    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public boolean isIsError() {
        return isError;
    }

    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}



