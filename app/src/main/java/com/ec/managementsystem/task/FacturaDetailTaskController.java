package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.FacturaDetailRequest;
import com.ec.managementsystem.clases.responses.ListFacturasDetasilResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IFacturaDetailTaskControl;


public class FacturaDetailTaskController extends AsyncTask<FacturaDetailRequest, Void, ListFacturasDetasilResponse> {
    private IFacturaDetailTaskControl listener;

    public void setListener(IFacturaDetailTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected ListFacturasDetasilResponse doInBackground(FacturaDetailRequest... params) {
        ListFacturasDetasilResponse response = WebServiceControl.GetFacturaDetail(params[0].getNumberSerie(), params[0].getNumberAlbaran());
        return response;
    }

    @Override
    protected void onPostExecute(ListFacturasDetasilResponse response) {
        if (listener != null) listener.onFacturaDetail(response);
    }
}
