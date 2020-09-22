package com.ec.managementsystem.moduleView.transfer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.PendingTransferOrder;
import com.ec.managementsystem.clases.responses.PendingTransferOrderResponse;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;
import com.ec.managementsystem.interfaces.OnItemClickListener;
import com.ec.managementsystem.moduleView.adapters.PendingTransferOrderAdapter;
import com.ec.managementsystem.task.PendingTransferOrderTaskController;

import java.util.List;

public class PendingTransferOrderActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener<PendingTransferOrder> {

    Button btnPendingTransferOrderSearch, btnPendingTransferOrderUpdate;
    RecyclerView rvPendingTransferOrder;
    private List<PendingTransferOrder> pendingTransferOrders;
    private PendingTransferOrderAdapter pendingTransferOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_transfer_order);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Ordenes de Traslado");
        }
        btnPendingTransferOrderSearch = findViewById(R.id.btnPendingTransferOrderSearch);
        btnPendingTransferOrderUpdate = findViewById(R.id.btnPendingTransferOrderUpdate);
        btnPendingTransferOrderSearch.setOnClickListener(this);
        btnPendingTransferOrderUpdate.setOnClickListener(this);
        rvPendingTransferOrder = findViewById(R.id.rvPendingTransferOrder);
    }

    private void initializeRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPendingTransferOrder.setLayoutManager(linearLayoutManager);
        pendingTransferOrderAdapter = new PendingTransferOrderAdapter(pendingTransferOrders, this);
        rvPendingTransferOrder.setAdapter(pendingTransferOrderAdapter);
    }

    private boolean showUpdateButton() {
        for (PendingTransferOrder pendingTransferOrder : pendingTransferOrders) {
            if (!pendingTransferOrder.isAssigned()) {
                return false;
            }
        }
        return true;
    }

    public IDelegateResponseGeneric<PendingTransferOrder> onDialogResponse() {
        return new IDelegateResponseGeneric<PendingTransferOrder>() {
            @Override
            public void onResponse(PendingTransferOrder item) {
                int index = pendingTransferOrders.indexOf(item);
                pendingTransferOrderAdapter.notifyItemChanged(index);
                if (showUpdateButton()) {
                    btnPendingTransferOrderUpdate.setEnabled(showUpdateButton());
                    btnPendingTransferOrderUpdate.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private IDelegateResponseGeneric<PendingTransferOrderResponse> onPendingTransferOrderResponse() {
        return new IDelegateResponseGeneric<PendingTransferOrderResponse>() {
            @Override
            public void onResponse(PendingTransferOrderResponse response) {
                if (response != null && response.getCode() == 200) {
                    pendingTransferOrders = response.getTrasladoList();
                    initializeRV();
                } else {
                    // Todo : no response or error
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPendingTransferOrderSearch:
                PendingTransferOrderTaskController pendingTransferOrderTaskController = new PendingTransferOrderTaskController();
                pendingTransferOrderTaskController.setListener(onPendingTransferOrderResponse());
                pendingTransferOrderTaskController.execute();
                break;
            case R.id.btnPendingTransferOrderUpdate:
                this.finish();
                break;
        }
    }

    @Override
    public void onClick(PendingTransferOrder item) {
        DialogAssignPendingTransferOrder dialogAssignPendingTransferOrder = new DialogAssignPendingTransferOrder(item, onDialogResponse());
        dialogAssignPendingTransferOrder.show(getSupportFragmentManager(), DialogAssignPendingTransferOrder.class.getSimpleName());
    }
}