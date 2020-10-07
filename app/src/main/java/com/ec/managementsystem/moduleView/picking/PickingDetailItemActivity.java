package com.ec.managementsystem.moduleView.picking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.request.BoxMasterRequest;
import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.request.RequestGetProductDetailBySomeParameters;
import com.ec.managementsystem.clases.request.ReturnProductValidationRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.GetProductDetailBySomeParameters;
import com.ec.managementsystem.clases.responses.LocationDetail;
import com.ec.managementsystem.clases.responses.PickingPedidoDetailResponse;
import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;
import com.ec.managementsystem.clases.responses.ResponseGetProductDetailBySomeParameters;
import com.ec.managementsystem.clases.responses.ReturnProductValidationResponse;
import com.ec.managementsystem.interfaces.IDelegateBoxMasterTaskControl;
import com.ec.managementsystem.interfaces.IDelegateGetProductDetailBySomeParameters;
import com.ec.managementsystem.interfaces.IDelegateReturnProductValidationControl;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;
import com.ec.managementsystem.interfaces.IListenerUbicaciones;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.SensorActivityWithCodeBar;
import com.ec.managementsystem.moduleView.adapters.UbicacionesListAdapter;
import com.ec.managementsystem.moduleView.boxMaster.ReubicacionActivity;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.BoxMasterTaskController;
import com.ec.managementsystem.task.GetProductDetailBySomeParametersTaskController;
import com.ec.managementsystem.task.PickingUpdateTaskController;
import com.ec.managementsystem.task.ReturnProductValidationTaskController;
import com.ec.managementsystem.util.MySingleton;

import java.util.ArrayList;
import java.util.List;

public class PickingDetailItemActivity extends BaseActivity implements DialogScanner.DialogScanerFinished, IDelegateUpdatePickingControl, IListenerUbicaciones
        , IDelegateGetProductDetailBySomeParameters, IDelegateReturnProductValidationControl, IDelegateBoxMasterTaskControl {
    public static final int CODE_FLOW_MASTER_BOX = 99, CODE_FLOW_QUANTITY = 100;
    public static final int CODE_FLOW_UBICATION = 101;
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2, CODIGO_BAR = 3;
    Toolbar toolbar;
    PickingPedidoDetailResponse pedidoDetailSelected;
    TextView tvNumberPedido, tvDescription, tvTalla, tvColor, tvQuantity, tvQuantityPicking;
    EditText etQuantityPicking;
    ImageView ivActionQuantityPicking, ivActionBarCode;
    LinearLayout llRegister;
    int quantityPicking = 0;
    List<String> codes;
    PickingPedidoUserResponse pedidoUserResponse;
    RecyclerView rvLocations;
    List<LocationDetail> originalList;
    List<LocationDetail> filterList;
    UbicacionesListAdapter locationApdater;
    RadioButton rbBoxmaster, rbUbicacion;
    TextView etBarCode;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    private GetProductDetailBySomeParameters productOtherDetails;
    private int lastCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_detail_item);
        setupView();
        initCollection();
        initRecyclerView();
        this.productLoad();
    }

    private void setupView() {
        try {
            productOtherDetails = null;

            // Set Toolbar
            toolbar = findViewById(R.id.toolbar);
            tvNumberPedido = findViewById(R.id.tvNumberPedido);
            tvDescription = findViewById(R.id.tvDescription);
            tvTalla = findViewById(R.id.tvTalla);
            tvColor = findViewById(R.id.tvColor);
            tvQuantity = findViewById(R.id.tvQuantity);
            tvQuantityPicking = findViewById(R.id.tvQuantityPicking);
            etQuantityPicking = findViewById(R.id.etQuantityPicking);
            ivActionQuantityPicking = findViewById(R.id.ivActionQuantityPicking);
            llRegister = findViewById(R.id.llRegister);
            rbUbicacion = findViewById(R.id.rbUbicacion);
            rbBoxmaster = findViewById(R.id.rbBoxmaster);
            rvLocations = findViewById(R.id.rvLocations);
            etBarCode = findViewById(R.id.etBarCode);
            ivActionBarCode = findViewById(R.id.ivActionBarCode);
            codes = new ArrayList<>();
            verificarYPedirPermisosDeCamara();
            this.toolbar.setTitle("Módulo de Picking");
            this.setupToolBar(toolbar);
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.containsKey("pedidoSelected")) {
                pedidoDetailSelected = (PickingPedidoDetailResponse) bundle.getSerializable("pedidoSelected");
                pedidoUserResponse = MySingleton.getInstance().getPickingUserResponse();
            }
            if (pedidoDetailSelected != null) {
                tvNumberPedido.setText(String.valueOf(pedidoDetailSelected.getNumberPedido()));
                tvDescription.setText(String.valueOf(pedidoDetailSelected.getDescription()));
                tvTalla.setText(String.valueOf(pedidoDetailSelected.getTalla()));
                tvColor.setText(String.valueOf(pedidoDetailSelected.getColor()));
                tvQuantity.setText(String.valueOf(pedidoDetailSelected.getUnidadesTotales()));//todo: cantidad
                tvQuantityPicking.setText(String.valueOf(quantityPicking));
            }

            ivActionQuantityPicking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastCode = CODE_FLOW_QUANTITY;
                    Log.i("Click Code", String.valueOf(lastCode));
                    showDialogScanner(true, CODIGO_INTENT);
                }
            });
            ivActionBarCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastCode = (rbBoxmaster.isChecked()) ? CODE_FLOW_MASTER_BOX : CODE_FLOW_UBICATION;
                    Log.i("Click Code", String.valueOf(lastCode));
                    showDialogScanner(false, CODIGO_BAR);
                }
            });
            llRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etQuantityPicking.getText().length() > 0 && etBarCode.getText().length() > 0) {
                        int quantity;
                        try {
                            quantity = Integer.parseInt(etQuantityPicking.getText().toString().trim());
                        } catch (Exception e) {
                            quantity = -1;
                        }
                        if (quantity <= pedidoDetailSelected.getUnidadesTotales() && quantity >= 0) {
                            PickingUpdateTaskController task = new PickingUpdateTaskController();
                            task.setListener(PickingDetailItemActivity.this);
                            PickingRequest request = new PickingRequest();
                            request.setNumberSerie(pedidoUserResponse.getNumberSerie());
                            request.setNumberPedido(pedidoUserResponse.getNumberPedido());
                            request.setCodeArticle(pedidoDetailSelected.getCodeArticle());
                            request.setQuantity(quantity);
                            if (rbBoxmaster.isChecked()) {
                                request.setBarCodeBoxMaster(etBarCode.getText().toString());
                                request.setBarCodeLocation("");
                            } else if (rbUbicacion.isChecked()) {
                                request.setBarCodeLocation(etBarCode.getText().toString());
                                request.setBarCodeBoxMaster("");
                            }
                            request.setTalla(pedidoDetailSelected.getTalla());
                            request.setColor(pedidoDetailSelected.getCodcolor());
                            int origen = 1;
                            request.setOrigen(origen);
                            request.setPath(2);
                            task.execute(request);
                        } else {
                            if (quantity > pedidoDetailSelected.getUnidadesTotales())
                                Toast.makeText(PickingDetailItemActivity.this, "La cantidad supera al maximo", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(PickingDetailItemActivity.this, "Si no existe productos para hacer picking colocar cantidad en 0", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        int quantity = Integer.parseInt(etQuantityPicking.getText().toString().trim());
                        if (quantity == 0) {
                            PickingUpdateTaskController task = new PickingUpdateTaskController();
                            task.setListener(PickingDetailItemActivity.this);
                            PickingRequest request = new PickingRequest();
                            request.setNumberSerie(pedidoUserResponse.getNumberSerie());
                            request.setNumberPedido(pedidoUserResponse.getNumberPedido());
                            request.setCodeArticle(pedidoDetailSelected.getCodeArticle());
                            request.setQuantity(0);
                            if (rbBoxmaster.isChecked()) {
                                request.setBarCodeLocation("");
                                request.setBarCodeBoxMaster("");
                            } else if (rbUbicacion.isChecked()) {
                                request.setBarCodeLocation("");
                                request.setBarCodeBoxMaster("");
                            }
                            request.setPath(2);
                            task.execute(request);
                        }else{
                            Toast.makeText(PickingDetailItemActivity.this, "Debe ingresar todos los campos", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });

            etQuantityPicking.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    tvQuantityPicking.setText(editable.toString());
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error inicializando la actividad", Toast.LENGTH_LONG).show();
        }
    }

    private void initCollection() {
        this.originalList = new ArrayList<>();
        this.filterList = new ArrayList<>();
        try {
            if (pedidoDetailSelected != null && pedidoDetailSelected.getLocationList() != null) {
                LocationDetail fake = new LocationDetail();
                originalList.add(fake);
                this.originalList.addAll(pedidoDetailSelected.getLocationList());
                filterList = new ArrayList<>(originalList);
            }
        } catch (Exception e) {
        }
    }

    private void initRecyclerView() {
        //Setup Trailer Recycler View
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(PickingDetailItemActivity.this);
        // DividerItemDecoration dividerTrailer = new DividerItemDecoration(PickingDetailActivity.this, linearLayoutManagerTrailer.getOrientation());
        this.locationApdater = new UbicacionesListAdapter(filterList);
        this.locationApdater.setListener(this);
        this.rvLocations.setAdapter(locationApdater);
        this.rvLocations.setLayoutManager(linearLayoutManagerTrailer);
    }

    private void showDialogScanner(boolean scanMultiple, int codeIntent) {
        int quantity = 0;
        if (etQuantityPicking.getText().length() > 0) {
            try {
                quantity = Integer.parseInt(etQuantityPicking.getText().toString().trim());
            } catch (Exception e) {
                quantity = 0;
            }
        }
        Log.i("showDialogScanner", String.valueOf(quantity));

        Intent i = new Intent(this, SensorActivityWithCodeBar.class);
        i.putExtra("scanMultiple", scanMultiple);
        i.putExtra("permisoCamaraConcedido", true);
        i.putExtra("permisoSolicitadoDesdeBoton", true);
        i.putExtra("totalUnit", pedidoDetailSelected.getUnidadesTotales());

        if (lastCode == CODE_FLOW_QUANTITY) {
            ArrayList<String> barCodes = new ArrayList<>();
            if (productOtherDetails.getBarcode1() != null) {
                barCodes.add(productOtherDetails.getBarcode1());
            }
            if (productOtherDetails.getBarcode2() != null) {
                barCodes.add(productOtherDetails.getBarcode2());
            }
            if (productOtherDetails.getBarcode3() != null) {
                barCodes.add(productOtherDetails.getBarcode3());
            }
            i.putStringArrayListExtra("barCodes", barCodes);
            i.putExtra("quantity", quantity);
        }
        i.setAction(String.valueOf(codeIntent));
        startActivityForResult(i, codeIntent);
    }

    private void updateQuantity(BundleResponse bundleResponse) {
        codes = new ArrayList<>();
        Log.i("bundleResponse", String.valueOf(bundleResponse.getMapCodes().size()));
        if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
            for (String name : bundleResponse.getMapCodes().keySet()) {
                Log.i("onScannerBarCode: ", name);
                codes.add(name);
            }
            tvQuantityPicking.setText(String.valueOf(codes.size()));
            etQuantityPicking.setText(String.valueOf(codes.size()));
        }
    }

    private void sendValidation(String data, int typeValidation) {
        Log.i("Send Validation", String.valueOf(typeValidation));
        Log.i("Send Validation", String.valueOf(data));
        /*ReturnProductValidationRequest request = new ReturnProductValidationRequest(data, typeValidation);
        ReturnProductValidationTaskController task = new ReturnProductValidationTaskController();
        task.setListener(PickingDetailItemActivity.this);
        task.execute(request);*/
        //Call Service
        BoxMasterRequest request = new BoxMasterRequest();
        if(rbBoxmaster.isChecked()){
            request.setActionPath(12);
        }else {
            request.setActionPath(13);
        }
        request.setBarCodeBoxMasterOrigin(data);
        request.setCodigoArticulo(pedidoDetailSelected.getCodeArticle());
        request.setTalla(pedidoDetailSelected.getTalla());
        request.setCodColor(pedidoDetailSelected.getCodcolor());
        BoxMasterTaskController task = new BoxMasterTaskController();
        task.setListener(this);
        task.execute(request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("onActivityResult", "onActivityResult");

        if (resultCode == Activity.RESULT_OK) {
            if (data != null && (data.getAction().equals(String.valueOf(CODIGO_INTENT)) ||
                    lastCode == CODE_FLOW_QUANTITY
            )
            ) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                this.updateQuantity(bundleResponse);
            }

            if (data != null && (data.getAction().equals(String.valueOf(CODIGO_BAR))
                    || lastCode == CODE_FLOW_UBICATION || lastCode == CODE_FLOW_MASTER_BOX)
            ) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    this.sendValidation(codeBar, lastCode);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rvLocations != null) {
            rvLocations.setAdapter(null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                        showDialogScanner(false, CODIGO_INTENT);
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(PickingDetailItemActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_LONG).show();
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(PickingDetailItemActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(PickingDetailItemActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }

    @Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {

        Log.i("onScannerBarCode: ", String.valueOf(bundleResponse.getMapCodes().size()));
        if (action == CODIGO_INTENT || lastCode == CODE_FLOW_QUANTITY) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                this.updateQuantity(bundleResponse);
            }
        }
        if (action == CODIGO_BAR || lastCode == CODE_FLOW_MASTER_BOX || lastCode == CODE_FLOW_UBICATION) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                this.sendValidation(codeBar, lastCode);
            }
        }
    }

    @Override
    public void onSuccessUpdate(GenericResponse response) {
        Intent intentResult = new Intent();
        if (response != null && response.getCode() == 200) {
            intentResult.putExtra("result", 1);
            Toast.makeText(PickingDetailItemActivity.this, "Picking registrado correctamente", Toast.LENGTH_LONG).show();
        } else {
            intentResult.putExtra("result", 0);
            Toast.makeText(PickingDetailItemActivity.this, "Error registrando el picking", Toast.LENGTH_LONG).show();
        }
        setResult(Activity.RESULT_OK, intentResult);
        finish();
    }

    public void productLoad() {
        Log.i("productLoad", "load");
        RequestGetProductDetailBySomeParameters request = new RequestGetProductDetailBySomeParameters(
                pedidoDetailSelected.getCodeArticle(),
                pedidoDetailSelected.getTalla(),
                pedidoDetailSelected.getCodcolor()
        );
        GetProductDetailBySomeParametersTaskController task = new GetProductDetailBySomeParametersTaskController();
        task.setListener(PickingDetailItemActivity.this);
        task.execute(request);
    }

    @Override
    public void onItemClick(LocationDetail item) {

    }

    @Override
    public void onItemActionClick(LocationDetail item) {

    }

    @Override
    public void onItemRemoveClick(LocationDetail item) {

    }

    @Override
    public void onGetProduct(ResponseGetProductDetailBySomeParameters response) {
        if (response.getProductDetail().getBarcode1() != null)
            Log.i("onGetProduct", response.getProductDetail().getBarcode1());
        if (response.getProductDetail().getBarcode2() != null)
            Log.i("onGetProduct", response.getProductDetail().getBarcode2());
        if (response.getProductDetail().getBarcode3() != null)
            Log.i("onGetProduct", response.getProductDetail().getBarcode3());
        Log.i("onGetProduct", String.valueOf(response.getCode()));
        productOtherDetails = response.getProductDetail();
        switch (response.getCode()) {
            case 200:
                break;
            default:
                Toast.makeText(PickingDetailItemActivity.this, "Ocurrio un problema intente nuevamente", Toast.LENGTH_LONG).show();
                onBackPressed();
                break;
        }
    }

    @Override
    public void onValidationMasterBoxUbication(ReturnProductValidationResponse returnProductValidationResponse) {
        Log.i("ReadResponseActivity", returnProductValidationResponse.getMessage());
        Log.i("ReadResponseActivity", String.valueOf(returnProductValidationResponse.getCode()));
        this.readValidation(
                returnProductValidationResponse.getBarCode(),
                returnProductValidationResponse.getCode(),
                returnProductValidationResponse.getTypeValidation()
        );
    }

    private boolean readValidation(String barCode, int code, int typeValidation) {
        Log.i("readValidation", barCode + "," + code + "," + typeValidation);
        codes = new ArrayList<>();

        boolean response = false;
        if (code == 200) {
            response = true;

            if (typeValidation == CODE_FLOW_MASTER_BOX) {
                Log.i("Assign Barcode", barCode);
                codes.add(barCode);
                etBarCode.setText(barCode);
            }
            if (typeValidation == CODE_FLOW_UBICATION) {
                Log.i("Assign Barcode", barCode);
                codes.add(barCode);
                etBarCode.setText(barCode);
            }

        } else {
            Log.i("sendValidation", "Ocurrio un problema");
            if (typeValidation == CODE_FLOW_MASTER_BOX) {
                Toast.makeText(PickingDetailItemActivity.this, "Caja maestra inválida", Toast.LENGTH_LONG).show();
            }
            if (typeValidation == CODE_FLOW_UBICATION) {
                Toast.makeText(PickingDetailItemActivity.this, "Ubicación inválida", Toast.LENGTH_LONG).show();
            }
        }
        return response;
    }

    @Override
    public void onBoxMasterResponse(GenericResponse response) {
        if (response != null && response.getCode() == 201) {
            if (rbBoxmaster.isChecked()) {
                Toast.makeText(PickingDetailItemActivity.this, "Caja maestra inválida", Toast.LENGTH_LONG).show();
            }
            if (rbUbicacion.isChecked()) {
                Toast.makeText(PickingDetailItemActivity.this, "Ubicación inválida", Toast.LENGTH_LONG).show();
            }
        }
    }
}
