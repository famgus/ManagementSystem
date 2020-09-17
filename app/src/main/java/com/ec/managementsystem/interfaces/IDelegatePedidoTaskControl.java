package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.PedidoResponse;

public interface IDelegatePedidoTaskControl {
    void onPedidoResponse(PedidoResponse response);
}
