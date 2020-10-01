package com.ec.managementsystem.clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Gorky Muñoz on 26/9/2020.
 * Indra
 * gamunozg@indracompany.com
 */
public class GuideModel implements Serializable {
    @SerializedName("numSerie")
    private String seriesNumber;
    @SerializedName("numPedido")
    private Integer orderNumber;
    @SerializedName("numAlbaran")
    private Integer albaranNumber;
    @SerializedName("supedido")
    private String seriesOrder;
    @SerializedName("numSerieFac")
    private String billSeriesNumber;
    @SerializedName("numFactura")
    private Integer billNumber;
    @SerializedName("guia")
    private String guide;
    @SerializedName("picking")
    private String idPicking;

    private boolean isVerified;

    public GuideModel() {
        isVerified = false;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public Integer getAlbaranNumber() {
        return albaranNumber;
    }

    public void setAlbaranNumber(Integer albaranNumber) {
        this.albaranNumber = albaranNumber;
    }

    public String getSeriesOrder() {
        return seriesOrder;
    }

    public void setSeriesOrder(String seriesOrder) {
        this.seriesOrder = seriesOrder;
    }

    public String getBillSeriesNumber() {
        return billSeriesNumber;
    }

    public void setBillSeriesNumber(String billSeriesNumber) {
        this.billSeriesNumber = billSeriesNumber;
    }

    public Integer getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(Integer billNumber) {
        this.billNumber = billNumber;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getIdPicking() {
        return idPicking;
    }

    public void setIdPicking(String idPicking) {
        this.idPicking = idPicking;
    }
}
