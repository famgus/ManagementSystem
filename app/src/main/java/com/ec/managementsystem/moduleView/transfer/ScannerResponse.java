package com.ec.managementsystem.moduleView.transfer;

import com.ec.managementsystem.clases.responses.BundleResponse;

/**
 * Created by Gorky Muñoz on 21/9/2020.
 * Indra
 * gamunozg@indracompany.com
 */
public class ScannerResponse {
    private BundleResponse response;
    private int intentCode;

    public ScannerResponse(BundleResponse response, int intentCode) {
        this.response = response;
        this.intentCode = intentCode;
    }

    public BundleResponse getResponse() {
        return response;
    }

    public int getIntentCode() {
        return intentCode;
    }
}
