package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.GenericResponse;

public interface IDelegateBarCodeTaskControl {
    void onSuccessCreateBarCodes(GenericResponse response);
}
