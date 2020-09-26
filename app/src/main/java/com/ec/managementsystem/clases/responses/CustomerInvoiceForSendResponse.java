package com.ec.managementsystem.clases.responses;

import com.ec.managementsystem.clases.GuideModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CustomerInvoiceForSendResponse implements Serializable {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("guiaModels")
    private List<GuideModel> invoices;

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

    public List<GuideModel> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<GuideModel> invoices) {
        this.invoices = invoices;
    }
}
