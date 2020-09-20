package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.ReturnProductChangeRequest;
import com.ec.managementsystem.clases.responses.ReturnProductChangeResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateReturnProductChangeControl;


public class ReturnProductChangeTaskController extends AsyncTask<ReturnProductChangeRequest, Void, ReturnProductChangeResponse> {
    private IDelegateReturnProductChangeControl listener;

    public void setListener(IDelegateReturnProductChangeControl listener) {
        this.listener = listener;
    }

    @Override
    protected ReturnProductChangeResponse doInBackground(ReturnProductChangeRequest... params) {
        ReturnProductChangeResponse response = null;
        ReturnProductChangeRequest request = params[0];
        if (request != null) {
            response = WebServiceControl.saveReturnProduct(request);
        }
        return response;
    }

    @Override
    protected void onPostExecute(ReturnProductChangeResponse response) {
        if (listener != null) listener.onReturnProduct(response);
    }
}
