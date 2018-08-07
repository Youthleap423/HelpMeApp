package com.veeritsolutions.uhelpme.models;

import java.io.Serializable;

/**
 * Created by hitesh on 07-09-2017.
 */

public class BaseModel implements Serializable {

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
