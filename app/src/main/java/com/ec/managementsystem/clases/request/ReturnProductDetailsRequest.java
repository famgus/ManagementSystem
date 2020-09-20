package com.ec.managementsystem.clases.request;

public class ReturnProductDetailsRequest {
    private String barCode;

    public ReturnProductDetailsRequest(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
