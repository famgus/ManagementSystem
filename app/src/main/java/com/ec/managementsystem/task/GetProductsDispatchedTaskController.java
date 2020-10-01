package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.GetProductsDispatchedResponse;
import com.ec.managementsystem.dataAccess.TransferWebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class GetProductsDispatchedTaskController extends AsyncTask<Void, Void, GetProductsDispatchedResponse> {

    private String seriesNumber;
    private int orderNumber;
    private IDelegateResponseGeneric<GetProductsDispatchedResponse> listener;

    public GetProductsDispatchedTaskController(String seriesNumber, int orderNumber, IDelegateResponseGeneric<GetProductsDispatchedResponse> listener) {
        this.seriesNumber = seriesNumber;
        this.orderNumber = orderNumber;
        this.listener = listener;
    }

    @Override
    protected GetProductsDispatchedResponse doInBackground(Void... voids) {
        return TransferWebServiceControl.getProductsDispatched(seriesNumber, orderNumber);
    }

    @Override
    protected void onPostExecute(GetProductsDispatchedResponse getProductsDispatchedResponse) {
        listener.onResponse(getProductsDispatchedResponse);
    }
}
