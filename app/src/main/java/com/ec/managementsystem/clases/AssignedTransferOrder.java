package com.ec.managementsystem.clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AssignedTransferOrder implements Serializable {

    @SerializedName("numseriepc")
    private String seriesNumber;
    @SerializedName("numpedidopc")
    private Integer orderNumber;
    @SerializedName("codvendedor")
    private Integer vendorCode;

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

    public Integer getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(Integer vendorCode) {
        this.vendorCode = vendorCode;
    }
}
