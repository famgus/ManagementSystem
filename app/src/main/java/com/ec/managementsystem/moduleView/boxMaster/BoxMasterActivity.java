package com.ec.managementsystem.moduleView.boxMaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.request.BoxMasterRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.interfaces.IDelegateBoxMasterTaskControl;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.ScannerActivity;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.BoxMasterTaskController;

public class BoxMasterActivity extends BaseActivity implements DialogScanner.DialogScanerFinished, IDelegateBoxMasterTaskControl {
    private static final int CODE_INTENT_BOX_MASTER = 1;
    Toolbar toolbar;
    ImageView ivScanBarCode, ivIngresos, ivDespacho, ivTraslado;
    EditText etBarCode;
    String codeBar;
    int pathSelected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_master);
        initComponent();
    }

    private void initComponent() {
        try {
            etBarCode = findViewById(R.id.etBarCode);
            ivScanBarCode = findViewById(R.id.ivScanBarCode);
            ivIngresos = findViewById(R.id.ivIngresos);
            ivDespacho = findViewById(R.id.ivDespacho);
            ivTraslado = findViewById(R.id.ivTraslado);
            ivIngresos.setEnabled(false);
            ivDespacho.setEnabled(false);
            ivTraslado.setEnabled(false);
            // Set Toolbar
            toolbar = findViewById(R.id.toolbarBar);
            this.toolbar.setTitle("Módulo de Cajas Master");
            this.setupToolBar(toolbar);
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            etBarCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = editable.toString();
                    if (text.length() > 0) {
                        ivIngresos.setEnabled(true);
                        ivDespacho.setEnabled(true);
                        ivTraslado.setEnabled(true);
                        codeBar = text;
                    } else {
                        ivIngresos.setEnabled(false);
                        ivDespacho.setEnabled(false);
                        ivTraslado.setEnabled(false);
                    }
                }
            });
            //Set Listener
            ivScanBarCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogScanner(false, CODE_INTENT_BOX_MASTER);
                    //scanBarCode();
                }
            });
            ivIngresos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pathSelected = 1;
                    ValidaBarCode();
                }
            });
            ivDespacho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pathSelected = 2;
                    ValidaBarCode();
                }
            });
            ivTraslado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pathSelected = 3;
                    ValidaBarCode();
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
        dialogScanner.setPermisoCamaraConcedido(true);
        dialogScanner.setPermisoSolicitadoDesdeBoton(true);
        dialogScanner.show(getSupportFragmentManager(), "alert dialog generate codes");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_BOX_MASTER))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etBarCode.setText(codeBar);
                    ivIngresos.setEnabled(true);
                    ivDespacho.setEnabled(true);
                    ivTraslado.setEnabled(true);
                }
            }
        }
    }

    private void scanBarCode() {
        Intent i = new Intent(this, ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        startActivityForResult(i, CODE_INTENT_BOX_MASTER);
    }

    @Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {
        if (action == CODE_INTENT_BOX_MASTER) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etBarCode.setText(codeBar);
                ivIngresos.setEnabled(true);
                ivDespacho.setEnabled(true);
                ivTraslado.setEnabled(true);
            }
        }
    }


    public void ValidaBarCode() {
        BoxMasterRequest request = new BoxMasterRequest();
        request.setActionPath(4);
        request.setBarCodeBoxMasterOrigin(codeBar);
        BoxMasterTaskController task = new BoxMasterTaskController();
        task.setListener(BoxMasterActivity.this);
        task.execute(request);
    }

    @Override
    public void onBoxMasterResponse(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            if (pathSelected == 1) {
                Intent i = new Intent(BoxMasterActivity.this, IngresosActivity.class);
                i.putExtra("codeBarBoxMaster", etBarCode.getText().toString());
                startActivity(i);
            } else if (pathSelected == 2) {
                Intent i = new Intent(BoxMasterActivity.this, DespachoActivity.class);
                i.putExtra("codeBarBoxMaster", etBarCode.getText().toString());
                startActivity(i);
            } else if (pathSelected == 3) {
                Intent i = new Intent(BoxMasterActivity.this, TrasladosActivity.class);
                i.putExtra("codeBarBoxMaster", etBarCode.getText().toString());
                startActivity(i);
            }
        } else {
            Toast.makeText(BoxMasterActivity.this, "El código de barras no existe en el sistema de cajas master", Toast.LENGTH_LONG).show();
        }
    }
}
