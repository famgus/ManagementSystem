package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

public class ReturnProductDetailsResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("productDetail")
    private ReturnProductDetail productDetail;

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

    public ReturnProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ReturnProductDetail productDetail) {
        this.productDetail = productDetail;
    }
}
