package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FacturasDetasilResponse implements Serializable {
    @SerializedName("numberSerie")
    public  String numberSerie ;
    @SerializedName("numberAlbaran")
    public  Integer numberAlbaran ;
    @SerializedName("numberLin")
    public  Integer numberLin ;
    @SerializedName("codeArticle")
    public  Integer codeArticle ;
    @SerializedName("reference")
    public  String reference ;
    @SerializedName("description")
    public  String description ;
    @SerializedName("talla")
    public  String talla ;
    @SerializedName("color")
    public  String color;
    @SerializedName("unidadesTotales")
    public  Double unidadesTotales ;
    @SerializedName("codeVendedor")
    public  Integer codeVendedor ;
    @SerializedName("codeAlmacen")
    public  String codeAlmacen ;
    @SerializedName("fechaEntrega")
    public  String fechaEntrega ;
    @SerializedName("fechaCaducidad")
    public  String fechaCaducidad ;
    private  boolean finish = false;

    public String getNumberSerie() {
        return numberSerie;
    }

    public void setNumberSerie(String numberSerie) {
        this.numberSerie = numberSerie;
    }

    public Integer getNumberAlbaran() {
        return numberAlbaran;
    }

    public void setNumberAlbaran(Integer numberAlbaran) {
        this.numberAlbaran = numberAlbaran;
    }

    public Integer getNumberLin() {
        return numberLin;
    }

    public void setNumberLin(Integer numberLin) {
        this.numberLin = numberLin;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getUnidadesTotales() {
        return unidadesTotales;
    }

    public void setUnidadesTotales(Double unidadesTotales) {
        this.unidadesTotales = unidadesTotales;
    }

    public Integer getCodeVendedor() {
        return codeVendedor;
    }

    public void setCodeVendedor(Integer codeVendedor) {
        this.codeVendedor = codeVendedor;
    }

    public String getCodeAlmacen() {
        return codeAlmacen;
    }

    public void setCodeAlmacen(String codeAlmacen) {
        this.codeAlmacen = codeAlmacen;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
