package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.PurchaseOrderRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateInsertPurchaseTaskControl;


public class InsertPurchaseTaskController extends AsyncTask<PurchaseOrderRequest, Void, GenericResponse> {
    private IDelegateInsertPurchaseTaskControl listener;

    public void setListener(IDelegateInsertPurchaseTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected GenericResponse doInBackground(PurchaseOrderRequest... params) {
        GenericResponse response = WebServiceControl.InsertPurchaseOrder(params[0]);
        return response;
    }

    @Override
    protected void onPostExecute(GenericResponse genericResponse) {
        if (listener != null) listener.onInsertPedidoResponse(genericResponse);
    }
}
