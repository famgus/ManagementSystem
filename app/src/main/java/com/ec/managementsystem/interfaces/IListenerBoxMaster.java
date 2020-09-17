package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.BoxMaster;

public interface IListenerBoxMaster {
    void onItemClick(BoxMaster item);

    void onItemActionClick(BoxMaster item);

    void onItemRemoveClick(BoxMaster item);
}
