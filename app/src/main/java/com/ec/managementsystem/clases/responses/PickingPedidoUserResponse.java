package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PickingPedidoUserResponse implements Serializable {

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("fechaPedido")
    private String fechaPedido;
    @SerializedName("supedido")
    private String supedido;
    @SerializedName("codeVendedor")
    private Integer codeVendedor;
    @SerializedName("nameClient")
    private String nameClient;
    @SerializedName("observation")
    private String observation;
    @SerializedName("numberSerie")
    private String numberSerie;
    @SerializedName("numberPedido")
    private Integer numberPedido;
    @SerializedName("stateReservation")
    private Integer stateReservation;
    @SerializedName("detailsResponse")
    ListPickingPedidoDetailResponse detailsResponse;
    private  boolean complete;

    public PickingPedidoUserResponse() {
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

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getSupedido() {
        return supedido;
    }

    public void setSupedido(String supedido) {
        this.supedido = supedido;
    }

    public Integer getCodeVendedor() {
        return codeVendedor;
    }

    public void setCodeVendedor(Integer codeVendedor) {
        this.codeVendedor = codeVendedor;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getNumberSerie() {
        return numberSerie;
    }

    public void setNumberSerie(String numberSerie) {
        this.numberSerie = numberSerie;
    }

    public Integer getNumberPedido() {
        return numberPedido;
    }

    public void setNumberPedido(Integer numberPedido) {
        this.numberPedido = numberPedido;
    }

    public Integer getStateReservation() {
        return stateReservation;
    }

    public void setStateReservation(Integer stateReservation) {
        this.stateReservation = stateReservation;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public ListPickingPedidoDetailResponse getDetailsResponse() {
        return detailsResponse;
    }

    public void setDetailsResponse(ListPickingPedidoDetailResponse detailsResponse) {
        this.detailsResponse = detailsResponse;
    }
}
