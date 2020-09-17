package com.ec.managementsystem.moduleView.packing;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.FacturaModel;
import com.ec.managementsystem.clases.request.FacturaDetailRequest;
import com.ec.managementsystem.clases.responses.FacturasClientResponse;
import com.ec.managementsystem.clases.responses.ListFacturasDetasilResponse;
import com.ec.managementsystem.interfaces.IDelegateSearchClientTaskControl;
import com.ec.managementsystem.interfaces.IFacturaDetailTaskControl;
import com.ec.managementsystem.interfaces.IListenerPacking;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.adapters.PackingAdapter;
import com.ec.managementsystem.task.FacturaDetailTaskController;
import com.ec.managementsystem.task.SearchClientTaskController;
import com.ec.managementsystem.util.MySingleton;

import java.util.ArrayList;
import java.util.List;

public class ListPackingActivity extends BaseActivity implements IListenerPacking, IDelegateSearchClientTaskControl, IFacturaDetailTaskControl {

    Toolbar toolbar;
    RecyclerView rvPedidoList;
    EditText etFacturasSearch, etCodeClient;
    ImageView ivFacturaSearch, ivSearchClient;
    TextView tvNumberClient, tvNameClient, tvMunicipio, tvAddress;

    LinearLayout item_container, llListFacturas;
    List<FacturaModel> originalList;
    List<FacturaModel> filterList;
    PackingAdapter packingAdapter;
    String filterQuery;
    FacturaModel itemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_packing_activity);
        setupView();
        initCollection();
        initRecyclerView();
    }

    private void setupView() {
        try {
            // Set Toolbar
            toolbar = findViewById(R.id.toolbarBar);
            this.toolbar.setTitle("Módulo de Packing");
            this.setupToolBar(toolbar);
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });


            ivSearchClient = findViewById(R.id.ivSearchClient);
            etCodeClient = findViewById(R.id.etCodeClient);
            item_container = findViewById(R.id.item_container);
            tvNumberClient = findViewById(R.id.tvNumberClient);
            tvNameClient = findViewById(R.id.tvNameClient);
            tvMunicipio = findViewById(R.id.tvMunicipio);
            tvAddress = findViewById(R.id.tvAddress);
            llListFacturas = findViewById(R.id.llListFacturas);


            etFacturasSearch = findViewById(R.id.etFacturasSearch);
            ivFacturaSearch = findViewById(R.id.ivFacturaSearch);
            rvPedidoList = findViewById(R.id.rvPedidoList);

            item_container.setVisibility(View.GONE);
            llListFacturas.setVisibility(View.GONE);


            ivSearchClient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etCodeClient.getText() != null && etCodeClient.getText().length() > 0) {
                        SearchClientTaskController task = new SearchClientTaskController();
                        task.setListener(ListPackingActivity.this);
                        task.execute(etCodeClient.getText().toString());
                    }
                }
            });

            this.etFacturasSearch.addTextChangedListener(new TextWatcher() {
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
            etFacturasSearch.clearFocus();
        } catch (Exception e) {
            Toast.makeText(this, "Error inicializando la actividad", Toast.LENGTH_LONG).show();
        }
    }

    public void setFilterTrailerQuery(String filterTrailerQuery) {
        this.filterQuery = filterTrailerQuery;
    }

    public void onQueryTrailerSearchChanged(final String query) {
        List<FacturaModel> filteredModelList = new ArrayList<>();
        // Filter collection
        if (!query.isEmpty()) {
            filteredModelList = this.filter(this.originalList, query);
        } else {
            filteredModelList.addAll(this.originalList);
        }
        if (this.packingAdapter != null) {
            this.packingAdapter.animateTo(filteredModelList);
        }
    }

    protected List<FacturaModel> filter(final List<FacturaModel> models, final String query) {
        List<FacturaModel> filteredModelList = new ArrayList<>();
        String filter;
        if (query != null) {
            filter = query.toLowerCase();
        } else {
            filter = "";
        }
        if (!models.isEmpty()) {
            for (FacturaModel model : models) {
                final String number = model.getNumberFactura().toString().toLowerCase();
                final String serie = model.getNumberSerie().toLowerCase();
                if (number.contains(filter) || serie.contains(filter)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }


    private void initRecyclerView() {
        //Setup Trailer Recycler View
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(ListPackingActivity.this);
        DividerItemDecoration dividerTrailer = new DividerItemDecoration(ListPackingActivity.this, linearLayoutManagerTrailer.getOrientation());
        this.packingAdapter = new PackingAdapter(filterList);
        this.packingAdapter.setListenerPacking(this);
        this.rvPedidoList.setAdapter(packingAdapter);
        this.rvPedidoList.setLayoutManager(linearLayoutManagerTrailer);
        this.rvPedidoList.addItemDecoration(dividerTrailer);
    }


    private void initCollection() {
        this.originalList = new ArrayList<>();
        this.filterList = new ArrayList<>();

        try {
            if (MySingleton.getInstance().packingResponse != null && MySingleton.getInstance().packingResponse.getFacturaModels().size() > 0) {
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
    public void onItemClick(FacturaModel item) {
        if (item != null) {
            itemSelected = item;
            FacturaDetailTaskController task = new FacturaDetailTaskController();
            task.setListener(this);
            FacturaDetailRequest request = new FacturaDetailRequest();
            request.setNumberSerie(item.getNumberSerieAc());
            request.setNumberAlbaran(item.getNumberAlbaran());
            task.execute(request);
        } else {
            Toast.makeText(this, "Error seleccionando el pedido", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSearchClientResponse(FacturasClientResponse response) {
        if (response != null && response.getCode() == 200) {
            MySingleton.getInstance().setPackingResponse(response);
            tvNumberClient.setText(String.valueOf(response.getNumberClient()));
            tvNameClient.setText(String.valueOf(response.getNameClient()));
            tvMunicipio.setText(String.valueOf(response.getMunicipio()));
            tvAddress.setText(String.valueOf(response.getAddress()));
            originalList = response.getFacturaModels();
            filterList.clear();
            filterList.addAll(originalList);
            packingAdapter.notifyDataSetChanged();
            etCodeClient.setText("");
            llListFacturas.setVisibility(View.VISIBLE);
            item_container.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(this, "Cliente no encontrado en el sistema", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFacturaDetail(ListFacturasDetasilResponse response) {
        if (response != null && response.getCode() == 200) {
            itemSelected.setFacturasDetasilResponses(response.getListFacturaDetail());
            Intent i = new Intent(ListPackingActivity.this, PackingDetailActivity.class);
            i.putExtra("facturaModel", itemSelected);
            startActivity(i);
        } else {
            Toast.makeText(this, "No se puede mostrar los detalles de la factura", Toast.LENGTH_LONG).show();
        }
    }
}