package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ${Hitesh} on 4/10/2017.
 */

public class HelpPicsModel extends BaseModel {

    @SerializedName("DataId")
    @Expose
    private long dataId = 0;
    @SerializedName("ClientPetPicsId")
    @Expose
    private long clientPetPicsId = 0;
    @SerializedName("ClientPetId")
    @Expose
    private long clientPetId = 0;
    @SerializedName("PicTitle")
    @Expose
    private String picTitle = "";
    @SerializedName("PicPath")
    @Expose
    private String picPath = "";
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn = "";
    @SerializedName("EndDate")
    @Expose
    private String endDate = "";

    private String base64image = "";


    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public long getClientPetPicsId() {
        return clientPetPicsId;
    }

    public void setClientPetPicsId(long clientPetPicsId) {
        this.clientPetPicsId = clientPetPicsId;
    }

    public long getClientPetId() {
        return clientPetId;
    }

    public void setClientPetId(long clientPetId) {
        this.clientPetId = clientPetId;
    }

    public String getPicTitle() {
        return picTitle;
    }

    public void setPicTitle(String picTitle) {
        this.picTitle = picTitle;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
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

    public String getBase64image() {
        return base64image;
    }

    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }
}


