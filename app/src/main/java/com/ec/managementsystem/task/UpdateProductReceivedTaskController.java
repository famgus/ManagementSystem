package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.UpdateProductReceivedRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.TransferWebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class UpdateProductReceivedTaskController extends AsyncTask<UpdateProductReceivedRequest, Void, GenericResponse> {

    private IDelegateResponseGeneric<GenericResponse> responseListener;

    public void setResponseListener(IDelegateResponseGeneric<GenericResponse> responseListener) {
        this.responseListener = responseListener;
    }

    @Override
    protected GenericResponse doInBackground(UpdateProductReceivedRequest... updateProductReceivedRequests) {
        return TransferWebServiceControl.updateProductReceived(updateProductReceivedRequests[0]);
    }

    @Override
    protected void onPostExecute(GenericResponse genericResponse) {
        responseListener.onResponse(genericResponse);
    }
}
