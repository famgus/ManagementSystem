package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PickingPedidoDetailResponse implements Serializable {
    @SerializedName("numberSerie")
   private String numberSerie ;
    @SerializedName("numberPedido")
   private Integer numberPedido ;
    @SerializedName("numberLin")
   private Integer numberLin ;
    @SerializedName("reference")
   private String reference ;
    @SerializedName("description")
   private String description ;
    @SerializedName("talla")
   private String talla ;
    @SerializedName("color")
   private String color ;
    @SerializedName("codeArticle")
   private Integer codeArticle ;
    @SerializedName("unidadesTotales")
   private Double unidadesTotales ;
    @SerializedName("unidadesPen")
   private Double unidadesPen ;
    @SerializedName("unidadesRec")
    private Double unidadesRec ;
    @SerializedName("ubicaciones")
    private List<LocationDetail> ubicaciones ;
    boolean finish;

   public  PickingPedidoDetailResponse(){}

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

    public Integer getNumberLin() {
        return numberLin;
    }

    public void setNumberLin(Integer numberLin) {
        this.numberLin = numberLin;
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

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public Double getUnidadesTotales() {
        return unidadesTotales;
    }

    public void setUnidadesTotales(Double unidadesTotales) {
        this.unidadesTotales = unidadesTotales;
    }

    public Double getUnidadesPen() {
        return unidadesPen;
    }

    public void setUnidadesPen(Double unidadesPen) {
        this.unidadesPen = unidadesPen;
    }

    public Double getUnidadesRec() {
        return unidadesRec;
    }

    public void setUnidadesRec(Double unidadesRec) {
        this.unidadesRec = unidadesRec;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public List<LocationDetail> getLocationList() {
        return ubicaciones;
    }

    public void setLocationList(List<LocationDetail> locationList) {
        this.ubicaciones = locationList;
    }
}
