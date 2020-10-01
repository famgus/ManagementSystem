package com.ec.managementsystem.moduleView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class SensorActivity extends BaseActivity {

    private static final int CODIGO_PERMISOS_CAMARA = 1;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    ImageView ivClose;
    EditText etBarCode;
    TextView tvCounter;
    ImageView ivCamera;
    LinearLayout llFinish;
    boolean scanMultiple = false;
    int pathReception = -1;
    int code_intent = -1;
    String codeReader = "";
    int count = 0;
    Map<String, Integer> mapCodes;
    double totalUnit = -1;
    boolean showDialog = false;

    public void setScanMultiple(boolean scanMultiple) {
        this.scanMultiple = scanMultiple;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_reader_view);

        ivClose = findViewById(R.id.btncnl_dialog);
        etBarCode = findViewById(R.id.etBarCode);
        tvCounter = findViewById(R.id.tvCounter);
        ivCamera = findViewById(R.id.ivCamera);
        llFinish = findViewById(R.id.llFinish);
        mapCodes = new HashMap<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("scanMultiple")) {
            scanMultiple = bundle.getBoolean("scanMultiple", false);
        }
        if (bundle != null && bundle.containsKey("codeReader")) {
            codeReader = bundle.getString("codeReader", "");
        }
        if (bundle != null && bundle.containsKey("path")) {
            pathReception = bundle.getInt("path", 1);
        }
        if (bundle != null && bundle.containsKey("totalUnit")) {
            totalUnit = bundle.getDouble("totalUnit", -1);
        }
        if (bundle != null && bundle.containsKey("permisoCamaraConcedido")) {
            permisoCamaraConcedido = bundle.getBoolean("permisoCamaraConcedido", false);
        }
        if (bundle != null && bundle.containsKey("permisoSolicitadoDesdeBoton")) {
            permisoSolicitadoDesdeBoton = bundle.getBoolean("permisoSolicitadoDesdeBoton", false);
        }

        code_intent = Integer.parseInt(getIntent().getAction());

        if (scanMultiple) {
            count = 0;
            tvCounter.setText(String.valueOf(0));
            tvCounter.setVisibility(View.VISIBLE);
        } else {
            tvCounter.setVisibility(View.GONE);
        }

        llFinish.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code = etBarCode.getText().toString();
                        if (scanMultiple && code.equals("")) {
                            createDataResponse();
                        } else {
                            if (!code.equals("")) {
                                Utils.StopSound();
                                codeReader = code;
                                if (!scanMultiple) {
                                    updateMap();
                                    Utils.PlaySound(false);
                                }
                                FinishActivity();
                            } else {
                                Toast.makeText(SensorActivity.this, "Debe ingresar un código de barras", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );

        ivClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!permisoCamaraConcedido) {
                    Toast.makeText(SensorActivity.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_LONG).show();
                    permisoSolicitadoDesdeBoton = true;
                    verificarYPedirPermisosDeCamara();
                    return;
                }
                Intent i = new Intent(SensorActivity.this, ScannerActivity.class);
                i.putExtra("scanMultiple", scanMultiple);
                i.putExtra("path", pathReception);
                i.putExtra("totalUnit", totalUnit);
                i.setAction(String.valueOf(code_intent));
                startActivityForResult(i, code_intent);
            }
        });
        etBarCode.requestFocus();
        etBarCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String code = editable.toString();
                if(code.length() >= 2){
                    if(code.substring(code.length() - 1, code.length()).equals("\n")){
                        {
                            //etBarCode.setText("637358535603008420JEACD");
                            Utils.StopSound();
                            codeReader = etBarCode.getText().toString().replace("\n", "");
                            //etBarCode.setText(codeReader);
                            if (!showDialog && totalUnit != -1 && count + 1 >= totalUnit) {
                                showDialog = true;
                                ShowDialog("Alerta", "El total de artículos contados supera el total de unidades de la orden de compra");
                            }else {
                                tvCounter.setText(String.valueOf(++count));
                                updateMap();
                            }

                            Utils.PlaySound(false);
                            if (!showDialog) {
                                if (!scanMultiple && etBarCode.getText().length() > 0) {
                                    FinishActivity();
                                } else {
                                    if (scanMultiple) {
                                        etBarCode.setText("");
                                    }
                                }
                            }else {
                                createDataResponse();
                            }
                        }
                    }
                }
            }
        });
    }

    public void createDataResponse() {
        Utils.StopSound();
        Intent intentRegreso = new Intent();
        BundleResponse bundleResponse = new BundleResponse();
        bundleResponse.setMapCodes(mapCodes);
        intentRegreso.putExtra("codigo", bundleResponse);
        intentRegreso.setAction(getIntent().getAction());
        setResult(Activity.RESULT_OK, intentRegreso);
        finish();
    }

    public void FinishActivity() {
        // Search
        String barCodes = etBarCode.getText().toString();
        if (!barCodes.equals("")) {
            createDataResponse();
        } else {

            Toast ToastGravity = Toast.makeText(this, "Debe escanear un código de barras", Toast.LENGTH_SHORT);
            ToastGravity.setGravity(Gravity.CENTER, 0, 0);
            ToastGravity.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                        Intent i = new Intent(SensorActivity.this, ScannerActivity.class);
                        i.putExtra("scanMultiple", false);
                        i.putExtra("path", pathReception);
                        i.putExtra("totalUnit", totalUnit);
                        startActivityForResult(i, code_intent);
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(SensorActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(SensorActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }

    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(SensorActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_LONG).show();
    }

    private void updateMap() {
        if (mapCodes.containsKey(codeReader)) {
            int count = mapCodes.get(codeReader) + 1;
            mapCodes.put(codeReader, count);
        } else {
            mapCodes.put(codeReader, 1);
        }
    }

    public void ShowDialog(String subject, String body) {
        try {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SensorActivity.this);
            alertDialogBuilder
                    .setCancelable(false)
                    .setMessage(body)
                    .setPositiveButton(SensorActivity.this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            if (subject != null && subject.length() > 0) {
                alertDialogBuilder.setTitle(subject);
            }

            try {
                //enable audio
                AudioManager mgr = (AudioManager) SensorActivity.this.getSystemService(Context.AUDIO_SERVICE);
                mgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                //Wake up screen
                PowerManager powerManager = (PowerManager) SensorActivity.this.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                wakeLock.acquire();
            } catch (Exception e) {
                Utils.CreateLogFile("Utils.ShowDialog: " + e.getMessage());
            }

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            Utils.CreateLogFile("Utils.ShowDialog: " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }

}
