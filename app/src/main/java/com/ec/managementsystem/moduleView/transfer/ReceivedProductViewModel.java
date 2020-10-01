package com.ec.managementsystem.moduleView.transfer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ec.managementsystem.clases.DispatchedProduct;

public class ReceivedProductViewModel extends ViewModel {

    private MutableLiveData<DispatchedProduct> dispatchedProductMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<DispatchedProduct> getDispatchedProductMutableLiveData() {
        return dispatchedProductMutableLiveData;
    }

    public void setDispatchedProductUpdated(DispatchedProduct dispatchedProductUpdated){
        dispatchedProductMutableLiveData.setValue(dispatchedProductUpdated);
    }
}
