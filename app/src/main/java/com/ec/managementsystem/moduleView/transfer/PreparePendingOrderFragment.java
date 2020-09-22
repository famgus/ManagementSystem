package com.ec.managementsystem.moduleView.transfer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.AssignedTransferOrder;
import com.ec.managementsystem.clases.responses.TransfersOrderForUserCodeResponse;
import com.ec.managementsystem.clases.responses.VendorByUserNameAndPasswordResponse;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;
import com.ec.managementsystem.interfaces.OnItemClickListener;
import com.ec.managementsystem.moduleView.adapters.AssignedPendingOrdersAdapter;
import com.ec.managementsystem.moduleView.login.User;
import com.ec.managementsystem.task.TransferOrderForUserTaskController;
import com.ec.managementsystem.task.VendorByUserAndPassword;
import com.ec.managementsystem.util.MySingleton;

import java.util.ArrayList;
import java.util.List;

public class PreparePendingOrderFragment extends Fragment implements OnItemClickListener<AssignedTransferOrder> {

    RecyclerView rvPendingTransferOrder;
    private AssignedPendingOrdersAdapter assignUserPendingOrderAdapter;
    private List<AssignedTransferOrder> suborderTransferList = new ArrayList<>();
    private int vendorCode = -1;

    public PreparePendingOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignUserPendingOrderAdapter = new AssignedPendingOrdersAdapter(suborderTransferList, this);
        getUserCode();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prepare_pending_order, container, false);
        rvPendingTransferOrder = rootView.findViewById(R.id.rv_preparependingorder);
        initializeRV();
        return rootView;
    }

    private void getUserCode() {
        User user = MySingleton.getUser();
        VendorByUserAndPassword vendorByUserAndPassword = new VendorByUserAndPassword(user.getUsername(), user.getPassword(), vendorByUserNameAndPasswordResponse());
        vendorByUserAndPassword.execute();
    }

    private IDelegateResponseGeneric<VendorByUserNameAndPasswordResponse> vendorByUserNameAndPasswordResponse() {
        return new IDelegateResponseGeneric<VendorByUserNameAndPasswordResponse>() {
            @Override
            public void onResponse(VendorByUserNameAndPasswordResponse response) {
                if (response != null && response.getCode() == 200 && response.getVendorCode() > 0) {
                    vendorCode = response.getVendorCode();
                    getAssignedTransferOrders(vendorCode);
                } else {
                    Toast.makeText(getContext(), "No se pudo obtener el código de usuario", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private IDelegateResponseGeneric<TransfersOrderForUserCodeResponse> transferOrderForUserCodeListener() {
        return new IDelegateResponseGeneric<TransfersOrderForUserCodeResponse>() {
            @Override
            public void onResponse(TransfersOrderForUserCodeResponse response) {
                if (response != null && response.getCode() == 200) {
                    if (response.getSuborderTransferList().size() > 0) {
                        suborderTransferList.clear();
                        suborderTransferList.addAll(response.getSuborderTransferList());
                        assignUserPendingOrderAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "No hay traslados asignados", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No se pudo los traslados asignados", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    private void getAssignedTransferOrders(int userCode) {
        TransferOrderForUserTaskController pendingTransferOrderTaskController = new TransferOrderForUserTaskController(userCode, transferOrderForUserCodeListener());
        pendingTransferOrderTaskController.execute();
    }

    private void initializeRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvPendingTransferOrder.setLayoutManager(linearLayoutManager);
        rvPendingTransferOrder.setAdapter(assignUserPendingOrderAdapter);
    }

    @Override
    public void onClick(AssignedTransferOrder item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        bundle.putInt("vendorCode", vendorCode);
        NavHostFragment.findNavController(this).navigate(R.id.action_preparePendingOrderFragment_to_pendingOrderDetailFragment, bundle);
    }
}