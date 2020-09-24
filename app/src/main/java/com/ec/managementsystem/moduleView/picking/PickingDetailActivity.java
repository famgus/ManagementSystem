package com.ec.managementsystem.moduleView.picking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.PickingPedidoDetailResponse;
import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;
import com.ec.managementsystem.interfaces.IListenerPickingDetail;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.SensorActivity;
import com.ec.managementsystem.moduleView.adapters.PickingDetailAdapter;
import com.ec.managementsystem.moduleView.ui.DialogScanner;
import com.ec.managementsystem.task.PickingUpdateTaskController;
import com.ec.managementsystem.util.MySingleton;

import java.util.ArrayList;
import java.util.List;

public class PickingDetailActivity extends BaseActivity implements IListenerPickingDetail, IDelegateUpdatePickingControl {
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    public static final int REGISTERED_PICKING_INTENT = 10001;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    Toolbar toolbar;
    RecyclerView rvPedidoList;
    TextView txtNumberPedidor, tvSeriesOrderNumber, tvCodeAgent, txtNameClient, tvDatePedido, tvDatePicking, tvObservation;
    LinearLayout llRegister;
    List<PickingPedidoDetailResponse> originalList;
    List<PickingPedidoDetailResponse> filterList;
    PickingDetailAdapter pickingAdapter;
    PickingPedidoDetailResponse pedidoDetailSelected;
    PickingPedidoUserResponse header;
    int totalProducts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_detail);
        setupView();
        initCollection();
        initRecyclerView();
    }

    private void setupView() {
        try {
            // Set Toolbar
            toolbar = findViewById(R.id.toolbar);
            this.toolbar.setTitle("Módulo de Picking");
            this.setupToolBar(toolbar);
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            rvPedidoList = findViewById(R.id.rvPedidoList);
            tvSeriesOrderNumber = findViewById(R.id.tv_id_content);
            llRegister = findViewById(R.id.llRegister);
            //verificarYPedirPermisosDeCamara();
            txtNumberPedidor = findViewById(R.id.txtNumberPedidor);
            tvCodeAgent = findViewById(R.id.tvCodeAgent);
            txtNameClient = findViewById(R.id.txtNameClient);
            tvDatePedido = findViewById(R.id.tvDatePedido);
            tvDatePicking = findViewById(R.id.tvDatePicking);
            tvObservation = findViewById(R.id.tvObservation);

            header = MySingleton.getInstance().getPickingUserResponse();
            if (header != null) {
                tvSeriesOrderNumber.setText(header.getSupedido());
                txtNumberPedidor.setText(String.valueOf(header.getNumberPedido()));
                tvCodeAgent.setText(String.valueOf(header.getNumberSerie()));
                txtNameClient.setText(String.valueOf(header.getNameClient()));
                tvDatePedido.setText(String.valueOf(header.getFechaPedido()));
                tvDatePicking.setText(String.valueOf(header.getFechaPedido()));
                tvObservation.setText(String.valueOf(header.getObservation()));
            }
            llRegister.setVisibility(View.GONE);
            llRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(totalProducts == (originalList.size()-1)){
                        PickingUpdateTaskController task = new PickingUpdateTaskController();
                        task.setListener(PickingDetailActivity.this);
                        PickingRequest request = new PickingRequest();
                        request.setNumberSerie(header.getNumberSerie());
                        request.setNumberPedido(header.getNumberPedido());
                        request.setState(3);
                        request.setPath(1);
                        task.execute(request);
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error inicializando la actividad", Toast.LENGTH_LONG).show();
        }
    }

    private void initRecyclerView() {
        //Setup Trailer Recycler View
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(PickingDetailActivity.this);
        // DividerItemDecoration dividerTrailer = new DividerItemDecoration(PickingDetailActivity.this, linearLayoutManagerTrailer.getOrientation());
        this.pickingAdapter = new PickingDetailAdapter(filterList);
        this.pickingAdapter.setListenerPicking(this);
        this.rvPedidoList.setAdapter(pickingAdapter);
        this.rvPedidoList.setLayoutManager(linearLayoutManagerTrailer);
    }


    private void initCollection() {
        this.originalList = new ArrayList<>();
        this.filterList = new ArrayList<>();
        try {
            PickingPedidoDetailResponse fake = new PickingPedidoDetailResponse();
            originalList.add(fake);
            this.originalList.addAll(MySingleton.getInstance().getPickingUserResponse().getDetailsResponse().getListPickingDetail());
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
    public void onItemClick(PickingPedidoDetailResponse item) {
        if (item != null) {
        } else {
            Toast.makeText(this, "Error seleccionando el pedido", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemActionClick(PickingPedidoDetailResponse item) {
        if (item != null) {
            pedidoDetailSelected = item;
            Intent i = new Intent(this, PickingDetailItemActivity.class);
            i.putExtra("pedidoSelected", item);
            startActivityForResult(i, REGISTERED_PICKING_INTENT);
        } else {
            Toast.makeText(this, "Error seleccionando el pedido", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemRemoveClick(PickingPedidoDetailResponse item) {
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
        Toast.makeText(PickingDetailActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_LONG).show();
    }

    private void verificarYPedirPermisosDeCamara() {
        int estadoDePermiso = ContextCompat.checkSelfPermission(PickingDetailActivity.this, Manifest.permission.CAMERA);
        if (estadoDePermiso == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            // y llamar al método
            permisoCamaraConcedido = true;
        } else {
            // Si no, pedimos permisos. Ahora mira onRequestPermissionsResult
            ActivityCompat.requestPermissions(PickingDetailActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CODIGO_PERMISOS_CAMARA);
        }
    }

    /*@Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {
        if (action == CODIGO_INTENT) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
            }
        }
    }*/

    private void showDialogScanner(boolean scanMultiple, int codeIntent) {
        Intent i = new Intent(this, SensorActivity.class);
        i.putExtra("scanMultiple", scanMultiple);
        i.putExtra("permisoCamaraConcedido", true);
        i.putExtra("permisoSolicitadoDesdeBoton", true);
        i.setAction(String.valueOf(codeIntent));
        startActivityForResult(i, codeIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTERED_PICKING_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                //filterList
                if (data != null) {
                    int result = data.getIntExtra("result", 0);
                    if (pedidoDetailSelected != null && result == 1) {
                        totalProducts++;
                        int index = originalList.indexOf(pedidoDetailSelected);
                        pedidoDetailSelected.setFinish(true);
                        originalList.set(index, pedidoDetailSelected);
                        filterList.set(index, pedidoDetailSelected);
                        pickingAdapter.notifyItemChanged(index);
                    }
                }
            }
            llRegister.setVisibility((totalProducts == (originalList.size() - 1)) ? View.VISIBLE : View.GONE);
        }
    }

    /*    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction().equals(String.valueOf(CODIGO_INTENT))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                }
            }
        }
    }*/

    @Override
    public void onSuccessUpdate(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            header.setComplete(true);
            MySingleton.getInstance().setPickingUserResponse(header);
            MySingleton.SavePedidoPicking();
            Toast.makeText(PickingDetailActivity.this, "Picking resgistrado correctamente", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Toast.makeText(PickingDetailActivity.this, "Error actualizando el estado del pedido", Toast.LENGTH_LONG).show();
        }
    }
}
