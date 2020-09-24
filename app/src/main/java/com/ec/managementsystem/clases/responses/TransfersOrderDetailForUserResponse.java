package com.ec.managementsystem.clases.responses;

import com.ec.managementsystem.clases.TransferSubOrder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TransfersOrderDetailForUserResponse implements Serializable{

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("subordentrasladoList")
    private List<TransferSubOrder> transferSubOrders;

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

    public List<TransferSubOrder> getTransferSubOrders() {
        return transferSubOrders;
    }

    public void setTransferSubOrders(List<TransferSubOrder> transferSubOrders) {
        this.transferSubOrders = transferSubOrders;
    }
}
