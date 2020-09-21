package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReturnProductListResponse extends GenericResponse {

    @SerializedName("pedidoList")
    private List<ReturnProductResponse> pedidoList;

    public List<ReturnProductResponse> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<ReturnProductResponse> pedidoList) {
        this.pedidoList = pedidoList;
    }
}
