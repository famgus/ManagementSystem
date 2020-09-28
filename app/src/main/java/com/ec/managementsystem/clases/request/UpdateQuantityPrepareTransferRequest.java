package com.ec.managementsystem.clases.request;

public class UpdateQuantityPrepareTransferRequest {

    private String seriesNumber, size, color;
    private int orderNumber, productCode, preparedQuantity, vendorCode;

    public UpdateQuantityPrepareTransferRequest(String seriesNumber, String size, String color, int orderNumber, int productCode, int preparedQuantity, int vendorCode) {
        this.seriesNumber = seriesNumber;
        this.size = size;
        this.color = color;
        this.orderNumber = orderNumber;
        this.productCode = productCode;
        this.preparedQuantity = preparedQuantity;
        this.vendorCode = vendorCode;
    }

    public int getVendorCode() {
        return vendorCode;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
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

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public int getPreparedQuantity() {
        return preparedQuantity;
    }

    public void setPreparedQuantity(int preparedQuantity) {
        this.preparedQuantity = preparedQuantity;
    }
}
