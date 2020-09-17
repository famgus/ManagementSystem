package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ListPedidoPickingResponse implements Serializable {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("pedidoPickingList")
    private List<PedidoPickingResponse> pedidoPickingList;
    int action;
    public ListPedidoPickingResponse(){}

    public List<PedidoPickingResponse> getPedidoPickingList() {
        return pedidoPickingList;
    }

    public void setPedidoPickingList(List<PedidoPickingResponse> pedidoPickingList) {
        this.pedidoPickingList = pedidoPickingList;
    }

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

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
