package com.ec.managementsystem.moduleView.picking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.LocationDetail;
import com.ec.managementsystem.clases.responses.PickingPedidoDetailResponse;
import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;
import com.ec.managementsystem.interfaces.IListenerUbicaciones;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.adapters.UbicacionesListAdapter;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.PickingUpdateTaskController;
import com.ec.managementsystem.util.MySingleton;

import java.util.ArrayList;
import java.util.List;

public class PickingDetailItemActivity extends BaseActivity implements DialogScanner.DialogScanerFinished, IDelegateUpdatePickingControl, IListenerUbicaciones {
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2, CODIGO_BAR = 3;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
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
    EditText etBarCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_detail_item);
        setupView();
        initCollection();
        initRecyclerView();
    }

    private void setupView() {
        try {
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
                tvQuantity.setText(String.valueOf(pedidoDetailSelected.getUnidadesTotales()));
                tvQuantityPicking.setText(String.valueOf(quantityPicking));
            }

            ivActionQuantityPicking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogScanner(false, CODIGO_INTENT);
                }
            });
            ivActionBarCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogScanner(false, CODIGO_BAR);
                }
            });
            llRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etQuantityPicking.getText().length() > 0 && etBarCode.getText().length() > 0) {
                        Integer quantity = Integer.valueOf(etQuantityPicking.getText().toString());
                        if (quantity <= pedidoDetailSelected.getUnidadesTotales()) {
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
                            request.setPath(2);
                            task.execute(request);
                        } else {
                            Toast.makeText(PickingDetailItemActivity.this, "Cantidad ingresada supera la cantidad del pedido", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(PickingDetailItemActivity.this, "Debe ingresar todos los campos", Toast.LENGTH_LONG).show();
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
        DialogScanner dialogScanner = new DialogScanner();
        dialogScanner.setScanMultiple(scanMultiple);
        dialogScanner.setCode_intent(codeIntent);
        dialogScanner.setPermisoCamaraConcedido(permisoCamaraConcedido);
        dialogScanner.setPermisoSolicitadoDesdeBoton(permisoSolicitadoDesdeBoton);
        dialogScanner.show(getSupportFragmentManager(), "alert dialog generate codes");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction().equals(String.valueOf(CODIGO_INTENT))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    codes.add(codeBar);
                    tvQuantityPicking.setText(String.valueOf(codes.size()));
                    etQuantityPicking.setText(String.valueOf(codes.size()));
                }
            }

            if (data != null && data.getAction().equals(String.valueOf(CODIGO_BAR))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etBarCode.setText(String.valueOf(codeBar));
                    //TODO VALIDATE
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
        if (action == CODIGO_INTENT) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                codes.add(codeBar);
                tvQuantityPicking.setText(String.valueOf(codes.size()));
                etQuantityPicking.setText(String.valueOf(codes.size()));
            }
        }
        if (action == CODIGO_BAR) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etBarCode.setText(codeBar);

            }
        }
    }

    @Override
    public void onSuccessUpdate(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            Toast.makeText(PickingDetailItemActivity.this, "Picking registrado correctamente", Toast.LENGTH_LONG).show();
            onBackPressed();

        } else {
            Toast.makeText(PickingDetailItemActivity.this, "Error registrando el picking", Toast.LENGTH_LONG).show();
        }
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
}
