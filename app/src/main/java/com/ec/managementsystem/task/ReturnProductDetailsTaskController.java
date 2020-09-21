package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.ReturnProductDetailsRequest;
import com.ec.managementsystem.clases.responses.ReturnProductDetailsResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateReturnProductDetailsControl;


public class ReturnProductDetailsTaskController extends AsyncTask<ReturnProductDetailsRequest, Void, ReturnProductDetailsResponse> {
    private IDelegateReturnProductDetailsControl listener;

    public void setListener(IDelegateReturnProductDetailsControl listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(ReturnProductDetailsResponse response) {
        if (listener != null) listener.onReturnProduct(response);
    }

    @Override
    protected ReturnProductDetailsResponse doInBackground(ReturnProductDetailsRequest... params) {
        ReturnProductDetailsResponse response = null;
        ReturnProductDetailsRequest request = params[0];
        if (request != null) {
            response = WebServiceControl.findProductById(request);
        }
        return response;
    }
}
