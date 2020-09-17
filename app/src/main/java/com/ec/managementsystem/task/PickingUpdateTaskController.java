package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;


public class PickingUpdateTaskController extends AsyncTask<PickingRequest, Void, GenericResponse> {
    private IDelegateUpdatePickingControl listener;

    public void setListener(IDelegateUpdatePickingControl listener) {
        this.listener = listener;
    }

    @Override
    protected GenericResponse doInBackground(PickingRequest... params) {
        PickingRequest request = params[0];
        if (request.getPath() == 1) {
            return WebServiceControl.UpdatePickingPedidoState(request.getNumberSerie(), request.getNumberPedido(), request.getState());
        } else if (request.getPath() == 2) {
            return WebServiceControl.UpdatePicking(request.getNumberSerie(), request.getNumberPedido(), request.getCodeArticle(), request.getQuantity(), request.getBarCodeBoxMaster(), request.getBarCodeLocation());
        }
        return null;
    }

    @Override
    protected void onPostExecute(GenericResponse response) {
        if (listener != null) listener.onSuccessUpdate(response);
    }
}
