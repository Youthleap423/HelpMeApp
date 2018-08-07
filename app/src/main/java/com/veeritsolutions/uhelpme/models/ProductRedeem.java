package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vaishali on 7/14/2017.
 */

public class ProductRedeem implements Serializable {
    @SerializedName("DataId")
    @Expose
    private int dataId=0;

    @SerializedName("ProductRedeemId")
    @Expose
    private int ProductRedeemId=0;

    @SerializedName("ClientId")
    @Expose
    private int ClientId=0;

    @SerializedName("FirstName")
    @Expose
    private String FirstName="";

    @SerializedName("ProductId")
    @Expose
    private int ProductId=0;

    @SerializedName("ProductName")
    @Expose
    private String ProductName="";

    @SerializedName("RedeemPoint")
    @Expose
    private int RedeemPoint=0;
    @SerializedName("Point")
    @Expose
    private int point=0;
    @SerializedName("ProductImage")
    @Expose
    private String ProductImage="";


    @SerializedName("CreatedOn")
    @Expose
    private String CreatedOn="";

    @SerializedName("EndDate")
    @Expose
    private String EndDate="";

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getProductRedeemId() {
        return ProductRedeemId;
    }

    public void setProductRedeemId(int productRedeemId) {
        ProductRedeemId = productRedeemId;
    }

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getRedeemPoint() {
        return RedeemPoint;
    }

    public void setRedeemPoint(int redeemPoint) {
        RedeemPoint = redeemPoint;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
