package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.LoginResponse;

public interface IDelegateLoginTaskControl {
    void onLogin(LoginResponse response);
}
