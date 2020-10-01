package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.TransferWebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class CreateBoxForDsispatchTaskController extends AsyncTask<String, Void, GenericResponse> {


    IDelegateResponseGeneric<GenericResponse> listener;

    public void setListener(IDelegateResponseGeneric<GenericResponse> listener){
        this.listener = listener;
    }



    @Override
    protected GenericResponse doInBackground(String... params) {
        return TransferWebServiceControl.createBoxForDsispatch(params[0]);
    }

    @Override
    protected void onPostExecute(GenericResponse genericResponse) {
        listener.onResponse(genericResponse);
    }
}
