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

public class DespachoActivity extends BaseActivity implements IDelegateBoxMasterTaskControl, DialogScanner.DialogScanerFinished {
    private static final int CODE_INTENT_ARTICLE = 1, CODE_INTENT_LOCATION = 2;
    Toolbar toolbar;
    String barCodeBoxMaster = "";
    LinearLayout llRegister;
    EditText etBarCodeArticle, etQuantity, etBarCodeLocation;
    ImageView ivScanBarCodeArticle, ivScanBarCodeLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despacho);
        initComponent();
    }

    private void initComponent() {
        try {
            llRegister = findViewById(R.id.llRegister);
            etBarCodeArticle = findViewById(R.id.etBarCodeArticle);
            ivScanBarCodeArticle = findViewById(R.id.ivScanBarCodeArticle);
            etQuantity = findViewById(R.id.etQuantity);
            etBarCodeLocation = findViewById(R.id.etBarCodeLocation);
            ivScanBarCodeLocation = findViewById(R.id.ivScanBarCodeLocation);
            // Set Toolbar
            toolbar = findViewById(R.id.toolbarBar);
            this.toolbar.setTitle("Despacho de artículos");
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
                    String codeLocation = etBarCodeLocation.getText().toString();
                    Integer quantity = Integer.parseInt(etQuantity.getText().toString());
                    if (codeBarArticle.isEmpty() || quantity == 0 || barCodeBoxMaster.isEmpty() || codeLocation.isEmpty()) {
                        Toast.makeText(DespachoActivity.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                    } else {
                        //Call Service
                        BoxMasterRequest request = new BoxMasterRequest();
                        request.setActionPath(3);
                        request.setBarCodeArticle(codeBarArticle);
                        request.setBarCodeBoxMasterOrigin(barCodeBoxMaster);
                        request.setQuantityArticle(quantity);
                        request.setCodeStorage(codeLocation);
                        BoxMasterTaskController task = new BoxMasterTaskController();
                        task.setListener(DespachoActivity.this);
                        task.execute(request);
                    }
                }
            });
            ivScanBarCodeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanBarCode(CODE_INTENT_ARTICLE);
                }
            });
            ivScanBarCodeLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanBarCode(CODE_INTENT_LOCATION);
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
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_LOCATION))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etBarCodeLocation.setText(codeBar);
                }
            }
        }
    }

    private void scanBarCode(int codeRequest) {
        /* Intent i = new Intent(this, ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        startActivityForResult(i, codeRequest);*/
        showDialogScanner(false, codeRequest);
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
            Toast.makeText(getApplicationContext(), "Los despachos fueron registrados correctamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error registrando los despachos.", Toast.LENGTH_LONG).show();
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
        if (action == CODE_INTENT_LOCATION) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etBarCodeLocation.setText(codeBar);
            }
        }
    }
}
