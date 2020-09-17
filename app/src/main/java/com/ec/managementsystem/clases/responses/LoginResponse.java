package com.ec.managementsystem.clases.responses;

import com.ec.managementsystem.clases.Vendedores;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class LoginResponse implements Serializable {

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("vendedoresList")
    private List<Vendedores> vendedoresList;

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

    public List<Vendedores> getVendedoresList() {
        return vendedoresList;
    }

    public void setVendedoresList(List<Vendedores> vendedoresList) {
        this.vendedoresList = vendedoresList;
    }

    public LoginResponse() {
    }
}
