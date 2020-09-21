package com.ec.managementsystem.clases.request;

public class ReturnProductValidationRequest {
    private String data;
    private int typeValidation;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTypeValidation() {
        return typeValidation;
    }

    public void setTypeValidation(int typeValidation) {
        this.typeValidation = typeValidation;
    }

    public ReturnProductValidationRequest(String data, int typeValidation) {
        this.data = data;
        this.typeValidation = typeValidation;
    }
}
