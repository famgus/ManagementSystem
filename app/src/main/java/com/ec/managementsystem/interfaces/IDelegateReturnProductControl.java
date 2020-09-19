package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.ReturnProductListResponse;

public interface IDelegateReturnProductControl {
    void onReturnProduct(ReturnProductListResponse response);
}
