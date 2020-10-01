package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.PickingWebService;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class RegisterIdPickingTaskController extends AsyncTask<Void, Void, GenericResponse> {

    private String seriesNumber, idPicking;
    private int orderNumber;
    private IDelegateResponseGeneric<GenericResponse> responseListener;

    public RegisterIdPickingTaskController(String seriesNumber, String idPicking, int orderNumber, IDelegateResponseGeneric<GenericResponse> responseListener) {
        this.seriesNumber = seriesNumber;
        this.idPicking = idPicking;
        this.orderNumber = orderNumber;
        this.responseListener = responseListener;
    }

    @Override
    protected GenericResponse doInBackground(Void... voids) {
        return PickingWebService.registerIdPicking(seriesNumber, orderNumber, idPicking);
    }

    @Override
    protected void onPostExecute(GenericResponse genericResponse) {
        responseListener.onResponse(genericResponse);
    }
}
