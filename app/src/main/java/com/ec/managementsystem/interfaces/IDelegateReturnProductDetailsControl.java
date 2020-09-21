package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.ReturnProductDetailsResponse;

public interface IDelegateReturnProductDetailsControl {
    void onReturnProduct(ReturnProductDetailsResponse response);
}
