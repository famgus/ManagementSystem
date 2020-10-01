package com.ec.managementsystem.clases;

import java.io.Serializable;

public class ProductQuantity implements Serializable {
    private  String barCodeProduct;
    private  String barCodeBoxMaster;
    private  Double totalPedido;
    private  Integer totalContado;
    private  boolean complete;

    public  ProductQuantity(){}

    public String getBarCodeProduct() {
        return barCodeProduct;
    }

    public void setBarCodeProduct(String barCodeProduct) {
        this.barCodeProduct = barCodeProduct;
    }

    public String getBarCodeBoxMaster() {
        return barCodeBoxMaster;
    }

    public void setBarCodeBoxMaster(String barCodeBoxMaster) {
        this.barCodeBoxMaster = barCodeBoxMaster;
    }

    public Double getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(Double totalPedido) {
        this.totalPedido = totalPedido;
    }

    public Integer getTotalContado() {
        return totalContado;
    }

    public void setTotalContado(Integer totalContado) {
        this.totalContado = totalContado;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
