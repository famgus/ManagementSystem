package com.ec.managementsystem.clases.request;

public class FacturaRequest {
    private String parametro;
    private Integer opcion;

    public FacturaRequest() {
    }

    public Integer getOpcion() {return opcion;}

    public void setOpcion(Integer opcion) {this.opcion = opcion;}

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }
}
