package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;

public interface IDelegatePickingPedidoUserTaskControl {
    void onSuccessPickingPedidoUser(PickingPedidoUserResponse response);
}
