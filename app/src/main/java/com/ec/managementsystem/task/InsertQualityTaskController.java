package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.QualityControlRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateInsertQualityTaskControl;


public class InsertQualityTaskController extends AsyncTask<QualityControlRequest, Void, GenericResponse> {
    private IDelegateInsertQualityTaskControl listener;

    public void setListener(IDelegateInsertQualityTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected GenericResponse doInBackground(QualityControlRequest... params) {
        QualityControlRequest request = params[0];
        return WebServiceControl.InsertQualityControl(request);
    }

    @Override
    protected void onPostExecute(GenericResponse response) {
        if (listener != null) listener.onInsertQualityResponse(response);
    }
}
