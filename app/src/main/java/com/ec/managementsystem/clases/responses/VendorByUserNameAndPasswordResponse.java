package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorByUserNameAndPasswordResponse implements Serializable {

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    private int vendorCode;

    public VendorByUserNameAndPasswordResponse() {
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getVendorCode() {
        return vendorCode;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setVendorCode(int vendorCode) {
        this.vendorCode = vendorCode;
    }
}
