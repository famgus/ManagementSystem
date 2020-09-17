package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.FacturasClientResponse;

public interface IDelegateSearchClientTaskControl {
    void onSearchClientResponse(FacturasClientResponse response);
}
