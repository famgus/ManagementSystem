package com.ec.managementsystem.moduleView.transfer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ec.managementsystem.clases.TransferSubOrder;

import java.util.List;

public class ProductPreparationViewModel extends ViewModel {

    private MutableLiveData<List<TransferSubOrder>> mutableLiveData;
    private MutableLiveData<Integer> lastProductInserted;

    public ProductPreparationViewModel() {
        mutableLiveData = new MutableLiveData<>();
        lastProductInserted = new MutableLiveData<>();
    }

    public List<TransferSubOrder> getRegisteredProducts() {
        return mutableLiveData.getValue();
    }

    public MutableLiveData<List<TransferSubOrder>> getMutableLiveData(){
        return mutableLiveData;
    }

    public void addRegisteredProduct(TransferSubOrder subOrder){
        if(mutableLiveData.getValue() != null){
            mutableLiveData.getValue().add(subOrder);
        }
    }

    public void clearRegisteredProduct(){
        if(mutableLiveData.getValue() != null){
            mutableLiveData.getValue().clear();
        }
    }

    public void setMutableLiveDataValue(List<TransferSubOrder> mutableLiveData) {
        this.mutableLiveData.setValue(mutableLiveData);
    }

    public MutableLiveData<Integer> getLastProductInserted() {
        return lastProductInserted;
    }

    public void setLastProductInserted(int lastProductInsertedIndex) {
        if(lastProductInsertedIndex == -1){
            this.lastProductInserted.setValue(null);
        }else{
            this.lastProductInserted.setValue(lastProductInsertedIndex);
        }
    }
}
