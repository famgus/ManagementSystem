package com.ec.managementsystem.clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductDetail implements Serializable {

    @SerializedName("codArticulo")
    private int codArticulo;
    @SerializedName("refproveedor")
    private String refproveedor;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("codBarras")
    private String codBarras;
    @SerializedName("talla")
    private String talla;
    @SerializedName("color")
    private String color;
    @SerializedName("cantidad")
    private Integer cantidad;

    public ProductDetail() {
    }

    public int getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(int codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getRefproveedor() {
        return refproveedor;
    }

    public void setRefproveedor(String refproveedor) {
        this.refproveedor = refproveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
