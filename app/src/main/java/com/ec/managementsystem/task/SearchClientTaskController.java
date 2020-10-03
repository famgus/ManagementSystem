package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.FacturaRequest;
import com.ec.managementsystem.clases.responses.FacturasClientResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateSearchClientTaskControl;


public class SearchClientTaskController extends AsyncTask<FacturaRequest, Void, FacturasClientResponse> {
    private IDelegateSearchClientTaskControl listener;

    public void setListener(IDelegateSearchClientTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected FacturasClientResponse doInBackground(FacturaRequest... params) {
        String numberClient = params[0].getParametro();
        Integer opcion = params[0].getOpcion();
        FacturasClientResponse response = WebServiceControl.GetClientFacturasPacking(numberClient,opcion);
        return response;
    }

    @Override
    protected void onPostExecute(FacturasClientResponse response) {
        if (listener != null) listener.onSearchClientResponse(response);
    }
}
