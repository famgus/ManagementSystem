package com.ec.managementsystem.moduleView.transfer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.DispatchedProduct;
import com.ec.managementsystem.clases.request.UpdateProductReceivedRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;
import com.ec.managementsystem.moduleView.SensorActivity;
import com.ec.managementsystem.moduleView.transfer.ScannerResponse;
import com.ec.managementsystem.moduleView.transfer.ScannerViewModel;
import com.ec.managementsystem.moduleView.transfer.TransferFlowActivity;
import com.ec.managementsystem.task.UpdateProductReceivedTaskController;
import com.ec.managementsystem.task.UpdateQuantityPrepareTransferTaskController;
import com.google.android.material.textfield.TextInputLayout;

import static com.ec.managementsystem.moduleView.transfer.TransferFlowActivity.CODE_INTENT_RECEIVED_QUANTITY;

public class ReceiveTransferOrderDetailFragment extends Fragment {

    private DispatchedProduct selectedProduct;
    private TextView tvProductCode, tvSize, tvColor, tvTotalDispatched, tvTotalReceived;
    private ScannerViewModel scannerViewModel;
    private EditText etQuantity;
    private TextInputLayout tilQuantity;
    private Button btnRegister;
    private ReceivedProductViewModel receivedProductViewModel;

    public ReceiveTransferOrderDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedProduct = (DispatchedProduct) getArguments().getSerializable("item");
            scannerViewModel = new ViewModelProvider(requireActivity()).get(ScannerViewModel.class);
        }
        receivedProductViewModel = new ViewModelProvider(getActivity()).get(ReceivedProductViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receive_transfer_order_detail, container, false);

        tvProductCode = view.findViewById(R.id.tv_receivetransferorderdetail_product_code);
        tvSize = view.findViewById(R.id.tv_receivetransferorderdetail_size);
        tvColor = view.findViewById(R.id.tv_receivetransferorderdetail_color);
        tvTotalDispatched = view.findViewById(R.id.tv_receivetransferorderdetail_dispatched);
        tvTotalReceived = view.findViewById(R.id.tv_receivetransferorderdetail_received);
        tilQuantity = view.findViewById(R.id.til_receivetransferorder_quantity);
        ImageView ivBarCode = view.findViewById(R.id.iv_receivetransferorder_scan_quantity);
        etQuantity = view.findViewById(R.id.et_receivetransferorder_quantity);
        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvTotalReceived.setText(getString(R.string.all_received_quantity, 0));
                try {
                    if (s.toString().isEmpty()) {
                        tilQuantity.setError("Este campo es obligatorio");
                    } else if (Integer.parseInt(s.toString()) > selectedProduct.getTotalDispatched()) {
                        tilQuantity.setError("La cantidad ingresada es mayor a la despachada");
                    } else if (Integer.parseInt(s.toString()) <= 0) {
                        tilQuantity.setError("La cantidad ingresada no puede ser menor o igual a 0");
                    } else {
                        tilQuantity.setError(null);
                        tvTotalReceived.setText(getString(R.string.all_received_quantity, Integer.parseInt(s.toString())));
                    }
                } catch (Exception e) {
                    tilQuantity.setError("Error con el valor ingresado");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnRegister = view.findViewById(R.id.btn_receivetransferorderdetail);
        btnRegister.setOnClickListener(onRegisterClickListener());
        ivBarCode.setOnClickListener(onBarCodeClickListener());
        scannerViewModel.getScannerResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ScannerResponse>() {
            @Override
            public void onChanged(ScannerResponse scannerResponse) {
                if (scannerResponse != null) {
                    if (scannerResponse.getIntentCode() == CODE_INTENT_RECEIVED_QUANTITY) {
                        String codeBar = scannerResponse.getResponse().getMapCodes().keySet().iterator().next();
                        int receivedQuantity = scannerResponse.getResponse().getMapCodes().get(codeBar);
                        etQuantity.setText(String.valueOf(receivedQuantity));
                        tvTotalReceived.setText(getString(R.string.all_received_quantity, receivedQuantity));
                    }
                    scannerViewModel.setScannerResponse(null);
                }
            }
        });
        setInformativeTexts();


        return view;
    }

    private View.OnClickListener onRegisterClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receivedQuantity = etQuantity.getText().toString();
                if (tilQuantity.getError() == null) {
                    int received = Integer.parseInt(receivedQuantity);
                    if (received <= selectedProduct.getTotalDispatched() && received > 0) {

                        executeTask(received);
                    } else {
                        Toast.makeText(getContext(), "Ha ingresado una cantidad incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Debe corregir la cantidad ingresada", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void executeTask(final int received) {
        UpdateProductReceivedTaskController updateProductReceivedTaskController = new UpdateProductReceivedTaskController();
        UpdateProductReceivedRequest params = new UpdateProductReceivedRequest(selectedProduct.getSeriesNumber(), selectedProduct.getSize(),
                selectedProduct.getColor(), selectedProduct.getOrderNumber(), selectedProduct.getProductCode(), received, selectedProduct.getCarrito());
        updateProductReceivedTaskController.setResponseListener(new IDelegateResponseGeneric<GenericResponse>() {
            @Override
            public void onResponse(GenericResponse response) {
                if(response != null && response.getCode() == 200){
                    selectedProduct.setTotalReceived(received);
                    selectedProduct.setRegistered(true);
                    receivedProductViewModel.setDispatchedProductUpdated(selectedProduct);
                    NavHostFragment.findNavController(ReceiveTransferOrderDetailFragment.this).navigateUp();
                }else{
                    Toast.makeText(getContext(), "Error registrando la información", Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateProductReceivedTaskController.execute(params);
    }

    private void showDialogScanner(int totalUnits) {
        Intent i = new Intent(getActivity(), SensorActivity.class);
        i.putExtra("scanMultiple", true);
        i.putExtra("permisoCamaraConcedido", true);
        i.putExtra("permisoSolicitadoDesdeBoton", true);
        i.putExtra("totalUnit", Double.valueOf(totalUnits));
        i.setAction(String.valueOf(CODE_INTENT_RECEIVED_QUANTITY));
        startActivityForResult(i, CODE_INTENT_RECEIVED_QUANTITY);
    }

    private void setInformativeTexts() {
        tvProductCode.setText(getString(R.string.all_product_code, selectedProduct.getProductCode()));
        tvSize.setText(getString(R.string.all_size, selectedProduct.getSize()));
        tvColor.setText(getString(R.string.all_color, selectedProduct.getColor()));
        tvTotalDispatched.setText(getString(R.string.all_dispatched_quantity, selectedProduct.getTotalDispatched()));
        tvTotalReceived.setText(getString(R.string.all_received_quantity, selectedProduct.getTotalReceived()));
    }

    private View.OnClickListener onBarCodeClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogScanner(selectedProduct.getTotalDispatched());
            }
        };
    }
}