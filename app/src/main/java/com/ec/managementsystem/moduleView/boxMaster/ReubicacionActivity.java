package com.ec.managementsystem.moduleView.boxMaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.request.BoxMasterRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.interfaces.IDelegateBoxMasterTaskControl;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.SensorActivity;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.BoxMasterTaskController;
import com.ec.managementsystem.util.KeyValue;
import com.ec.managementsystem.util.KeyValueSpinner;

import java.util.ArrayList;

public class ReubicacionActivity extends BaseActivity implements IDelegateBoxMasterTaskControl, DialogScanner.DialogScanerFinished {
    private static final int CODE_INTENT_ARTICLE = 1, CODE_INTENT_BOX_MASTER_DESTINY = 2, CODE_INTENT_UBICACION_ORIGIN = 3, CODE_INTENT_UBICACION_DESTINY = 4, CODE_INTENT_BOX_MASTER_ORIGIN_2 = 5;
    Toolbar toolbar;
    String barCodeBoxMaster = "";
    LinearLayout llRegister;
    EditText etBarCodeArticle, etQuantity, etBarCodeBoxMasterDestiny;
    ImageView ivScanBarCodeArticle, ivScanBarCodeBoxMasterDestiny;
    ImageView ivBarCodeUbicacionOrigen, ivUbicacionDestino;
    EditText etBarCodeUbicacionDestino, etBarCodeUbicacionOrigen;
    Spinner spAction;
    LinearLayout llBoxMaster, llArticle;
    KeyValue actionSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reubicacion);
        initComponent();
    }

    private void initComponent() {
        try {
            llRegister = findViewById(R.id.llRegister);
            etBarCodeArticle = findViewById(R.id.etBarCodeArticle);
            ivScanBarCodeArticle = findViewById(R.id.ivScanBarCodeArticle);
            etQuantity = findViewById(R.id.etQuantity);
            etBarCodeBoxMasterDestiny = findViewById(R.id.etBarCodeBoxMasterDestiny);
            ivScanBarCodeBoxMasterDestiny = findViewById(R.id.ivScanBarCodeBoxMasterDestiny);
            spAction = findViewById(R.id.spAction);
            llBoxMaster = findViewById(R.id.llBoxMaster);
            llArticle = findViewById(R.id.llArticle);

            ivBarCodeUbicacionOrigen = findViewById(R.id.ivBarCodeUbicacionOrigen);
            etBarCodeUbicacionOrigen = findViewById(R.id.etBarCodeUbicacionOrigen);

            ivUbicacionDestino = findViewById(R.id.ivUbicacionDestino);
            etBarCodeUbicacionDestino = findViewById(R.id.etBarCodeUbicacionDestino);

            llBoxMaster.setVisibility(View.GONE);
            // Set Toolbar
            toolbar = findViewById(R.id.toolbarBar);
            this.toolbar.setTitle("Reubicación de artículos");
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
                    if (actionSelected != null && actionSelected.getId() == 1) {
                        String codeBarBoxMasterDestinity = etBarCodeBoxMasterDestiny.getText().toString();
                        String codeBarArticle = etBarCodeArticle.getText().toString();
                        String codeBarBoxMasterDestiny = etBarCodeBoxMasterDestiny.getText().toString();
                        Integer quantity = Integer.parseInt(etQuantity.getText().toString());
                        if (codeBarArticle.isEmpty() || quantity == 0 || barCodeBoxMaster.isEmpty() || codeBarBoxMasterDestiny.isEmpty() || codeBarBoxMasterDestinity.isEmpty()) {
                            Toast.makeText(ReubicacionActivity.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                        } else {
                            //Call Service
                            BoxMasterRequest request = new BoxMasterRequest();
                            request.setActionPath(2);
                            request.setBarCodeArticle(codeBarArticle);
                            request.setBarCodeBoxMasterOrigin(barCodeBoxMaster);
                            request.setBarCodeBoxMasterDestiny(codeBarBoxMasterDestiny);
                            request.setQuantityArticle(quantity);
                            BoxMasterTaskController task = new BoxMasterTaskController();
                            task.setListener(ReubicacionActivity.this);
                            task.execute(request);
                        }
                    } else if (actionSelected != null && actionSelected.getId() == 2) {
                        String codeUbicacionOrigen = etBarCodeUbicacionOrigen.getText().toString();
                        String codeUbicacionDestinity = etBarCodeUbicacionDestino.getText().toString();
                        if (barCodeBoxMaster.isEmpty() || codeUbicacionOrigen.isEmpty() || codeUbicacionDestinity.isEmpty()) {
                            Toast.makeText(ReubicacionActivity.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                        } else {
                            //Call Service
                            BoxMasterRequest request = new BoxMasterRequest();
                            request.setActionPath(7);
                            request.setBarCodeBoxMasterOrigin(barCodeBoxMaster);
                            request.setUbicacionOrigen(codeUbicacionOrigen);
                            request.setUbicacionDestiny(codeUbicacionDestinity);
                            BoxMasterTaskController task = new BoxMasterTaskController();
                            task.setListener(ReubicacionActivity.this);
                            task.execute(request);
                        }
                    }
                }
            });

            //Article
            ivScanBarCodeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanBarCode(CODE_INTENT_ARTICLE);
                }
            });
            ivScanBarCodeBoxMasterDestiny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanBarCode(CODE_INTENT_BOX_MASTER_DESTINY);
                }
            });

            //Box Master
            ivBarCodeUbicacionOrigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanBarCode(CODE_INTENT_UBICACION_ORIGIN);
                }
            });
            ivUbicacionDestino.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanBarCode(CODE_INTENT_UBICACION_DESTINY);
                }
            });

            //Load Spinner
            loadSpinner();
        } catch (Exception e) {
            Toast.makeText(this, "Error inicializando la actividad", Toast.LENGTH_LONG).show();
        }
    }

    private void loadSpinner() {
        ArrayList<KeyValue> items2 = new ArrayList<>();
        items2.add(new KeyValue(1, "Artículo"));
        items2.add(new KeyValue(2, "Caja Master"));
        KeyValueSpinner motiveAdapter = new KeyValueSpinner(this, items2);
        spAction.setAdapter(motiveAdapter);
        spAction.post(new Runnable() {
            @Override
            public void run() {
                spAction.setSelection(0);
                actionSelected = (KeyValue) spAction.getSelectedItem();
            }
        });
        spAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actionSelected = (KeyValue) spAction.getSelectedItem();
                if (position == 0) {
                    llArticle.setVisibility(View.VISIBLE);
                    llBoxMaster.setVisibility(View.GONE);
                } else {
                    llArticle.setVisibility(View.GONE);
                    llBoxMaster.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_BOX_MASTER_DESTINY))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etBarCodeBoxMasterDestiny.setText(codeBar);
                }
            }

            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_UBICACION_ORIGIN))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etBarCodeUbicacionOrigen.setText(codeBar);
                }
            }
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_UBICACION_DESTINY))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etBarCodeUbicacionDestino.setText(codeBar);
                }
            }
        }
    }

    private void scanBarCode(int requestCode) {
        /*Intent i = new Intent(this, ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        startActivityForResult(i, requestCode);*/
        showDialogScanner(false, requestCode);
    }

    private void showDialogScanner(boolean scanMultiple, int codeIntent) {
        Intent i = new Intent(this, SensorActivity.class);
        i.putExtra("scanMultiple", scanMultiple);
        i.putExtra("permisoCamaraConcedido", true);
        i.putExtra("permisoSolicitadoDesdeBoton", true);
        i.setAction(String.valueOf(codeIntent));
        startActivityForResult(i, codeIntent);

    }

    @Override
    public void onBoxMasterResponse(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            Toast.makeText(getApplicationContext(), "La reubicación fue registrada correctamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error registrando la reubicacion.", Toast.LENGTH_LONG).show();
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
        if (action == CODE_INTENT_BOX_MASTER_DESTINY) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etBarCodeBoxMasterDestiny.setText(codeBar);
            }
        }

        //Box Master

        if (action == CODE_INTENT_UBICACION_ORIGIN) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etBarCodeUbicacionOrigen.setText(codeBar);
            }
        }

        if (action == CODE_INTENT_UBICACION_DESTINY) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etBarCodeUbicacionDestino.setText(codeBar);
            }
        }

    }
}
