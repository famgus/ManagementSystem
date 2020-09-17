package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.BarCodeRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateBarCodeTaskControl;


public class BarCodeTaskController extends AsyncTask<BarCodeRequest, Void, GenericResponse> {
    private IDelegateBarCodeTaskControl listener;

    public void setListener(IDelegateBarCodeTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected GenericResponse doInBackground(BarCodeRequest... params) {
        GenericResponse response = WebServiceControl.CreateBarCodeForBoxMaster(params[0]);
        return response;
    }

    @Override
    protected void onPostExecute(GenericResponse response) {
        if (listener != null) listener.onSuccessCreateBarCodes(response);
    }
}
