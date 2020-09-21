package com.ec.managementsystem.clases.request;

public class ReturnProductRequest {
    private int order;

    public ReturnProductRequest(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
