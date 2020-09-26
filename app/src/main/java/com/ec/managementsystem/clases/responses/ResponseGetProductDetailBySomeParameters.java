package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

public class ResponseGetProductDetailBySomeParameters extends GenericResponse {

    @SerializedName("codArticulo")
    int itemCode;

    @SerializedName("refproveedor")
    String providerReference;

    @SerializedName("descripcion")
    String description;

    @SerializedName("codBarras")
    String barcode1;

    @SerializedName("codBarras2")
    String barcode2;

    @SerializedName("codBarras3")
    String barcode3;

    @SerializedName("talla")
    String size;

    @SerializedName("color")
    String color;

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public String getProviderReference() {
        return providerReference;
    }

    public void setProviderReference(String providerReference) {
        this.providerReference = providerReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode1() {
        return barcode1;
    }

    public void setBarcode1(String barcode1) {
        this.barcode1 = barcode1;
    }

    public String getBarcode2() {
        return barcode2;
    }

    public void setBarcode2(String barcode2) {
        this.barcode2 = barcode2;
    }

    public String getBarcode3() {
        return barcode3;
    }

    public void setBarcode3(String barcode3) {
        this.barcode3 = barcode3;
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
