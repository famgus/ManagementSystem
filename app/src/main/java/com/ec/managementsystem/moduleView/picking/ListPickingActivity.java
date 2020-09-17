package com.ec.managementsystem.moduleView.picking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.ListPickingPedidoDetailResponse;
import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;
import com.ec.managementsystem.interfaces.IListenerPicking;
import com.ec.managementsystem.interfaces.IListenerPickingPedidoDetail;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.adapters.PickingAdapter;
import com.ec.managementsystem.task.PickingPedidoDetailTaskController;
import com.ec.managementsystem.task.PickingUpdateTaskController;
import com.ec.managementsystem.util.MySingleton;

import java.util.ArrayList;
import java.util.List;

public class ListPickingActivity extends BaseActivity implements IListenerPicking, IDelegateUpdatePickingControl, IListenerPickingPedidoDetail {

    Toolbar toolbar;
    RecyclerView rvPedidoList;
    EditText etPedidoSearch;
    ImageView ivPedidoSearch;
    List<PickingPedidoUserResponse> originalList;
    List<PickingPedidoUserResponse> filterList;
    PickingAdapter pickingAdapter;
    String filterQuery;
    PickingPedidoUserResponse itemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_picking_activity);
        setupView();
        initCollection();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemSelected = MySingleton.getInstance().getPickingUserResponse();
        if (itemSelected != null && itemSelected.isComplete()) {
            MySingleton.getInstance().setPickingUserResponse(null);
            MySingleton.RemovePedidoPicking();
            finish();
        }
    }

    private void setupView() {
        try {
            // Set Toolbar
            toolbar = findViewById(R.id.toolbarBar);
            this.toolbar.setTitle("Módulo de Picking");
            this.setupToolBar(toolbar);
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            etPedidoSearch = findViewById(R.id.etPedidoSearch);
            ivPedidoSearch = findViewById(R.id.ivPedidoSearch);
            rvPedidoList = findViewById(R.id.rvPedidoList);

            this.etPedidoSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    setFilterTrailerQuery(charSequence.toString().toLowerCase());
                    onQueryTrailerSearchChanged(charSequence.toString().toLowerCase());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            etPedidoSearch.clearFocus();
        } catch (Exception e) {
            Toast.makeText(this, "Error inicializando la actividad", Toast.LENGTH_LONG).show();
        }
    }

    public void setFilterTrailerQuery(String filterTrailerQuery) {
        this.filterQuery = filterTrailerQuery;
    }

    public void onQueryTrailerSearchChanged(final String query) {
        List<PickingPedidoUserResponse> filteredModelList = new ArrayList<>();
        // Filter collection
        if (!query.isEmpty()) {
            filteredModelList = this.filter(this.originalList, query);
        } else {
            filteredModelList.addAll(this.originalList);
        }
        if (this.pickingAdapter != null) {
            this.pickingAdapter.animateTo(filteredModelList);
        }
    }

    protected List<PickingPedidoUserResponse> filter(final List<PickingPedidoUserResponse> models, final String query) {
        List<PickingPedidoUserResponse> filteredModelList = new ArrayList<>();
        String filter;
        if (query != null) {
            filter = query.toLowerCase();
        } else {
            filter = "";
        }
        if (!models.isEmpty()) {
            for (PickingPedidoUserResponse model : models) {
                final String number = model.getNumberPedido().toString();
                final String nameClient = model.getNameClient().toLowerCase();
                if (number.contains(filter) || nameClient.contains(filter)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }


    private void initRecyclerView() {
        //Setup Trailer Recycler View
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(ListPickingActivity.this);
        DividerItemDecoration dividerTrailer = new DividerItemDecoration(ListPickingActivity.this, linearLayoutManagerTrailer.getOrientation());
        this.pickingAdapter = new PickingAdapter(filterList);
        this.pickingAdapter.setListenerPicking(this);
        this.rvPedidoList.setAdapter(pickingAdapter);
        this.rvPedidoList.setLayoutManager(linearLayoutManagerTrailer);
        this.rvPedidoList.addItemDecoration(dividerTrailer);
    }


    private void initCollection() {
        this.originalList = new ArrayList<>();
        this.filterList = new ArrayList<>();
        try {
            if (MySingleton.getInstance().getPickingUserResponse() != null) {
                this.originalList.add(MySingleton.getInstance().getPickingUserResponse());
                filterList = new ArrayList<>(originalList);
            }

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
    public void onItemClick(PickingPedidoUserResponse item) {
        if (item != null) {
            MySingleton.getInstance().setPickingUserResponse(item);
            itemSelected = item;
            PickingUpdateTaskController task = new PickingUpdateTaskController();
            task.setListener(this);
            PickingRequest request = new PickingRequest();
            request.setNumberSerie(item.getNumberSerie());
            request.setNumberPedido(item.getNumberPedido());
            request.setState(2);
            request.setPath(1);
            task.execute(request);
        } else {
            Toast.makeText(this, "Error seleccionando el pedido", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccessUpdate(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            MySingleton.SavePedidoPicking();
            PickingPedidoDetailTaskController task = new PickingPedidoDetailTaskController();
            task.setListener(this);
            PickingRequest request = new PickingRequest();
            request.setNumberSerie(itemSelected.getNumberSerie());
            request.setNumberPedido(itemSelected.getNumberPedido());
            task.execute(request);
        } else {
            Toast.makeText(this, "Error actualizando el estado del pedido", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPickingPedidoDetail(ListPickingPedidoDetailResponse response) {
        if (response != null && response.getCode() == 200) {
            itemSelected.setDetailsResponse(response);
            MySingleton.getInstance().setPickingUserResponse(itemSelected);
            Intent i = new Intent(ListPickingActivity.this, PickingDetailActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Error obteniendo los detalles del pedido", Toast.LENGTH_LONG).show();
        }
    }
}
