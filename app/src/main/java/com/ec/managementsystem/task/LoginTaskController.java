package com.ec.managementsystem.task;

import android.os.AsyncTask;

import com.ec.managementsystem.clases.request.LoginRequest;
import com.ec.managementsystem.clases.responses.LoginResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateLoginTaskControl;


public class LoginTaskController extends AsyncTask<LoginRequest, Void, LoginResponse> {
    private IDelegateLoginTaskControl listener;

    public void setListener(IDelegateLoginTaskControl listener) {
        this.listener = listener;
    }

    @Override
    protected LoginResponse doInBackground(LoginRequest... params) {
        String userName = params[0].getUserName();
        String password = params[0].getPassword();
        long date = params[0].getDate();
        LoginResponse response = WebServiceControl.LogIn(userName, password, date);
        String message = "";
        if (response != null && !response.getMessage().isEmpty()) {
            message = response.getMessage();
        }
        if (response != null && response.getMessage().equals("OK")) {
            return response;
        } else {
            return null;
        }

    }

    @Override
    protected void onPostExecute(LoginResponse loginTaskResponse) {
        if (listener != null) listener.onLogin(loginTaskResponse);
    }
}
