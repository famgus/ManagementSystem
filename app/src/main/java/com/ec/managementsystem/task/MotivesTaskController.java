package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.responses.ListMotivesResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateMotivesControl;


public class MotivesTaskController extends AsyncTask<Void, Void, ListMotivesResponse> {
    private IDelegateMotivesControl listener;

    public void setListener(IDelegateMotivesControl listener) {
        this.listener = listener;
    }

    @Override
    protected ListMotivesResponse doInBackground(Void... params) {
        return WebServiceControl.GetMotivesQualityControl();
    }

    @Override
    protected void onPostExecute(ListMotivesResponse response) {
        if (listener != null) listener.onSuccessMotives(response);
    }
}
