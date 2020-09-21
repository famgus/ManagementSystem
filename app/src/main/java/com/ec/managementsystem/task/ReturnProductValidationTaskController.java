package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.ReturnProductValidationRequest;
import com.ec.managementsystem.clases.responses.ReturnProductChangeResponse;
import com.ec.managementsystem.clases.responses.ReturnProductValidationResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateReturnProductValidationControl;


public class ReturnProductValidationTaskController extends AsyncTask<ReturnProductValidationRequest, Void, ReturnProductValidationResponse> {
    private IDelegateReturnProductValidationControl listener;

    public void setListener(IDelegateReturnProductValidationControl listener) {
        this.listener = listener;
    }

    @Override
    protected ReturnProductValidationResponse doInBackground(ReturnProductValidationRequest... params) {
        ReturnProductValidationRequest returnProductValidationRequest = params[0];
        ReturnProductValidationResponse response = null;
        if (returnProductValidationRequest != null) {
            response = WebServiceControl.validationMasterBoxUbication(returnProductValidationRequest);
        }
        return response;
    }

    @Override
    protected void onPostExecute(ReturnProductValidationResponse response) {
        if (listener != null) listener.onValidationMasterBoxUbication(response);
    }

}
