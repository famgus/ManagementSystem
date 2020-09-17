package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;

public interface IListenerPicking {
    void onItemClick(PickingPedidoUserResponse item);
}
