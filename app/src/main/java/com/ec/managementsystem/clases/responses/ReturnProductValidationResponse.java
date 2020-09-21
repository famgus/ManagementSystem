package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

public class ReturnProductValidationResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("typeValidation")
    private int typeValidation;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTypeValidation() {
        return typeValidation;
    }

    public void setTypeValidation(int typeValidation) {
        this.typeValidation = typeValidation;
    }
}
