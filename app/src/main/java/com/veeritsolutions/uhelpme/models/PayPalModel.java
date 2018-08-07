package com.veeritsolutions.uhelpme.models;

import java.io.Serializable;

/**
 * Created by VEER7 on 7/14/2017.
 */

public class PayPalModel implements Serializable {

   private String create_time;
    private String id;
    private String intent;
    private String state;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
