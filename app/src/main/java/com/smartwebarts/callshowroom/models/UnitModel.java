package com.smartwebarts.callshowroom.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnitModel {

    @SerializedName("unit_in")
    @Expose
    private String unitIn;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("buingprice")
    @Expose
    private String buingprice;

    @SerializedName("quantity")
    @Expose
    private String quantity;

//    @SerializedName("currentprice")
//    @Expose
//    private String temp;


    public String getCurrentprice() {
        return currentprice;
    }

    public void setCurrentprice(String currentprice) {
        this.currentprice = currentprice;
    }

    @SerializedName("currentprice")
    @Expose
    private String currentprice;

    public UnitModel(String unitIn, String unit, String currentprice, String buingprice) {
        this.unitIn = unitIn;
        this.unit = unit;
        this.buingprice = currentprice;
        this.buingprice = buingprice;
    }

    public String getUnitIn() {
        return unitIn;
    }

    public void setUnitIn(String unitIn) {
        this.unitIn = unitIn;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBuingprice() {
        return buingprice;
    }

    public void setBuingprice(String buingprice) {
        this.buingprice = buingprice;
    }

    public String getQuantity() {
        return quantity!=null?quantity:"0";
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

//    public String getCurrentprice() {
//        return currentprice;
//    }
//
//    public void setCurrentprice(String currentprice) {
//        this.currentprice = currentprice;
//    }
}
