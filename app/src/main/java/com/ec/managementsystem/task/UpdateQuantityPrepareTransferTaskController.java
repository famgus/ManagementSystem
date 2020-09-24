package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.UpdateQuantityPrepareTransferRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class UpdateQuantityPrepareTransferTaskController extends AsyncTask<UpdateQuantityPrepareTransferRequest, Void, GenericResponse> {

    IDelegateResponseGeneric<GenericResponse> listener;

    public void setListener(IDelegateResponseGeneric<GenericResponse> listener) {
        this.listener = listener;
    }

    @Override
    protected GenericResponse doInBackground(UpdateQuantityPrepareTransferRequest... updateQuantityPrepareTransferRequests) {
        return WebServiceControl.updateQuantityPrepareTransfer(updateQuantityPrepareTransferRequests[0]);
    }

    @Override
    protected void onPostExecute(GenericResponse genericResponse) {
        if(listener != null) listener.onResponse(genericResponse);
    }
}
