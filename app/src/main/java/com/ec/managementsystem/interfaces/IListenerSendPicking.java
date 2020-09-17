package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.PedidoPickingResponse;

public interface IListenerSendPicking {
    void onItemClick(PedidoPickingResponse item);
}
