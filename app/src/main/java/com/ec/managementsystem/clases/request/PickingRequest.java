package com.ec.managementsystem.clases.request;

import java.io.Serializable;

public class PickingRequest implements Serializable {
    private String numberSerie;
    private Integer numberPedido;
    private Integer codeArticle;
    private Integer quantity;
    private Integer state;
    private Integer path;
    private String  barCodeLocation;
    private String barCodeBoxMaster;


    public PickingRequest() {
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

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPath() {
        return path;
    }

    public void setPath(Integer path) {
        this.path = path;
    }

    public String getBarCodeLocation() {
        return barCodeLocation;
    }

    public void setBarCodeLocation(String barCodeLocation) {
        this.barCodeLocation = barCodeLocation;
    }

    public String getBarCodeBoxMaster() {
        return barCodeBoxMaster;
    }

    public void setBarCodeBoxMaster(String barCodeBoxMaster) {
        this.barCodeBoxMaster = barCodeBoxMaster;
    }
}
