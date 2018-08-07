package com.veeritsolutions.uhelpme.models;

/**
 * This class store the values of the device token model
 * and do registration device token to server
 */
public class DeviceTokenModel {

    //variable declaration
    private static final String TAG = DeviceTokenModel.class.getName();
    private int Id;
    private int customerId;
    private int vetId;
    private int deviceType;
    private String deviceToken;
    private String deviceDetails;
    private String AcTokenId;

    public int getVetId() {
        return vetId;
    }

    public void setVetId(int vetId) {
        this.vetId = vetId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(String deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

}
