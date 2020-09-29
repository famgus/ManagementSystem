package com.ec.managementsystem.clases.request;

public class UpdateProductReceivedRequest {
    String seriesNumber, size, color;
    int orderNumber, productCode, receivedQuantity;

    public UpdateProductReceivedRequest(String seriesNumber, String size, String color, int orderNumber, int productCode, int receivedQuantity) {
        this.seriesNumber = seriesNumber;
        this.size = size;
        this.color = color;
        this.orderNumber = orderNumber;
        this.productCode = productCode;
        this.receivedQuantity = receivedQuantity;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getProductCode() {
        return productCode;
    }

    public int getReceivedQuantity() {
        return receivedQuantity;
    }
}
