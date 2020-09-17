package com.ec.managementsystem.clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PedidoDetail implements Serializable {
    @SerializedName("numSerie")
    private String numSerie;
    @SerializedName("numPedido")
    private int numPedido;
    @SerializedName("suPedido")
    private String suPedido;
    @SerializedName("fechaPedido")
    private String fechaPedido;
    @SerializedName("fechaEntrega")
    private String fechaEntrega;
    @SerializedName("todoRecibido")
    private String todoRecibido;
    @SerializedName("codProvedor")
    private int codProvedor;
    @SerializedName("codArticulo")
    private int codArticulo;
    @SerializedName("talla")
    private String talla;
    @SerializedName("color")
    private String color;
    @SerializedName("descricion")
    private String descricion;
    @SerializedName("unidadesTotales")
    private double unidadesTotales;
    @SerializedName("unidadesPorCaja")
    private int unidadesPorCaja;
    @SerializedName("nomProveedor")
    private String nomProveedor;
    @SerializedName("nif20")
    private String nif20;
    @SerializedName("codigoBarras")
    private String codigoBarra;
    @SerializedName("cb2")
    private String cb2;
    @SerializedName("cBar3")
    private String cBar3;
    @SerializedName("numlinea")
    private Integer numlinea;
    @SerializedName("referencia")
    private String referencia;
    @SerializedName("codalmacen")
    private String codalmacen;
    @SerializedName("unidades")
    private Integer unidades;
    private Boolean status;
    private  boolean finish = false;


    public PedidoDetail() {
        status = false;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public int getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(int numPedido) {
        this.numPedido = numPedido;
    }

    public String getSuPedido() {
        return suPedido;
    }

    public void setSuPedido(String suPedido) {
        this.suPedido = suPedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getTodoRecibido() {
        return todoRecibido;
    }

    public void setTodoRecibido(String todoRecibido) {
        this.todoRecibido = todoRecibido;
    }

    public int getCodProvedor() {
        return codProvedor;
    }

    public void setCodProvedor(int codProvedor) {
        this.codProvedor = codProvedor;
    }

    public int getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(int codArticulo) {
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

    public String getDescricion() {
        return descricion;
    }

    public void setDescricion(String descricion) {
        this.descricion = descricion;
    }

    public int getUnidadesPorCaja() {
        return unidadesPorCaja;
    }

    public void setUnidadesPorCaja(int unidadesPorCaja) {
        this.unidadesPorCaja = unidadesPorCaja;
    }

    public String getNomProveedor() {
        return nomProveedor;
    }

    public void setNomProveedor(String nomProveedor) {
        this.nomProveedor = nomProveedor;
    }

    public String getNif20() {
        return nif20;
    }

    public void setNif20(String nif20) {
        this.nif20 = nif20;
    }

    public double getUnidadesTotales() {
        return unidadesTotales;
    }

    public void setUnidadesTotales(double unidadesTotales) {
        this.unidadesTotales = unidadesTotales;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getCb2() {
        return cb2;
    }

    public void setCb2(String cb2) {
        this.cb2 = cb2;
    }

    public String getcBar3() {
        return cBar3;
    }

    public void setcBar3(String cBar3) {
        this.cBar3 = cBar3;
    }

    public Integer getNumlinea() {
        return numlinea;
    }

    public void setNumlinea(Integer numlinea) {
        this.numlinea = numlinea;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCodalmacen() {
        return codalmacen;
    }

    public void setCodalmacen(String codalmacen) {
        this.codalmacen = codalmacen;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
}
