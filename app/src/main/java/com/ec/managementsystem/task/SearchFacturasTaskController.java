package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.responses.FacturasClientResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateSearchFacturasTaskControl;


public class SearchFacturasTaskController extends AsyncTask<PickingRequest, Void, FacturasClientResponse> {
    private IDelegateSearchFacturasTaskControl listener;

    public void setListener(IDelegateSearchFacturasTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected FacturasClientResponse doInBackground(PickingRequest... params) {
        PickingRequest request = params[0];
        FacturasClientResponse response = WebServiceControl.SearchFacturasSend(request.getNumberPedido(), request.getNumberSerie());
        return response;
    }

    @Override
    protected void onPostExecute(FacturasClientResponse response) {
        if (listener != null) listener.onSearchFacturaResponse(response);
    }
}
