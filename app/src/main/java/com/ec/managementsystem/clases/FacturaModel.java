package com.ec.managementsystem.clases;

import com.ec.managementsystem.clases.request.FacturaDetailRequest;
import com.ec.managementsystem.clases.responses.FacturasDetasilResponse;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FacturaModel implements Serializable {
    @SerializedName("numberFactura")
    private Integer numberFactura;
    @SerializedName("codeClient")
    private Integer codeClient;
    @SerializedName("numberSerie")
    private String numberSerie;
    @SerializedName("numberPedido")
    private Integer numberPedido;
    @SerializedName("stateReservation")
    private Integer stateReservation;
    @SerializedName("date")
    private String date;
    @SerializedName("numberSerieAc")
    private String numberSerieAc;
    @SerializedName("numberAlbaran")
    private Integer numberAlbaran;
    @SerializedName("numberSerieFac")
    private String numberSerieFac;
    private boolean complete = false;
    private List<FacturasDetasilResponse> facturasDetasilResponses;
    private  boolean ckecked = false;


    public FacturaModel() {
    }

    public Integer getNumberFactura() {
        return numberFactura;
    }

    public void setNumberFactura(Integer numberFactura) {
        this.numberFactura = numberFactura;
    }

    public Integer getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(Integer codeClient) {
        this.codeClient = codeClient;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumberSerieAc() {
        return numberSerieAc;
    }

    public void setNumberSerieAc(String numberSerieAc) {
        this.numberSerieAc = numberSerieAc;
    }

    public Integer getNumberAlbaran() {
        return numberAlbaran;
    }

    public void setNumberAlbaran(Integer numberAlbaran) {
        this.numberAlbaran = numberAlbaran;
    }

    public String getNumberSerieFac() {
        return numberSerieFac;
    }

    public void setNumberSerieFac(String numberSerieFac) {
        this.numberSerieFac = numberSerieFac;
    }

    public List<FacturasDetasilResponse> getFacturasDetasilResponses() {
        return facturasDetasilResponses;
    }

    public void setFacturasDetasilResponses(List<FacturasDetasilResponse> facturasDetasilResponses) {
        this.facturasDetasilResponses = facturasDetasilResponses;
    }

    public boolean isCkecked() {
        return ckecked;
    }

    public void setCkecked(boolean ckecked) {
        this.ckecked = ckecked;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
