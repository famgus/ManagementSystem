package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.RequestGetProductDetailBySomeParameters;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.ResponseGetProductDetailBySomeParameters;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateGetProductDetailBySomeParameters;


public class GetProductDetailBySomeParametersTaskController extends AsyncTask<RequestGetProductDetailBySomeParameters, Void, ResponseGetProductDetailBySomeParameters> {
    private IDelegateGetProductDetailBySomeParameters listener;

    public void setListener(IDelegateGetProductDetailBySomeParameters listener) {
        this.listener = listener;
    }

    @Override
    protected ResponseGetProductDetailBySomeParameters doInBackground(RequestGetProductDetailBySomeParameters... params) {
        ResponseGetProductDetailBySomeParameters response = WebServiceControl.getProductDetailBySomeParameters(params[0]);
        return response;
    }

    @Override
    protected void onPostExecute(ResponseGetProductDetailBySomeParameters response) {
        if (listener != null) listener.onGetProduct(response);
    }
}
