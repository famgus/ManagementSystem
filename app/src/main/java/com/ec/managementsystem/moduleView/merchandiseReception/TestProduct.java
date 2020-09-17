package com.ec.managementsystem.moduleView.merchandiseReception;

public class TestProduct {

    private String detalle;
    private int cantidad;


    public TestProduct(String detalle, int cantidad) {
        this.detalle = detalle;
        this.cantidad = cantidad;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
