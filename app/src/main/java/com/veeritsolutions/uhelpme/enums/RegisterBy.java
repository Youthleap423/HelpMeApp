package com.veeritsolutions.uhelpme.enums;

/**
 * Created by admin on 12/12/2016.
 */

public enum RegisterBy {

    APP("0"),
    GOOGLE("1"),
    FACEBOOK("2");

    String registerBy;

    RegisterBy(String i) {
        this.registerBy = i;
    }

    public String getRegisterBy() {
        return registerBy;
    }

    public void setRegisterBy(String registerBy) {
        this.registerBy = registerBy;
    }
}
