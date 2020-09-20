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
    private TableLayout tableUbications;
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
        String preparationDate = "Not Found";
        String quantity = "Not Found";

        if (product.getDescription() != null) {
            description = product.getDescription();
        }
        if (product.getItemCode() != null) {
            itemCode = product.getItemCode();
        }
        if (product.getSize() != null) {
            size = product.getSize();
        }
        if (product.getColor() != null) {
            color = product.getColor();
        }
        if (product.getApplicationDate() != null) {
            applicationDate = product.getApplicationDate();
        }
        if (product.getPreparationDate() != null) {
            preparationDate = product.getPreparationDate();
        }
        if (product.getQuantity() != null) {
            quantity = product.getQuantity();
        }

        tableUbications= findViewById(R.id.tableUbications);
        this.mapperBoxMasterUbication();

        textDescription.setText(description);
        textItemCode.setText(itemCode);
        textSize.setText(size);
        textColor.setText(color);
        textApplicationDate.setText(applicationDate);
        textPreparationDate.setText(preparationDate);
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

    private void mapperBoxMasterUbication() {
        tableUbications.removeAllViews();
        TableRow header = new TableRow(this);
        TableRow.LayoutParams layoutFila1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(layoutFila1);
        header.setBackgroundColor(getResources().getColor(R.color.star_gold));
        tableUbications.addView(header);

        TableRow.LayoutParams layoutUbication = new TableRow.LayoutParams(0, 60, (float) 0.4);
        layoutUbication.setMargins(4, 1, 3, 3);
        TableRow.LayoutParams layoutMasterBox = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
        layoutMasterBox.setMargins(1, 1, 3, 3);

        TextView headerUbication = new TextView(this);
        TextView headerMasterBox = new TextView(this);
        headerUbication.setLayoutParams(layoutUbication);
        headerUbication.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerUbication.setPadding(0, 0, 0, 0);
        headerUbication.setText("Código Ubicación");
        headerUbication.setTextColor(getResources().getColor(R.color.white));
        headerUbication.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        header.addView(headerUbication);

        headerMasterBox.setLayoutParams(layoutMasterBox);
        headerMasterBox.setBackgroundColor(getResources().getColor(R.color.star_gold));
        headerMasterBox.setPadding(0, 0, 0, 0);
        headerMasterBox.setText("Código Caja Master");
        headerMasterBox.setTextColor(getResources().getColor(R.color.white));
        headerMasterBox.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        header.addView(headerMasterBox);

        TableRow.LayoutParams layoutItemUbication = new TableRow.LayoutParams(0, 50, (float) 0.5);
        TableRow.LayoutParams layoutItemMasterBox = new TableRow.LayoutParams(0, 50, (float) 0.5);

        layoutItemUbication.setMarginStart(1);
        layoutItemMasterBox.setMarginStart(1);

        for (int i = 0; i <= product.getUbication().size()-1; i++) {
            Log.i("Loading Ubication",String.valueOf(i));
            TableRow row = new TableRow(this);
            row.setLayoutParams(layoutItemUbication);
            row.setBackgroundColor(getResources().getColor(R.color.white));
            row.setPadding(0, 0, 1, 1);

            TextView textUbication = new TextView(this);
            TextView textMasterBox = new TextView(this);

            textUbication.setLayoutParams(layoutItemUbication);
            textUbication.setGravity(Gravity.CENTER);
            textUbication.setPadding(2, 2, 2, 2);
            textUbication.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textUbication.setTextSize(12);
            textUbication.setBackgroundResource(R.color.white);
            textUbication.setText(product.getUbication().get(i).getUbicationCode());

            textMasterBox.setLayoutParams(layoutItemMasterBox);
            textMasterBox.setGravity(Gravity.CENTER);
            textMasterBox.setBackgroundResource(R.color.white);
            textMasterBox.setPadding(2, 2, 2, 2);
            textMasterBox.setGravity(Gravity.CENTER);
            textMasterBox.setTextSize(12);
            textMasterBox.setText(product.getUbication().get(i).getBarCodeMasterBox());

            row.addView(textUbication);
            row.addView(textMasterBox);
            tableUbications.addView(row);
        }

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
