package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.ProductoResponse;

public interface IDelegateProductTaskControl {
    void onProductResponse(ProductoResponse response);
}
