package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.PedidoRequest;
import com.ec.managementsystem.clases.responses.PedidoResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegatePedidoTaskControl;


public class PedidoTaskController extends AsyncTask<PedidoRequest, Void, PedidoResponse> {
    private IDelegatePedidoTaskControl listener;

    public void setListener(IDelegatePedidoTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected PedidoResponse doInBackground(PedidoRequest... params) {
        int numero = params[0].getNumero();
        PedidoResponse response = WebServiceControl.GetPedidoDetails(numero);
        return response;
    }

    @Override
    protected void onPostExecute(PedidoResponse pedidoResponse) {
        if (listener != null) listener.onPedidoResponse(pedidoResponse);
    }
}
