package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by VEER7 on 7/6/2017.
 */

public class ChatUsersListModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId = 0;
    @SerializedName("IsGroup")
    @Expose
    private int isGroup = 0;
    @SerializedName("Id")
    @Expose
    private int id = 0;
    @SerializedName("Name")
    @Expose
    private String name = "";
    @SerializedName("ProfilePic")
    @Expose
    private String profilePic = "";
    @SerializedName("IsAdmin")
    @Expose
    private int isAdmin;

    private int position = 0;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}

