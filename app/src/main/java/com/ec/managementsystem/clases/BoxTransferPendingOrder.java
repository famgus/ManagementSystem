package com.ec.managementsystem.clases;

import com.ec.managementsystem.util.Utils;

import java.util.List;

public class BoxTransferPendingOrder {
    private String boxCode;
    private int totalRegisteredProducts;
    private List<TransferSubOrder> registeredProducts;
    private int rvType;

    public BoxTransferPendingOrder(String boxCode, int totalRegisteredProducts, List<TransferSubOrder> registeredProducts) {
        this.boxCode = boxCode;
        this.totalRegisteredProducts = totalRegisteredProducts;
        this.registeredProducts = registeredProducts;
        this.rvType = Utils.ITEM_TYPE;
    }

    public BoxTransferPendingOrder() {
        this.rvType = Utils.HEADER_TYPE;
    }

    public int getRvType() {
        return rvType;
    }

    public void setRvType(int rvType) {
        this.rvType = rvType;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public int getTotalRegisteredProducts() {
        return totalRegisteredProducts;
    }

    public void setTotalRegisteredProducts(int totalRegisteredProducts) {
        this.totalRegisteredProducts = totalRegisteredProducts;
    }

    public List<TransferSubOrder> getRegisteredProducts() {
        return registeredProducts;
    }

    public void setRegisteredProducts(List<TransferSubOrder> registeredProducts) {
        this.registeredProducts = registeredProducts;
    }

    @Override
    public String toString() {
        return "BoxTransferPendingOrder{" +
                "boxCode='" + boxCode + '\'' +
                ", totalRegisteredProducts=" + totalRegisteredProducts +
                ", registeredProducts=" + registeredProducts +
                '}';
    }
}
