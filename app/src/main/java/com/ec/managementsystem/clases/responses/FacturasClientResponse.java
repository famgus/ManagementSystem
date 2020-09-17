package com.ec.managementsystem.clases.responses;

import com.ec.managementsystem.clases.FacturaModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FacturasClientResponse implements Serializable {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("numberClient")
    private Integer numberClient;
    @SerializedName("nameClient")
    private String nameClient;
    @SerializedName("municipio")
    private String municipio;
    @SerializedName("address")
    private String address;
    @SerializedName("facturaModels")
    private List<FacturaModel> facturaModels;


    public Integer getNumberClient() {
        return numberClient;
    }

    public void setNumberClient(Integer numberClient) {
        this.numberClient = numberClient;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<FacturaModel> getFacturaModels() {
        return facturaModels;
    }

    public void setFacturaModels(List<FacturaModel> facturaModels) {
        this.facturaModels = facturaModels;
    }

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

    public FacturasClientResponse() {
    }
}

