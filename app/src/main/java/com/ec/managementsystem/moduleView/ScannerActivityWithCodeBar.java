package com.ec.managementsystem.moduleView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.util.Utils;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivityWithCodeBar extends BaseActivity implements ZXingScannerView.ResultHandler {
    String codeReader = "";
    int count = 0;
    Map<String, Integer> mapCodes;
    int pathReception = -1;
    double totalUnit = -1;
    boolean showDialog = false;
    ArrayList<String> barCodes = new ArrayList<>();
    private ZXingScannerView escanerZXing;
    private LinearLayout llCamera;
    private TextView etBarCode;
    private ImageView ivFinish;
    private TextView tvCounter;
    private boolean scanMultiple = false;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;

    public ArrayList<String> getBarCodes() {
        return barCodes;
    }

    public void setBarCodes(ArrayList<String> barCodes) {
        this.barCodes = barCodes;
    }

    public void setScanMultiple(boolean scanMultiple) {
        this.scanMultiple = scanMultiple;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        escanerZXing = new ZXingScannerView(this);
        // Hacer que el contenido de la actividad sea el escaner
        //setContentView(escanerZXing);
        setContentView(R.layout.camera_view_with_codebar);
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
                        updateMapCodeRead();
                        Utils.PlaySound(false);
                    }
                    finishReader(1);
                } else {
                    Toast.makeText(ScannerActivityWithCodeBar.this, "Debe ingresar un código de barras", Toast.LENGTH_LONG).show();
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
        if (bundle != null && bundle.containsKey("barCodes")) {
            Log.i("onCreate", "Read Barcode");
            barCodes = bundle.getStringArrayList("barCodes");
            Log.i("onCreate", barCodes.toString());
        }
        if (scanMultiple) {
            count = 0;
            tvCounter.setText(String.valueOf(0));
            tvCounter.setVisibility(View.VISIBLE);
        } else {
            tvCounter.setVisibility(View.GONE);
        }

        if (bundle != null && bundle.containsKey("quantity")) {
            Log.i("onCreate", "Read Quantity");
            count = bundle.getInt("quantity");
            Log.i("onCreate", String.valueOf(count));
            tvCounter.setText(String.valueOf(count));
            for (int i = 1; i <= count; i++) {
                updateMap();
            }
        }
    }


    private void updateMapCodeRead() {
        int count = mapCodes.size();
        Log.i("updateMapCodeRead", String.valueOf(count));
        mapCodes.put(codeReader, count);
    }

    private void updateMap() {
        int count = mapCodes.size();
        Log.i("updateMap", String.valueOf(count));
        mapCodes.put(String.valueOf(count), count);
    }

    @Override
    public void onResume() {
        super.onResume();
        // El "manejador" del resultado es esta misma clase, por eso implementamos ZXingScannerView.ResultHandler
        // Comenzar la cámara en onResume
        escanerZXing.setResultHandler(ScannerActivityWithCodeBar.this);
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
        boolean isValid=true;
        Log.i("handleResult", String.valueOf(count));
        Log.i("handleResult", resultado.getText());
        Utils.StopSound();
        codeReader = resultado.getText();
        etBarCode.setText(codeReader);

        if (totalUnit != -1 && count + 1 > totalUnit) {
            showDialog = true;
            ShowDialog(ScannerActivityWithCodeBar.this, "Alerta", "El total de artículos contados supera el total de unidades de la orden de compra");
            isValid=false;
        } else {
            if (!barCodes.isEmpty()) {
                boolean valid = false;
                for (int i = 0; i < barCodes.size() - 1; i++) {
                    String barCode = barCodes.get(i).trim();
                    Log.i("Compare 1", codeReader.trim());
                    Log.i("Compare 2", barCode);
                    if (codeReader.trim().equals(barCode)) {
                        Log.i("Done", barCodes.get(i));
                        valid = true;
                        break;
                    }
                }
                Log.i("Valid", String.valueOf(valid));
                if (!valid) {
                    Toast.makeText(ScannerActivityWithCodeBar.this, "El producto no coincide", Toast.LENGTH_LONG).show();
                } else {
                    tvCounter.setText(String.valueOf(++count));
                    updateMap();
                }
            } else {
                tvCounter.setText(String.valueOf(++count));
                updateMapCodeRead();
            }
        }

        if (isValid) {
            Utils.PlaySound(false);
            if (!showDialog) {
                if (scanMultiple) {
                    escanerZXing.resumeCameraPreview(ScannerActivityWithCodeBar.this);
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
            } else {
                finishReader(2);
            }
        }

    }

    public void createDataResponse() {
        Intent intentRegreso = new Intent();
        BundleResponse bundleResponse = new BundleResponse();
        bundleResponse.setMapCodes(mapCodes);
        intentRegreso.putExtra("codigo", bundleResponse);
        intentRegreso.setAction(getIntent().getAction());
        setResult(Activity.RESULT_OK, intentRegreso);
    }

    private void finishReader(int path) {
        try {
            this.createDataResponse();
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
