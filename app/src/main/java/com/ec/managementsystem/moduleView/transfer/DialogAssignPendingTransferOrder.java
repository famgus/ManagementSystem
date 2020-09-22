package com.ec.managementsystem.moduleView.transfer;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.PendingTransferOrder;
import com.ec.managementsystem.clases.Vendedores;
import com.ec.managementsystem.clases.request.SplitOrderRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.VendorsByTypeResponse;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;
import com.ec.managementsystem.moduleView.adapters.AssignUserPendingOrderAdapter;
import com.ec.managementsystem.task.SplitOrderTaskController;
import com.ec.managementsystem.task.VendorsByTypeTaskController;

import java.util.ArrayList;
import java.util.List;

public class DialogAssignPendingTransferOrder extends AppCompatDialogFragment implements IDelegateResponseGeneric<VendorsByTypeResponse> {

    private Button btnAssignPendingOrder;
    private RecyclerView rvVendors;
    private List<Vendedores> vendorsList;
    private PendingTransferOrder pendingTransferOrder;
    private IDelegateResponseGeneric<PendingTransferOrder> onDialogResponse;
    private AlertDialog alertDialog;

    public DialogAssignPendingTransferOrder(PendingTransferOrder pendingTransferOrder, IDelegateResponseGeneric<PendingTransferOrder> responseGeneric) {
        this.pendingTransferOrder = pendingTransferOrder;
        this.onDialogResponse = responseGeneric;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_assign_pending_order, null);
        builder.setView(view);
        alertDialog = builder.create();
        btnAssignPendingOrder = view.findViewById(R.id.btnDialogAssignPendingOrder);
        btnAssignPendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasSelectedVendors()) {
                    SplitOrderTaskController splitOrderTaskController = new SplitOrderTaskController(splitOrderResponseListener());
                    splitOrderTaskController.execute(getParams());
                } else {
                    Toast.makeText(getContext(), "No ha seleccionado Vendedores", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rvVendors = view.findViewById(R.id.rvDialogAssingPendingOrder);
        getVendors();
        return alertDialog;
    }

    private IDelegateResponseGeneric<GenericResponse> splitOrderResponseListener() {
        return new IDelegateResponseGeneric<GenericResponse>() {
            @Override
            public void onResponse(GenericResponse response) {
                if (response != null && response.getCode() == 200){
                    pendingTransferOrder.setAssigned(true);
                    onDialogResponse.onResponse(pendingTransferOrder);
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private SplitOrderRequest getParams() {
        SplitOrderRequest params = new SplitOrderRequest();
        params.setOrderNumber(pendingTransferOrder.getOrderNumber());
        params.setSeriesNumber(pendingTransferOrder.getSeriesNumber());
        params.setUsersCode(getSelectedVendorsCode());
        return params;
    }

    private boolean hasSelectedVendors() {
        for (Vendedores vendor : vendorsList) {
            if (vendor.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private void getVendors() {
        VendorsByTypeTaskController vendorsByTypeTaskController = new VendorsByTypeTaskController(this);
        vendorsByTypeTaskController.execute(4);
    }

    private String getSelectedVendorsCode() {
        List<String> codes = new ArrayList<>();
        for (Vendedores vendor : vendorsList) {
            if (vendor.isSelected()) {
                codes.add(vendor.getCodvendedor().toString());
            }
        }
        return TextUtils.join(",", codes);
    }

    private void initializeRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvVendors.setLayoutManager(linearLayoutManager);
        AssignUserPendingOrderAdapter pendingTransferOrderAdapter = new AssignUserPendingOrderAdapter(vendorsList);
        rvVendors.setAdapter(pendingTransferOrderAdapter);
    }

    @Override
    public void onResponse(VendorsByTypeResponse response) {
        if (response != null && response.getCode() == 200) {
            if (response.getVendedoresList().size() > 0) {
                vendorsList = response.getVendedoresList();
                initializeRV();
            } else {
                btnAssignPendingOrder.setEnabled(false);
            }
        } else {
            // Todo: mostrar caso de error
        }
    }
}
