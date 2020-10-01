package com.ec.managementsystem.clases.responses;

import com.google.gson.annotations.SerializedName;

public class ResponseGetProductDetailBySomeParameters extends GenericResponse {

    @SerializedName("productDetail")
    private GetProductDetailBySomeParameters productDetail;

    public GetProductDetailBySomeParameters getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(GetProductDetailBySomeParameters productDetail) {
        this.productDetail = productDetail;
    }
}
