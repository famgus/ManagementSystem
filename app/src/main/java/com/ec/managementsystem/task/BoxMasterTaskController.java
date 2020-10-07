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
                case 1://Create Ingreso
                    response = WebServiceControl.CreateIngreso(request);
                    break;
                case 2://Create Reubicacion Article
                    response = WebServiceControl.CreateReubicacionArticle(request);
                    break;
                case 3://Create Despacho
                    response = WebServiceControl.CreateDespacho(request);
                    break;
                case 4://With CODBARMAS
                    response = WebServiceControl.ValidateBoxMasterCodeBar(request.getBarCodeBoxMasterOrigin(), 1);
                    response.setPath(request.getActionPath());
                    break;
                case 5://With CODBARMAS
                    response = WebServiceControl.ValidateBoxMasterCodeBar(request.getBarCodeBoxMasterOrigin(), 0);
                    response.setPath(request.getActionPath());
                    break;
                case 6://With Location
                    response = WebServiceControl.ValidateLocationCodeBar(request.getBarCodeBoxMasterOrigin());
                    break;
                case 7://Create Reubicacion Box Master
                    response = WebServiceControl.CreateReubicacionBoxMaster(request);
                    break;
                case 8://With BOXMASTER2
                    response = WebServiceControl.ValidateExistBoxMaster(request.getBarCodeBoxMasterOrigin());
                    response.setPath(request.getActionPath());
                    break;
                case 9://Reubicacion de articulo ubicacion por ubicacion
                    response = WebServiceControl.CreateReubicacionArticleSinCajaMasterUbicacion(request);
                    break;
                case 10://Reubicacion de articulo CajaMaster a ubicacion
                    response = WebServiceControl.CreateReubicacionArticleCajaMaster(request);
                    break;
                case 11://Reubicacion de articulo ubicacion a CajaMaster
                    response = WebServiceControl.CreateReubicacionArticleACajaMaster(request);
                    break;
                case 12:
                    response = WebServiceControl.ValidateExistProductInBoxMaster(request);
                    break;
                case 13:
                    response = WebServiceControl.ValidateExistProductInLocation(request);
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
