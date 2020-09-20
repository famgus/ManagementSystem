package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

public class ReturnProductResponse {
    @SerializedName("numSerie")
    private String numSerie;

    @SerializedName("numPedido")
    private String numPedido;

    @SerializedName("codProvedor")
    private String codProvedor;

    @SerializedName("fechaPedido")
    private String fechaPedido;

    @SerializedName("codArticulo")
    private String codArticulo;

    @SerializedName("talla")
    private String talla;

    @SerializedName("color")
    private String color;

    @SerializedName("codigoBarras")
    private String barCode;

    @SerializedName("descricion")
    private String description;
    @SerializedName("unidadesTotales")
    private String unidadesTotales;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public String getCodProvedor() {
        return codProvedor;
    }

    public void setCodProvedor(String codProvedor) {
        this.codProvedor = codProvedor;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
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

    public String getUnidadesTotales() {
        return unidadesTotales;
    }

    public void setUnidadesTotales(String unidadesTotales) {
        this.unidadesTotales = unidadesTotales;
    }
}
