package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.FacturasClientResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateSearchClientTaskControl;


public class SearchClientTaskController extends AsyncTask<String, Void, FacturasClientResponse> {
    private IDelegateSearchClientTaskControl listener;

    public void setListener(IDelegateSearchClientTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected FacturasClientResponse doInBackground(String... params) {
        String numberClient = params[0];
        FacturasClientResponse response = WebServiceControl.GetClientFacturasPacking(numberClient);
        return response;
    }

    @Override
    protected void onPostExecute(FacturasClientResponse response) {
        if (listener != null) listener.onSearchClientResponse(response);
    }
}
