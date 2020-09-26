package com.ec.managementsystem.clases.request;

public class RequestGetProductDetailBySomeParameters {
    String productCode;
    String size;
    String color;

    public RequestGetProductDetailBySomeParameters(String productCode, String size, String color) {
        this.productCode = productCode;
        this.size = size;
        this.color = color;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
