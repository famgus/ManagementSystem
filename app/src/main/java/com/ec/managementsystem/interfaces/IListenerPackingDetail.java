package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.FacturasDetasilResponse;

public interface IListenerPackingDetail {
    void onItemClick(FacturasDetasilResponse item);

    void onItemActionClick(FacturasDetasilResponse item);

    void onItemRemoveClick(FacturasDetasilResponse item);
}
