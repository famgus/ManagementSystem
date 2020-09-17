package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.responses.ListPickingPedidoDetailResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IListenerPickingPedidoDetail;


public class PickingPedidoDetailTaskController extends AsyncTask<PickingRequest, Void, ListPickingPedidoDetailResponse> {
    private IListenerPickingPedidoDetail listener;

    public void setListener(IListenerPickingPedidoDetail listener) {
        this.listener = listener;
    }

    @Override
    protected ListPickingPedidoDetailResponse doInBackground(PickingRequest... params) {
        PickingRequest request = params[0];
        return WebServiceControl.GetPickingPedidoDetail(request.getNumberSerie(), request.getNumberPedido());
    }

    @Override
    protected void onPostExecute(ListPickingPedidoDetailResponse response) {
        if (listener != null) listener.onPickingPedidoDetail(response);
    }
}
