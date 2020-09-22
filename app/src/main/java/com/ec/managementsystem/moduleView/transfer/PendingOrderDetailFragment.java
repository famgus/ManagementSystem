package com.ec.managementsystem.moduleView.transfer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.AssignedTransferOrder;
import com.ec.managementsystem.clases.BoxTransferPendingOrder;
import com.ec.managementsystem.clases.TransferSubOrder;
import com.ec.managementsystem.clases.request.RegisterTransferPendingOrderRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.TransfersOrderDetailForUserResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;
import com.ec.managementsystem.interfaces.OnItemClickListener;
import com.ec.managementsystem.moduleView.adapters.BoxTransferPendingOrdersAdapter;
import com.ec.managementsystem.moduleView.adapters.ProductToPrepareAdapter;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.TransfersOrderDetailForUserTaskController;
import com.ec.managementsystem.task.ValidateBoxMasterCodeBar;
import com.ec.managementsystem.util.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderDetailFragment extends Fragment implements IDelegateResponseGeneric<TransfersOrderDetailForUserResponse>, OnItemClickListener<TransferSubOrder> {

    private RecyclerView rvProductToPrepare, rvBoxMaster;
    private TextView tvRegisteredProductsNumber;
    private List<TransferSubOrder> transferSubOrders = new ArrayList<>();
    private List<BoxTransferPendingOrder> boxTransferPendingOrders = new ArrayList<>();
    private BoxTransferPendingOrdersAdapter boxTransferPendingOrdersAdapter;
    private ProductToPrepareAdapter productToPrepareAdapter;
    private Button btnOrderDetailStart, btnAddBox;
    private ProductPreparationViewModel productPreparationViewModel;
    private ScannerViewModel scannerViewModel;
    private int vendorCode;


    public PendingOrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            AssignedTransferOrder assignedTransferOrder = (AssignedTransferOrder) getArguments().getSerializable("item");
            vendorCode = getArguments().getInt("vendorCode");
            productPreparationViewModel = new ViewModelProvider(requireActivity()).get(ProductPreparationViewModel.class);
            scannerViewModel = new ViewModelProvider(requireActivity()).get(ScannerViewModel.class);
            if (assignedTransferOrder != null) {
                TransfersOrderDetailForUserTaskController transfersOrderDetailForUserTaskController = new TransfersOrderDetailForUserTaskController(
                        assignedTransferOrder.getSeriesNumber(),
                        assignedTransferOrder.getOrderNumber(),
                        assignedTransferOrder.getVendorCode(),
                        this);

                transfersOrderDetailForUserTaskController.execute();
            }
        }
        productToPrepareAdapter = new ProductToPrepareAdapter(transferSubOrders, this);
        boxTransferPendingOrders.add(0, new BoxTransferPendingOrder());
        boxTransferPendingOrdersAdapter = new BoxTransferPendingOrdersAdapter(boxTransferPendingOrders);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productPreparationViewModel.setMutableLiveDataValue(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pending_order_detail, container, false);
        rvProductToPrepare = rootView.findViewById(R.id.rv_pendingorderdetail);
        rvBoxMaster = rootView.findViewById(R.id.rv_pendingorderdetail_box);
        tvRegisteredProductsNumber = rootView.findViewById(R.id.tv_pendingorderdetail_registered_products_number);
        tvRegisteredProductsNumber.setText(R.string.pendingorderdetail_process_not_initialized);
        scannerViewModel.getScannerResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ScannerResponse>() {
            @Override
            public void onChanged(ScannerResponse scannerResponse) {
                if (scannerResponse != null) {
                    if (scannerResponse.getIntentCode() == TransferFlowActivity.CODE_INTENT_CONTAINER_BOX) {
                        String codeBar = scannerResponse.getResponse().getMapCodes().keySet().iterator().next();
                        Log.d("barCode: ", codeBar);
                        if (!isBoxMasterRegistered(codeBar)) {
                            validateBoxMaster(codeBar);
                        }
                    }
                }
            }
        });

        initializeRV();

        initializeBoxRV();

        btnOrderDetailStart = rootView.findViewById(R.id.btn_pendingorderdetail);
        btnAddBox = rootView.findViewById(R.id.btn_pendingorderdetail_add_box);
        btnAddBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productPreparationViewModel.clearRegisteredProduct();
                showDialogScanner();
            }
        });

        changeVisibilityButton(boxTransferPendingOrders.size() < 2, btnAddBox, View.GONE);

        changeVisibilityButton(productPreparationViewModel.getRegisteredProducts() != null, btnOrderDetailStart, View.GONE);
        btnOrderDetailStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productPreparationViewModel.getRegisteredProducts() == null) {
                    showDialogScanner();
                } else {
                    btnAddBox.setVisibility(View.GONE);
                    List<BoxTransferPendingOrder> boxTransfers = new ArrayList<>(boxTransferPendingOrders);
                    boxTransfers.remove(0);
                    RegisterTransferPendingOrderRequest params = new RegisterTransferPendingOrderRequest(vendorCode, boxTransfers);
                    String json = new Gson().toJson(params);
                    // Todo : llamar WS
                    Log.d("FINISH_PROCESS", json);
                }
            }
        });

        productPreparationViewModel.getLastProductInserted().
                observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer index) {
                        productToPrepareAdapter.notifyItemChanged(index);


                        int lastIndexBox = boxTransferPendingOrders.size() - 1;
                        TransferSubOrder registeredProduct = transferSubOrders.get(index);
                        BoxTransferPendingOrder lastBoxRegistered = boxTransferPendingOrders.get(lastIndexBox);
                        lastBoxRegistered.getRegisteredProducts().add(registeredProduct);
                        int previousTotal = lastBoxRegistered.getTotalRegisteredProducts();
                        int newTotal = registeredProduct.getPreparedUnits() + previousTotal;
                        lastBoxRegistered.setTotalRegisteredProducts(newTotal);
                        boxTransferPendingOrdersAdapter.notifyItemChanged(lastIndexBox);

                        int registered = 0, remaining = 0, sizeWithoutHeader = transferSubOrders.size() - 1;
                        for (int indexLoop = 1; indexLoop < boxTransferPendingOrders.size(); indexLoop++) {
                            BoxTransferPendingOrder current = boxTransferPendingOrders.get(indexLoop);
                            registered += current.getRegisteredProducts().size();
                        }

                        if (sizeWithoutHeader == registered) {
                            btnOrderDetailStart.setVisibility(View.VISIBLE);
                        } else {
                            remaining = sizeWithoutHeader - registered;
                        }
                        tvRegisteredProductsNumber.setText(getString(R.string.pendingorderdetail_remaining_products, remaining));

                    }
                });

        return rootView;
    }

    private void validateBoxMaster(final String codeBar) {
        // 637357613432254224JEA45
        ValidateBoxMasterCodeBar boxMasterCodeBar = new ValidateBoxMasterCodeBar(codeBar, 1, WebServiceControl.VALIDATE_BOX_MASTER_CODE_BAR,
                new IDelegateResponseGeneric<GenericResponse>() {
                    @Override
                    public void onResponse(GenericResponse response) {
                        if(response != null){
                            if (response.getCode() == 200) {
                                
                                if (productPreparationViewModel.getRegisteredProducts() == null) {
                                    productToPrepareAdapter.changeIsEditable();
                                    productPreparationViewModel.setMutableLiveDataValue(new ArrayList<TransferSubOrder>());
                                    btnOrderDetailStart.setVisibility(View.GONE);
                                }

                                int index = boxTransferPendingOrders.size();
                                boxTransferPendingOrders.add(new BoxTransferPendingOrder(codeBar, 0, new ArrayList<TransferSubOrder>()));
                                boxTransferPendingOrdersAdapter.notifyItemInserted(index);
                            } else if (response.getCode() == 201) {
                                Toast.makeText(getContext(), "La caja ingresada no existe o ya fue registrada", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getContext(), "Error validando la caja", Toast.LENGTH_SHORT).show();
                        }
                        changeVisibilityButton(boxTransferPendingOrders.size() >= 2, btnAddBox, View.VISIBLE);
                    }


                });
        boxMasterCodeBar.execute();
    }

    private boolean isBoxMasterRegistered(String codeBar) {
        for (int index = 1; index < boxTransferPendingOrders.size(); index++) {
            if (boxTransferPendingOrders.get(index).getBoxCode().equals(codeBar)) {
                return true;
            }
        }
        return false;
    }

    private void changeVisibilityButton(boolean b, Button btnAddBox, int visible) {
        if (b) {
            btnAddBox.setVisibility(visible);
        }
    }

    private void initializeRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvProductToPrepare.setLayoutManager(linearLayoutManager);
        rvProductToPrepare.setAdapter(productToPrepareAdapter);
    }

    private void initializeBoxRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvBoxMaster.setLayoutManager(linearLayoutManager);
        rvBoxMaster.setAdapter(boxTransferPendingOrdersAdapter);
    }

    private void showDialogScanner() {
        DialogScanner dialogScanner = new DialogScanner();
        dialogScanner.setScanMultiple(false);
        dialogScanner.setCode_intent(TransferFlowActivity.CODE_INTENT_CONTAINER_BOX);
        dialogScanner.setPermisoCamaraConcedido(true);
        dialogScanner.setPermisoSolicitadoDesdeBoton(true);
        dialogScanner.show(getChildFragmentManager(), DialogScanner.class.getSimpleName());
    }

    @Override
    public void onResponse(TransfersOrderDetailForUserResponse response) {
        if (response != null && response.getCode() == 200) {
            if (response.getTransferSubOrders().size() > 0) {
                transferSubOrders.clear();
                transferSubOrders.addAll(response.getTransferSubOrders());
                transferSubOrders.add(0, new TransferSubOrder(Utils.HEADER_TYPE));
                productToPrepareAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(TransferSubOrder item) {
        Bundle args = new Bundle();
        int index = transferSubOrders.indexOf(item);
        args.putSerializable(TransferProductDetailFragment.PRODUCT_TO_PREPARE_SELECTED, item);
        args.putInt("index", index);
        NavHostFragment.findNavController(this).navigate(R.id.action_pendingOrderDetailFragment_to_transferProductDetailFragment, args);
    }

}