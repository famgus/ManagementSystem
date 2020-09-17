package com.ec.managementsystem.clases.request;

import java.io.Serializable;

public class FacturaDetailRequest implements Serializable {
    String numberSerie;
    Integer numberAlbaran;

    public FacturaDetailRequest() {
    }

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
}
