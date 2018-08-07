package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by VEER7 on 7/13/2017.
 */

public class PaymentModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private long dataId = 0;
    @SerializedName("ClientPaymentTypeId")
    @Expose
    private long clientPaymentTypeId = 0;
    @SerializedName("ClientId")
    @Expose
    private long clientId = 0;
    @SerializedName("PaymentType")
    @Expose
    private long paymentType = 0;
    @SerializedName("PaymentTypeName")
    @Expose
    private String paymentTypeName = "";
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn = "";
    @SerializedName("EndDate")
    @Expose
    private String endDate = "";


    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public long getClientPaymentTypeId() {
        return clientPaymentTypeId;
    }

    public void setClientPaymentTypeId(long clientPaymentTypeId) {
        this.clientPaymentTypeId = clientPaymentTypeId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(long paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
