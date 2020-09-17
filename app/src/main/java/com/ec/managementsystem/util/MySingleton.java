package com.ec.managementsystem.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ec.managementsystem.clases.BoxMaster;
import com.ec.managementsystem.clases.responses.FacturasClientResponse;
import com.ec.managementsystem.clases.responses.PedidoPickingResponse;
import com.ec.managementsystem.clases.responses.PedidoResponse;
import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;
import com.ec.managementsystem.clases.responses.ProductoResponse;
import com.ec.managementsystem.moduleView.login.User;
import com.google.gson.Gson;
import com.google.gson.internal.bind.JsonTreeReader;

import java.util.ArrayList;
import java.util.List;

public class MySingleton {
    private static MySingleton ourInstance;
    private Boolean flag;
    private User currentuser;
    private PedidoResponse pedidoResponse;
    private int posclicked;
    private Boolean statusitenclicked;
    private ProductoResponse productoResponse;
    public List<BoxMaster> boxMasterList;
    public PickingPedidoUserResponse pickingResponse;
    public FacturasClientResponse packingResponse;
    private boolean registerPurchaseOrder;

    public static MySingleton getInstance() {
        try {
            if (ourInstance == null) {
                ourInstance = new MySingleton();
                ourInstance.currentuser = getUser();
                ourInstance.pedidoResponse = null;
                ourInstance.posclicked = -1;
                ourInstance.statusitenclicked = false;
                ourInstance.productoResponse = null;
                ourInstance.boxMasterList = new ArrayList<>();
                ourInstance.registerPurchaseOrder = false;
            }
        } catch (Exception e) {
        }
        return ourInstance;
    }

    public PedidoResponse getPedidoResponse() {
        return pedidoResponse;
    }

    public void setPedidoResponse(PedidoResponse pedidoResponse) {
        this.pedidoResponse = pedidoResponse;
    }

    public Boolean getStatusitenclicked() {
        return statusitenclicked;
    }

    public void setStatusitenclicked(Boolean statusitenclicked) {
        this.statusitenclicked = statusitenclicked;
    }

    public ProductoResponse getProductoResponse() {
        return productoResponse;
    }

    public void setProductoResponse(ProductoResponse productoResponse) {
        this.productoResponse = productoResponse;
    }

    public List<BoxMaster> getBoxMasterList() {
        return boxMasterList;
    }

    public void setBoxMasterList(List<BoxMaster> boxMasterList) {
        this.boxMasterList = boxMasterList;
    }

    public boolean isRegisterPurchaseOrder() {
        return registerPurchaseOrder;
    }

    public void setRegisterPurchaseOrder(boolean registerPurchaseOrder) {
        this.registerPurchaseOrder = registerPurchaseOrder;
    }

    public void setPickingUserResponse(PickingPedidoUserResponse pickingResponse) {
        this.pickingResponse = pickingResponse;
    }

    public FacturasClientResponse getPackingResponse() {
        return packingResponse;
    }

    public void setPackingResponse(FacturasClientResponse packingResponse) {
        this.packingResponse = packingResponse;
    }

    public PickingPedidoUserResponse getPickingUserResponse() {
        return pickingResponse;
    }


    public static User getUser() {

        SharedPreferences sharedPreferences = MyApplication.GetAppContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        Integer rol = sharedPreferences.getInt("rol", 0);
        User u = new User(username, password, rol);
        return u;
    }

    public int getPosclicked() {
        return posclicked;
    }

    public void setPosclicked(int posclicked2) {
        posclicked = posclicked2;
    }


    public static void SaveUser(User user) {

        SharedPreferences sharedPreferences = MyApplication.GetAppContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putInt("rol", user.getRol());
        editor.apply();
    }



    public static void RestorePedidoPiking() {
        SharedPreferences sharedPreferences = MyApplication.GetAppContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
        String data = sharedPreferences.getString("pickingResponse", "");
        if(!data.isEmpty()) {
            Gson gson = new Gson();
            PickingPedidoUserResponse pickingPedidoUserResponse = gson.fromJson(data, PickingPedidoUserResponse.class);
            getInstance().setPickingUserResponse(pickingPedidoUserResponse);
        }
    }

    public static void SavePedidoPicking() {

        SharedPreferences sharedPreferences = MyApplication.GetAppContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String data = gson.toJson(getInstance().getPickingUserResponse());
        editor.putString("pickingResponse", data);
        editor.apply();
    }

    public static void RemovePedidoPicking() {
        SharedPreferences sharedPreferences = MyApplication.GetAppContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pickingResponse", "");
        editor.apply();
    }


}


