package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

public class ReturnProductDetailUbication {

    @SerializedName("codubicacion")
    private String ubicationCode;

    @SerializedName("codbarrascajamaster")
    private String barCodeMasterBox;

    @SerializedName("cantidadarticulo")
    private String quantity;

    public String getUbicationCode() {
        return ubicationCode;
    }

    public void setUbicationCode(String ubicationCode) {
        this.ubicationCode = ubicationCode;
    }

    public String getBarCodeMasterBox() {
        return barCodeMasterBox;
    }

    public void setBarCodeMasterBox(String barCodeMasterBox) {
        this.barCodeMasterBox = barCodeMasterBox;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
