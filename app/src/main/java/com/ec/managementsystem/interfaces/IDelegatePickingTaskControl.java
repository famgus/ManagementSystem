package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.ListPedidoPickingResponse;

public interface IDelegatePickingTaskControl {
    void onListPickingResponse(ListPedidoPickingResponse response);
}
