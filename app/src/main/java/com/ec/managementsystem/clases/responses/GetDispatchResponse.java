package com.ec.managementsystem.clases.responses;


import com.ec.managementsystem.clases.TransferPackOff;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetDispatchResponse implements Serializable {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("transferLis")
    private List<TransferPackOff> transferPackOffs;


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

    public List<TransferPackOff> getTransferPackOffs() {
        return transferPackOffs;
    }

    public void setTransferPackOffs(List<TransferPackOff> transferPackOffs) {
        this.transferPackOffs = transferPackOffs;
    }
}
