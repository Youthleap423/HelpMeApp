package com.veeritsolutions.uhelpme.models;

import java.io.Serializable;

/**
 * Created by VEER7 on 7/7/2017.
 */

public class ChatModel implements Serializable {

    private int clientId = 0;
    private String clientName = "";
    private String message = "";
    private String date = "";
    private int isChatGroup = 0;

    public ChatModel() {
    }

    public ChatModel(int clientId, String clientName, String message, String date, int isChatGroup) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.message = message;
        this.date = date;
        this.isChatGroup = isChatGroup;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsChatGroup() {
        return isChatGroup;
    }

    public void setIsChatGroup(int isChatGroup) {
        this.isChatGroup = isChatGroup;
    }
}
