package com.ec.managementsystem.clases;

import com.ec.managementsystem.moduleView.transfer.PendingOrderDetailFragment;
import com.ec.managementsystem.util.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TransferSubOrder implements Serializable {
    @SerializedName("numseriepc")
    private String seriesNumber;
    @SerializedName("numpedidopc")
    private Integer orderNumber;
    @SerializedName("codarticulo")
    private Integer productCode;
    @SerializedName("referencia")
    private String reference;
    @SerializedName("talla")
    private String size;
    @SerializedName("color")
    private String color;
    @SerializedName("unidadessolicitadas")
    private Integer requestedUnits;
    @SerializedName("unidadespreparadas")
    private Integer preparedUnits;
    @SerializedName("estado")
    private Integer state;
    @SerializedName("codvendedor")
    private Integer vendorCode;
    @SerializedName("vendedorasignado")
    private String assignedVendor;
    @SerializedName("fechaasignación")
    private String assignedDate;
    @SerializedName("fechapreparacion")
    private String preparedDate;
    @SerializedName("ubicaciones")
    private List<ProductUbication> ubications;
    private int rvItem;
    private boolean isRegistered;
    private String originType;
    private String origin;

    public TransferSubOrder() {
        this.rvItem = Utils.ITEM_TYPE;
        this.isRegistered = false;
    }

    public TransferSubOrder(int rvItem) {
        this.rvItem = rvItem;
    }

    public String getOriginType() {
        return originType;
    }

    public void setOriginType(String originType) {
        this.originType = originType;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<ProductUbication> getUbications() {
        return ubications;
    }

    public void setUbications(List<ProductUbication> ubications) {
        this.ubications = ubications;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public int getRvItem() {
        return rvItem;
    }

    public void setRvItem(int rvItem) {
        this.rvItem = rvItem;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getProductCode() {
        return productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    public Integer getRequestedUnits() {
        return requestedUnits;
    }

    public void setRequestedUnits(Integer requestedUnits) {
        this.requestedUnits = requestedUnits;
    }

    public Integer getPreparedUnits() {
        return preparedUnits;
    }

    public void setPreparedUnits(Integer preparedUnits) {
        this.preparedUnits = preparedUnits;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(Integer vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getAssignedVendor() {
        return assignedVendor;
    }

    public void setAssignedVendor(String assignedVendor) {
        this.assignedVendor = assignedVendor;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getPreparedDate() {
        return preparedDate;
    }

    public void setPreparedDate(String preparedDate) {
        this.preparedDate = preparedDate;
    }
}
