package com.ec.managementsystem.moduleView.returnproduct;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.ReturnProductListResponse;
import com.ec.managementsystem.clases.responses.ReturnProductResponse;
import com.ec.managementsystem.interfaces.IDelegateReturnProductControl;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReturnProductDetailsActivity extends BaseActivity implements IDelegateReturnProductControl {

    private TextView txtSerialNumberValue, txtOrderNumberValue, txtProviderCodeValue, txtPreparationDateValue;
    private Toolbar toolbar;
    private List<ReturnProductResponse> products;
    private TableLayout tableReturnProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set title and activity
        setContentView(R.layout.activity_return_product_details);
        toolbar = findViewById(R.id.toolbar_rl);
        txtSerialNumberValue = findViewById(R.id.txtSerialNumberValue);
        txtOrderNumberValue = findViewById(R.id.txtOrderNumberValue);
        txtProviderCodeValue = findViewById(R.id.txtProviderCodeValue);
        txtPreparationDateValue = findViewById(R.id.txtPreparationDateValue);
        tableReturnProducts = findViewById(R.id.tableReturnProducts);
        //end set title and activity

        //load arg
        Bundle bundle = getIntent().getExtras();
        String arg = bundle.getString("arg");
        Log.i("onCreate", arg);
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<ReturnProductResponse>>() {
        }.getType();
        products = new Gson().fromJson(arg, listType);
        Log.i("Loaded products", String.valueOf(products.size()));
        //end load arg

        //set data to view
        String providerCode = "Not Found";
        String serialNumber = "Not Found";
        String orderNumber = "Not Found";
        String preparationDate = "Not Found";

        for (int i = 0; i <= products.size() - 1; i++) {
            Log.i("Loading Product", String.valueOf(i));
            ReturnProductResponse product = products.get(i);
            if (product.getCodProvedor() != null) {
                providerCode = product.getCodProvedor();
            }
            if (product.getNumSerie() != null) {
                serialNumber = product.getNumSerie();
            }
            if (product.getNumPedido() != null) {
                orderNumber = product.getNumPedido();
            }
            if (product.getFechaPedido() != null) { //todo: change this field to fechaPreparation
                preparationDate = product.getFechaPedido();
            }

            tableReturnProducts.addView(this.mapperRow(product,i), new TableLayout.LayoutParams());

        }
        txtSerialNumberValue.setText(serialNumber);
        txtOrderNumberValue.setText(orderNumber);
        txtProviderCodeValue.setText(providerCode);
        txtPreparationDateValue.setText(preparationDate);
        //end set data to view

        // Set Toolbar
        this.toolbar.setTitle(R.string.text_toolbar_return_product);
        this.setupToolBar(toolbar);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setupToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_white_24);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onReturnProduct(ReturnProductListResponse response) {
        int code = response.getCode();
        switch (code) {
            case 200:
                Log.i("onReturnProduct", "Loading Product");
                if (response.getPedidoList() == null || response.getPedidoList().size() == 0) {
                    Toast.makeText(ReturnProductDetailsActivity.this, "La orden no tiene productos", Toast.LENGTH_LONG).show();
                } else {
                    Log.i("onReturnProduct", "Loading Products");
                    //todo: load other screen
                }
                break;
            case 401:
                Log.i("onReturnProduct", "Ocurrio un problema 401");
                Toast.makeText(ReturnProductDetailsActivity.this, "Ocurrio un problema 401", Toast.LENGTH_LONG).show();
            case 400:
                Log.i("onReturnProduct", "La orden no fue encontrada");
                Toast.makeText(ReturnProductDetailsActivity.this, "La orden no fue encontrada", Toast.LENGTH_LONG).show();
            default:
                Log.i("onReturnProduct", "Ocurrio un problema intente mas tarde");
                Toast.makeText(ReturnProductDetailsActivity.this, "Ocurrio un problema intente mas tarde", Toast.LENGTH_LONG).show();
                break;

        }

        Log.i("onReturnProduct", response.getMessage());
        Log.i("onReturnProduct", String.valueOf(response.getPedidoList().size()));
        Log.i("onReturnProduct", String.valueOf(code));
    }

    private TableRow mapperRow(ReturnProductResponse product, int num) {
        String itemCode = "Not Found";
        String size = "Not Found";
        String color = "Not Found";
        String quantity = "Not Found";

        if (product.getCodArticulo() != null) {
            itemCode = product.getCodArticulo();
        }
        if (product.getTalla() != null) {
            size = product.getTalla();
        }
        if (product.getColor() != null) {
            color = product.getColor();
        }
        if (product.getUnidadesTotales() != null) { //todo: change this field to fechaPreparation
            quantity = product.getUnidadesTotales();
        }

        TableRow row = new TableRow(this);
        for (int i = 1; i <= 6; i++) {
            String text = "";

            switch (i) {
                case 2:
                    text = itemCode;
                    break;
                case 3:
                    text = size;
                    break;
                case 4:
                    text = color;
                    break;
                case 5:
                    text = quantity;
                    break;

                default:
                    text = String.valueOf(num);
                    break;
            }
            TextView label = new TextView(this);
            label.setText(text);
            label.setBackgroundColor(Color.WHITE);
            label.setTextColor(Color.BLACK);
            
            row.addView(label, new TableRow.LayoutParams(i));
        }

        return row;

    }
}
