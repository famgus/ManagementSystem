package com.ec.managementsystem.moduleView.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.BoxMaster;
import com.ec.managementsystem.clases.PedidoDetail;
import com.ec.managementsystem.clases.ProductDetail;
import com.ec.managementsystem.clases.request.ProductoRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.ProductoResponse;
import com.ec.managementsystem.interfaces.IDelegateProductTaskControl;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.ScannerActivity;
import com.ec.managementsystem.moduleView.ui.DialogLocationBoxMaster;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.ProductTaskController;
import com.ec.managementsystem.util.MySingleton;
import com.ec.managementsystem.util.Utils;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProductDetailsActivity extends BaseActivity implements IDelegateProductTaskControl, DialogLocationBoxMaster.DialogListener, DialogScanner.DialogScanerFinished {

    Toolbar toolbar;
    TextView tvCode, tvName, tvColor, tvSize, tvLocation, tvProvider;
    TextView tvCounterNumber, tvProductNameTable, tvCountScanner, tvRestScanner, tvTotalBoxMaster;
    ImageView scannerubication, btnnextscanner, btncompletescanner, btnNewProduct;
    FloatingActionButton btnEscanearByUnit;
    FloatingActionButton btnEscanearByLot;
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2, NEW_CODIGO_PRODUCT_INTENT = 3, CODIGO_INTENT_LOCATION = 4, CODE_INTENT_LOCATION = 400;
    private static boolean scannerkind = false;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    List<String> codes;
    double totalUnidades = 1;
    int totalcontados = 1;
    BoxMaster boxMasterSelected;
    int posBoxSelected;
    ProductDetail productDetailSelected;
    int totalInBoxMaster = 1;
    LinearLayout llTotalMasterBox;
    //By Lot
    LinearLayout llTotalMasterBoxByLot;
    EditText etTotalBoxMasterByLot;
    int pathReception = -1;
    DialogLocationBoxMaster dialogLocationBoxMaster;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        codes = new ArrayList<>();
        toolbar = findViewById(R.id.toolbarBar);
        tvCode = findViewById(R.id.tvCode);
        tvName = findViewById(R.id.tvProductName);
        tvColor = findViewById(R.id.tvColor);
        tvSize = findViewById(R.id.tvSize);
        tvLocation = findViewById(R.id.tvLocation);
        tvTotalBoxMaster = findViewById(R.id.tvTotalBoxMaster);
        tvProvider = findViewById(R.id.tvProvider);
        scannerubication = findViewById(R.id.btnscannerubicacion);
        btnEscanearByUnit = findViewById(R.id.fabByUnit);
        btnEscanearByLot = findViewById(R.id.fabByLot);
        btnnextscanner = findViewById(R.id.Nextscanner);
        btnNewProduct = findViewById(R.id.btnNewProduct);
        tvCountScanner = findViewById(R.id.tvCountScanner);
        tvRestScanner = findViewById(R.id.tvRestScanner);
        tvProductNameTable = findViewById(R.id.tvProductNameTable);
        btncompletescanner = findViewById(R.id.CompleteScanner);
        llTotalMasterBox = findViewById(R.id.llTotalMasterBox);
        //By Lot
        etTotalBoxMasterByLot = findViewById(R.id.etTotalBoxMasterByLot);
        llTotalMasterBoxByLot = findViewById(R.id.llTotalMasterBoxByLot);
        // Set Toolbar
        this.toolbar.setTitle("Detalles del producto");
        this.setupToolBar(toolbar);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String code = bundle.getString("code");
            if (existCodeBar(code)) {
                tvCode.setText(code);
                updateView();
            } else {
                Toast.makeText(this, "El código escaneado no corresponde a ningún producto en la orden de compra", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            pathReception = bundle.getInt("path", 1);
        }

        if (pathReception == 1) {  //Reception by Unit
            llTotalMasterBox.setVisibility(View.VISIBLE);
            llTotalMasterBoxByLot.setVisibility(View.GONE);
        } else {
            llTotalMasterBox.setVisibility(View.GONE);
            llTotalMasterBoxByLot.setVisibility(View.VISIBLE);
            etTotalBoxMasterByLot.setText(String.valueOf(totalcontados));
        }


        btncompletescanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalUnidades < totalcontados && pathReception == 1) {
                    ShowDialog(ProductDetailsActivity.this, "Alerta", "La cantidad de productos escaneada supera, supera el total de unidades.", 1);
                } else if (totalUnidades > totalcontados && pathReception == 1) {
                    ShowDialog(ProductDetailsActivity.this, "Alerta", "La cantidad de productos escaneada es menor que el total de unidades.", 1);
                } else {
                    dialogLocationBoxMaster = new DialogLocationBoxMaster();
                    dialogLocationBoxMaster.show(getSupportFragmentManager(), "alert dialog purchase");
                }
            }
        });

        btnnextscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escanear();
            }
        });

        btnEscanearByUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanear();
            }
        });

        scannerubication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannerUbication();
            }
        });

        btnNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escanearNewProduct();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.StopSound();
    }

    private void updateView() {
        if (MySingleton.getInstance().getProductoResponse() != null && MySingleton.getInstance().getProductoResponse().getProductDetail() != null) {
            productDetailSelected = MySingleton.getInstance().getProductoResponse().getProductDetail();
            tvCode.setText(productDetailSelected.getCodBarras());
            tvName.setText(productDetailSelected.getDescripcion());
            tvSize.setText(productDetailSelected.getTalla());
            PedidoDetail pedidoDetail = findProductDetail(productDetailSelected.getCodBarras());
            if (pedidoDetail != null) {
                tvColor.setText(pedidoDetail.getColor());
                tvProvider.setText(pedidoDetail.getNomProveedor());
                tvCountScanner.setText(String.valueOf(pedidoDetail.getUnidadesTotales()));
                tvRestScanner.setText(String.valueOf(totalcontados));
                totalUnidades = pedidoDetail.getUnidadesTotales();
                tvCountScanner.setText(String.valueOf(totalUnidades));
                tvRestScanner.setText(String.valueOf(totalcontados));
                tvTotalBoxMaster.setText(String.valueOf(totalInBoxMaster));
                codes.add(productDetailSelected.getCodBarras());
            } else {
                tvColor.setText(productDetailSelected.getColor());
                tvProvider.setText(productDetailSelected.getRefproveedor());
                tvCountScanner.setText(String.valueOf(totalUnidades));
                tvRestScanner.setText(String.valueOf(totalcontados));
                tvTotalBoxMaster.setText(String.valueOf(totalInBoxMaster));
                codes.add(productDetailSelected.getCodBarras());
            }
            tvProductNameTable.setText(productDetailSelected.getDescripcion());
            posBoxSelected = MySingleton.getInstance().getPosclicked();
            boxMasterSelected = MySingleton.getInstance().boxMasterList.get(posBoxSelected);
            boxMasterSelected.setCountProduct(codes.size());
            MySingleton.getInstance().getBoxMasterList().set(posBoxSelected, boxMasterSelected);
            if (boxMasterSelected != null) {
                tvLocation.setText(boxMasterSelected.getBarCode());
            } else {
                tvLocation.setText("");
            }
            if (totalUnidades < totalcontados) {
                ShowDialog(this, "Alerta", "La cantidad de productos escaneada supera, supera el total de unidades.", 0);
            }
        }
    }

    private boolean existCodeBar(String code) {
        for (PedidoDetail item : MySingleton.getInstance().getPedidoResponse().getPedidoDetailList()) {
            if ((item.getCodigoBarra() != null && item.getCodigoBarra().equals(code) || (item.getCb2() != null && item.getCb2().equals(code)) || (item.getcBar3() != null && item.getcBar3().equals(code)))) {
                return true;
            }
        }
        return false;
    }


    private PedidoDetail findProductDetail(String barCode) {
        for (PedidoDetail pedidoDetail : MySingleton.getInstance().getPedidoResponse().getPedidoDetailList()) {
            if ((pedidoDetail.getCodigoBarra() != null && pedidoDetail.getCodigoBarra().equals(barCode)) || (pedidoDetail.getCb2() != null && pedidoDetail.getCb2().equals(barCode)) || (pedidoDetail.getcBar3() != null && pedidoDetail.getcBar3().equals(barCode))) {
                return pedidoDetail;
            }
        }
        return null;
    }


    public void setupToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_white_24);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void escanear() {
        scannerkind = false;
       /* Intent i = new Intent(ProductDetailsActivity.this, ScannerActivity.class);
        i.putExtra("scanMultiple", true);
        i.putExtra("codeReader", productDetailSelected.getCodBarras());
        i.putExtra("totalUnit", totalUnidades);
        startActivityForResult(i, CODIGO_INTENT);*/
        showDialogScanner(true, CODIGO_INTENT, totalUnidades, productDetailSelected.getCodBarras());
    }

    private void escanearNewProduct() {
        scannerkind = false;
        /*Intent i = new Intent(ProductDetailsActivity.this, ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        startActivityForResult(i, NEW_CODIGO_PRODUCT_INTENT);*/
        showDialogScanner(false, NEW_CODIGO_PRODUCT_INTENT, -1, "");
    }

    private void showDialogScanner(boolean scanMultiple, int codeIntent, double totalU, String codeReader) {
        DialogScanner dialogScanner = new DialogScanner();
        dialogScanner.setScanMultiple(scanMultiple);
        dialogScanner.setPathReception(pathReception);
        dialogScanner.setCode_intent(codeIntent);
        dialogScanner.setPermisoCamaraConcedido(true);
        dialogScanner.setPermisoSolicitadoDesdeBoton(true);
        dialogScanner.setTotalUnit(totalU);
        dialogScanner.setCodeReader(codeReader);
        dialogScanner.show(getSupportFragmentManager(), "alert dialog generate codes");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction().equals(String.valueOf(CODIGO_INTENT))) {
                if (!scannerkind) {
                    BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                    if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                        String existDifferentProducts = "";
                        for (Map.Entry<String, Integer> item : bundleResponse.getMapCodes().entrySet()) {
                            if (!item.getKey().equals(productDetailSelected.getCodBarras())) {
                                Toast.makeText(this, "Nuevo producto encontrado", Toast.LENGTH_LONG).show();
                                totalcontados = 0;
                                existDifferentProducts = item.getKey();
                            }
                            for (int i = 0; i < item.getValue(); i++) {
                                codes.add(item.getKey());
                                totalInBoxMaster++;
                                totalcontados++;
                            }
                        }
                        tvRestScanner.setText(String.valueOf(totalcontados));
                        tvTotalBoxMaster.setText(String.valueOf(totalInBoxMaster));
                        btnEscanearByLot.setVisibility(View.GONE);
                        boxMasterSelected.setCountProduct(totalInBoxMaster);
                        MySingleton.getInstance().getBoxMasterList().set(posBoxSelected, boxMasterSelected);
                        if (!existDifferentProducts.equals("")) {
                            ProductoRequest productoRequest = new ProductoRequest();
                            productoRequest.setCodigoBarra(existDifferentProducts);
                            ProductTaskController taskController = new ProductTaskController();
                            taskController.setListener(ProductDetailsActivity.this);
                            taskController.execute(productoRequest);
                        }
                    } else {
                        Toast.makeText(this, "El código escaneado no corresponde al producto en la orden de compra", Toast.LENGTH_LONG).show();
                    }
                }
            }
            if (data != null && data.getAction().equals(String.valueOf(NEW_CODIGO_PRODUCT_INTENT))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    ProductoRequest productoRequest = new ProductoRequest();
                    productoRequest.setCodigoBarra(codeBar);
                    ProductTaskController taskController = new ProductTaskController();
                    taskController.setListener(ProductDetailsActivity.this);
                    taskController.execute(productoRequest);
                }
            }
        }
    }

    private void ScannerUbication() {
        scannerkind = true;
        Intent i = new Intent(ProductDetailsActivity.this, ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        startActivityForResult(i, CODIGO_INTENT_LOCATION);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton && !scannerkind) {
                        escanear();
                    }
                    if ((permisoSolicitadoDesdeBoton && !scannerkind)) {
                        ScannerUbication();
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(ProductDetailsActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProductResponse(ProductoResponse response) {
        if (response != null && response.getCode() == 200 && response.getProductDetail() != null) {
            MySingleton.getInstance().setProductoResponse(response);
            productDetailSelected = response.getProductDetail();
            if (existCodeBar(productDetailSelected.getCodBarras())) {
                tvCode.setText(productDetailSelected.getCodBarras());
                totalcontados = 1;
                totalInBoxMaster = totalInBoxMaster + 1;
                updateView();
            } else {
                Toast.makeText(this, "El código escaneado no corresponde a ningún producto en la orden de compra", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Producto no encontrada en el sistema", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void applyTexts(String locationBarCode) {
        if (locationBarCode != null && !locationBarCode.equals("")) {
            boxMasterSelected.setComplete(true);
            boxMasterSelected.setLocationBarCode(locationBarCode);
            if (pathReception == 2 && etTotalBoxMasterByLot.getText() != null && etTotalBoxMasterByLot.getText().length() > 0) {
                totalInBoxMaster = Integer.parseInt(etTotalBoxMasterByLot.getText().toString());
                codes = new ArrayList<>();
                for (int i = 0; i < totalInBoxMaster; i++) {
                    codes.add(productDetailSelected.getCodBarras());
                }
                boxMasterSelected.setComplete(true);
                boxMasterSelected.setCountProduct(totalInBoxMaster);
            }
            boxMasterSelected.setCodesProduct(codes);
            MySingleton.getInstance().getBoxMasterList().set(posBoxSelected, boxMasterSelected);
            MySingleton.getInstance().setStatusitenclicked(true);
            onBackPressed();
        }
    }

    public void ShowDialog(Activity actv, String subject, String body, final int action) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(actv);
            alertDialogBuilder
                    .setCancelable(false)
                    .setMessage(body)
                    .setPositiveButton(actv.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            if (action == 1) { //Terminar with box master
                                DialogLocationBoxMaster dialogLocationBoxMaster = new DialogLocationBoxMaster();
                                dialogLocationBoxMaster.setFragmentManager(getSupportFragmentManager());
                                dialogLocationBoxMaster.show(getSupportFragmentManager(), "alert dialog purchase");
                            }
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

    @Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {
        if (action == CODIGO_INTENT){
            if (!scannerkind) {
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String existDifferentProducts = "";
                    for (Map.Entry<String, Integer> item : bundleResponse.getMapCodes().entrySet()) {
                        if (!item.getKey().equals(productDetailSelected.getCodBarras())) {
                            Toast.makeText(this, "Nuevo producto encontrado", Toast.LENGTH_LONG).show();
                            totalcontados = 0;
                            existDifferentProducts = item.getKey();
                        }
                        for (int i = 0; i < item.getValue(); i++) {
                            codes.add(item.getKey());
                            totalInBoxMaster++;
                            totalcontados++;
                        }
                    }
                    tvRestScanner.setText(String.valueOf(totalcontados));
                    tvTotalBoxMaster.setText(String.valueOf(totalInBoxMaster));
                    btnEscanearByLot.setVisibility(View.GONE);
                    boxMasterSelected.setCountProduct(totalInBoxMaster);
                    MySingleton.getInstance().getBoxMasterList().set(posBoxSelected, boxMasterSelected);
                    if (!existDifferentProducts.equals("")) {
                        ProductoRequest productoRequest = new ProductoRequest();
                        productoRequest.setCodigoBarra(existDifferentProducts);
                        ProductTaskController taskController = new ProductTaskController();
                        taskController.setListener(ProductDetailsActivity.this);
                        taskController.execute(productoRequest);
                    }
                } else {
                    Toast.makeText(this, "El código escaneado no corresponde al producto en la orden de compra", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (action == NEW_CODIGO_PRODUCT_INTENT){
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                ProductoRequest productoRequest = new ProductoRequest();
                productoRequest.setCodigoBarra(codeBar);
                ProductTaskController taskController = new ProductTaskController();
                taskController.setListener(ProductDetailsActivity.this);
                taskController.execute(productoRequest);
            }
        }

        if(action == CODE_INTENT_LOCATION && dialogLocationBoxMaster != null){
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                dialogLocationBoxMaster.getEtLocation().setText(codeBar);
            }
        }
    }
}
