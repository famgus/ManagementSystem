package com.ec.managementsystem.clases.request;

import java.io.Serializable;

public class SplitOrderRequest implements Serializable {
    private String seriesNumber, usersCode;
    private int orderNumber;

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getUsersCode() {
        return usersCode;
    }

    public void setUsersCode(String usersCode) {
        this.usersCode = usersCode;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}
