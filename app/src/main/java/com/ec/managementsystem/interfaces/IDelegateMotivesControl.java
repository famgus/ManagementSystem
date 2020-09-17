package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.ListMotivesResponse;

public interface IDelegateMotivesControl {
    void onSuccessMotives(ListMotivesResponse response);
}
