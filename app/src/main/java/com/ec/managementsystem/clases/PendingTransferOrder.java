package com.ec.managementsystem.clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PendingTransferOrder implements Serializable {
    @SerializedName("numseriepc")
    private String seriesNumber;
    @SerializedName("numpedidopc")
    private Integer orderNumber;
    @SerializedName("supedidopc")
    private String supedidopc;
    @SerializedName("codclientepc")
    private Integer codclientepc;
    @SerializedName("seriealbaranpc")
    private String seriealbaranpc;
    @SerializedName("numeroalbaranpc")
    private Integer numeroalbaranpc;
    @SerializedName("tipodocpc")
    private Integer tipodocpc;
    @SerializedName("codarticulopl")
    private Integer codarticulopl;
    @SerializedName("referenciapl")
    private String referenciapl;
    @SerializedName("tallapl")
    private String tallapl;
    @SerializedName("colorpl")
    private String colorpl;
    @SerializedName("descripcionpl")
    private String descripcionpl;
    @SerializedName("unidadestotalpl")
    private Integer unidadestotalpl;
    @SerializedName("unidadesrecpl")
    private Integer unidadesrecpl;
    @SerializedName("unidadespenpl")
    private Integer unidadespenpl;
    @SerializedName("codalmacenpl")
    private String codalmacenpl;
    @SerializedName("esclientedelgrupoc")
    private Boolean esclientedelgrupoc;
    private boolean isAssigned;

    public PendingTransferOrder() {
        this.isAssigned = false;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSupedidopc() {
        return supedidopc;
    }

    public void setSupedidopc(String supedidopc) {
        this.supedidopc = supedidopc;
    }

    public Integer getCodclientepc() {
        return codclientepc;
    }

    public void setCodclientepc(Integer codclientepc) {
        this.codclientepc = codclientepc;
    }

    public String getSeriealbaranpc() {
        return seriealbaranpc;
    }

    public void setSeriealbaranpc(String seriealbaranpc) {
        this.seriealbaranpc = seriealbaranpc;
    }

    public Integer getNumeroalbaranpc() {
        return numeroalbaranpc;
    }

    public void setNumeroalbaranpc(Integer numeroalbaranpc) {
        this.numeroalbaranpc = numeroalbaranpc;
    }

    public Integer getTipodocpc() {
        return tipodocpc;
    }

    public void setTipodocpc(Integer tipodocpc) {
        this.tipodocpc = tipodocpc;
    }

    public Integer getCodarticulopl() {
        return codarticulopl;
    }

    public void setCodarticulopl(Integer codarticulopl) {
        this.codarticulopl = codarticulopl;
    }

    public String getReferenciapl() {
        return referenciapl;
    }

    public void setReferenciapl(String referenciapl) {
        this.referenciapl = referenciapl;
    }

    public String getTallapl() {
        return tallapl;
    }

    public void setTallapl(String tallapl) {
        this.tallapl = tallapl;
    }

    public String getColorpl() {
        return colorpl;
    }

    public void setColorpl(String colorpl) {
        this.colorpl = colorpl;
    }

    public String getDescripcionpl() {
        return descripcionpl;
    }

    public void setDescripcionpl(String descripcionpl) {
        this.descripcionpl = descripcionpl;
    }

    public Integer getUnidadestotalpl() {
        return unidadestotalpl;
    }

    public void setUnidadestotalpl(Integer unidadestotalpl) {
        this.unidadestotalpl = unidadestotalpl;
    }

    public Integer getUnidadesrecpl() {
        return unidadesrecpl;
    }

    public void setUnidadesrecpl(Integer unidadesrecpl) {
        this.unidadesrecpl = unidadesrecpl;
    }

    public Integer getUnidadespenpl() {
        return unidadespenpl;
    }

    public void setUnidadespenpl(Integer unidadespenpl) {
        this.unidadespenpl = unidadespenpl;
    }

    public String getCodalmacenpl() {
        return codalmacenpl;
    }

    public void setCodalmacenpl(String codalmacenpl) {
        this.codalmacenpl = codalmacenpl;
    }

    public Boolean getEsclientedelgrupoc() {
        return esclientedelgrupoc;
    }

    public void setEsclientedelgrupoc(Boolean esclientedelgrupoc) {
        this.esclientedelgrupoc = esclientedelgrupoc;
    }
}
