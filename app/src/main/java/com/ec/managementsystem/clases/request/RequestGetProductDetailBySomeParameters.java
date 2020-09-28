package com.ec.managementsystem.clases.request;

public class RequestGetProductDetailBySomeParameters {
    int productCode;
    String size;
    String color;

    public RequestGetProductDetailBySomeParameters(int productCode, String size, String color) {
        this.productCode = productCode;
        this.size = size;
        this.color = color;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
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
