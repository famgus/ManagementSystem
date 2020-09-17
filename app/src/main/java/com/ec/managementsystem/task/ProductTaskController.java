package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.ProductoRequest;
import com.ec.managementsystem.clases.responses.ProductoResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateProductTaskControl;


public class ProductTaskController extends AsyncTask<ProductoRequest, Void, ProductoResponse> {
    private IDelegateProductTaskControl listener;

    public void setListener(IDelegateProductTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected ProductoResponse doInBackground(ProductoRequest... params) {
        String codigoBarra = params[0].getCodigoBarra();
        ProductoResponse response = WebServiceControl.GetProductDetails(codigoBarra);
        return response;
    }

    @Override
    protected void onPostExecute(ProductoResponse productoResponse) {
        if (listener != null) listener.onProductResponse(productoResponse);
    }
}
