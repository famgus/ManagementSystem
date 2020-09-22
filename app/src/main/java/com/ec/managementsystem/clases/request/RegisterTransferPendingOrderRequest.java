package com.ec.managementsystem.clases.request;

import com.ec.managementsystem.clases.BoxTransferPendingOrder;

import java.util.List;

/**
 * Created by Gorky Muñoz on 21/9/2020.
 * Indra
 * gamunozg@indracompany.com
 */
public class RegisterTransferPendingOrderRequest {
    private int vendorCode;
    private List<BoxTransferPendingOrder>  detail;

    public RegisterTransferPendingOrderRequest(int vendorCode, List<BoxTransferPendingOrder> detail) {
        this.vendorCode = vendorCode;
        this.detail = detail;
    }
}
