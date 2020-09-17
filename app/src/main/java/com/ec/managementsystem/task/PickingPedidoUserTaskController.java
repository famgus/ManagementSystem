package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegatePickingPedidoUserTaskControl;


public class PickingPedidoUserTaskController extends AsyncTask<Void, Void, PickingPedidoUserResponse> {
    private IDelegatePickingPedidoUserTaskControl listener;

    public void setListener(IDelegatePickingPedidoUserTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected PickingPedidoUserResponse doInBackground(Void... params) {
        PickingPedidoUserResponse response = WebServiceControl.GetPedidoPickingForUser();
        return response;
    }

    @Override
    protected void onPostExecute(PickingPedidoUserResponse pedidoResponse) {
        if (listener != null) listener.onSuccessPickingPedidoUser(pedidoResponse);
    }
}
