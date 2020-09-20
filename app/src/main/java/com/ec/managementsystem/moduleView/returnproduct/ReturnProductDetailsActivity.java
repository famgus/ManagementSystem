package com.ec.managementsystem.moduleView.returnproduct;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import com.ec.managementsystem.util.MySingleton;
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

            this.mapperRow();
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

    private void mapperRow() {
        tableReturnProducts.removeAllViews();
        TableRow headder = new TableRow(this);
        TableRow.LayoutParams layoutFila1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        headder.setLayoutParams(layoutFila1);
        headder.setBackgroundColor(getResources().getColor(R.color.star_gold));
        tableReturnProducts.addView(headder);

        TableRow.LayoutParams layoutNumber = new TableRow.LayoutParams(0, 30, (float) 0.4);
        layoutNumber.setMargins(4, 1, 3, 3);
        TableRow.LayoutParams layoutItemCode = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
        layoutItemCode.setMargins(1, 1, 3, 3);
        TableRow.LayoutParams layoutSize = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, (float) 0.6);
        layoutSize.setMargins(1, 1, 4, 3);
        TableRow.LayoutParams layoutColor = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, (float) 0.6);
        layoutColor.setMargins(1, 1, 4, 3);
        TableRow.LayoutParams layoutQuantity = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, (float) 0.6);
        layoutQuantity.setMargins(1, 1, 4, 3);
        TableRow.LayoutParams layoutSelect = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, (float) 0.5);
        layoutSelect.setMargins(0, 1, 4, 3);

        TextView headerNumber = new TextView(this);
        TextView headerItemCode = new TextView(this);
        TextView headerSize = new TextView(this);
        TextView headerColor = new TextView(this);
        TextView headerQuantity = new TextView(this);
        final TextView headerSelect = new TextView(this);

        headerNumber.setLayoutParams(layoutNumber);
        headerNumber.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerNumber.setPadding(0, 0, 0, 0);
        headerNumber.setText("No.");
        headerNumber.setTextColor(getResources().getColor(R.color.white));
        headerNumber.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        headder.addView(headerNumber);

        headerItemCode.setLayoutParams(layoutItemCode);
        headerItemCode.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerItemCode.setPadding(0, 0, 0, 0);
        headerItemCode.setText("Codigo Articulo");
        headerItemCode.setTextColor(getResources().getColor(R.color.white));
        headerItemCode.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        headder.addView(headerItemCode);

        headerSize.setLayoutParams(layoutSize);
        headerSize.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerSize.setPadding(0, 0, 0, 0);
        headerSize.setText("Talla");
        headerSize.setTextColor(getResources().getColor(R.color.white));
        headerSize.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        headder.addView(headerSize);

        headerColor.setLayoutParams(layoutColor);
        headerColor.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerColor.setPadding(0, 0, 0, 0);
        headerColor.setText("Color");
        headerColor.setTextColor(getResources().getColor(R.color.white));
        headerColor.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        headder.addView(headerColor);

        headerQuantity.setLayoutParams(layoutQuantity);
        headerQuantity.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerQuantity.setPadding(0, 0, 0, 0);
        headerQuantity.setText("Cant");
        headerQuantity.setTextColor(getResources().getColor(R.color.white));
        headerQuantity.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        headder.addView(headerQuantity);

        headerSelect.setLayoutParams(layoutSelect);
        headerSelect.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerSelect.setPadding(0, 0, 0, 0);
        headerSelect.setText("---");
        headerSelect.setTextColor(getResources().getColor(R.color.white));
        headerSelect.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        headder.addView(headerSelect);

        TableRow.LayoutParams layoutItemRow = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutnumero = new TableRow.LayoutParams(0, 50, (float) 0.4);
        TableRow.LayoutParams layoutdetalle = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
        TableRow.LayoutParams layoutcantidad = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, (float) 0.6);
        TableRow.LayoutParams layoutbotoniniciar = new TableRow.LayoutParams(0, 50, (float) 0.5);
        TableRow.LayoutParams layoutbotondelete = new TableRow.LayoutParams(0, 50, (float) 0.5);

        layoutItemRow.setMarginStart(1);
        layoutdetalle.setMarginStart(1);
        layoutcantidad.setMarginStart(1);
        layoutbotoniniciar.setMarginStart(1);
        layoutbotondelete.setMarginStart(1);

        for (int i = 0; i < products.size(); i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(layoutItemRow);
            row.setBackgroundColor(getResources().getColor(R.color.white));
            row.setPadding(0, 0, 1, 1);
            tableReturnProducts.addView(row);

            TextView textNumber = new TextView(this);
            TextView textItemCode = new TextView(this);
            TextView textSize = new TextView(this);
            TextView textColor = new TextView(this);
            TextView textQuantity = new TextView(this);
            final ImageView iconSelect = new ImageView(this);
            final ImageView iconUnselect = new ImageView(this);

            textNumber.setLayoutParams(layoutnumero);
            textNumber.setGravity(Gravity.CENTER);
            textNumber.setPadding(2, 2, 2, 2);
            textNumber.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textNumber.setBackgroundResource(R.color.white);
            textNumber.setText(String.valueOf(i + 1));

            textItemCode.setLayoutParams(layoutdetalle);
            textItemCode.setBackgroundResource(R.color.white);
            textItemCode.setPadding(2, 2, 2, 2);
            textItemCode.setGravity(Gravity.CENTER);
            textItemCode.setText(products.get(i).getCodArticulo());

            textSize.setGravity(Gravity.CENTER);
            textSize.setLayoutParams(layoutcantidad);
            textSize.setBackgroundResource(R.color.white);
            textSize.setPadding(2, 2, 2, 2);
            textSize.setText(String.valueOf(products.get(i).getTalla()));

            textColor.setGravity(Gravity.CENTER);
            textColor.setLayoutParams(layoutcantidad);
            textColor.setBackgroundResource(R.color.white);
            textColor.setPadding(2, 2, 2, 2);
            textColor.setText(String.valueOf(products.get(i).getColor()));

            textQuantity.setGravity(Gravity.CENTER);
            textQuantity.setLayoutParams(layoutcantidad);
            textQuantity.setBackgroundResource(R.color.white);
            textQuantity.setPadding(2, 2, 2, 2);
            textQuantity.setText(String.valueOf(products.get(i).getUnidadesTotales()));

            iconSelect.setLayoutParams(layoutbotoniniciar);
            iconSelect.setBackgroundColor(getResources().getColor(R.color.white));
            iconSelect.setImageResource(R.drawable.icon_return_product_unselect);
            iconSelect.setPadding(2, 2, 2, 2);
            iconSelect.setId(i + 100);
            iconSelect.setVisibility(View.GONE);
            iconSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            iconUnselect.setLayoutParams(layoutbotondelete);
            iconUnselect.setBackgroundColor(getResources().getColor(R.color.white));
            iconUnselect.setPadding(2, 2, 6, 2);
            iconUnselect.setId(i + 1000);
            iconUnselect.setVisibility(View.VISIBLE);
            iconUnselect.setImageResource(R.drawable.icon_return_product_unselect);

            row.addView(textNumber);
            row.addView(textItemCode);
            row.addView(textSize);
            row.addView(textColor);
            row.addView(textQuantity);
            row.addView(iconSelect);
            row.addView(iconUnselect);
        }

    }
}
