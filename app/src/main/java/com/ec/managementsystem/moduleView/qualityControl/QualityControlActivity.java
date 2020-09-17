package com.ec.managementsystem.moduleView.qualityControl;

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
import com.ec.managementsystem.clases.request.QualityControlRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.ListMotivesResponse;
import com.ec.managementsystem.clases.responses.MotivesResponse;
import com.ec.managementsystem.interfaces.IDelegateInsertQualityTaskControl;
import com.ec.managementsystem.interfaces.IDelegateMotivesControl;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.InsertQualityTaskController;
import com.ec.managementsystem.task.MotivesTaskController;
import com.ec.managementsystem.util.KeyValue;
import com.ec.managementsystem.util.KeyValueSpinner;
import com.ec.managementsystem.util.MySingleton;

import java.util.ArrayList;

public class QualityControlActivity extends BaseActivity implements IDelegateInsertQualityTaskControl, DialogScanner.DialogScanerFinished, IDelegateMotivesControl {
    private static final int CODE_INTENT_ARTICLE = 1, CODE_INTENT_LOCATION = 2;
    Toolbar toolbar;
    EditText etBarCodeArticle, etBarCodeLocation;
    ImageView ivScanBarCodeArticle, ivScanBarCodeLocation;
    Spinner spMotive;
    LinearLayout llRegister;
    KeyValue motiveSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_calidad);
        initComponent();
        downloadMotives();
    }

    private void downloadMotives() {
        MotivesTaskController task = new MotivesTaskController();
        task.setListener(this);
        task.execute();
    }

    private void initComponent() {
        try {
            this.toolbar = findViewById(R.id.toolbarBar);
            this.etBarCodeArticle = findViewById(R.id.etBarCodeArticle);
            this.etBarCodeLocation = findViewById(R.id.etBarCodeLocation);
            this.ivScanBarCodeArticle = findViewById(R.id.ivScanBarCodeArticle);
            this.ivScanBarCodeLocation = findViewById(R.id.ivScanBarCodeLocation);
            this.spMotive = findViewById(R.id.spMotive);
            this.llRegister = findViewById(R.id.llRegister);
            // Set Toolbar
            this.toolbar = findViewById(R.id.toolbarBar);
            this.toolbar.setTitle("Control de calidad");
            this.setupToolBar(toolbar);
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            //Put Listener
            this.llRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String codeBarArticle = etBarCodeArticle.getText().toString();
                    String codeBarLocation = etBarCodeLocation.getText().toString();
                    if (codeBarArticle.isEmpty() || codeBarLocation.isEmpty() || motiveSelected == null) {
                        Toast.makeText(QualityControlActivity.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                    } else {
                        //Call Service
                        QualityControlRequest request = new QualityControlRequest();
                        request.setMotive(motiveSelected.getId());
                        request.setDescriptionMotive(motiveSelected.getValue());
                        request.setBarCode(codeBarArticle);
                        request.setCodeLocation(codeBarLocation);
                        request.setUser(MySingleton.getUser().getUsername());
                        InsertQualityTaskController task = new InsertQualityTaskController();
                        task.setListener(QualityControlActivity.this);
                        task.execute(request);
                    }
                }
            });
            this.ivScanBarCodeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanBarCode(CODE_INTENT_ARTICLE);
                }
            });
            this.ivScanBarCodeLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scanBarCode(CODE_INTENT_LOCATION);
                }
            });
            //Load Spinner
            loadSpinner();
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

    private void scanBarCode(int requestCode) {
        /*Intent i = new Intent(this, ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        startActivityForResult(i, requestCode);*/
        showDialogScanner(false, requestCode);
    }

    private void showDialogScanner(boolean scanMultiple, int codeIntent) {
        DialogScanner dialogScanner = new DialogScanner();
        dialogScanner.setScanMultiple(scanMultiple);
        dialogScanner.setCode_intent(codeIntent);
        dialogScanner.setPermisoCamaraConcedido(true);
        dialogScanner.setPermisoSolicitadoDesdeBoton(true);
        dialogScanner.show(getSupportFragmentManager(), "alert dialog generate codes");

    }

    private void loadSpinner() {
        ArrayList<KeyValue> items2 = new ArrayList<>();
        items2.add(new KeyValue(1, "Bueno"));
        items2.add(new KeyValue(2, "Dañado"));
        items2.add(new KeyValue(3, "Roto"));
        items2.add(new KeyValue(4, "Mojado"));
        KeyValueSpinner motiveAdapter = new KeyValueSpinner(this, items2);
        spMotive.setAdapter(motiveAdapter);
        spMotive.post(new Runnable() {
            @Override
            public void run() {
                spMotive.setSelection(0);
                motiveSelected = (KeyValue) spMotive.getSelectedItem();
            }
        });
        spMotive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                motiveSelected = (KeyValue) spMotive.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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

    @Override
    public void onSuccessMotives(ListMotivesResponse response) {
        if (response != null && response.getCode() == 200) {
            ArrayList<KeyValue> items2 = new ArrayList<>();
            for (MotivesResponse item : response.getListMotives()) {
                items2.add(new KeyValue(item.getIdMotive(), item.getDescription()));
            }
            KeyValueSpinner motiveAdapter = new KeyValueSpinner(this, items2);
            spMotive.setAdapter(motiveAdapter);
        } else {
            Toast.makeText(getApplicationContext(), "Error obteniendo los motivos.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInsertQualityResponse(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            Toast.makeText(getApplicationContext(), "Control de Calidad creado correctamente", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Error creando el control de calidad.", Toast.LENGTH_LONG).show();
        }
    }
}
