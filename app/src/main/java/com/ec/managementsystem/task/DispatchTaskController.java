package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.GetDispatchResponse;
import com.ec.managementsystem.dataAccess.TransferWebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class DispatchTaskController extends AsyncTask<Void, Void, GetDispatchResponse> {

    private String seriesNumber;
    private int orderNumber;
    private IDelegateResponseGeneric<GetDispatchResponse> listener;
    public DispatchTaskController(String seriesNumber, int orderNumber, IDelegateResponseGeneric<GetDispatchResponse> listener) {
        this.seriesNumber = seriesNumber;
        this.orderNumber = orderNumber;
        this.listener = listener;
    }

    @Override
    protected GetDispatchResponse doInBackground(Void... voids) {
        return TransferWebServiceControl.getDispatch(seriesNumber, orderNumber);
    }

    @Override
    protected void onPostExecute(GetDispatchResponse getDispatchResponse) {
        listener.onResponse(getDispatchResponse);
    }
}
