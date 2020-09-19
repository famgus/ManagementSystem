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

    @SerializedName("unidadesTotales")
    private String unidadesTotales;
}
