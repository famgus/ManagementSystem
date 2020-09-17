package com.ec.managementsystem.clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PedidoDetailSmall implements Serializable {

    @SerializedName("suPedido")
    private String suPedido;
    @SerializedName("codArticulo")
    private int codArticulo;
    @SerializedName("talla")
    private String talla;
    @SerializedName("color")
    private String color;
    @SerializedName("descricion")
    private String descricion;
    @SerializedName("nomProveedor")
    private String nomProveedor;
    @SerializedName("codigoBarras")
    private String codigoBarra;
    @SerializedName("cb2")
    private String cb2;
    @SerializedName("cBar3")
    private String cBar3;

    public PedidoDetailSmall(){}

    public String getSuPedido() {
        return suPedido;
    }

    public void setSuPedido(String suPedido) {
        this.suPedido = suPedido;
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

    public String getNomProveedor() {
        return nomProveedor;
    }

    public void setNomProveedor(String nomProveedor) {
        this.nomProveedor = nomProveedor;
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
}
