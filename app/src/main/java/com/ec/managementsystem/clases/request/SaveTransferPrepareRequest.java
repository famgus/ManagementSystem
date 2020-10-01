package com.ec.managementsystem.clases.request;

public class SaveTransferPrepareRequest {

    private String seriesNumber, size, color, cart, cajamastertraslado, formatotraslado, cajamasterorigen, ubicacionorigen;
    private int orderNumber, productCode, unidades;

    public SaveTransferPrepareRequest(String seriesNumber, String size, String color, String cart, String cajamastertraslado, String formatotraslado, String cajamasterorigen, String ubicacionorigen, int orderNumber, int productCode, int unidades) {
        this.seriesNumber = seriesNumber;
        this.size = size;
        this.color = color;
        this.cart = cart;
        this.cajamastertraslado = cajamastertraslado;
        this.formatotraslado = formatotraslado;
        this.cajamasterorigen = cajamasterorigen;
        this.ubicacionorigen = ubicacionorigen;
        this.orderNumber = orderNumber;
        this.productCode = productCode;
        this.unidades = unidades;
    }

    public String getCart() {
        return cart;
    }

    public String getCajamastertraslado() {
        return cajamastertraslado;
    }

    public String getFormatotraslado() {
        return formatotraslado;
    }

    public String getCajamasterorigen() {
        return cajamasterorigen;
    }

    public String getUbicacionorigen() {
        return ubicacionorigen;
    }

    public int getUnidades() {
        return unidades;
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
}
