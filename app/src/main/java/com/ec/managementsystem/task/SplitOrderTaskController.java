package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.SplitOrderRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class SplitOrderTaskController extends AsyncTask<SplitOrderRequest, Void, GenericResponse> {

    private IDelegateResponseGeneric<GenericResponse> responseGenericListener;

    public SplitOrderTaskController(IDelegateResponseGeneric<GenericResponse> responseGenericListener) {
        this.responseGenericListener = responseGenericListener;
    }

    @Override
    protected GenericResponse doInBackground(SplitOrderRequest... splitOrderRequests) {
        SplitOrderRequest param = splitOrderRequests[0];
        return WebServiceControl.splitOrder(param.getSeriesNumber(), param.getOrderNumber(), param.getUsersCode());
    }

    @Override
    protected void onPostExecute(GenericResponse genericResponse) {
        if (responseGenericListener != null) responseGenericListener.onResponse(genericResponse);
    }
}
