package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by VEER7 on 7/6/2017.
 */

public class ChatGroupModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId=0;
    @SerializedName("ChatGroupId")
    @Expose
    private int chatGroupId=0;
    @SerializedName("ChatGroupName")
    @Expose
    private String chatGroupName="";
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn="";
    @SerializedName("EndDate")
    @Expose
    private String endDate="";

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(int chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public String getChatGroupName() {
        return chatGroupName;
    }

    public void setChatGroupName(String chatGroupName) {
        this.chatGroupName = chatGroupName;
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
