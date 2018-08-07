package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by VEER7 on 7/15/2017.
 */

public class ProductModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private long dataId = 0;
    @SerializedName("ProductId")
    @Expose
    private long productId = 0;
    @SerializedName("ProductName")
    @Expose
    private String productName = "";
    @SerializedName("Description")
    @Expose
    private String description = "";
    @SerializedName("ProductImage")
    @Expose
    private String productImage = "";
    @SerializedName("Point")
    @Expose
    private long point = 0;
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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
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
