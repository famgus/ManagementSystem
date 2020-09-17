package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.PickingPedidoDetailResponse;

public interface IListenerPickingDetail {
    void onItemClick(PickingPedidoDetailResponse item);

    void onItemActionClick(PickingPedidoDetailResponse item);

    void onItemRemoveClick(PickingPedidoDetailResponse item);
}
