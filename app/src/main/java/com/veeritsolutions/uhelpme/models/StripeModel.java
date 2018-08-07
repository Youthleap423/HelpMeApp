package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by VEER7 on 7/22/2017.
 */

public class StripeModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private long dataId = 0;
    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("amount")
    @Expose
    private String amount = "";
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("failure_code")
    @Expose
    private String failureCode = "";
    @SerializedName("failure_message")
    @Expose
    private String failureMessage = "";

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(String failureCode) {
        this.failureCode = failureCode;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }
}
