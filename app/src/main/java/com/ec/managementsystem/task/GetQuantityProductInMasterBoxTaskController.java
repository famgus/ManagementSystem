package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.GetQuantityProductInMasterBoxResponse;
import com.ec.managementsystem.dataAccess.TransferWebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class GetQuantityProductInMasterBoxTaskController extends AsyncTask<Void, Void, GetQuantityProductInMasterBoxResponse> {

    private int productCode;
    private String size, color, barCode;
    private IDelegateResponseGeneric<GetQuantityProductInMasterBoxResponse> responseListener;

    public GetQuantityProductInMasterBoxTaskController(int productCode, String size, String color, String barCode, IDelegateResponseGeneric<GetQuantityProductInMasterBoxResponse> responseListener) {
        this.productCode = productCode;
        this.size = size;
        this.color = color;
        this.barCode = barCode;
        this.responseListener = responseListener;
    }

    @Override
    protected GetQuantityProductInMasterBoxResponse doInBackground(Void... voids) {
        return TransferWebServiceControl.getQuantityProductInMasterBox(productCode, size, color, barCode);
    }

    @Override
    protected void onPostExecute(GetQuantityProductInMasterBoxResponse getQuantityProductInMasterBoxResponse) {
        responseListener.onResponse(getQuantityProductInMasterBoxResponse);
    }
}
