package com.ec.managementsystem.moduleView.send;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.FacturaModel;
import com.ec.managementsystem.clases.GuideModel;
import com.ec.managementsystem.clases.request.PickingRequest;
import com.ec.managementsystem.clases.responses.CustomerInvoiceForSendResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.interfaces.IDelegateResponseGeneric;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;
import com.ec.managementsystem.interfaces.IListenerPacking;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.moduleView.SensorActivity;
import com.ec.managementsystem.moduleView.adapters.CustomerInvoicesForSendAdapter;
import com.ec.managementsystem.moduleView.adapters.PackingAdapter;
import com.ec.managementsystem.task.CustomerInvoicesForSendTaskController;
import com.ec.managementsystem.task.PickingUpdateTaskController;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class SendPickingActivity extends BaseActivity implements IDelegateUpdatePickingControl, IDelegateResponseGeneric<CustomerInvoiceForSendResponse> {

    private final int BAR_CODE_INTENT_CODE = 10003;

    Toolbar toolbar;
    RecyclerView rvFacturasList;
    EditText etFacturasSearch;
    RadioGroup rgSearchOption;
    ImageView ivBarCodeIcon;
    Button btnSearch, btnRegister;
    TextInputEditText tiedCodeToValidate;
    LinearLayout llFacturasContainer;
    List<GuideModel> originalList, filterList;
    CustomerInvoicesForSendAdapter invoicesForSendAdapter;

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
            tiedCodeToValidate = findViewById(R.id.tiet_sendpicking);
            tiedCodeToValidate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    for(GuideModel guideModel : originalList){
                        if(guideModel.getSeriesNumber().equals(s.toString())){
                            guideModel.setVerified(true);
                            int index = originalList.indexOf(guideModel);
                            filterList.set(index, guideModel);
                            invoicesForSendAdapter.notifyItemChanged(index);
                            btnRegister.setEnabled(true);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            ivBarCodeIcon = findViewById(R.id.iv_sendpicking_barcode);
            ivBarCodeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogScanner();
                }
            });
            rgSearchOption = findViewById(R.id.rg_sendpicking_search);
            etFacturasSearch = findViewById(R.id.etFacturasSearch);
            btnSearch = findViewById(R.id.btn_sendpicking_search);
            btnRegister = findViewById(R.id.btn_sendpicking_register);
            rvFacturasList = findViewById(R.id.rvFacturasList);
            llFacturasContainer = findViewById(R.id.llFacturasContainer);
            llFacturasContainer.setVisibility(View.GONE);
            this.etFacturasSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.d("onTextChanged: ", charSequence.toString());
                    onQueryTrailerSearchChanged(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            etFacturasSearch.clearFocus();
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rgSearchOption.getCheckedRadioButtonId() != -1){
                        int selectedOption = -1;
                        switch (rgSearchOption.getCheckedRadioButtonId()) {
                            case R.id.rb_sendpicking_guide:
                                selectedOption = 1;
                                break;
                            case R.id.rb_sendpicking_other:
                                selectedOption = 0;
                                break;
                        }
                        CustomerInvoicesForSendTaskController task = new CustomerInvoicesForSendTaskController(SendPickingActivity.this);
                        task.execute(selectedOption);
                        btnSearch.setEnabled(false);
                    }else {
                        Toast.makeText(SendPickingActivity.this, "Debe seleccionar una opción de búsqueda", Toast.LENGTH_LONG).show();
                    }
                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PickingUpdateTaskController task = new PickingUpdateTaskController();
                    task.setListener(SendPickingActivity.this);
                    PickingRequest request = new PickingRequest();
                    request.setNumberSerie(originalList.get(0).getSeriesNumber());
                    request.setNumberPedido(originalList.get(0).getOrderNumber());
                    request.setState(6);
                    request.setPath(1);
                    task.execute(request);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error inicializando la actividad", Toast.LENGTH_LONG).show();
        }
    }

    private boolean hasVerified(){
        for (GuideModel item : originalList) {
            if (item.isVerified()) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyFacturasSelected() {
        int countChecked = 0;
        for (GuideModel item : originalList) {
            if (item.isVerified()) {
                countChecked++;
            }
        }
        return countChecked == originalList.size();
    }

    private void onQueryTrailerSearchChanged(final String query) {
        if(!query.isEmpty()){
            try {
                int insertedBillNumber = Integer.parseInt(query);
                filterList = filter(originalList, insertedBillNumber);
            }catch (Exception e){
                filterList = new ArrayList<>(originalList);
            }
        }else{
            filterList = new ArrayList<>(originalList);
        }
        invoicesForSendAdapter.updateData(filterList);
    }

    protected List<GuideModel> filter(final List<GuideModel> models, final int query)  {
        List<GuideModel> filteredModelList = new ArrayList<>();
        if (!models.isEmpty()) {
            for (GuideModel model : models) {
                if(model.getBillNumber() == query){
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManagerTrailer = new LinearLayoutManager(SendPickingActivity.this);
        DividerItemDecoration dividerTrailer = new DividerItemDecoration(SendPickingActivity.this, linearLayoutManagerTrailer.getOrientation());
        this.invoicesForSendAdapter = new CustomerInvoicesForSendAdapter(filterList);
        this.rvFacturasList.setAdapter(invoicesForSendAdapter);
        this.rvFacturasList.setLayoutManager(linearLayoutManagerTrailer);
        this.rvFacturasList.addItemDecoration(dividerTrailer);
    }

    private void showDialogScanner() {
        Intent i = new Intent(this, SensorActivity.class);
        i.putExtra("scanMultiple", false);
        i.putExtra("permisoCamaraConcedido", true);
        i.putExtra("permisoSolicitadoDesdeBoton", true);
        i.setAction(String.valueOf(BAR_CODE_INTENT_CODE));
        startActivityForResult(i, BAR_CODE_INTENT_CODE);

    }

    private void initCollection() {
        this.originalList = new ArrayList<>();
        this.filterList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rvFacturasList != null) {
            rvFacturasList.setAdapter(null);
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

    @Override
    public void onResponse(CustomerInvoiceForSendResponse response) {
        if(response != null && response.getCode() == 200){
            if(response.getInvoices().size() > 0){
                llFacturasContainer.setVisibility(View.VISIBLE);
                originalList = response.getInvoices();
                filterList = response.getInvoices();
                invoicesForSendAdapter.updateData(filterList);
            }else{
                btnSearch.setEnabled(true);
                llFacturasContainer.setVisibility(View.GONE);
                Toast.makeText(this, "No hay facturas", Toast.LENGTH_SHORT).show();
            }
        }else{
            btnSearch.setEnabled(true);
            llFacturasContainer.setVisibility(View.GONE);
            Toast.makeText(this, "Error obteniendo la información", Toast.LENGTH_SHORT).show();
        }
    }
}
