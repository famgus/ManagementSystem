package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

/**
 * Created by Gorky Muñoz on 21/9/2020.
 * Indra
 * gamunozg@indracompany.com
 */
public class ValidateBoxMasterCodeBar extends AsyncTask<Void, Void, GenericResponse> {

    private String barCode;
    private int state;
    private String methodName;
    private IDelegateResponseGeneric<GenericResponse> listener;

    public ValidateBoxMasterCodeBar(String barCode, int state, String methodName, IDelegateResponseGeneric<GenericResponse> listener) {
        this.barCode = barCode;
        this.state = state;
        this.methodName = methodName;
        this.listener = listener;
    }

    @Override
    protected GenericResponse doInBackground(Void... voids) {
        return WebServiceControl.validateBoxMasterCodeBar(barCode, state, methodName);
    }

    @Override
    protected void onPostExecute(GenericResponse genericResponse) {
        listener.onResponse(genericResponse);
    }
}
