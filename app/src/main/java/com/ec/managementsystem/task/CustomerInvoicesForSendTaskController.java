package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.CustomerInvoiceForSendResponse;
import com.ec.managementsystem.dataAccess.PickingWebService;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class CustomerInvoicesForSendTaskController extends AsyncTask<Integer, Void, CustomerInvoiceForSendResponse> {

    private IDelegateResponseGeneric<CustomerInvoiceForSendResponse> responseListener;

    public CustomerInvoicesForSendTaskController(IDelegateResponseGeneric<CustomerInvoiceForSendResponse> responseListener) {
        this.responseListener = responseListener;
    }

    @Override
    protected CustomerInvoiceForSendResponse doInBackground(Integer... integers) {
        int selectedOption = integers[0];
        return PickingWebService.getClientInvoicesForSend(selectedOption);
    }

    @Override
    protected void onPostExecute(CustomerInvoiceForSendResponse customerInvoiceForSendResponse) {
        responseListener.onResponse(customerInvoiceForSendResponse);
    }
}
