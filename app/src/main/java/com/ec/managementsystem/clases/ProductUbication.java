package com.ec.managementsystem.clases;

import com.ec.managementsystem.moduleView.transfer.PendingOrderDetailFragment;
import com.ec.managementsystem.util.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductUbication implements Serializable {
    @SerializedName("codubicacion")
    private String ubicationCode;
    @SerializedName("codbarrascajamaster")
    private String boxMasterBarCode;
    @SerializedName("cantidadarticulo")
    private String availableQuantity;
    private int rvItem;

    public ProductUbication() {
        this.rvItem = Utils.ITEM_TYPE;
    }

    public ProductUbication(int rvItem) {
        this.rvItem = rvItem;
    }

    public int getRvItem() {
        return rvItem;
    }

    public void setRvItem(int rvItem) {
        this.rvItem = rvItem;
    }

    public String getUbicationCode() {
        return ubicationCode;
    }

    public void setUbicationCode(String ubicationCode) {
        this.ubicationCode = ubicationCode;
    }

    public String getBoxMasterBarCode() {
        return boxMasterBarCode;
    }

    public void setBoxMasterBarCode(String boxMasterBarCode) {
        this.boxMasterBarCode = boxMasterBarCode;
    }

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
