package com.ec.managementsystem.moduleView.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.ProductUbication;
import com.ec.managementsystem.clases.TransferSubOrder;
import com.ec.managementsystem.clases.request.UpdateQuantityPrepareTransferRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.dataAccess.WebServiceControl;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;
import com.ec.managementsystem.moduleView.SensorActivity;
import com.ec.managementsystem.moduleView.adapters.ProductUbicationsAdapter;
import com.ec.managementsystem.task.UpdateQuantityPrepareTransferTaskController;
import com.ec.managementsystem.task.ValidateBoxMasterCodeBar;
import com.ec.managementsystem.util.Utils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class TransferProductDetailFragment extends Fragment {

    public static final String PRODUCT_TO_PREPARE_SELECTED = "TransferSubOrder";

    private TransferSubOrder productToPrepare;
    private int productToPrepareIndex;
    private RadioGroup rgProductOrigin;
    private TextView tvProductCode, tvSize, tvColor, tvRequestedDate, tvPreparationDate, tvRequestedQuantity;
    private ImageView ivBarCode, ivPreparedQuantity;
    private Button btnRegisterPreparedProduct;
    private TextInputLayout tilQuantity;
    private ProductPreparationViewModel productPreparationViewModel;
    private ScannerViewModel scannerViewModel;
    private EditText etQuantity, etBarCode;
    private RecyclerView rvUbications;
    private boolean isValidQuantity = false;

    public TransferProductDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productToPrepare = (TransferSubOrder) getArguments().getSerializable(PRODUCT_TO_PREPARE_SELECTED);
            productToPrepareIndex = getArguments().getInt("index");
            productPreparationViewModel = new ViewModelProvider(requireActivity()).get(ProductPreparationViewModel.class);
            scannerViewModel = new ViewModelProvider(requireActivity()).get(ScannerViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transfer_product_detail, container, false);
        tvProductCode = rootView.findViewById(R.id.tv_transferproductdetail_product_code);
        tvSize = rootView.findViewById(R.id.tv_transferproductdetail_size);
        tvColor = rootView.findViewById(R.id.tv_transferproductdetail_color);
        rgProductOrigin = rootView.findViewById(R.id.rg_transferproductdetail);
        tvRequestedDate = rootView.findViewById(R.id.tv_transferproductdetail_request_date);
        tvPreparationDate = rootView.findViewById(R.id.tv_transferproductdetail_prepared_date);
        tvRequestedQuantity = rootView.findViewById(R.id.tv_transferproductdetail_requested_quantity);
        tilQuantity = rootView.findViewById(R.id.til_quantity);
        etQuantity = rootView.findViewById(R.id.et_quantity);
        etBarCode = rootView.findViewById(R.id.et_barcode);
        etBarCode.setEnabled(false);
        rvUbications = rootView.findViewById(R.id.rv_transferproductdetail);
        ivBarCode = rootView.findViewById(R.id.iv_transferproductdetail_scan_barcode);
        ivPreparedQuantity = rootView.findViewById(R.id.iv_transferproductdetail_scan_quantity);
        btnRegisterPreparedProduct = rootView.findViewById(R.id.btn_transferproductdetail);
        btnRegisterPreparedProduct.setEnabled(isValidQuantity);
        initializeUbicationsRV();
        setListener();
        setInformativeTexts();

        scannerViewModel.getScannerResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ScannerResponse>() {
            @Override
            public void onChanged(ScannerResponse scannerResponse) {
                if (scannerResponse != null) {
                    String codeBar = scannerResponse.getResponse().getMapCodes().keySet().iterator().next();
                    if (scannerResponse.getIntentCode() == TransferFlowActivity.CODE_INTENT_BAR_CODE_DETAIL) {
                        validateBoxMaster(codeBar);
                    } else if (scannerResponse.getIntentCode() == TransferFlowActivity.CODE_INTENT_PREPARED_QUANTITY) {
                        etQuantity.setText(codeBar);
                    }
                }
            }
        });


        return rootView;
    }

    private void validateBoxMaster(final String codeBar) {
        // 637357613432254224JEA45
        ValidateBoxMasterCodeBar boxMasterCodeBar = new ValidateBoxMasterCodeBar(codeBar, 1, WebServiceControl.VALIDATE_EXIST_BOX_MASTER,
                new IDelegateResponseGeneric<GenericResponse>() {
                    @Override
                    public void onResponse(GenericResponse response) {
                        if (response != null) {
                            if (response.getCode() == 200) {
                                etBarCode.setText(codeBar);
                            } else if (response.getCode() == 201) {
                                Toast.makeText(getContext(), "La caja ingresada no existe o ya fue registrada", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error al validar la caja", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        boxMasterCodeBar.execute();
    }

    private void initializeUbicationsRV() {
        List<ProductUbication> productUbications = productToPrepare.getUbications();
        productUbications.add(0, new ProductUbication(Utils.HEADER_TYPE));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ProductUbicationsAdapter ubicationsAdapter = new ProductUbicationsAdapter(productUbications);
        rvUbications.setLayoutManager(linearLayoutManager);
        rvUbications.setAdapter(ubicationsAdapter);
    }

    private void setListener() {
        rgProductOrigin.setOnCheckedChangeListener(radioGroupListener());
        etQuantity.addTextChangedListener(quantityTextListener());
        btnRegisterPreparedProduct.setOnClickListener(registerButtonListener());
        ivBarCode.setOnClickListener(iconsListener(false, TransferFlowActivity.CODE_INTENT_BAR_CODE_DETAIL));
        ivPreparedQuantity.setOnClickListener(iconsListener(true, TransferFlowActivity.CODE_INTENT_PREPARED_QUANTITY));
    }

    private View.OnClickListener iconsListener(final boolean scanMultiple, final int codeIntent) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogScanner(scanMultiple, codeIntent);
            }
        };
    }

    private void updateQuantity(int preparedUnits) {
        UpdateQuantityPrepareTransferRequest params = new UpdateQuantityPrepareTransferRequest(
                productToPrepare.getSeriesNumber(), productToPrepare.getSize(),
                productToPrepare.getColor(), productToPrepare.getOrderNumber(),
                productToPrepare.getProductCode(), preparedUnits);
        UpdateQuantityPrepareTransferTaskController prepareTransferTaskController = new UpdateQuantityPrepareTransferTaskController();
        prepareTransferTaskController.setListener(updateQuantityListener());
        prepareTransferTaskController.execute(params);
    }

    private void showDialogScanner(boolean scanMultiple, int codeIntent) {
        Intent i = new Intent(getActivity(), SensorActivity.class);
        i.putExtra("scanMultiple", scanMultiple);
        i.putExtra("permisoCamaraConcedido", true);
        i.putExtra("permisoSolicitadoDesdeBoton", true);
        i.setAction(String.valueOf(codeIntent));
        startActivityForResult(i, codeIntent);
    }

    private void setInformativeTexts() {
        tvProductCode.setText(getString(R.string.productdetail_product_code, productToPrepare.getProductCode()));
        tvSize.setText(getString(R.string.productdetail_size, productToPrepare.getSize()));
        tvColor.setText(getString(R.string.productdetail_color, productToPrepare.getColor()));
        tvRequestedDate.setText(getString(R.string.productdetail_requested_date, productToPrepare.getAssignedDate()));
        tvPreparationDate.setText(getString(R.string.productdetail_preparation_date, productToPrepare.getPreparedDate()));
        tvRequestedQuantity.setText(getString(R.string.productdetail_requested_quantity, productToPrepare.getRequestedUnits()));
    }

    private RadioGroup.OnCheckedChangeListener radioGroupListener() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_transferproductdetail_boxmaster:
                        etBarCode.setEnabled(false);
                        break;
                    case R.id.rb_transferproductdetail_ubication:
                        etBarCode.setText("");
                        etBarCode.setEnabled(true);
                        break;
                }
            }
        };
    }

    private View.OnClickListener registerButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidQuantity && rgProductOrigin.getCheckedRadioButtonId() != -1 && etBarCode.getText().toString().length() > 0) {
                    int preparedUnits = Integer.parseInt(etQuantity.getText().toString());
                    updateQuantity(preparedUnits);
                }
            }
        };
    }

    private TextWatcher quantityTextListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    tilQuantity.setError("Requerido");
                    isValidQuantity = false;
                } else if (Integer.parseInt(s.toString()) > productToPrepare.getRequestedUnits()) {
                    tilQuantity.setError("No puede ingresar más de lo solicitado");
                    isValidQuantity = false;
                } else {
                    isValidQuantity = true;
                    tilQuantity.setError(null);
                }
                btnRegisterPreparedProduct.setEnabled(isValidQuantity);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private IDelegateResponseGeneric<GenericResponse> codeBarValidateResponse() {
        return new IDelegateResponseGeneric<GenericResponse>() {
            @Override
            public void onResponse(GenericResponse response) {

            }
        };
    }

    private IDelegateResponseGeneric<GenericResponse> updateQuantityListener() {
        return new IDelegateResponseGeneric<GenericResponse>() {
            @Override
            public void onResponse(GenericResponse response) {
                if (response != null && response.getCode() == 200) {

                    switch (rgProductOrigin.getCheckedRadioButtonId()) {
                        case R.id.rb_transferproductdetail_boxmaster:
                            productToPrepare.setOriginType("C");
                            break;
                        case R.id.rb_transferproductdetail_ubication:
                            productToPrepare.setOriginType("U");
                            break;
                    }

                    int preparedUnits = Integer.parseInt(etQuantity.getText().toString());
                    productToPrepare.setRegistered(true);
                    productToPrepare.setOrigin(etBarCode.getText().toString());
                    productToPrepare.setPreparedUnits(preparedUnits);
                    productPreparationViewModel.addRegisteredProduct(productToPrepare);
                    productPreparationViewModel.setLastProductInserted(productToPrepareIndex);
                    NavHostFragment.findNavController(TransferProductDetailFragment.this).navigateUp();
                } else {
                    Toast.makeText(getContext(), "No se pudo registrar la información", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
