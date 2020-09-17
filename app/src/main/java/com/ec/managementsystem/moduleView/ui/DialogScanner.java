package com.ec.managementsystem.moduleView.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.moduleView.ScannerActivity;
import com.ec.managementsystem.moduleView.merchandiseReception.PurchaseOrderDetailsActivity;
import com.ec.managementsystem.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static com.ec.managementsystem.util.Utils.ShowDialog;


public class DialogScanner extends AppCompatDialogFragment implements View.OnKeyListener {
    private static final int CODIGO_PERMISOS_CAMARA = 1;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    private DialogScanerFinished listener;
    ImageView ivClose;
    EditText etBarCode;
    TextView tvCounter;
    ImageView ivCamera;
    ImageView ivFinish;
    AlertDialog alertDialog;
    boolean scanMultiple = false;
    int pathReception = -1;
    Context context;
    int code_intent = -1;
    String codeReader = "";
    int count = 0;
    Map<String, Integer> mapCodes;
    double totalUnit = -1;
    boolean showDialog = false;

    public void setScanMultiple(boolean scanMultiple) {
        this.scanMultiple = scanMultiple;
    }

    public void setPathReception(int pathReception) {
        this.pathReception = pathReception;
    }

    public void setCode_intent(int code_intent) {
        this.code_intent = code_intent;
    }


    public void setPermisoCamaraConcedido(boolean permisoCamaraConcedido) {
        this.permisoCamaraConcedido = permisoCamaraConcedido;
    }

    public void setPermisoSolicitadoDesdeBoton(boolean permisoSolicitadoDesdeBoton) {
        this.permisoSolicitadoDesdeBoton = permisoSolicitadoDesdeBoton;
    }


    public void setCodeReader(String codeReader) {
        this.codeReader = codeReader;
    }

    public void setTotalUnit(double totalUnit) {
        this.totalUnit = totalUnit;
    }

    public DialogScanerFinished getListener() {
        return listener;
    }

    public void setListener(DialogScanerFinished listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.sensor_reader_view, null);
        ivClose = view.findViewById(R.id.btncnl_dialog);
        etBarCode = view.findViewById(R.id.etBarCode);
        tvCounter = view.findViewById(R.id.tvCounter);
        ivCamera = view.findViewById(R.id.ivCamera);
        ivFinish = view.findViewById(R.id.ivFinish);
        mapCodes = new HashMap<>();
        builder.setView(view);
        this.alertDialog = builder.create();
        if (scanMultiple) {
            count = 0;
            tvCounter.setText(String.valueOf(0));
            tvCounter.setVisibility(View.VISIBLE);
        } else {
            tvCounter.setVisibility(View.GONE);
        }
        ivFinish.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code = etBarCode.getText().toString();
                        if (!code.equals("")) {
                            Utils.StopSound();
                            codeReader = code;
                            if (!scanMultiple) {
                                updateMap();
                                Utils.PlaySound(false);
                            }
                            FinishDialog();
                        } else {
                            Toast.makeText(context, "Debe ingresar un código de barras", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        ivClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Aceptar
                        alertDialog.dismiss();
                    }
                }
        );

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!permisoCamaraConcedido) {
                    Toast.makeText(context, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_LONG).show();
                    permisoSolicitadoDesdeBoton = true;
                    verificarYPedirPermisosDeCamara();
                    return;
                }
                Intent i = new Intent(context, ScannerActivity.class);
                i.putExtra("scanMultiple", scanMultiple);
                i.putExtra("path", pathReception);
                i.putExtra("totalUnit", totalUnit);
                i.setAction(String.valueOf(code_intent));
                startActivityForResult(i, code_intent);
                alertDialog.dismiss();
            }
        });
        etBarCode.requestFocus();
        etBarCode.setOnKeyListener(this);
        etBarCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return alertDialog;

    }

    public void FinishDialog() {
        // Search
        String barCodes = etBarCode.getText().toString();
        if (!barCodes.equals("")) {
            BundleResponse bundleResponse = new BundleResponse();
            bundleResponse.setMapCodes(mapCodes);
            listener.onScannerBarCode(bundleResponse,code_intent);
            alertDialog.dismiss();
        } else {

            Toast ToastGravity = Toast.makeText(alertDialog.getContext(), "Debe escanear un código de barras", Toast.LENGTH_SHORT);
            ToastGravity.setGravity(Gravity.CENTER, 0, 0);
            ToastGravity.show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            listener = (DialogScanerFinished) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        switch (keyEvent.getAction())
        {
            case KeyEvent.ACTION_DOWN:
                break;
            case KeyEvent.ACTION_UP:
                //etBarCode.setText("001MD43991001310");
                Utils.StopSound();
                codeReader = etBarCode.getText().toString();
                etBarCode.setText(codeReader);
                tvCounter.setText(String.valueOf(++count));
                updateMap();
                if (!showDialog && totalUnit != -1 && count > totalUnit) {
                    showDialog = true;
                    ShowDialog( "Alerta", "El total de artículos contados supera el total de unidades de la orden de compra");
                }

                Utils.PlaySound(false);
                if (!scanMultiple && etBarCode.getText().length() > 0) {
                    FinishDialog();
                }
                break;
        }
        return  true;
    }

    public interface DialogScanerFinished {
        void onScannerBarCode(BundleResponse bundleResponse, int action);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                        Intent i = new Intent(context, ScannerActivity.class);
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
        int estadoDePermiso = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions((PurchaseOrderDetailsActivity) context,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }

    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(context, "No puedes escanear si no das permiso", Toast.LENGTH_LONG).show();
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
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
            alertDialogBuilder
                    .setCancelable(false)
                    .setMessage(body)
                    .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            if (subject != null && subject.length() > 0) {
                alertDialogBuilder.setTitle(subject);
            }

            try {
                //enable audio
                AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                mgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                //Wake up screen
                PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
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

}
