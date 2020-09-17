package com.ec.managementsystem.moduleView.packing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.ec.managementsystem.clases.FacturaModel;
import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.FacturasDetasilResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;
import com.ec.managementsystem.interfaces.IListenerPackingDetail;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.adapters.PackingDetailAdapter;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.PickingUpdateTaskController;
import com.ec.managementsystem.util.MySingleton;

import java.util.ArrayList;
import java.util.List;

public class PackingDetailActivity extends BaseActivity implements IListenerPackingDetail, DialogScanner.DialogScanerFinished, IDelegateUpdatePickingControl {
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    Toolbar toolbar;
    RecyclerView rvPedidoList;
    EditText etIdPicking;
    LinearLayout llRegister;
    List<FacturasDetasilResponse> originalList;
    List<FacturasDetasilResponse> filterList;
    PackingDetailAdapter pickingAdapter;
    FacturaModel facturaModelSelected;
    FacturasDetasilResponse pedidoDetailSelected;

    TextView tvNumberFactura, tvSerie, tvNumberPedido, tvState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_detail);
        setupView();
        initCollection();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pedidoDetailSelected != null) {
            int index = originalList.indexOf(pedidoDetailSelected);
            pedidoDetailSelected.setFinish(true);
            originalList.set(index, pedidoDetailSelected);
            filterList.set(index, pedidoDetailSelected);
            pickingAdapter.notifyDataSetChanged();
        }
    }

    private void setupView() {
        try {
            // Set Toolbar
            toolbar = findViewById(R.id.toolbar);
            this.toolbar.setTitle("Módulo de Packing");
            this.setupToolBar(toolbar);
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            tvNumberFactura = findViewById(R.id.tvNumberFactura);
            tvSerie = findViewById(R.id.tvSerie);
            tvNumberPedido = findViewById(R.id.tvNumberPedido);
            tvState = findViewById(R.id.tvState);
            rvPedidoList = findViewById(R.id.rvPedidoList);
            etIdPicking = findViewById(R.id.etIdPicking);
            llRegister = findViewById(R.id.llRegister);
            verificarYPedirPermisosDeCamara();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.containsKey("facturaModel")) {
                facturaModelSelected = (FacturaModel) bundle.getSerializable("facturaModel");
                if (facturaModelSelected != null) {
                    tvNumberFactura.setText(String.valueOf(facturaModelSelected.getNumberFactura()));
                    tvSerie.setText(String.valueOf(facturaModelSelected.getNumberSerie()));
                    tvNumberPedido.setText(String.valueOf(facturaModelSelected.getNumberPedido()));
                    tvState.setText(String.valueOf(facturaModelSelected.getStateReservation()));
                }
            }
            llRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PickingUpdateTaskController task = new PickingUpdateTaskController();
                    task.setListener(PackingDetailActivity.this);
                    PickingRequest request = new PickingRequest();
                    request.setNumberSerie(facturaModelSelected.getNumberSerie());
                    request.setNumberPedido(facturaModelSelected.getNumberPedido());
                    request.setState(5);
                    request.setPath(1);
                    task.execute(request);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error inicializando la actividad", Toast.LENGTH_LONG).show();
        }
    }

    private void initRecyclerView() {
        //Setup Trailer Recycler View
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(PackingDetailActivity.this);
        // DividerItemDecoration dividerTrailer = new DividerItemDecoration(PickingDetailActivity.this, linearLayoutManagerTrailer.getOrientation());
        this.pickingAdapter = new PackingDetailAdapter(filterList);
        this.pickingAdapter.setListenerPicking(this);
        this.rvPedidoList.setAdapter(pickingAdapter);
        this.rvPedidoList.setLayoutManager(linearLayoutManagerTrailer);
    }


    private void initCollection() {
        this.originalList = new ArrayList<>();
        this.filterList = new ArrayList<>();
        try {

            FacturasDetasilResponse fake = new FacturasDetasilResponse();
            originalList.add(fake);
            this.originalList.addAll(facturaModelSelected.getFacturasDetasilResponses());
            filterList = new ArrayList<>(originalList);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rvPedidoList != null) {
            rvPedidoList.setAdapter(null);
        }
    }

    @Override
    public void onItemClick(FacturasDetasilResponse item) {
        if (item != null) {
        } else {
            Toast.makeText(this, "Error seleccionando el pedido", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemActionClick(FacturasDetasilResponse item) {
        if (item != null) {
            pedidoDetailSelected = item;
            Intent i = new Intent(this, PackingDetailItemActivity.class);
            i.putExtra("pedidoSelected", item);
            i.putExtra("facturaModelSelected", facturaModelSelected);
            startActivity(i);
        } else {
            Toast.makeText(this, "Error seleccionando el pedido", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemRemoveClick(FacturasDetasilResponse item) {
        if (item != null) {
        } else {
            Toast.makeText(this, "Error seleccionando el pedido", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Escanear directamten solo si fue pedido desde el botón
                    if (permisoSolicitadoDesdeBoton) {
                        showDialogScanner(false, CODIGO_INTENT);
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
        Toast.makeText(PackingDetailActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_LONG).show();
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(PackingDetailActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(PackingDetailActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }

    @Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {
        if (action == CODIGO_INTENT) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etIdPicking.setText(String.valueOf(codeBar));
            }
        }
    }

    private void showDialogScanner(boolean scanMultiple, int codeIntent) {
        DialogScanner dialogScanner = new DialogScanner();
        dialogScanner.setScanMultiple(scanMultiple);
        dialogScanner.setCode_intent(codeIntent);
        dialogScanner.setPermisoCamaraConcedido(permisoCamaraConcedido);
        dialogScanner.setPermisoSolicitadoDesdeBoton(permisoSolicitadoDesdeBoton);
        dialogScanner.show(getSupportFragmentManager(), "alert dialog generate codes");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction().equals(String.valueOf(CODIGO_INTENT))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etIdPicking.setText(String.valueOf(codeBar));
                }
            }
        }
    }

    @Override
    public void onSuccessUpdate(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            int index = MySingleton.getInstance().packingResponse.getFacturaModels().indexOf(facturaModelSelected);
            facturaModelSelected.setComplete(true);
            MySingleton.getInstance().packingResponse.getFacturaModels().set(index, facturaModelSelected);
            Toast.makeText(PackingDetailActivity.this, "Packing resgistrado correctamente", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Toast.makeText(PackingDetailActivity.this, "Error actualizando el estado del packing", Toast.LENGTH_LONG).show();
        }
    }
}
