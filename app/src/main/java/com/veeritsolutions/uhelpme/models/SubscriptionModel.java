package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by VEER7 on 7/14/2017.
 */

public class SubscriptionModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private long dataId = 0;
    @SerializedName("SubscriptionId")
    @Expose
    private long subscriptionId = 0;
    @SerializedName("ClientId")
    @Expose
    private long clientId = 0;
    @SerializedName("FirstName")
    @Expose
    private String firstName = "";
    @SerializedName("LastName")
    @Expose
    private String lastName = "";
    @SerializedName("PackageId")
    @Expose
    private long packageId = 0;
    @SerializedName("CreditPost")
    @Expose
    private long creditPost = 0;
    @SerializedName("CreditPoint")
    @Expose
    private long creditPoint = 0;
    @SerializedName("PackageName")
    @Expose
    private String packageName = "";
    @SerializedName("PaymentAmount")
    @Expose
    private String paymentAmount = "";
    @SerializedName("PaymentTime")
    @Expose
    private String paymentTime = "";
    @SerializedName("PaymentId")
    @Expose
    private String paymentId = "";
    @SerializedName("PaymentStatus")
    @Expose
    private String paymentStatus = "";
    @SerializedName("PaymentResponse")
    @Expose
    private String paymentResponse = "";
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

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    public long getCreditPost() {
        return creditPost;
    }

    public void setCreditPost(long creditPost) {
        this.creditPost = creditPost;
    }

    public long getCreditPoint() {
        return creditPoint;
    }

    public void setCreditPoint(long creditPoint) {
        this.creditPoint = creditPoint;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(String paymentResponse) {
        this.paymentResponse = paymentResponse;
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
