package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.ReturnProductRequest;
import com.ec.managementsystem.clases.responses.ReturnProductListResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateReturnProductControl;


public class ReturnProductTaskController extends AsyncTask<ReturnProductRequest, Void, ReturnProductListResponse> {
    private IDelegateReturnProductControl listener;

    public void setListener(IDelegateReturnProductControl listener) {
        this.listener = listener;
    }

    @Override
    protected ReturnProductListResponse doInBackground(ReturnProductRequest... params) {
        ReturnProductListResponse response = null;
        ReturnProductRequest request = params[0];
        if (request != null) {
            response = WebServiceControl.findReturnProductOrder(request);
        }
        return response;
    }

    @Override
    protected void onPostExecute(ReturnProductListResponse response) {
        if (listener != null) listener.onReturnProduct(response);
    }
}
