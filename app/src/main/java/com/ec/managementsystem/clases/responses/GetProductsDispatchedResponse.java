package com.ec.managementsystem.clases.responses;

import com.ec.managementsystem.clases.DispatchedProduct;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetProductsDispatchedResponse implements Serializable {

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("listProductDispatched")
    private List<DispatchedProduct> dispatchedProducts;

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

    public List<DispatchedProduct> getDispatchedProducts() {
        return dispatchedProducts;
    }
}
