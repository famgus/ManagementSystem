package com.ec.managementsystem.clases;

import com.ec.managementsystem.util.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransferPackOff implements Serializable {

    @SerializedName("numserie")
    private String seriesNumber;
    @SerializedName("numpedido")
    private Integer orderNumber;
    @SerializedName("usuariopreparo")
    private Integer userWhoPrepared;
    @SerializedName("codbarrascajamaster")
    private String boxMasterBarCode;
    @SerializedName("cantidadarticulosencaja")
    private Integer quantityProductsInBox;
    private int rvType;
    private boolean isChecked = false;

    public TransferPackOff() {
        rvType = Utils.ITEM_TYPE;
    }

    public TransferPackOff(int rvType) {
        this.rvType = rvType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getRvType() {
        return rvType;
    }

    public void setRvType(int rvType) {
        this.rvType = rvType;
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

    public Integer getUserWhoPrepared() {
        return userWhoPrepared;
    }

    public void setUserWhoPrepared(Integer userWhoPrepared) {
        this.userWhoPrepared = userWhoPrepared;
    }

    public String getBoxMasterBarCode() {
        return boxMasterBarCode;
    }

    public void setBoxMasterBarCode(String boxMasterBarCode) {
        this.boxMasterBarCode = boxMasterBarCode;
    }

    public Integer getQuantityProductsInBox() {
        return quantityProductsInBox;
    }

    public void setQuantityProductsInBox(Integer quantityProductsInBox) {
        this.quantityProductsInBox = quantityProductsInBox;
    }
}
