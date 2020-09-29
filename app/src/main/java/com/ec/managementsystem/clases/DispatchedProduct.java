package com.ec.managementsystem.clases;

import com.ec.managementsystem.util.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DispatchedProduct implements Serializable {

    @SerializedName("numPedido")
    private Integer orderNumber;
    @SerializedName("numSerie")
    private String seriesNumber;
    @SerializedName("codArticulo")
    private Integer productCode;
    @SerializedName("talla")
    private String size;
    @SerializedName("color")
    private String color;
    @SerializedName("totalDespachado")
    private Integer totalDispatched;
    private Integer totalReceived = 0;
    private int rvType = Utils.ITEM_TYPE;
    private boolean isRegistered = false;

    public DispatchedProduct() {
    }

    public DispatchedProduct(int rvType) {
        this.rvType = rvType;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public Integer getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(Integer totalReceived) {
        this.totalReceived = totalReceived;
    }

    public int getRvType() {
        return rvType;
    }

    public void setRvType(int rvType) {
        this.rvType = rvType;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public Integer getProductCode() {
        return productCode;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public Integer getTotalDispatched() {
        return totalDispatched;
    }
}
