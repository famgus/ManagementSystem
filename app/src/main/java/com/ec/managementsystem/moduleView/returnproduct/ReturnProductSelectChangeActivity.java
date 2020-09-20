package com.ec.managementsystem.moduleView.returnproduct;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.ReturnProductDetail;
import com.ec.managementsystem.clases.responses.ReturnProductDetailsResponse;
import com.ec.managementsystem.clases.responses.ReturnProductListResponse;
import com.ec.managementsystem.interfaces.IDelegateReturnProductControl;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ReturnProductSelectChangeActivity extends BaseActivity implements
        IDelegateReturnProductControl, ReturnProductDialogScanner.DialogScanerFinished, IDelegateUpdatePickingControl {

    private static final int CODIGO_PERMISOS_CAMARA = 1, CODIGO_INTENT = 2;
    ImageView imageCodeBar;
    List<String> codes;
    private TextView textDescription, textItemCode, textSize, textColor, textApplicationDate, textPreparationDate, textQuantity;
    private Toolbar toolbar;
    private TableLayout tableReturnProducts;
    private ReturnProductDetail product;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    private TextView txtCodeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set title and activity
        setContentView(R.layout.activity_return_product_select_change);
        toolbar = findViewById(R.id.toolbar_rl);
        textDescription = findViewById(R.id.textDescription);
        textItemCode = findViewById(R.id.textItemCode);
        textSize = findViewById(R.id.textSize);
        textColor = findViewById(R.id.textColor);
        textApplicationDate = findViewById(R.id.textApplicationDate);
        textPreparationDate = findViewById(R.id.textPreparationDate);
        textQuantity = findViewById(R.id.textQuantity);
        //end set title and activity

        //load arg
        Bundle bundle = getIntent().getExtras();
        String arg = bundle.getString("arg");
        Log.i("onCreate", arg);
        Gson gson = new Gson();
        product = new Gson().fromJson(arg, ReturnProductDetail.class);
        Log.i("Loaded product", String.valueOf(product));
        //end load arg

        //set data to view
        String description = "Not Found";
        String itemCode = "Not Found";
        String size = "Not Found";
        String color = "Not Found";
        String applicationDate = "Not Found";
        String PreparationDate = "Not Found";
        String quantity = "Not Found";

        if (product.getDescription() != null) {
            description = product.getDescription();
        }
        if (product.getItemCode() != null) {
            itemCode = product.getItemCode();
        }
        if (product.getSize()!= null) {
            size = product.getSize();
        }
        if (product.getColor()!= null) {
            color = product.getColor();
        }

//        this.mapperProductsOrder();

        textDescription.setText(description);
        textItemCode.setText(itemCode);
        textSize.setText(size);
        textColor.setText(color);
        textApplicationDate.setText(applicationDate);
        textPreparationDate.setText(PreparationDate);
        textQuantity.setText(quantity);
        //end set data to view

        //prepare codebar
        codes = new ArrayList<>();
        imageCodeBar = findViewById(R.id.imageCodeBar);
        imageCodeBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogScanner(false, CODIGO_INTENT);
            }
        });
        txtCodeBar = findViewById(R.id.txtCodeBar);
        //end prepare codebar

        //Set Toolbar
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
        Log.i("onReturnProduct", response.getMessage());
    }

    private void mapperProductsOrder() {
        tableReturnProducts.removeAllViews();
        TableRow header = new TableRow(this);
        TableRow.LayoutParams layoutFila1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(layoutFila1);
        header.setBackgroundColor(getResources().getColor(R.color.star_gold));
        tableReturnProducts.addView(header);

        TableRow.LayoutParams layoutNumber = new TableRow.LayoutParams(0, 60, (float) 0.4);
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
        header.addView(headerNumber);

        headerItemCode.setLayoutParams(layoutItemCode);
        headerItemCode.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerItemCode.setPadding(0, 0, 0, 0);
        headerItemCode.setText("Codigo Articulo");
        headerItemCode.setTextColor(getResources().getColor(R.color.white));
        headerItemCode.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        header.addView(headerItemCode);

        headerSize.setLayoutParams(layoutSize);
        headerSize.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerSize.setPadding(0, 0, 0, 0);
        headerSize.setText("Talla");
        headerSize.setTextColor(getResources().getColor(R.color.white));
        headerSize.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        header.addView(headerSize);

        headerColor.setLayoutParams(layoutColor);
        headerColor.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerColor.setPadding(0, 0, 0, 0);
        headerColor.setText("Color");
        headerColor.setTextColor(getResources().getColor(R.color.white));
        headerColor.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        header.addView(headerColor);

        headerQuantity.setLayoutParams(layoutQuantity);
        headerQuantity.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerQuantity.setPadding(0, 0, 0, 0);
        headerQuantity.setText("Cant");
        headerQuantity.setTextColor(getResources().getColor(R.color.white));
        headerQuantity.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        header.addView(headerQuantity);

        headerSelect.setLayoutParams(layoutSelect);
        headerSelect.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerSelect.setPadding(0, 0, 0, 0);
        headerSelect.setText("---");
        headerSelect.setTextColor(getResources().getColor(R.color.white));
        headerSelect.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        header.addView(headerSelect);

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

//        for (int i = 0; i < products.size(); i++) {
//            TableRow row = new TableRow(this);
//            row.setLayoutParams(layoutItemRow);
//            row.setBackgroundColor(getResources().getColor(R.color.white));
//            row.setPadding(0, 0, 1, 1);
//            tableReturnProducts.addView(row);
//
//            TextView textNumber = new TextView(this);
//            TextView textItemCode = new TextView(this);
//            TextView textSize = new TextView(this);
//            TextView textColor = new TextView(this);
//            TextView textQuantity = new TextView(this);
//            final ImageView iconSelect = new ImageView(this);
//            final ImageView iconUnselect = new ImageView(this);
//
//            textNumber.setLayoutParams(layoutnumero);
//            textNumber.setGravity(Gravity.CENTER);
//            textNumber.setPadding(2, 2, 2, 2);
//            textNumber.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textNumber.setBackgroundResource(R.color.white);
//            textNumber.setText(String.valueOf(i + 1));
//
//            textItemCode.setLayoutParams(layoutdetalle);
//            textItemCode.setBackgroundResource(R.color.white);
//            textItemCode.setPadding(2, 2, 2, 2);
//            textItemCode.setGravity(Gravity.CENTER);
//            textItemCode.setText(products.get(i).getCodArticulo());
//
//            textSize.setGravity(Gravity.CENTER);
//            textSize.setLayoutParams(layoutcantidad);
//            textSize.setBackgroundResource(R.color.white);
//            textSize.setPadding(2, 2, 2, 2);
//            textSize.setText(String.valueOf(products.get(i).getTalla()));
//
//            textColor.setGravity(Gravity.CENTER);
//            textColor.setLayoutParams(layoutcantidad);
//            textColor.setBackgroundResource(R.color.white);
//            textColor.setPadding(2, 2, 2, 2);
//            textColor.setText(String.valueOf(products.get(i).getColor()));
//
//            textQuantity.setGravity(Gravity.CENTER);
//            textQuantity.setLayoutParams(layoutcantidad);
//            textQuantity.setBackgroundResource(R.color.white);
//            textQuantity.setPadding(2, 2, 2, 2);
//            textQuantity.setText(String.valueOf(products.get(i).getUnidadesTotales()));
//
//            iconSelect.setLayoutParams(layoutbotoniniciar);
//            iconSelect.setBackgroundColor(getResources().getColor(R.color.white));
//            iconSelect.setImageResource(R.drawable.icon_return_product_unselect);
//            iconSelect.setPadding(2, 2, 2, 2);
//            iconSelect.setId(i + 100);
//            iconSelect.setVisibility(View.GONE);
//            iconSelect.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//            iconUnselect.setLayoutParams(layoutbotondelete);
//            iconUnselect.setBackgroundColor(getResources().getColor(R.color.white));
//            iconUnselect.setPadding(2, 2, 6, 2);
//            iconUnselect.setId(i + 1000);
//            iconUnselect.setVisibility(View.VISIBLE);
//            iconUnselect.setImageResource(R.drawable.icon_return_product_unselect);
//
//            row.addView(textNumber);
//            row.addView(textItemCode);
//            row.addView(textSize);
//            row.addView(textColor);
//            row.addView(textQuantity);
//            row.addView(iconSelect);
//            row.addView(iconUnselect);
//        }

    }

    private void showDialogScanner(boolean scanMultiple, int codeIntent) {
        ReturnProductDialogScanner returnProductDialogScanner = new ReturnProductDialogScanner();
        returnProductDialogScanner.setScanMultiple(scanMultiple);
        returnProductDialogScanner.setCode_intent(codeIntent);
        returnProductDialogScanner.setPermisoCamaraConcedido(permisoCamaraConcedido);
        returnProductDialogScanner.setPermisoSolicitadoDesdeBoton(permisoSolicitadoDesdeBoton);
        returnProductDialogScanner.show(getSupportFragmentManager(), "alert dialog generate codes");
    }

    @Override
    public void onSuccessUpdate(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            Toast.makeText(ReturnProductSelectChangeActivity.this, "Packing registrado correctamente", Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Toast.makeText(ReturnProductSelectChangeActivity.this, "Error registrando el packing", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {
        if (action == CODIGO_INTENT) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                Log.i("Code Read", codeBar);
                codes.add(codeBar);
                txtCodeBar.setText(codeBar);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction().equals(String.valueOf(CODIGO_INTENT))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    Log.i("Code Read", codeBar);
                    codes.add(codeBar);
                    txtCodeBar.setText(codeBar);
                }
            }
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
        Toast.makeText(ReturnProductSelectChangeActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_LONG).show();
    }
}
