package com.ec.managementsystem.clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoxMaster implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("barCode")
    private  String barCode;
    @SerializedName("countProduct")
    private  int countProduct;
    @SerializedName("complete")
    private  boolean complete;
    @SerializedName("locationBarCode")
    private  String locationBarCode;
    @SerializedName("userRegister")
    private  String userRegister;
    @SerializedName("codesProduct")
    private List<String> codesProduct;
    @SerializedName("pedidoDetailSmall")
    private List<PedidoDetailSmall> productDetail;

    public BoxMaster(){
        codesProduct = new ArrayList<>();
        productDetail = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getCountProduct() {
        return countProduct;
    }

    public void setCountProduct(int countProduct) {
        this.countProduct = countProduct;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getLocationBarCode() {
        return locationBarCode;
    }

    public void setLocationBarCode(String locationBarCode) {
        this.locationBarCode = locationBarCode;
    }

    public List<String> getCodesProduct() {
        return codesProduct;
    }

    public void setCodesProduct(List<String> codesProduct) {
        this.codesProduct = codesProduct;
    }

    public List<PedidoDetailSmall> getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(List<PedidoDetailSmall> productDetail) {
        this.productDetail = productDetail;
    }

    public String getUserRegister() {
        return userRegister;
    }

    public void setUserRegister(String userRegister) {
        this.userRegister = userRegister;
    }
}
