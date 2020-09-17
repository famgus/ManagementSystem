package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ListMotivesResponse implements Serializable {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("listMotives")
    private List<MotivesResponse> listMotives;

    public ListMotivesResponse() {
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

    public List<MotivesResponse> getListMotives() {
        return listMotives;
    }

    public void setListMotives(List<MotivesResponse> listMotives) {
        this.listMotives = listMotives;
    }
}
