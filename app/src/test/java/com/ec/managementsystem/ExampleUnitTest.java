package com.ec.managementsystem;

import android.widget.Toast;

import com.ec.managementsystem.clases.BoxMaster;
import com.ec.managementsystem.clases.PedidoDetail;
import com.ec.managementsystem.clases.request.BarCodeRequest;
import com.ec.managementsystem.clases.request.PurchaseOrderRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.interfaces.IDelegateBarCodeTaskControl;
import com.ec.managementsystem.interfaces.IDelegateInsertPurchaseTaskControl;
import com.ec.managementsystem.task.BarCodeTaskController;
import com.ec.managementsystem.task.InsertPurchaseTaskController;
import com.ec.managementsystem.util.MySingleton;
import com.ec.managementsystem.util.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest implements IDelegateInsertPurchaseTaskControl, IDelegateBarCodeTaskControl {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void TestPedido() {
        Map<String, Integer> mapCode = new HashMap<>();
        List<BoxMaster> boxMasterList = new ArrayList<>();
        BoxMaster b1 = new BoxMaster();
        b1.setComplete(true);
        b1.setBarCode("1234");
        b1.setCountProduct(4);
        b1.setId(1);
        b1.setLocationBarCode("Location 1");
        List<String> codes = new ArrayList<>();
        codes.add("001MD43991001280");
        codes.add("001MD43991001280");
        codes.add("001MD43991001280");
        codes.add("001MD43991001280");
        b1.setCodesProduct(codes);
        boxMasterList.add(b1);

        BoxMaster b2 = new BoxMaster();
        b2.setComplete(true);
        b2.setBarCode("123456");
        b2.setCountProduct(2);
        b2.setId(2);
        b2.setLocationBarCode("Location2");
        List<String> codes2 = new ArrayList<>();
        codes2.add("001MD43991001300");
        codes2.add("001MD43991001300");
        b2.setCodesProduct(codes2);
        boxMasterList.add(b2);

        for (BoxMaster box : boxMasterList) {
            for (String barCodeProduct : box.getCodesProduct()) {
                if (mapCode.containsKey(barCodeProduct)) {
                    int count = mapCode.get(barCodeProduct) + 1;
                    mapCode.put(barCodeProduct, count);
                } else {
                    mapCode.put(barCodeProduct, 1);
                }
            }
        }
        if (mapCode.size() > 0) {
            PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest();
            // purchaseOrderRequest.setBoxMasterList(boxMasterList);
            List<PedidoDetail> pedidoDetailList = new ArrayList<>();
            for (Map.Entry<String, Integer> item : mapCode.entrySet()) {
                PedidoDetail pedido = null;
                for (PedidoDetail pedidoDetail : MySingleton.getInstance().getPedidoResponse().getPedidoDetailList()) {
                    if ((pedidoDetail.getCodigoBarra() != null && pedidoDetail.getCodigoBarra().equals(item.getKey())) || (pedidoDetail.getCb2() != null && pedidoDetail.getCb2().equals(item.getKey())) || (pedidoDetail.getcBar3() != null && pedidoDetail.getcBar3().equals(item.getKey()))) {
                        pedido = pedidoDetail;
                    }
                }
                if (pedido != null) {
                    pedido.setUnidades(item.getValue());
                    pedidoDetailList.add(pedido);
                }
            }
            purchaseOrderRequest.setPedidoList(pedidoDetailList);
            InsertPurchaseTaskController task = new InsertPurchaseTaskController();
            task.setListener(ExampleUnitTest.this);
            task.execute(purchaseOrderRequest);
        }
    }




    @Override
    public void onInsertPedidoResponse(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
        } else {
        }
    }

    @Test
    public void  createBarCodes(){
        BarCodeRequest request = new BarCodeRequest();
        request.setQuantity(5);
        request.setUserRegister("Rafael");
        BarCodeTaskController task = new BarCodeTaskController();
        task.setListener(ExampleUnitTest.this);
        task.execute(request);
    }

    @Override
    public void onSuccessCreateBarCodes(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
        } else {
        }
    }
}