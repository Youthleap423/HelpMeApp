package com.veeritsolutions.uhelpme.models;

import java.io.Serializable;

/**
 * Created by ABC on 10/2/2017.
 */

public class PaymentTypeModel implements Serializable {

    private int position = 0;
    private int paymentTypeId = 0;

    private String paymentTypeName = "";

    public PaymentTypeModel(int id, String paymentTypeName) {
        this.paymentTypeId = id;
        this.paymentTypeName = paymentTypeName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(int paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }



    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

}

