package com.ec.managementsystem.moduleView.send;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.FacturaModel;
import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.responses.FacturasClientResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.interfaces.IDelegateSearchFacturasTaskControl;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;
import com.ec.managementsystem.interfaces.IListenerPacking;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.adapters.PackingAdapter;
import com.ec.managementsystem.task.PickingUpdateTaskController;
import com.ec.managementsystem.task.SearchFacturasTaskController;

import java.util.ArrayList;
import java.util.List;

public class SendPickingActivity extends BaseActivity implements IListenerPacking, IDelegateSearchFacturasTaskControl, IDelegateUpdatePickingControl {

    Toolbar toolbar;
    RecyclerView rvFacturasList;
    EditText etNumSerie, etNumPedido, etFacturasSearch;
    LinearLayout llSearch, llSaveSelected, llFacturasContainer;
    List<FacturaModel> originalList;
    List<FacturaModel> filterList;
    PackingAdapter packingAdapter;
    String filterQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_picking_activity);
        setupView();
        initCollection();
        initRecyclerView();
    }

    private void setupView() {
        try {
            // Set Toolbar
            toolbar = findViewById(R.id.toolbarBar);
            this.toolbar.setTitle("Módulo de Envío");
            this.setupToolBar(toolbar);
            this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            etNumSerie = findViewById(R.id.etNumSerie);
            etNumPedido = findViewById(R.id.etNumPedido);
            etFacturasSearch = findViewById(R.id.etFacturasSearch);
            llSearch = findViewById(R.id.llSearch);
            llSaveSelected = findViewById(R.id.llSaveSelected);
            rvFacturasList = findViewById(R.id.rvFacturasList);
            llFacturasContainer = findViewById(R.id.llFacturasContainer);
            llFacturasContainer.setVisibility(View.GONE);
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
            llSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    originalList.clear();
                    filterList.clear();
                    packingAdapter.notifyDataSetChanged();
                    if (etNumPedido.getText().length() > 0 && etNumSerie.getText().length() > 0) {
                        PickingRequest request = new PickingRequest();
                        request.setNumberPedido(Integer.valueOf(etNumPedido.getText().toString()));
                        request.setNumberSerie(etNumSerie.getText().toString());
                        SearchFacturasTaskController task = new SearchFacturasTaskController();
                        task.setListener(SendPickingActivity.this);
                        task.execute(request);
                    } else {
                        Toast.makeText(SendPickingActivity.this, "Debe llenar todos os campos", Toast.LENGTH_LONG).show();
                    }
                }
            });

            llSaveSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (verifyFacturasSelected()) {
                        PickingUpdateTaskController task = new PickingUpdateTaskController();
                        task.setListener(SendPickingActivity.this);
                        PickingRequest request = new PickingRequest();
                        request.setNumberSerie(etNumSerie.getText().toString());
                        request.setNumberPedido(Integer.valueOf(etNumPedido.getText().toString()));
                        request.setState(6);
                        request.setPath(1);
                        task.execute(request);
                    } else {
                        Toast.makeText(SendPickingActivity.this, "Debe seleccionar las facturas para envío", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error inicializando la actividad", Toast.LENGTH_LONG).show();
        }
    }

    private boolean verifyFacturasSelected() {
        int countChecked = 0;
        for (FacturaModel item : originalList) {
            if (item.isCkecked()) {
                countChecked++;
            }
        }
        return countChecked == originalList.size();
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
                final String number = model.getNumberPedido().toString().toLowerCase();
                final String nameClient = model.getNumberSerie().toLowerCase();
                if (number.contains(filter) || nameClient.contains(filter)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }


    private void initRecyclerView() {
        //Setup Trailer Recycler View
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(SendPickingActivity.this);
        DividerItemDecoration dividerTrailer = new DividerItemDecoration(SendPickingActivity.this, linearLayoutManagerTrailer.getOrientation());
        this.packingAdapter = new PackingAdapter(filterList);
        this.packingAdapter.setSendView(true);
        this.packingAdapter.setListenerPacking(this);
        this.rvFacturasList.setAdapter(packingAdapter);
        this.rvFacturasList.setLayoutManager(linearLayoutManagerTrailer);
        this.rvFacturasList.addItemDecoration(dividerTrailer);
    }


    private void initCollection() {
        this.originalList = new ArrayList<>();
        this.filterList = new ArrayList<>();
        try {
            filterList = new ArrayList<>(originalList);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rvFacturasList != null) {
            rvFacturasList.setAdapter(null);
        }
    }

    @Override
    public void onItemClick(FacturaModel item) {
        int index = originalList.indexOf(item);
        item.setCkecked(!item.isCkecked());
        originalList.set(index, item);
        filterList.set(index, item);
        packingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchFacturaResponse(FacturasClientResponse response) {
        if (response != null && response.getCode() == 200) {
            if (response.getFacturaModels().size() > 0) {
                originalList.addAll(response.getFacturaModels());
                filterList.clear();
                filterList.addAll(originalList);
                packingAdapter.notifyDataSetChanged();
                llFacturasContainer.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(this, "Facturas no encontradas en el sistema", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Facturas no encontradas en el sistema", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccessUpdate(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            Toast.makeText(SendPickingActivity.this, "Envío resgistrado correctamente", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Toast.makeText(SendPickingActivity.this, "Error actualizando el envío", Toast.LENGTH_LONG).show();
        }
    }
}
