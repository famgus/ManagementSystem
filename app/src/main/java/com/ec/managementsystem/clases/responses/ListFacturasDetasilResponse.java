package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ListFacturasDetasilResponse implements Serializable {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("listFacturaDetail")
    private List<FacturasDetasilResponse> listFacturaDetail;
    int action;
    public ListFacturasDetasilResponse(){}

    public List<FacturasDetasilResponse> getListFacturaDetail() {
        return listFacturaDetail;
    }
    public void setListFacturaDetail(List<FacturasDetasilResponse> listFacturaDetail) {
        this.listFacturaDetail = listFacturaDetail;
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
