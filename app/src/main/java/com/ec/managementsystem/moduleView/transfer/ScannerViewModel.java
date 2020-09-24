package com.ec.managementsystem.moduleView.transfer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by Gorky Muñoz on 21/9/2020.
 * Indra
 * gamunozg@indracompany.com
 */
public class ScannerViewModel extends ViewModel {

    private MutableLiveData<ScannerResponse> scannerResponseMutableLiveData;

    public ScannerViewModel() {
        this.scannerResponseMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ScannerResponse> getScannerResponseMutableLiveData() {
        return scannerResponseMutableLiveData;
    }

    public void setScannerResponse(ScannerResponse response){
        scannerResponseMutableLiveData.setValue(response);
    }
}
