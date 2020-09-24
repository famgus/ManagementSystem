package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.TransfersOrderForUserCodeResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;

public class TransferOrderForUserTaskController extends AsyncTask<Void, Void, TransfersOrderForUserCodeResponse> {

    private int userCode;
    private IDelegateResponseGeneric<TransfersOrderForUserCodeResponse> responseListener;

    public TransferOrderForUserTaskController(int userCode, IDelegateResponseGeneric<TransfersOrderForUserCodeResponse> responseListener) {
        this.userCode = userCode;
        this.responseListener = responseListener;
    }

    @Override
    protected TransfersOrderForUserCodeResponse doInBackground(Void... voids) {
        return WebServiceControl.getTransfersOrderForUserCode(userCode);
    }

    @Override
    protected void onPostExecute(TransfersOrderForUserCodeResponse transfersOrderForUserCodeResponse) {
        if(responseListener != null) responseListener.onResponse(transfersOrderForUserCodeResponse);
    }
}
