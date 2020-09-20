package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReturnProductDetail {

    @SerializedName("codArticulo")
    private String itemCode;

    @SerializedName("refproveedor")
    private String providerCode;

    @SerializedName("descripcion")
    private String description;

    @SerializedName("codBarras")
    private String barCode;

    @SerializedName("talla")
    private String size;

    @SerializedName("color")
    private String color;

    @SerializedName("ubicaciones")
    private List<ReturnProductDetailUbication> ubication;

    @SerializedName("fechaaplicacion")
    private String applicationDate;

    @SerializedName("fechapreparacion")
    private String preparationDate;

    @SerializedName("cantidad")
    private String quantity;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getPreparationDate() {
        return preparationDate;
    }

    public void setPreparationDate(String preparationDate) {
        this.preparationDate = preparationDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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

    public List<ReturnProductDetailUbication> getUbication() {
        return ubication;
    }

    public void setUbication(List<ReturnProductDetailUbication> ubication) {
        this.ubication = ubication;
    }
}
