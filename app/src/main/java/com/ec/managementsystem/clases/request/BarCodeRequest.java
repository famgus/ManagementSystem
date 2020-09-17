package com.ec.managementsystem.clases.request;

import java.io.Serializable;

public class BarCodeRequest implements Serializable {
    private String userRegister;
    private Integer quantity;

    public BarCodeRequest() {
    }

    public String getUserRegister() {
        return userRegister;
    }

    public void setUserRegister(String userRegister) {
        this.userRegister = userRegister;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
