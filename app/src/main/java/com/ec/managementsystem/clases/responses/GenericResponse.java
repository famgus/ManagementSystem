package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class GenericResponse implements Serializable {

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    private  int path;

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

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public GenericResponse() {
    }
}
