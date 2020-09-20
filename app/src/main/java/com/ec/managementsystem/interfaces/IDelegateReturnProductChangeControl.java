package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.ReturnProductChangeResponse;

public interface IDelegateReturnProductChangeControl {
    void onReturnProduct(ReturnProductChangeResponse response);
}
