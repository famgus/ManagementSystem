package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PedidoPickingResponse implements Serializable {

    @SerializedName("numberPedido")
    private String numberPedido;
    @SerializedName("nameClient")
    private String nameClient;
    @SerializedName("datePedido")
    private String datePedido;
    @SerializedName("datePicking")
    private String datePicking;
    public PedidoPickingResponse(){}

    public String getNumberPedido() {
        return numberPedido;
    }

    public void setNumberPedido(String numberPedido) {
        this.numberPedido = numberPedido;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getDatePedido() {
        return datePedido;
    }

    public void setDatePedido(String datePedido) {
        this.datePedido = datePedido;
    }

    public String getDatePicking() {
        return datePicking;
    }

    public void setDatePicking(String datePicking) {
        this.datePicking = datePicking;
    }
}
