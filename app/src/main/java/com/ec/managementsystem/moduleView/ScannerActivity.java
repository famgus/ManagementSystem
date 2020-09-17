package com.ec.managementsystem.moduleView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.util.Utils;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView escanerZXing;
    private LinearLayout llCamera;
    private EditText etBarCode;
    private ImageView ivFinish;
    private TextView tvCounter;
    String codeReader = "";
    private boolean scanMultiple = false;
    int count = 0;
    Map<String, Integer> mapCodes;
    int pathReception = -1;
    double totalUnit = -1;
    boolean showDialog = false;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        escanerZXing = new ZXingScannerView(this);
        // Hacer que el contenido de la actividad sea el escaner
        //setContentView(escanerZXing);
        setContentView(R.layout.camera_view);
        llCamera = findViewById(R.id.llCamera);
        llCamera.addView(escanerZXing);
        etBarCode = findViewById(R.id.etBarCode);
        ivFinish = findViewById(R.id.ivFinish);
        tvCounter = findViewById(R.id.tvCounter);
        mapCodes = new HashMap<>();

        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etBarCode.getText().toString();
                if (!code.equals("")) {
                    Utils.StopSound();
                    codeReader = code;
                    if (!scanMultiple) {
                        updateMap();
                        Utils.PlaySound(false);
                    }
                    finishReader(1);
                } else {
                    Toast.makeText(ScannerActivity.this, "Debe ingresar un código de barras", Toast.LENGTH_LONG).show();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("scanMultiple")) {
            scanMultiple = bundle.getBoolean("scanMultiple", false);
        }
        if (bundle != null && bundle.containsKey("path")) {
            pathReception = bundle.getInt("path", 1);
        }
        if (bundle != null && bundle.containsKey("totalUnit")) {
            totalUnit = bundle.getDouble("totalUnit", -1);
        }
        if (scanMultiple) {
            count = 0;
            tvCounter.setText(String.valueOf(0));
            tvCounter.setVisibility(View.VISIBLE);
        } else {
            tvCounter.setVisibility(View.GONE);
        }
    }

    private void updateMap() {
        if (mapCodes.containsKey(codeReader)) {
            int count = mapCodes.get(codeReader) + 1;
            mapCodes.put(codeReader, count);
        } else {
            mapCodes.put(codeReader, 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // El "manejador" del resultado es esta misma clase, por eso implementamos ZXingScannerView.ResultHandler
        // Comenzar la cámara en onResume
        escanerZXing.setResultHandler(ScannerActivity.this);
        escanerZXing.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        escanerZXing.stopCamera(); // Pausar en onPause
    }

    // Estamos sobrescribiendo un método de la interfaz ZXingScannerView.ResultHandler
    @Override
    public void handleResult(Result resultado) {

        // Si quieres que se siga escaneando después de haber leído el código, descomenta lo siguiente:
        // Si la descomentas no recomiendo que llames a finish
        // Obener código/texto leído
        Utils.StopSound();
        codeReader = resultado.getText();
        etBarCode.setText(codeReader);
        tvCounter.setText(String.valueOf(++count));
        updateMap();
        if (!showDialog && totalUnit != -1 && count > totalUnit) {
            showDialog = true;
            ShowDialog(ScannerActivity.this, "Alerta", "El total de artículos contados supera el total de unidades de la orden de compra");
        }

        Utils.PlaySound(false);
        if (scanMultiple) {
            escanerZXing.resumeCameraPreview(ScannerActivity.this);
            new Thread() {
                public void run() {
                    try {
                        sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            finishReader(2);
        }
    }

    private void finishReader(int path) {
        try {
            // Preparar un Intent para regresar datos a la actividad que nos llamó
            Intent intentRegreso = new Intent();
            BundleResponse bundleResponse = new BundleResponse();
            bundleResponse.setMapCodes(mapCodes);
            intentRegreso.putExtra("codigo", bundleResponse);
            intentRegreso.setAction(getIntent().getAction());
            setResult(Activity.RESULT_OK, intentRegreso);
            // Cerrar la actividad. Ahora mira onActivityResult de MainActivity

            if (!scanMultiple || path == 1) {
                finish();
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog(Activity actv, String subject, String body) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(actv);
            alertDialogBuilder
                    .setCancelable(false)
                    .setMessage(body)
                    .setPositiveButton(actv.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            if (subject != null && subject.length() > 0) {
                alertDialogBuilder.setTitle(subject);
            }

            try {
                //enable audio
                AudioManager mgr = (AudioManager) actv.getSystemService(Context.AUDIO_SERVICE);
                mgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                //Wake up screen
                PowerManager powerManager = (PowerManager) actv.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
                wakeLock.acquire();
            } catch (Exception e) {
                Utils.CreateLogFile("Utils.ShowDialog: " + e.getMessage());
            }

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            Utils.CreateLogFile("Utils.ShowDialog: " + e.getMessage());
        }
    }
}
