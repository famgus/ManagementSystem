package com.ec.managementsystem.clases.responses;

import com.ec.managementsystem.clases.AssignedTransferOrder;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TransfersOrderForUserCodeResponse implements Serializable {

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("subordentrasladoList")
    private List<AssignedTransferOrder> suborderTransferList;

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

    public List<AssignedTransferOrder> getSuborderTransferList() {
        return suborderTransferList;
    }

    public void setSuborderTransferList(List<AssignedTransferOrder> suborderTransferList) {
        this.suborderTransferList = suborderTransferList;
    }
}
