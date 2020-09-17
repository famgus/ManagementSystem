package com.ec.managementsystem.clases.responses;

import com.ec.managementsystem.clases.PedidoDetail;
import com.ec.managementsystem.clases.Vendedores;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class PedidoResponse implements Serializable {

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("pedidoList")
    private List<PedidoDetail> pedidoDetailList;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PedidoDetail> getPedidoDetailList() {
        return pedidoDetailList;
    }

    public void setPedidoDetailList(List<PedidoDetail> pedidoDetailList) {
        this.pedidoDetailList = pedidoDetailList;
    }

    public PedidoResponse() {
    }
}
