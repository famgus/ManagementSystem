package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.BoxMasterRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateBoxMasterTaskControl;


public class BoxMasterTaskController extends AsyncTask<BoxMasterRequest, Void, GenericResponse> {
    private IDelegateBoxMasterTaskControl listener;

    public void setListener(IDelegateBoxMasterTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected GenericResponse doInBackground(BoxMasterRequest... params) {
        BoxMasterRequest request = params[0];
        if (request != null) {
            GenericResponse response = null;
            switch (request.getActionPath()) {
                case 1:
                    response = WebServiceControl.CreateIngreso(request);
                    break;
                case 2:
                    response = WebServiceControl.CreateTraslado(request);
                    break;
                case 3:
                    response = WebServiceControl.CreateDespacho(request);
                    break;
                case 4:
                    response = WebServiceControl.ValidateBoxMasterCodeBar(request.getBarCodeBoxMasterOrigin(), 1);
                    response.setPath(request.getActionPath());
                    break;
                case 5:
                    response = WebServiceControl.ValidateBoxMasterCodeBar(request.getBarCodeBoxMasterOrigin(), 0);
                    response.setPath(request.getActionPath());
                    break;
                case 6:
                    response = WebServiceControl.ValidateLocationCodeBar(request.getBarCodeBoxMasterOrigin());
                    break;
            }
            return response;
        }
        return null;
    }

    @Override
    protected void onPostExecute(GenericResponse response) {
        if (listener != null) listener.onBoxMasterResponse(response);
    }
}
