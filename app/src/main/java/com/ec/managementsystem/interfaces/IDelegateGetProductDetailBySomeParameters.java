package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.ResponseGetProductDetailBySomeParameters;

public interface IDelegateGetProductDetailBySomeParameters {
    void onGetProduct(ResponseGetProductDetailBySomeParameters response);
}
