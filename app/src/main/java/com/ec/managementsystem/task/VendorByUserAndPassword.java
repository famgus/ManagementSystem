package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.VendorByUserNameAndPasswordResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class VendorByUserAndPassword extends AsyncTask<Void, Void, VendorByUserNameAndPasswordResponse> {

    private String username, pass;
    private IDelegateResponseGeneric<VendorByUserNameAndPasswordResponse> responseIDelegateResponseGeneric;

    public VendorByUserAndPassword(String username, String pass, IDelegateResponseGeneric<VendorByUserNameAndPasswordResponse> responseIDelegateResponseGeneric) {
        this.username = username;
        this.pass = pass;
        this.responseIDelegateResponseGeneric = responseIDelegateResponseGeneric;
    }

    @Override
    protected VendorByUserNameAndPasswordResponse doInBackground(Void... voids) {
        return WebServiceControl.getVendorByUserNameAndPassword(username,pass);
    }

    @Override
    protected void onPostExecute(VendorByUserNameAndPasswordResponse vendorByUserNameAndPasswordResponse) {
        if(responseIDelegateResponseGeneric != null) responseIDelegateResponseGeneric.onResponse(vendorByUserNameAndPasswordResponse);
    }
}
