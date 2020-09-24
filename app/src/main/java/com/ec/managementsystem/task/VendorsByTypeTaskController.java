package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.VendorsByTypeResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class VendorsByTypeTaskController extends AsyncTask<Integer, Void, VendorsByTypeResponse> {

    private IDelegateResponseGeneric<VendorsByTypeResponse> listener;

    public VendorsByTypeTaskController(IDelegateResponseGeneric<VendorsByTypeResponse> listener) {
        this.listener = listener;
    }

    @Override
    protected VendorsByTypeResponse doInBackground(Integer... params) {
        Integer userType = params[0];
        return WebServiceControl.getVendorByType(userType);
    }

    @Override
    protected void onPostExecute(VendorsByTypeResponse transferOrderResponse) {
        if (listener != null) listener.onResponse(transferOrderResponse);
    }
}
