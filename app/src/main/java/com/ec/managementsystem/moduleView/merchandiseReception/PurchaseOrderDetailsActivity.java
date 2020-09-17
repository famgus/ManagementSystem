package com.ec.managementsystem.moduleView.merchandiseReception;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.BoxMaster;
import com.ec.managementsystem.clases.PedidoDetail;
import com.ec.managementsystem.clases.PedidoDetailSmall;
import com.ec.managementsystem.clases.request.BarCodeRequest;
import com.ec.managementsystem.clases.request.BoxMasterRequest;
import com.ec.managementsystem.clases.request.ProductoRequest;
import com.ec.managementsystem.clases.request.PurchaseOrderRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.ProductoResponse;
import com.ec.managementsystem.interfaces.IDelegateBarCodeTaskControl;
import com.ec.managementsystem.interfaces.IDelegateBoxMasterTaskControl;
import com.ec.managementsystem.interfaces.IDelegateInsertPurchaseTaskControl;
import com.ec.managementsystem.interfaces.IDelegateProductTaskControl;
import com.ec.managementsystem.interfaces.IListenerBoxMaster;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.adapters.BoxMasterAdapter;
import com.ec.managementsystem.moduleView.login.User;
import com.ec.managementsystem.moduleView.product.ProductDetailsActivity;
import com.ec.managementsystem.moduleView.ui.DialogGenerateBarCodes;
import com.ec.managementsystem.moduleView.ui.DialogNumFragment;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.BarCodeTaskController;
import com.ec.managementsystem.task.BoxMasterTaskController;
import com.ec.managementsystem.task.InsertPurchaseTaskController;
import com.ec.managementsystem.task.ProductTaskController;
import com.ec.managementsystem.util.MySingleton;
import com.ec.managementsystem.util.Utils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PurchaseOrderDetailsActivity extends BaseActivity implements DialogNumFragment.DialogListenerFraccionar, IDelegateProductTaskControl, IDelegateInsertPurchaseTaskControl, IDelegateBarCodeTaskControl, DialogGenerateBarCodes.DialogListener, DialogScanner.DialogScanerFinished, IListenerBoxMaster, IDelegateBoxMasterTaskControl {

    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2, BOX_CODE_INTENT = 3;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    Toolbar toolbar;
    RecyclerView rvBoxMasterList;
    private TableLayout tableLayoutinformation;
    private TableLayout tableLayoutBoxMaster;
    FloatingActionButton fabByUnit;
    FloatingActionButton fabByLot;
    FloatingActionMenu btnmenu;
    TextView textheadcolumnhide, txtNumberOrder, txtProviderValue, txtDateOrderValue, txtDateReceptionValue;
    TextView tvheaderhiddenCodes;
    public List<TestProduct> productList;
    ImageView ivAddBoxMaster;
    LinearLayout llRegister;
    public List<PedidoDetail> pedidoDetailList;
    public List<BoxMaster> boxMasterList;
    public List<BoxMaster> filterBoxMasterList;
    int pathReception = 1;
    BoxMasterAdapter boxMasterAdapter;
    String codeBar = "";
    BoxMaster boxMasterToRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_details);
        MySingleton.getInstance().setRegisterPurchaseOrder(false);
        verificarYPedirPermisosDeCamara();
        tableLayoutinformation = findViewById(R.id.tlTable01D);
        tableLayoutBoxMaster = findViewById(R.id.tlTable01DCodes);
        fabByUnit = findViewById(R.id.fabByUnit);
        fabByLot = findViewById(R.id.fabByLot);
        btnmenu = findViewById(R.id.btn_menu);
        toolbar = findViewById(R.id.toolbar);
        ivAddBoxMaster = findViewById(R.id.ivActionAdd);
        llRegister = findViewById(R.id.llRegister);
        rvBoxMasterList = findViewById(R.id.rvBoxMasterList);

        txtNumberOrder = findViewById(R.id.txtNumberOrder);
        txtProviderValue = findViewById(R.id.txtNameProvider);
        txtDateOrderValue = findViewById(R.id.txtFechaEntrega);
        txtDateReceptionValue = findViewById(R.id.txtFechaRecibo);
        productList = new ArrayList<>();

        pedidoDetailList = new ArrayList<>(MySingleton.getInstance().getPedidoResponse().getPedidoDetailList());
        boxMasterList = new ArrayList<>();
        FillInformation();

        initCollection();
        initRecyclerView();

        // Set Toolbar
        this.toolbar.setTitle("Detalles Orden de Compra");
        this.setupToolBar(toolbar);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fabByUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnmenu.close(true);
                pathReception = 1;
                ShowHidden();
            }
        });

        fabByLot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnmenu.close(true);
                pathReception = 2;
                ShowHidden();
            }
        });

        textheadcolumnhide = findViewById(R.id.tvheaderhidden);
        textheadcolumnhide.setVisibility(View.GONE);
        tvheaderhiddenCodes = findViewById(R.id.tvheaderhiddenCodes);
        tvheaderhiddenCodes.setVisibility(View.GONE);
        //DrawTable();

        ivAddBoxMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickScanBarCode();

            }
        });

        llRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Integer> mapCode = new HashMap<>();
                List<BoxMaster> boxMasterList = MySingleton.getInstance().boxMasterList;
                for (BoxMaster box : boxMasterList) {
                    box.getProductDetail().clear();
                    for (String barCodeProduct : box.getCodesProduct()) {
                        String key = box.getBarCode() + "-" + barCodeProduct;
                        if (mapCode.containsKey(key)) {
                            int count = mapCode.get(key) + 1;
                            mapCode.put(key, count);
                        } else {
                            mapCode.put(key, 1);
                            PedidoDetail pedido = findProductDetail(barCodeProduct);
                            if (pedido != null) {
                                PedidoDetailSmall pedidoDetailSmall = new PedidoDetailSmall();
                                pedidoDetailSmall.setSuPedido(pedido.getSuPedido());
                                pedidoDetailSmall.setNomProveedor(pedido.getNomProveedor());
                                pedidoDetailSmall.setCodArticulo(pedido.getCodArticulo());
                                pedidoDetailSmall.setColor(pedido.getColor());
                                pedidoDetailSmall.setTalla(pedido.getTalla());
                                pedidoDetailSmall.setDescricion(pedido.getDescricion());
                                pedidoDetailSmall.setCodigoBarra(pedido.getCodigoBarra());
                                pedidoDetailSmall.setCb2(pedido.getCb2());
                                pedidoDetailSmall.setcBar3(pedido.getcBar3());
                                box.getProductDetail().add(pedidoDetailSmall);
                            }
                        }
                    }
                }
                if (mapCode.size() > 0) {
                    PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest();
                    // purchaseOrderRequest.setBoxMasterList(boxMasterList);
                    List<PedidoDetail> pedidoDetailList = new ArrayList<>();
                    for (Map.Entry<String, Integer> item : mapCode.entrySet()) {
                        String [] keys = item.getKey().split("-");
                        String barCodeProduct = keys[1];
                        PedidoDetail pedido = findProductDetail(barCodeProduct);
                        if (pedido != null) {
                            pedido.setUnidades(item.getValue());
                            pedidoDetailList.add(pedido);
                        }
                    }
                    purchaseOrderRequest.setPedidoList(pedidoDetailList);
                    purchaseOrderRequest.setBoxMasterList(boxMasterList);
                    InsertPurchaseTaskController task = new InsertPurchaseTaskController();
                    task.setListener(PurchaseOrderDetailsActivity.this);
                    task.execute(purchaseOrderRequest);
                }
            }
        });
    }

    private boolean existPedido(PedidoDetail pedido, List<PedidoDetailSmall> pedidoDetailSmallList) {
        for (PedidoDetailSmall pedidoDetailSmall : pedidoDetailSmallList) {
            if ((pedidoDetailSmall.getCodigoBarra() != null && pedidoDetailSmall.getCodigoBarra().equals(pedido.getCodigoBarra())) || (pedidoDetailSmall.getCb2() != null && pedidoDetailSmall.getCb2().equals(pedido.getCb2())) || (pedidoDetailSmall.getcBar3() != null && pedidoDetailSmall.getcBar3().equals(pedido.getcBar3()))) {
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


    @Override
    protected void onResume() {
        super.onResume();
        //ChangeIconStatus();
        boxMasterAdapter.notifyDataSetChanged();
        llRegister.setVisibility(View.VISIBLE);
        Utils.StopSound();
        //TestPedido();
    }

    public void FillInformation() {
        txtNumberOrder.setText(String.valueOf(pedidoDetailList.get(0).getSuPedido()));
        txtProviderValue.setText(String.valueOf(pedidoDetailList.get(0).getNomProveedor()));
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        txtDateOrderValue.setText(pedidoDetailList.get(0).getFechaPedido());
        txtDateReceptionValue.setText(pedidoDetailList.get(0).getFechaEntrega());
    }

    public void ChangeIconStatus() {
        int isCheck = 0;
        for (BoxMaster item : boxMasterList) {
            if (item.isComplete()) {
                isCheck++;
                View view = tableLayoutBoxMaster.getChildAt(boxMasterList.indexOf(item) + 1);
                if (view instanceof TableRow) {
                    TableRow row = (TableRow) view;
                    row.getChildAt(3).setBackgroundColor(getResources().getColor(R.color.white));
                    row.getChildAt(3).setForeground(getResources().getDrawable(R.drawable.baseline_done_black_48));
                    ((ImageView) row.getChildAt(3)).setImageDrawable(null);
                    row.getChildAt(3).setForegroundGravity(Gravity.CENTER);
                    row.getChildAt(3).setForegroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    ((TextView) row.getChildAt(2)).setText(String.valueOf(item.getCountProduct()));
                }
            }
        }
        if (isCheck > 0) {
            ShowHidden();
        }
        if (isCheck != 0 && isCheck == boxMasterList.size()) {
            llRegister.setVisibility(View.VISIBLE);
        }
    }


    public void clickIniciarRecepcionxUnidad() {
        /*if (!permisoCamaraConcedido) {
            Toast.makeText(PurchaseOrderDetailsActivity.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_LONG).show();
            permisoSolicitadoDesdeBoton = true;
            verificarYPedirPermisosDeCamara();
            return;
        }
        Intent i = new Intent(PurchaseOrderDetailsActivity.this, ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        i.putExtra("path", pathReception);
        startActivityForResult(i, CODIGO_INTENT);*/
        showDialogScanner(false, CODIGO_INTENT);
    }

    public void clickStartReceptionByLot() {
        /*if (!permisoCamaraConcedido) {
            Toast.makeText(PurchaseOrderDetailsActivity.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_LONG).show();
            permisoSolicitadoDesdeBoton = true;
            verificarYPedirPermisosDeCamara();
            return;
        }
        Intent i = new Intent(PurchaseOrderDetailsActivity.this, ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        i.putExtra("path", pathReception);
        startActivityForResult(i, CODIGO_INTENT);*/

        showDialogScanner(false, CODIGO_INTENT);
    }

    public void clickScanBarCode() {
       /* if (!permisoCamaraConcedido) {
            Toast.makeText(PurchaseOrderDetailsActivity.this, "Por favor permite que la app acceda a la cámara", Toast.LENGTH_LONG).show();
            permisoSolicitadoDesdeBoton = true;
            verificarYPedirPermisosDeCamara();
            return;
        }
        Intent i = new Intent(PurchaseOrderDetailsActivity.this, ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        i.putExtra("path", pathReception);
        startActivityForResult(i, BOX_CODE_INTENT);*/
        showDialogScanner(false, BOX_CODE_INTENT);
    }

    private void showDialogScanner(boolean scanMultiple, int codeIntent) {
        DialogScanner dialogScanner = new DialogScanner();
        dialogScanner.setScanMultiple(scanMultiple);
        dialogScanner.setPathReception(pathReception);
        dialogScanner.setCode_intent(codeIntent);
        dialogScanner.setPermisoCamaraConcedido(permisoCamaraConcedido);
        dialogScanner.setPermisoSolicitadoDesdeBoton(permisoSolicitadoDesdeBoton);
        dialogScanner.show(getSupportFragmentManager(), "alert dialog generate codes");
    }


    private boolean CheckStatusBoxMaster(int pos) {
        return boxMasterList.get(pos - 20).isComplete();

    }

    private boolean CheckStatusBoxMaster2(int pos) {
        return boxMasterList.get(pos).isComplete();

    }

    public void ShowHidden() {
        tvheaderhiddenCodes.setVisibility(View.VISIBLE);
        for (int i = 0; i < tableLayoutBoxMaster.getChildCount() + 1; i++) {
            View view = tableLayoutBoxMaster.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                row.getChildAt(3).setVisibility(View.VISIBLE);
            }
        }
    }


    public void setupToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_white_24);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction().equals(String.valueOf(CODIGO_INTENT))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    ProductoRequest productoRequest = new ProductoRequest();
                    productoRequest.setCodigoBarra(codeBar);
                    ProductTaskController taskController = new ProductTaskController();
                    taskController.setListener(PurchaseOrderDetailsActivity.this);
                    taskController.execute(productoRequest);
                }
            }
            if (data != null && data.getAction().equals(String.valueOf(BOX_CODE_INTENT))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    if (!existCodeBar(codeBar)) {
                        BoxMasterRequest request = new BoxMasterRequest();
                        request.setActionPath(4);
                        request.setBarCodeBoxMasterOrigin(codeBar);
                        BoxMasterTaskController task = new BoxMasterTaskController();
                        task.setListener(PurchaseOrderDetailsActivity.this);
                        task.execute(request);
                    } else {
                        Toast.makeText(PurchaseOrderDetailsActivity.this, "Ya existe una caja master con el código ingresado", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private boolean existCodeBar(String code) {
        for (BoxMaster item : boxMasterList) {
            if (item.getBarCode() != null && item.getBarCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                       /* Intent i = new Intent(PurchaseOrderDetailsActivity.this, ScannerActivity.class);
                        i.putExtra("scanMultiple", false);
                        i.putExtra("path", pathReception);
                        startActivityForResult(i, CODIGO_INTENT);*/
                        showDialogScanner(false, CODIGO_INTENT);
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(PurchaseOrderDetailsActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(PurchaseOrderDetailsActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }

    private void permisoDeCamaraDenegado() {
        // Esto se llama cuando el usuario hace click en "Denegar" o
        // cuando lo denegó anteriormente
        Toast.makeText(PurchaseOrderDetailsActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_LONG).show();
    }

    @Override
    public void applyTexts(String numcolaboradores) {
        Intent i = new Intent(this, PurchaseOrderDetailsActivity.class);
        i.putExtra("numcolaboradores", numcolaboradores);
        startActivity(i);
    }

    @Override
    public void onProductResponse(ProductoResponse response) {
        if (response != null && response.getCode() == 200 && response.getProductDetail() != null) {
            MySingleton.getInstance().setProductoResponse(response);
            Intent i = new Intent(this, ProductDetailsActivity.class);
            i.putExtra("code", response.getProductDetail().getCodBarras());
            i.putExtra("path", pathReception);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Producto no encontrada en el sistema", Toast.LENGTH_LONG).show();
        }
    }

    public void refreshViewTable() {
        tableLayoutBoxMaster.removeAllViews();
        TableRow headder = new TableRow(this);
        TableRow.LayoutParams layoutFila1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        headder.setLayoutParams(layoutFila1);
        headder.setBackgroundColor(getResources().getColor(R.color.fondotable));
        tableLayoutBoxMaster.addView(headder);

        TableRow.LayoutParams layouttvnumero = new TableRow.LayoutParams(0, 90, (float) 0.4);
        layouttvnumero.setMargins(4, 1, 3, 3);
        TableRow.LayoutParams layoutdetalles = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
        layoutdetalles.setMargins(1, 1, 3, 3);
        TableRow.LayoutParams layoutcantidades = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, (float) 0.6);
        layoutcantidades.setMargins(1, 1, 4, 3);
        TableRow.LayoutParams layoutbotonaction = new TableRow.LayoutParams(0, 90, (float) 0.5);
        layoutbotonaction.setMargins(0, 1, 4, 3);

        TextView tvnumero = new TextView(this);
        TextView tvdetalle = new TextView(this);
        TextView tvcant = new TextView(this);
        TextView tvaction = new TextView(this);
        final TextView tvdelete = new TextView(this);


        tvnumero.setLayoutParams(layouttvnumero);
        tvnumero.setBackgroundColor(getResources().getColor(R.color.white));
        tvnumero.setPadding(2, 2, 2, 2);
        tvnumero.setText(getResources().getText(R.string.headder1));
        tvnumero.setTextColor(getResources().getColor(R.color.color_text_blue));
        tvnumero.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        headder.addView(tvnumero);

        tvdetalle.setLayoutParams(layoutdetalles);
        tvdetalle.setBackgroundColor(getResources().getColor(R.color.white));
        tvdetalle.setPadding(2, 2, 2, 2);
        tvdetalle.setText(getResources().getText(R.string.headder2));
        tvdetalle.setTextColor(getResources().getColor(R.color.color_text_blue));
        tvdetalle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        headder.addView(tvdetalle);


        tvcant.setLayoutParams(layoutcantidades);
        tvcant.setBackgroundColor(getResources().getColor(R.color.white));
        tvcant.setPadding(2, 2, 2, 2);
        tvcant.setText(getResources().getText(R.string.headder3));
        tvcant.setTextColor(getResources().getColor(R.color.color_text_blue));
        tvcant.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        headder.addView(tvcant);


        tvaction.setLayoutParams(layoutbotonaction);
        tvaction.setBackgroundColor(getResources().getColor(R.color.white));
        tvaction.setPadding(2, 2, 2, 2);
        tvaction.setText(getResources().getText(R.string.headder4));
        tvaction.setTextColor(getResources().getColor(R.color.color_text_blue));
        tvaction.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        tvaction.setVisibility(View.GONE);
        headder.addView(tvaction);

        tvdelete.setLayoutParams(layoutbotonaction);
        tvdelete.setBackgroundColor(getResources().getColor(R.color.white));
        tvdelete.setPadding(2, 2, 2, 2);
        tvdelete.setText(getResources().getText(R.string.headder5));
        tvdelete.setTextColor(getResources().getColor(R.color.color_text_blue));
        tvdelete.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        tvdelete.setVisibility(View.VISIBLE);
        headder.addView(tvdelete);


        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutnumero = new TableRow.LayoutParams(0, 90, (float) 0.4);
        TableRow.LayoutParams layoutdetalle = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
        TableRow.LayoutParams layoutcantidad = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, (float) 0.6);
        TableRow.LayoutParams layoutbotoniniciar = new TableRow.LayoutParams(0, 90, (float) 0.5);
        TableRow.LayoutParams layoutbotondelete = new TableRow.LayoutParams(0, 90, (float) 0.5);

        layoutnumero.setMarginStart(1);
        layoutdetalle.setMarginStart(1);
        layoutcantidad.setMarginStart(1);
        layoutbotoniniciar.setMarginStart(1);
        layoutbotondelete.setMarginStart(1);

        for (int i = 0; i < boxMasterList.size(); i++) {
            TableRow filas = new TableRow(this);
            filas.setLayoutParams(layoutFila);
            filas.setBackgroundColor(getResources().getColor(R.color.fondotable));

            TextView tvNumber = new TextView(this);
            tvNumber.setText(String.valueOf(i + 1));
            TextView tvBarCode = new TextView(this);
            tvBarCode.setText("");
            TextView tvCount = new TextView(this);
            tvCount.setText(String.valueOf(boxMasterList.get(i).getCountProduct()));
            final ImageView ivicono = new ImageView(this);
            final ImageView ivdeleteaction = new ImageView(this);

            filas.setPadding(0, 0, 1, 1);
            tableLayoutBoxMaster.addView(filas);

            tvNumber.setLayoutParams(layoutnumero);
            tvNumber.setGravity(Gravity.CENTER);
            tvNumber.setPadding(2, 2, 2, 2);
            tvNumber.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tvNumber.setBackgroundResource(R.color.white);
            tvNumber.setText(String.valueOf(i + 1));
            //tvnoarticulo.setVisibility(View.VISIBLE);


            //lparams.setMargins(1,0,0,0);
            tvBarCode.setLayoutParams(layoutdetalle);
            tvBarCode.setBackgroundResource(R.color.white);
            tvBarCode.setPadding(2, 2, 2, 2);
            tvBarCode.setGravity(Gravity.CENTER);
            tvBarCode.setText(boxMasterList.get(i).getBarCode());

            //lparams.setMargins(1,0,0,0);
            tvCount.setGravity(Gravity.CENTER);
            tvCount.setLayoutParams(layoutcantidad);
            tvCount.setBackgroundResource(R.color.white);
            tvCount.setPadding(2, 2, 2, 2);
            tvCount.setText(String.valueOf(boxMasterList.get(i).getCountProduct()));


            ivicono.setLayoutParams(layoutbotoniniciar);
            ivicono.setBackgroundColor(getResources().getColor(R.color.white));
            ivicono.setImageResource(R.drawable.recibirmercaderiasmall);
            ivicono.setPadding(2, 2, 2, 2);
            ivicono.setId(i + 20);
            ivicono.setVisibility(View.GONE);
            ivdeleteaction.setColorFilter(R.color.gray);
            ivicono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CheckStatusBoxMaster((int) ivicono.getId())) {
                        MySingleton.getInstance().setStatusitenclicked(false);
                        MySingleton.getInstance().setPosclicked((int) ivicono.getId());
                        if (pathReception == 1) {
                            clickIniciarRecepcionxUnidad();
                        } else {
                            clickStartReceptionByLot();
                        }
                    }
                }
            });

            ivdeleteaction.setLayoutParams(layoutbotondelete);
            //ivdeleteaction.setScaleX((float) 0.9);
            //ivdeleteaction.setScaleY((float) 0.8);
            ivdeleteaction.setBackgroundColor(getResources().getColor(R.color.white));
            ivdeleteaction.setPadding(2, 2, 6, 2);
            ivdeleteaction.setId(i + 1000);
            ivdeleteaction.setVisibility(View.VISIBLE);
            ivdeleteaction.setImageResource(R.mipmap.ic_delete_black_48dp);
            ivdeleteaction.setColorFilter(R.color.gray);
            ivdeleteaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) ivdeleteaction.getId() - 1000;
                    if (pos >= 0 && pos < boxMasterList.size()) {
                        boxMasterList.remove(pos);
                        refreshViewTable();
                    }
                }
            });

            filas.addView(tvNumber);
            filas.addView(tvBarCode);
            filas.addView(tvCount);
            filas.addView(ivicono);
            filas.addView(ivdeleteaction);
        }

    }

    @Override
    public void onInsertPedidoResponse(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            MySingleton.getInstance().setRegisterPurchaseOrder(true);
            Toast.makeText(getApplicationContext(), "Orden de compra registrada en el sistema", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Error insertando orden de compra", Toast.LENGTH_LONG).show();
        }
    }

    public void TestPedido() {
        Map<String, Integer> mapCode = new HashMap<>();
        List<BoxMaster> boxMasterList = new ArrayList<>();
        BoxMaster b1 = new BoxMaster();
        b1.setComplete(true);
        b1.setBarCode("1234");
        b1.setCountProduct(5);
        b1.setId(1);
        b1.setLocationBarCode("Location 1");
        List<String> codes = new ArrayList<>();
        codes.add("001MD43991001280");
        codes.add("001MD43991001280");
        codes.add("001MD43991001280");
        codes.add("001MD43991001280");
        codes.add("001MD43991001280");
        b1.setCodesProduct(codes);
        boxMasterList.add(b1);

        BoxMaster b2 = new BoxMaster();
        b2.setComplete(true);
        b2.setBarCode("123456");
        b2.setCountProduct(3);
        b2.setId(2);
        b2.setLocationBarCode("Location2");
        List<String> codes2 = new ArrayList<>();
        codes2.add("001MD43991001300");
        codes2.add("001MD43991001300");
        codes2.add("001MD43991001300");
        b2.setCodesProduct(codes2);
        boxMasterList.add(b2);

        for (BoxMaster box : boxMasterList) {
            for (String barCodeProduct : box.getCodesProduct()) {
                if (mapCode.containsKey(barCodeProduct)) {
                    int count = mapCode.get(barCodeProduct) + 1;
                    mapCode.put(barCodeProduct, count);
                } else {
                    mapCode.put(barCodeProduct, 1);
                }
            }
        }
        if (mapCode.size() > 0) {
            PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest();
            // purchaseOrderRequest.setBoxMasterList(boxMasterList);
            List<PedidoDetail> pedidoDetailList = new ArrayList<>();
            for (Map.Entry<String, Integer> item : mapCode.entrySet()) {
                PedidoDetail pedido = null;
                for (PedidoDetail pedidoDetail : MySingleton.getInstance().getPedidoResponse().getPedidoDetailList()) {
                    if ((pedidoDetail.getCodigoBarra() != null && pedidoDetail.getCodigoBarra().equals(item.getKey())) || (pedidoDetail.getCb2() != null && pedidoDetail.getCb2().equals(item.getKey())) || (pedidoDetail.getcBar3() != null && pedidoDetail.getcBar3().equals(item.getKey()))) {
                        pedido = pedidoDetail;
                    }
                }
                if (pedido != null) {
                    pedido.setUnidades(item.getValue());
                    pedidoDetailList.add(pedido);
                }
            }
            purchaseOrderRequest.setPedidoList(pedidoDetailList);
            InsertPurchaseTaskController task = new InsertPurchaseTaskController();
            task.setListener(PurchaseOrderDetailsActivity.this);
            task.execute(purchaseOrderRequest);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.actionCreateBarCodes:
                    OpenAlertDialog();
                    break;
            }
        } catch (Exception e) {
        }
        return true;
    }

    public void OpenAlertDialog() {
        DialogGenerateBarCodes dialog = new DialogGenerateBarCodes();
        dialog.show(getSupportFragmentManager(), "alert dialog generate codes");
    }

    @Override
    public void applyQuantityTexts(String quantity) {
        BarCodeRequest request = new BarCodeRequest();
        request.setQuantity(Integer.parseInt(quantity));
        User user = MySingleton.getUser();
        request.setUserRegister(user.getUsername());
        BarCodeTaskController task = new BarCodeTaskController();
        task.setListener(PurchaseOrderDetailsActivity.this);
        task.execute(request);

    }

    @Override
    public void onSuccessCreateBarCodes(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            Toast.makeText(getApplicationContext(), "Códigos de Barras generados correctamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error generando los códigos de barras.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {
        if (action == CODIGO_INTENT) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                ProductoRequest productoRequest = new ProductoRequest();
                productoRequest.setCodigoBarra(codeBar);
                ProductTaskController taskController = new ProductTaskController();
                taskController.setListener(PurchaseOrderDetailsActivity.this);
                taskController.execute(productoRequest);
            }
        }
        if (action == BOX_CODE_INTENT) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                if (!existCodeBar(codeBar)) {
                    BoxMasterRequest request = new BoxMasterRequest();
                    request.setActionPath(4);
                    request.setBarCodeBoxMasterOrigin(codeBar);
                    BoxMasterTaskController task = new BoxMasterTaskController();
                    task.setListener(PurchaseOrderDetailsActivity.this);
                    task.execute(request);
                } else {
                    Toast.makeText(PurchaseOrderDetailsActivity.this, "Ya existe una caja master con el código ingresado", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void initRecyclerView() {
        //Setup Trailer Recycler View
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(PurchaseOrderDetailsActivity.this);
        //DividerItemDecoration dividerTrailer = new DividerItemDecoration(PurchaseOrderDetailsActivity.this, linearLayoutManagerTrailer.getOrientation());
        this.boxMasterAdapter = new BoxMasterAdapter(filterBoxMasterList);
        this.boxMasterAdapter.setListenerBoxMaster(this);
        this.rvBoxMasterList.setAdapter(boxMasterAdapter);
        this.rvBoxMasterList.setLayoutManager(linearLayoutManagerTrailer);
        //this.rvBoxMasterList.addItemDecoration(dividerTrailer);
    }


    private void initCollection() {
        BoxMaster fakeBoxMaster = new BoxMaster();
        fakeBoxMaster.setBarCode("");
        fakeBoxMaster.setCountProduct(0);
        this.boxMasterList = new ArrayList<>();
        boxMasterList.add(fakeBoxMaster);
        this.filterBoxMasterList = new ArrayList<>();
        filterBoxMasterList = new ArrayList<>(boxMasterList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rvBoxMasterList != null) {
            rvBoxMasterList.setAdapter(null);
        }
    }

    @Override
    public void onItemClick(BoxMaster item) {

    }

    @Override
    public void onItemActionClick(BoxMaster item) {
        if (item != null) {
            if (!CheckStatusBoxMaster2(boxMasterList.indexOf(item))) {
                MySingleton.getInstance().setStatusitenclicked(false);
                MySingleton.getInstance().setPosclicked((int) boxMasterList.indexOf(item));
                if (pathReception == 1) {
                    clickIniciarRecepcionxUnidad();
                } else {
                    clickStartReceptionByLot();
                }
            }
        }
    }

    @Override
    public void onItemRemoveClick(BoxMaster item) {
        if (item != null) {
            boxMasterToRemove = item;
            BoxMasterRequest request = new BoxMasterRequest();
            request.setActionPath(5);
            request.setBarCodeBoxMasterOrigin(codeBar);
            BoxMasterTaskController task = new BoxMasterTaskController();
            task.setListener(PurchaseOrderDetailsActivity.this);
            task.execute(request);
        }
    }

    @Override
    public void onBoxMasterResponse(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            if (response.getPath() == 4) { //Create Box Master
                BoxMaster boxMaster = new BoxMaster();
                boxMaster.setId(boxMasterList.size() + 1);
                boxMaster.setBarCode(codeBar);
                boxMaster.setCountProduct(0);
                boxMaster.setComplete(false);
                User user = MySingleton.getUser();
                boxMaster.setUserRegister(user.getUsername());
                boxMasterList.add(boxMaster);
                filterBoxMasterList.add(boxMaster);
                boxMasterAdapter.notifyDataSetChanged();
                MySingleton.getInstance().setBoxMasterList(boxMasterList);
                //refreshViewTable();
            } else if (response.getPath() == 5) {//Delete Box Master
                int index = boxMasterList.indexOf(boxMasterToRemove);
                boxMasterList.remove(index);
                filterBoxMasterList.remove(index);
                boxMasterAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(PurchaseOrderDetailsActivity.this, "El código de barras no existe en el sistema de cajas master", Toast.LENGTH_LONG).show();
        }
    }
}

