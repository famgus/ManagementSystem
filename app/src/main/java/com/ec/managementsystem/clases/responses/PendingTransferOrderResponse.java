package com.ec.managementsystem.clases.responses;

import com.ec.managementsystem.clases.PendingTransferOrder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PendingTransferOrderResponse implements Serializable {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("trasladoList")
    private List<PendingTransferOrder> trasladoList;

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

    public List<PendingTransferOrder> getTrasladoList() {
        return trasladoList;
    }

    public void setTrasladoList(List<PendingTransferOrder> trasladoList) {
        this.trasladoList = trasladoList;
    }
}
