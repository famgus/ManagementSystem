package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.PendingTransferOrderResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class PendingTransferOrderTaskController extends AsyncTask<Void, Void, PendingTransferOrderResponse> {

    private IDelegateResponseGeneric<PendingTransferOrderResponse> listener;

    public void setListener(IDelegateResponseGeneric<PendingTransferOrderResponse> listener) {
        this.listener = listener;
    }

    @Override
    protected PendingTransferOrderResponse doInBackground(Void... voids) {
        return WebServiceControl.getPendingTransferOrders();
    }

    @Override
    protected void onPostExecute(PendingTransferOrderResponse genericResponse) {
        if (listener != null) listener.onResponse(genericResponse);
    }
}
