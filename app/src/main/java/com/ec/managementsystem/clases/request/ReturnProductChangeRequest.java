package com.ec.managementsystem.clases.request;

public class ReturnProductChangeRequest {
    private String detail;

    public ReturnProductChangeRequest(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
