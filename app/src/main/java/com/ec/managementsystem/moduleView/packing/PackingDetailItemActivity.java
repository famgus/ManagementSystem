package com.ec.managementsystem.moduleView.packing;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.FacturaModel;
import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.FacturasDetasilResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.picking.PickingDetailItemActivity;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.PickingUpdateTaskController;

import java.util.ArrayList;
import java.util.List;

public class PackingDetailItemActivity extends BaseActivity implements DialogScanner.DialogScanerFinished, IDelegateUpdatePickingControl {
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    Toolbar toolbar;
    FacturasDetasilResponse pedidoDetailSelected;
    FacturaModel facturaModelSelected;
    TextView tvNumberPedido, tvDescription, tvTalla, tvColor, tvQuantity, tvQuantityPicking;
    EditText etQuantityPicking;
    ImageView ivActionQuantityPicking;
    LinearLayout llRegister;
    int quantityPicking = 0;
    List<String> codes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_detail_item);
        setupView();
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
            codes = new ArrayList<>();
            verificarYPedirPermisosDeCamara();
            this.toolbar.setTitle("Módulo de Packing");
            this.setupToolBar(toolbar);
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.containsKey("pedidoSelected")) {
                pedidoDetailSelected = (FacturasDetasilResponse) bundle.getSerializable("pedidoSelected");
            }
            if (bundle != null && bundle.containsKey("facturaModelSelected")) {
                facturaModelSelected = (FacturaModel) bundle.getSerializable("facturaModelSelected");
            }
            if (pedidoDetailSelected != null) {
                tvNumberPedido.setText(String.valueOf(pedidoDetailSelected.getNumberSerie()));
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
            llRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etQuantityPicking.getText().length() > 0) {
                        if (etQuantityPicking.getText().length() > 0) {
                            PickingUpdateTaskController task = new PickingUpdateTaskController();
                            task.setListener(PackingDetailItemActivity.this);
                            PickingRequest request = new PickingRequest();
                            request.setNumberSerie(pedidoDetailSelected.getNumberSerie());
                            request.setNumberPedido(facturaModelSelected.getNumberPedido());
                            request.setCodeArticle(pedidoDetailSelected.getCodeArticle());
                            request.setQuantity(Integer.valueOf(etQuantityPicking.getText().toString()));
                            request.setPath(2);
                            task.execute(request);

                        } else {
                            Toast.makeText(PackingDetailItemActivity.this, "Debe ingresar la cantidad", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(PackingDetailItemActivity.this, "Debe ingresar la cantidad", Toast.LENGTH_LONG).show();
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
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        Toast.makeText(PackingDetailItemActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_LONG).show();
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(PackingDetailItemActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(PackingDetailItemActivity.this,
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
    }

    @Override
    public void onSuccessUpdate(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            Toast.makeText(PackingDetailItemActivity.this, "Packing registrado correctamente", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Toast.makeText(PackingDetailItemActivity.this, "Error registrando el packing", Toast.LENGTH_LONG).show();
        }
    }
}
