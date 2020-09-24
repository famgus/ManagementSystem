package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.ReturnProductValidationResponse;

public interface IDelegateReturnProductValidationControl {
    void onValidationMasterBoxUbication(ReturnProductValidationResponse returnProductValidationResponse);
}
