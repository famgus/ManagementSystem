package com.ec.managementsystem.clases.responses;

import com.ec.managementsystem.clases.ProductDetail;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ProductoResponse implements Serializable {

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("productDetail")
    private ProductDetail productDetail;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public ProductoResponse() {
    }
}
