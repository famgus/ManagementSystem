package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ListPickingPedidoDetailResponse implements Serializable {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("listPickingDetail")
    private List<PickingPedidoDetailResponse> listPickingDetail;

    public ListPickingPedidoDetailResponse(){}

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

    public List<PickingPedidoDetailResponse> getListPickingDetail() {
        return listPickingDetail;
    }

    public void setListPickingDetail(List<PickingPedidoDetailResponse> listPickingDetail) {
        this.listPickingDetail = listPickingDetail;
    }
}
