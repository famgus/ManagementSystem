package com.ec.managementsystem.moduleView.boxMaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.request.BoxMasterRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.interfaces.IDelegateBoxMasterTaskControl;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.BoxMasterTaskController;

public class IngresosActivity extends BaseActivity implements IDelegateBoxMasterTaskControl, DialogScanner.DialogScanerFinished {
    private static final int CODE_INTENT_ARTICLE = 1;
    Toolbar toolbar;
    String barCodeBoxMaster = "";
    LinearLayout llRegister;
    EditText etBarCodeArticle, etQuantity;
    ImageView ivScanBarCodeArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);
        initComponent();
    }

    private void initComponent() {
        try {
            llRegister = findViewById(R.id.llRegister);
            etBarCodeArticle = findViewById(R.id.etBarCodeArticle);
            ivScanBarCodeArticle = findViewById(R.id.ivScanBarCodeArticle);
            etQuantity = findViewById(R.id.etQuantity);
            // Set Toolbar
            toolbar = findViewById(R.id.toolbarBar);
            this.toolbar.setTitle("Ingreso de artículos");
            this.setupToolBar(toolbar);
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.containsKey("codeBarBoxMaster")) {
                barCodeBoxMaster = bundle.getString("codeBarBoxMaster");
            }
            //Set Listener
            llRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String codeBarArticle = etBarCodeArticle.getText().toString();
                    Integer quantity = Integer.parseInt(etQuantity.getText().toString());
                    if (codeBarArticle.isEmpty() || quantity == 0 || barCodeBoxMaster.isEmpty()) {
                        Toast.makeText(IngresosActivity.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                    } else {
                        //Call Service
                        BoxMasterRequest request = new BoxMasterRequest();
                        request.setActionPath(1);
                        request.setBarCodeArticle(codeBarArticle);
                        request.setBarCodeBoxMasterOrigin(barCodeBoxMaster);
                        request.setQuantityArticle(quantity);
                        BoxMasterTaskController task = new BoxMasterTaskController();
                        task.setListener(IngresosActivity.this);
                        task.execute(request);
                    }
                }
            });
            ivScanBarCodeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanBarCode();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error inicializando la actividad", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_ARTICLE))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etBarCodeArticle.setText(codeBar);
                }
            }
        }
    }

    private void scanBarCode() {
        /*Intent i = new Intent(this, ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        startActivityForResult(i, CODE_INTENT_ARTICLE);*/
        showDialogScanner(false, CODE_INTENT_ARTICLE);
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
    public void onBoxMasterResponse(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            etBarCodeArticle.setText("");
            etQuantity.setText("");
            Toast.makeText(getApplicationContext(), "Los Ingresos fueron registrados correctamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error registrando los ingresos.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {
        if (action == CODE_INTENT_ARTICLE) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etBarCodeArticle.setText(codeBar);
            }
        }
    }
}
