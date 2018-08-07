package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vaishali on 7/10/2017.
 */

public class Packages implements Serializable {
    @SerializedName("DataId")
    @Expose
    private int dataId=0;

    @SerializedName("PackageId")
    @Expose
    private int PackageId=0;

    @SerializedName("PackageName")
    @Expose
    private String PackageName="";

    @SerializedName("Description")
    @Expose
    private String Description="";

    @SerializedName("CreditPost")
    @Expose
    private int CreditPost=0;

    @SerializedName("CreditPoint")
    @Expose
    private int CreditPoint=0;

    @SerializedName("Amount")
    @Expose
    private int Amount=0;

    @SerializedName("CreatedOn")
    @Expose
    private String CreatedOn="";

    @SerializedName("EndDate")
    @Expose
    private String  EndDate="";


    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getPackageId() {
        return PackageId;
    }

    public void setPackageId(int packageId) {
        PackageId = packageId;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getCreditPost() {
        return CreditPost;
    }

    public void setCreditPost(int creditPost) {
        CreditPost = creditPost;
    }

    public int getCreditPoint() {
        return CreditPoint;
    }

    public void setCreditPoint(int creditPoint) {
        CreditPoint = creditPoint;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
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
}
