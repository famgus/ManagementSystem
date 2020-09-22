package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.TransfersOrderDetailForUserResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class TransfersOrderDetailForUserTaskController extends AsyncTask<Void, Void, TransfersOrderDetailForUserResponse> {

    private String seriesNumber;
    private int orderNumber, vendorCode;
    private IDelegateResponseGeneric<TransfersOrderDetailForUserResponse> responseListener;

    public TransfersOrderDetailForUserTaskController(String seriesNumber, int orderNumber, int vendorCode, IDelegateResponseGeneric<TransfersOrderDetailForUserResponse> responseListener) {
        this.seriesNumber = seriesNumber;
        this.orderNumber = orderNumber;
        this.vendorCode = vendorCode;
        this.responseListener = responseListener;
    }

    @Override
    protected TransfersOrderDetailForUserResponse doInBackground(Void... voids) {
        return WebServiceControl.getTransfersOrderDetailForUser(seriesNumber, orderNumber, vendorCode);
    }

    @Override
    protected void onPostExecute(TransfersOrderDetailForUserResponse transfersOrderDetailForUserResponse) {
        if (responseListener != null)
            responseListener.onResponse(transfersOrderDetailForUserResponse);
    }
}
