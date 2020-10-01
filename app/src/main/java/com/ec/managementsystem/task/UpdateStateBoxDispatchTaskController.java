package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.TransferWebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class UpdateStateBoxDispatchTaskController extends AsyncTask<Void, Void, GenericResponse> {

    String barCodes, transport;
    IDelegateResponseGeneric<GenericResponse> listener;

    public UpdateStateBoxDispatchTaskController(String barCodes, String transport, IDelegateResponseGeneric<GenericResponse> listener) {
        this.barCodes = barCodes;
        this.transport = transport;
        this.listener = listener;
    }

    @Override
    protected GenericResponse doInBackground(Void... voids) {
        return TransferWebServiceControl.updateStateBoxDispatch(barCodes, transport);
    }

    @Override
    protected void onPostExecute(GenericResponse genericResponse) {
        listener.onResponse(genericResponse);
    }
}
