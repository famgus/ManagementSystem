package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.FacturasClientResponse;

public interface IDelegateSearchFacturasTaskControl {
    void onSearchFacturaResponse(FacturasClientResponse response);
}
