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
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.BoxMasterTaskController;
import com.ec.managementsystem.util.KeyValue;
import com.ec.managementsystem.util.KeyValueSpinner;

import java.util.ArrayList;

public class ReubicacionActivity extends BaseActivity implements IDelegateBoxMasterTaskControl, DialogScanner.DialogScanerFinished {
    private static final int CODE_INTENT_BOX_MASTER_ORIGEN = 5,CODE_INTENT_BOX_MASTER = 6,CODE_INTENT_ARTICLE = 1, CODE_INTENT_BOX_MASTER_DESTINY = 2,  CODE_INTENT_UBICACION_ORIGIN= 3,  CODE_INTENT_UBICACION_DESTINY= 4,  CODE_INTENT_BOX_MASTER_ORIGIN_2 = 5;
    private static final int CODE_INTENT_AU_UBICACION_ORIGIN = 1, CODE_INTENT_AU_UBICACION_DESTINY= 2, CODE_INTENT_AU_ARTICLE= 3;
    private static final int CODE_INTENT_ACMU_BOX_MASTER_ORIGEN = 1,CODE_INTENT_ACMU_UBICACION_DESTINY= 2,CODE_INTENT_ACMU_ARTICLE= 3;
    private static final int CODE_INTENT_AUCM_BOX_MASTER_DESTINY = 1,CODE_INTENT_AUCM_UBICACION_ORIGEN= 2,CODE_INTENT_AUCM_ARTICLE= 3;
    Toolbar toolbar;
    String barCodeBoxMaster = "";
    LinearLayout llRegister;
    EditText etBarCodeArticle, etQuantity, etBarCodeBoxMasterDestiny, etBarCodeBoxMasterOrigen,etAUBarCodeUbicacionOrigen,etAUBarCodeUbicacionDestino,etAUBarCodeArticle,etAUQuantity;
    ImageView ivScanBarCodeArticle, ivScanBarCodeBoxMasterDestiny,ivScanBarCodeBoxMasterOrigen,ivAUBarCodeUbicacionOrigen,ivAUUbicacionDestino,ivAUScanBarCodeArticle;
    ImageView ivBarCodeUbicacionOrigen, ivUbicacionDestino,ivScanBarCodeCajaMaster;
    EditText  etBarCodeUbicacionDestino, etBarCodeUbicacionOrigen,etBarCodeCajaMaster;
    Spinner spAction;
    LinearLayout llBoxMaster, llArticle, llArticloXUbication;
    KeyValue actionSelected;

    //articulo de cajaMaster a ubicacion
    LinearLayout llArticloCajaMasterUbicacion;
    EditText etACMUBarCodeMasterBoxOrigen,etACMUBarCodeUbicacionDestino,etACMUBarCodeArticle,etACMUQuantity;
    ImageView ivACMUBarCodeArticle,ivACMUBarCodeUbicacionDestino,ivACMUBarCodeMasterBoxOrigen;

    //articulo de ubicacion a cajamaster
    LinearLayout llArticloUbicacionCajaMaster;
    EditText etAUCMBarCodeMasterBoxDestiny,etAUCMBarCodeUbicacionOrigen,etAUCMBarCodeArticle,etAUCMQuantity;
    ImageView ivAUCMBarCodeMasterBoxDestiny,ivAUCMBarCodeUbicacionOrigen,ivAUCMBarCodeArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reubicacion);
        initComponent();
    }

    private void initComponent() {
        try {
            llRegister = findViewById(R.id.llRegister);

            // articulo ubicacio por ubicacion
            llArticloXUbication = findViewById(R.id.llArticloXUbication);
            etAUBarCodeUbicacionOrigen = findViewById(R.id.etAUBarCodeUbicacionOrigen);
            etAUBarCodeUbicacionDestino = findViewById(R.id.etAUBarCodeUbicacionDestino);
            etAUBarCodeArticle = findViewById(R.id.etAUBarCodeArticle);

            ivAUBarCodeUbicacionOrigen = findViewById(R.id.ivAUBarCodeUbicacionOrigen);
            ivAUUbicacionDestino=findViewById(R.id.ivAUUbicacionDestino);
            ivAUScanBarCodeArticle = findViewById(R.id.ivAUScanBarCodeArticle);

            etAUQuantity = findViewById(R.id.etAUQuantity);
            //Fin Articulo ubicacion por ubicacion

            //articulo de cajaMaster a ubicacion
            llArticloCajaMasterUbicacion = findViewById(R.id.llArticloCajaMasterUbicacion);
            etACMUBarCodeMasterBoxOrigen = findViewById(R.id.etACMUBarCodeMasterBoxOrigen);
            etACMUBarCodeUbicacionDestino = findViewById(R.id.etACMUBarCodeUbicacionDestino);
            etACMUBarCodeArticle = findViewById(R.id.etACMUBarCodeArticle);
            etACMUQuantity = findViewById(R.id.etACMUQuantity);
            ivACMUBarCodeArticle = findViewById(R.id.ivACMUBarCodeArticle);
            ivACMUBarCodeUbicacionDestino = findViewById(R.id.ivACMUBarCodeUbicacionDestino);
            ivACMUBarCodeMasterBoxOrigen = findViewById(R.id.ivACMUBarCodeMasterBoxOrigen);
            //fin articulo de cajaMaster a ubicacion

            //articulo de ubicacion a caja master
            llArticloUbicacionCajaMaster = findViewById(R.id.llArticloUbicacionCajaMaster);
            etAUCMBarCodeMasterBoxDestiny = findViewById(R.id.etAUCMBarCodeMasterBoxDestiny);
            etAUCMBarCodeUbicacionOrigen = findViewById(R.id.etAUCMBarCodeUbicacionOrigen);
            etAUCMBarCodeArticle = findViewById(R.id.etAUCMBarCodeArticle);
            etAUCMQuantity = findViewById(R.id.etAUCMQuantity);
            ivAUCMBarCodeMasterBoxDestiny = findViewById(R.id.ivAUCMBarCodeMasterBoxDestiny);
            ivAUCMBarCodeUbicacionOrigen = findViewById(R.id.ivAUCMBarCodeUbicacionOrigen);
            ivAUCMBarCodeArticle = findViewById(R.id.ivAUCMBarCodeArticle);
            //fin articulo de ubicacion a caja master

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

            //seteo las vistas del campo de codigo de barras de caja master
            ivScanBarCodeCajaMaster = findViewById(R.id.ivScanBarCodeCajaMaster);
            etBarCodeCajaMaster=findViewById(R.id.etBarCodeCajaMaster);

            etBarCodeBoxMasterOrigen = findViewById(R.id.etBarCodeBoxMasterOrigen);
            ivScanBarCodeBoxMasterOrigen = findViewById(R.id.ivScanBarCodeBoxMasterOrigen);


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
//            Bundle bundle = getIntent().getExtras();
//            if (bundle != null && bundle.containsKey("codeBarBoxMaster")) {
//                barCodeBoxMaster = bundle.getString("codeBarBoxMaster");
//            }
            //Set Listener
            llRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(actionSelected != null && actionSelected.getId() == 1) {
                        String codeBarBoxMasterDestinity = etBarCodeBoxMasterDestiny.getText().toString();
                        String codeBarArticle = etBarCodeArticle.getText().toString();
                        String codeBarBoxMasterDestiny = etBarCodeBoxMasterDestiny.getText().toString();
                        String codeBarBoxMasterOrigen = etBarCodeBoxMasterOrigen.getText().toString();
                        Integer quantity = Integer.parseInt(etQuantity.getText().toString());
                        if (codeBarArticle.isEmpty() || quantity == 0 || codeBarBoxMasterOrigen.isEmpty() || codeBarBoxMasterDestiny.isEmpty() || codeBarBoxMasterDestinity.isEmpty()) {
                            Toast.makeText(ReubicacionActivity.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                        } else {
                            //Call Service
                            BoxMasterRequest request = new BoxMasterRequest();
                            request.setActionPath(2);
                            request.setBarCodeArticle(codeBarArticle);
                            request.setBarCodeBoxMasterOrigin(codeBarBoxMasterOrigen);
                            request.setBarCodeBoxMasterDestiny(codeBarBoxMasterDestinity);
                            request.setQuantityArticle(quantity);
                            BoxMasterTaskController task = new BoxMasterTaskController();
                            task.setListener(ReubicacionActivity.this);
                            task.execute(request);
                        }
                    }else  if(actionSelected != null && actionSelected.getId() == 2)  {
                        String codeUbicacionOrigen = etBarCodeUbicacionOrigen.getText().toString();
                        String codeUbicacionDestinity = etBarCodeUbicacionDestino.getText().toString();
                        String codeCajaMaster = etBarCodeCajaMaster.getText().toString();
                        if (codeCajaMaster.isEmpty() || codeUbicacionOrigen.isEmpty() || codeUbicacionDestinity.isEmpty()) {
                            Toast.makeText(ReubicacionActivity.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                        } else {
                            //Call Service
                            BoxMasterRequest request = new BoxMasterRequest();
                            request.setActionPath(7);
                            request.setBarCodeBoxMasterOrigin(codeCajaMaster);
                            request.setUbicacionOrigen(codeUbicacionOrigen);
                            request.setUbicacionDestiny(codeUbicacionDestinity);
                            BoxMasterTaskController task = new BoxMasterTaskController();
                            task.setListener(ReubicacionActivity.this);
                            task.execute(request);
                        }
                    }else  if(actionSelected != null && actionSelected.getId() == 3)  {
                        String codeUbicacionOrigen = etAUBarCodeUbicacionOrigen.getText().toString();
                        String codeUbicacionDestinity = etAUBarCodeUbicacionDestino.getText().toString();
                        String codeBarArticle = etAUBarCodeArticle.getText().toString();
                        Integer quantity = Integer.parseInt(etAUQuantity.getText().toString());

                        //Integer quantity = Integer.parseInt(etAUQuantity.getText().toString());
                        if (codeBarArticle.isEmpty() || codeUbicacionOrigen.isEmpty() || codeUbicacionDestinity.isEmpty() || quantity == 0) {
                            Toast.makeText(ReubicacionActivity.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                        } else {
                            //Call Service
                            BoxMasterRequest request = new BoxMasterRequest();
                            request.setActionPath(9);
                            request.setUbicacionOrigen(codeUbicacionOrigen);
                            request.setBarCodeArticle(codeBarArticle);
                            request.setQuantityArticle(quantity);
                            request.setUbicacionDestiny(codeUbicacionDestinity);
                            BoxMasterTaskController task = new BoxMasterTaskController();
                            task.setListener(ReubicacionActivity.this);
                            task.execute(request);
                        }
                    }else  if(actionSelected != null && actionSelected.getId() == 4)  {
                        String codeBarBoxMasterOrigen = etACMUBarCodeMasterBoxOrigen.getText().toString();
                        String codeUbicacionDestinity = etACMUBarCodeUbicacionDestino.getText().toString();
                        String codeBarArticle = etACMUBarCodeArticle.getText().toString();
                        Integer quantity = Integer.parseInt(etACMUQuantity.getText().toString());

                        //Integer quantity = Integer.parseInt(etAUQuantity.getText().toString());
                        if (codeBarArticle.isEmpty() || codeBarBoxMasterOrigen.isEmpty() || codeUbicacionDestinity.isEmpty() || quantity == 0) {
                            Toast.makeText(ReubicacionActivity.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                        } else {
                            //Call Service
                            BoxMasterRequest request = new BoxMasterRequest();
                            request.setActionPath(10);
                            request.setBarCodeBoxMasterOrigin(codeBarBoxMasterOrigen);
                            request.setBarCodeArticle(codeBarArticle);
                            request.setQuantityArticle(quantity);
                            request.setUbicacionDestiny(codeUbicacionDestinity);
                            BoxMasterTaskController task = new BoxMasterTaskController();
                            task.setListener(ReubicacionActivity.this);
                            task.execute(request);
                        }
                    }else  if(actionSelected != null && actionSelected.getId() == 5)  {
                        String codeBarBoxMasterDestiny = etAUCMBarCodeMasterBoxDestiny.getText().toString();
                        String codeUbicacionOrigen = etAUCMBarCodeUbicacionOrigen.getText().toString();
                        String codeBarArticle = etAUCMBarCodeArticle.getText().toString();
                        Integer quantity = Integer.parseInt(etAUCMQuantity.getText().toString());
                        if (codeBarArticle.isEmpty() || codeBarBoxMasterDestiny.isEmpty() || codeUbicacionOrigen.isEmpty() || quantity == 0) {
                            Toast.makeText(ReubicacionActivity.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                        } else {
                            //Call Service
                            BoxMasterRequest request = new BoxMasterRequest();
                            request.setActionPath(11);
                            request.setBarCodeBoxMasterDestiny(codeBarBoxMasterDestiny);
                            request.setBarCodeArticle(codeBarArticle);
                            request.setQuantityArticle(quantity);
                            request.setUbicacionOrigen(codeUbicacionOrigen);
                            BoxMasterTaskController task = new BoxMasterTaskController();
                            task.setListener(ReubicacionActivity.this);
                            task.execute(request);
                        }
                    }
                }
            });

            //Article de cajaMaster a CajaMaster
           ivScanBarCodeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_ARTICLE);
                }
            });
            ivScanBarCodeBoxMasterDestiny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_BOX_MASTER_DESTINY);
                }
            });
            ivScanBarCodeBoxMasterOrigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_BOX_MASTER_ORIGEN);
                }
            });

            //BoxMaster de Ubicacion a ubicacion
            ivBarCodeUbicacionOrigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_UBICACION_ORIGIN);
                }
            });
            ivScanBarCodeCajaMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode( CODE_INTENT_BOX_MASTER);
                }
            });

            ivUbicacionDestino.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_UBICACION_DESTINY);
                }
            });

            //articulo de ubicacion por ubicacion
            ivAUBarCodeUbicacionOrigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_AU_UBICACION_ORIGIN);
                }
            });
            ivAUUbicacionDestino.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_AU_UBICACION_DESTINY);
                }
            });
            ivAUScanBarCodeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // scanBarCode(CODE_INTENT_AU_ARTICLE);
                }
            });

            //articulo CajaMaster a ubicacion
            ivACMUBarCodeMasterBoxOrigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_ACMU_BOX_MASTER_ORIGEN);
                }
            });
            ivACMUBarCodeUbicacionDestino.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_ACMU_UBICACION_DESTINY);
                }
            });
            ivACMUBarCodeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_ACMU_ARTICLE);
                }
            });

            //articulo ubicacion a cajamaster
            ivAUCMBarCodeMasterBoxDestiny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_AUCM_BOX_MASTER_DESTINY);
                }
            });
            ivAUCMBarCodeUbicacionOrigen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_AUCM_UBICACION_ORIGEN);
                }
            });
            ivAUCMBarCodeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //scanBarCode(CODE_INTENT_AUCM_ARTICLE);
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
        items2.add(new KeyValue(1, "Artículo De Caja Master a Caja Master"));
        items2.add(new KeyValue(2, "Caja Master de Ubicación a Ubicación"));
        items2.add(new KeyValue(3, "Artículo de Ubicación a  Ubicación"));
        items2.add(new KeyValue(4, "Artículo de Caja Master a  Ubicación"));
        items2.add(new KeyValue(5, "Artículo de Ubicación a  Caja Master"));
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
                if(position == 0){
                    llArticle.setVisibility(View.VISIBLE);
                    llBoxMaster.setVisibility(View.GONE);
                    llArticloXUbication.setVisibility(View.GONE);
                    llArticloCajaMasterUbicacion.setVisibility(View.GONE);
                    llArticloUbicacionCajaMaster.setVisibility(View.GONE);
                }else if (position == 1){
                    llArticle.setVisibility(View.GONE);
                    llBoxMaster.setVisibility(View.VISIBLE);
                    llArticloXUbication.setVisibility(View.GONE);
                    llArticloCajaMasterUbicacion.setVisibility(View.GONE);
                    llArticloUbicacionCajaMaster.setVisibility(View.GONE);
                }else if (position == 2){
                    llArticle.setVisibility(View.GONE);
                    llBoxMaster.setVisibility(View.GONE);
                    llArticloXUbication.setVisibility(View.VISIBLE);
                    llArticloCajaMasterUbicacion.setVisibility(View.GONE);
                    llArticloUbicacionCajaMaster.setVisibility(View.GONE);
                }else if (position == 3){
                    llArticle.setVisibility(View.GONE);
                    llBoxMaster.setVisibility(View.GONE);
                    llArticloXUbication.setVisibility(View.GONE);
                    llArticloCajaMasterUbicacion.setVisibility(View.VISIBLE);
                    llArticloUbicacionCajaMaster.setVisibility(View.GONE);
                }else if (position == 4){
                    llArticle.setVisibility(View.GONE);
                    llBoxMaster.setVisibility(View.GONE);
                    llArticloXUbication.setVisibility(View.GONE);
                    llArticloCajaMasterUbicacion.setVisibility(View.GONE);
                    llArticloUbicacionCajaMaster.setVisibility(View.VISIBLE);
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
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_BOX_MASTER))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etBarCodeCajaMaster.setText(codeBar);

                }
            }
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_BOX_MASTER_ORIGEN))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etBarCodeBoxMasterOrigen.setText(codeBar);
                }
            }
            //articulos ubicacion por ubicacion
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_AU_UBICACION_ORIGIN))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etAUBarCodeUbicacionOrigen.setText(codeBar);
                }
            }
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_AU_UBICACION_DESTINY))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etAUBarCodeUbicacionDestino.setText(codeBar);
                }
            }
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_AU_ARTICLE))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etAUBarCodeArticle.setText(codeBar);
                }
            }
            //articulo cajamaster a ubicacion
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_ACMU_BOX_MASTER_ORIGEN))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etACMUBarCodeMasterBoxOrigen.setText(codeBar);
                }
            }
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_ACMU_UBICACION_DESTINY))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etACMUBarCodeUbicacionDestino.setText(codeBar);
                }
            }
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_ACMU_ARTICLE))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etACMUBarCodeArticle.setText(codeBar);
                }
            }
            //articulo ubicacion a Caja Master
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_AUCM_BOX_MASTER_DESTINY))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etAUCMBarCodeMasterBoxDestiny.setText(codeBar);
                }
            }
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_AUCM_UBICACION_ORIGEN))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etAUCMBarCodeUbicacionOrigen.setText(codeBar);
                }
            }
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_AUCM_ARTICLE))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etAUCMBarCodeArticle.setText(codeBar);
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

    @Override
    public void onBoxMasterResponse(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            Toast.makeText(getApplicationContext(), "La reubicación fue registrada correctamente: "+response.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error registrando la reubicacion: "+response.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void ValidaBarCode(String codigo) {
        BoxMasterRequest request = new BoxMasterRequest();
        request.setActionPath(8);
        request.setBarCodeBoxMasterOrigin(codigo);
        BoxMasterTaskController task = new BoxMasterTaskController();
        task.setListener(ReubicacionActivity.this);
        task.execute(request);
    }
    @Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {
        //Articulo cajamaster a cajaMaster
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
        if (action == CODE_INTENT_BOX_MASTER_ORIGEN) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etBarCodeBoxMasterOrigen.setText(codeBar);
            }
        }
        //Box Master Ubicacion a Ubicacion
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
        if (action == CODE_INTENT_BOX_MASTER) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etBarCodeCajaMaster.setText(codeBar);

            }
        }
        //articulo ubicacion a ubicacion
        if (action == CODE_INTENT_AU_ARTICLE) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etAUBarCodeArticle.setText(codeBar);
            }
        }
        if (action == CODE_INTENT_AU_UBICACION_DESTINY) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etAUBarCodeUbicacionDestino.setText(codeBar);
            }
        }
        if (action == CODE_INTENT_AU_UBICACION_ORIGIN) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etAUBarCodeUbicacionOrigen.setText(codeBar);
            }
        }
        //articulo Caja Master a ubicacion
        if (action == CODE_INTENT_ACMU_ARTICLE) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etACMUBarCodeArticle.setText(codeBar);
            }
        }
        if (action == CODE_INTENT_ACMU_UBICACION_DESTINY) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etACMUBarCodeUbicacionDestino.setText(codeBar);
            }
        }
        if (action == CODE_INTENT_ACMU_BOX_MASTER_ORIGEN) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etACMUBarCodeMasterBoxOrigen.setText(codeBar);
            }
        }
        // articulo ubicacion a Caja Master
        if (action == CODE_INTENT_AUCM_ARTICLE) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etAUCMBarCodeArticle.setText(codeBar);
            }
        }
        if (action == CODE_INTENT_AUCM_UBICACION_ORIGEN) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etAUCMBarCodeUbicacionOrigen.setText(codeBar);
            }
        }
        if (action == CODE_INTENT_AUCM_BOX_MASTER_DESTINY) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etAUCMBarCodeMasterBoxDestiny.setText(codeBar);
            }
        }
    }
}
