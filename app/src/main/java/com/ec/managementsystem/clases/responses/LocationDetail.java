package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LocationDetail implements Serializable {
    @SerializedName("codubicacion")
    public String codubicacion;
    @SerializedName("codbarrascajamaster")
    public String codbarrascajamaster;
    @SerializedName("cantidadarticulo")
    public Integer cantidadarticulo;

    public  LocationDetail(){

    }

    public String getCodubicacion() {
        return codubicacion;
    }

    public void setCodubicacion(String codubicacion) {
        this.codubicacion = codubicacion;
    }

    public String getCodbarrascajamaster() {
        return codbarrascajamaster;
    }

    public void setCodbarrascajamaster(String codbarrascajamaster) {
        this.codbarrascajamaster = codbarrascajamaster;
    }

    public Integer getCantidadarticulo() {
        return cantidadarticulo;
    }

    public void setCantidadarticulo(Integer cantidadarticulo) {
        this.cantidadarticulo = cantidadarticulo;
    }
}
