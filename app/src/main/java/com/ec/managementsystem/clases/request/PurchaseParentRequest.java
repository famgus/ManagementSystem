package com.ec.managementsystem.clases.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PurchaseParentRequest implements Serializable {
    @SerializedName("pedido")
    private  PurchaseOrderRequest pedido;

    public  PurchaseParentRequest(){
    }

    public PurchaseOrderRequest getPedido() {
        return pedido;
    }

    public void setPedido(PurchaseOrderRequest pedido) {
        this.pedido = pedido;
    }
}
