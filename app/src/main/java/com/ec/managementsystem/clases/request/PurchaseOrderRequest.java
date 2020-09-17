package com.ec.managementsystem.clases.request;

import com.ec.managementsystem.clases.BoxMaster;
import com.ec.managementsystem.clases.PedidoDetail;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PurchaseOrderRequest implements Serializable {
    @SerializedName("boxMasterList")
    List<BoxMaster> boxMasterList;
    @SerializedName("pedidoList")
    List<PedidoDetail> pedidoList;

    public PurchaseOrderRequest() {
    }

    public List<PedidoDetail> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<PedidoDetail> pedidoList) {
        this.pedidoList = pedidoList;
    }

    public List<BoxMaster> getBoxMasterList() {
        return boxMasterList;
    }

    public void setBoxMasterList(List<BoxMaster> boxMasterList) {
        this.boxMasterList = boxMasterList;
    }
}
