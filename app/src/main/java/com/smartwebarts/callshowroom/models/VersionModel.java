package com.smartwebarts.callshowroom.models;

public class VersionModel {

    String id;
    String vcode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVcode() {
        return vcode!=null && !vcode.trim().isEmpty() ? Integer.parseInt("0"+vcode) : 0;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }
}
