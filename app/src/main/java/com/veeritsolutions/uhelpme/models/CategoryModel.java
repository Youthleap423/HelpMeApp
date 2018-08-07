package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 6/26/2017.
 */

public class CategoryModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId = 0;
    @SerializedName("CategoryId")
    @Expose
    private int categoryId = 0;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName = "";
    @SerializedName("CategoryPoints")
    @Expose
    private int categoryPoints = 0;

    private boolean isSelected = false;

    private int position = 0;
    @SerializedName("Icon1")
    @Expose
    private String icon1 = "";
    @SerializedName("Icon2")
    @Expose
    private String icon2 = "";
    @SerializedName("ColorCode")
    @Expose
    private String colorCode = "";


    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryPoints() {
        return categoryPoints;
    }

    public void setCategoryPoints(int categoryPoints) {
        this.categoryPoints = categoryPoints;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public String getIcon2() {
        return icon2;
    }

    public void setIcon2(String icon2) {
        this.icon2 = icon2;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}