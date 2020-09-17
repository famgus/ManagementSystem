package com.ec.managementsystem.interfaces;


import com.ec.managementsystem.clases.responses.LocationDetail;

public interface IListenerUbicaciones {
    void onItemClick(LocationDetail item);

    void onItemActionClick(LocationDetail item);

    void onItemRemoveClick(LocationDetail item);
}
