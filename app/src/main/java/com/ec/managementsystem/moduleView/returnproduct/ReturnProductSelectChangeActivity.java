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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.request.ReturnProductChangeRequest;
import com.ec.managementsystem.clases.request.ReturnProductValidationRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.ReturnProductChangeResponse;
import com.ec.managementsystem.clases.responses.ReturnProductDetail;
import com.ec.managementsystem.clases.responses.ReturnProductValidationResponse;
import com.ec.managementsystem.interfaces.IDelegateReturnProductChangeControl;
import com.ec.managementsystem.interfaces.IDelegateReturnProductValidationControl;
import com.ec.managementsystem.interfaces.IDelegateUpdatePickingControl;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.task.ReturnProductChangeTaskController;
import com.ec.managementsystem.task.ReturnProductValidationTaskController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ReturnProductSelectChangeActivity extends BaseActivity implements
        IDelegateReturnProductChangeControl, ReturnProductDialogScanner.DialogScanerFinished,
        IDelegateUpdatePickingControl, IDelegateReturnProductValidationControl {

    public static final int CODE_FLOW_MASTER_BOX = 99, CODE_FLOW_QUANTITY = 100;
    public static final int CODE_FLOW_UBICATION = 101;
    private static final int CODIGO_PERMISOS_CAMARA = 1, CODE_INDENT = 2;
    ImageView imageCodeBar, quantityCodeBar;
    List<String> codes;
    private int lastCode = 1;
    private TextView textDescription, textItemCode, textSize, textColor, textApplicationDate, textPreparationDate, textQuantity;
    private Toolbar toolbar;
    private TableLayout tableUbications;
    private ReturnProductDetail product;
    private boolean permisoCamaraConcedido = false, permisoSolicitadoDesdeBoton = false;
    private EditText txtCodeBar, txtQuantityCodeBar;
    private LinearLayout buttonSave;
    private RadioButton radioMasterBox, radioUbication;
    private boolean finish = false;

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

        tableUbications = findViewById(R.id.tableUbications);
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
        quantityCodeBar = findViewById(R.id.quantityCodeBar);
        imageCodeBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastCode = (radioMasterBox.isChecked()) ? CODE_FLOW_MASTER_BOX : CODE_FLOW_UBICATION;
                Log.i("Click Code", String.valueOf(lastCode));
                showDialogScanner(false, CODE_INDENT);
            }
        });
        quantityCodeBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastCode = CODE_FLOW_QUANTITY;
                Log.i("Click Code", String.valueOf(lastCode));
                showDialogScanner(false, CODE_INDENT);
            }
        });
        txtCodeBar = findViewById(R.id.txtCodeBar);
        txtQuantityCodeBar = findViewById(R.id.txtQuantityCodeBar);
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
        //end set toolbar

        //radiobuttons
        radioMasterBox = findViewById(R.id.radioMasterBox);
        radioUbication = findViewById(R.id.radioUbication);
        //end radiobuttons

        //save button
        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int validation = 0;
                String valueTxtCodeBar = txtCodeBar.getText().toString();
                String valueTxtQuantity = txtQuantityCodeBar.getText().toString();
                int maxQuantity = Integer.parseInt(product.getQuantity());
                int quantity = 0;

                Log.i("Validation", quantity + "/" + maxQuantity);

                if (valueTxtCodeBar.trim().equals("")) {
                    validation = 1;
                }
                if (valueTxtQuantity.trim().equals("") || valueTxtQuantity.equals("0")) {
                    validation = 2;
                } else {
                    quantity = Integer.parseInt(txtQuantityCodeBar.getText().toString());
                    if (quantity > maxQuantity) {
                        validation = 3;
                    }
                }

                if (validation == 1) {
                    Toast.makeText(ReturnProductSelectChangeActivity.this, "Debe colocar una Caja Maestra/Ubicación", Toast.LENGTH_LONG).show();
                }
                if (validation == 2) {
                    Toast.makeText(ReturnProductSelectChangeActivity.this, "Debe colocar una cantidad", Toast.LENGTH_LONG).show();
                }
                if (validation == 3) {
                    Toast.makeText(ReturnProductSelectChangeActivity.this, "La cantidad supera al maximo a devolver", Toast.LENGTH_LONG).show();

                }

                if (validation == 0) {

                    int type = (radioMasterBox.isChecked()) ? 1 : 2;

                    String data = "";
                    data += product.getItemCode() + ",";
                    data += product.getSize() + ",";
                    data += product.getColor() + ",";
                    data += product.getQuantity() + ",";
                    data += type + ",";
                    data += product.getBarCode() + ",";
                    data += String.valueOf(quantity);

                    Log.i("Send Data", data);
                    ReturnProductChangeRequest request = new ReturnProductChangeRequest(data);
                    ReturnProductChangeTaskController task = new ReturnProductChangeTaskController();
                    task.setListener(ReturnProductSelectChangeActivity.this);
                    task.execute(request);
                }

            }
        });
        //end save button
    }

    public void setupToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_white_24);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Log.i("onBackPressed", String.valueOf(finish));
        if (finish) {
            Log.i("Sent", String.valueOf(finish));
            Intent data = new Intent();
            data.putExtra("barCode", product.getBarCode());
            setResult(RESULT_OK, data);
            finish();
        } else {
            Intent data = new Intent();
            finish();
        }
    }

    @Override
    public void onReturnProduct(ReturnProductChangeResponse response) {
        int code = response.getCode();
        switch (code) {
            case 200:
                Log.i("onReturnProduct", "Saved");
                Toast.makeText(ReturnProductSelectChangeActivity.this, "Cambio realizado con exito", Toast.LENGTH_LONG).show();
                finish = true;
                onBackPressed();
                break;
            case 401:
                Log.i("onReturnProduct", "Ocurrio un problema 401");
                Toast.makeText(ReturnProductSelectChangeActivity.this, "Ocurrio un problema 401", Toast.LENGTH_LONG).show();
                break;
            case 400:
                Log.i("onReturnProduct", "La orden no fue encontrada");
                Toast.makeText(ReturnProductSelectChangeActivity.this, "Ocurrion un problema 400", Toast.LENGTH_LONG).show();
                break;
            default:
                Log.i("onReturnProduct", "Ocurrio un problema intente mas tarde");
                Toast.makeText(ReturnProductSelectChangeActivity.this, "Ocurrio un problema intente mas tarde", Toast.LENGTH_LONG).show();
                break;
        }

        Log.i("onReturnProduct", response.getMessage());
        Log.i("onReturnProduct", String.valueOf(code));

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

        for (int i = 0; i <= product.getUbication().size() - 1; i++) {
            Log.i("Loading Ubication", String.valueOf(i));
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
        Log.i("showDialogScanner", String.valueOf(codeIntent));
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
        Log.i("onScannerBarCode", String.valueOf(action));
        if (lastCode == CODE_FLOW_MASTER_BOX || lastCode == CODE_FLOW_UBICATION) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String barCode = bundleResponse.getMapCodes().keySet().iterator().next();
                Log.i("Barcode Read", barCode);
                this.sendValidation(barCode, lastCode);
            }
        }
        if (lastCode == CODE_FLOW_QUANTITY) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String barCode = bundleResponse.getMapCodes().keySet().iterator().next();
                Log.i("Barcode Read", barCode);
                this.addQuantity(barCode);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("onActivityResult", String.valueOf(resultCode));
        Log.i("Last Code", String.valueOf(lastCode));
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction().equals(String.valueOf(CODE_INDENT))
                    && (lastCode == CODE_FLOW_MASTER_BOX || lastCode == CODE_FLOW_UBICATION)
            ) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String barCode = bundleResponse.getMapCodes().keySet().iterator().next();
                    Log.i("Barcode Read", barCode);
                    this.sendValidation(barCode, lastCode);
                }
            }
            if (data != null && data.getAction().equals(String.valueOf(CODE_INDENT)) && lastCode == CODE_FLOW_QUANTITY) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String barCode = bundleResponse.getMapCodes().keySet().iterator().next();
                    Log.i("Barcode Read", barCode);
                    this.addQuantity(barCode);
                }
            }
        }
    }

    public void addQuantity(String barcode) {
        int maxQuality = Integer.parseInt(product.getQuantity());
        int quantity = 0;
        String valueTxtQuantity = txtQuantityCodeBar.getText().toString();

        if (!valueTxtQuantity.trim().equals("")) {
            quantity = Integer.parseInt(txtQuantityCodeBar.getText().toString());
        }

        Log.i("addQuantity", quantity + "/" + maxQuality);

        if (!barcode.equals(product.getBarCode())) {
            Toast.makeText(ReturnProductSelectChangeActivity.this, "El producto no coincide", Toast.LENGTH_LONG).show();
        } else {

            if (quantity >= maxQuality) {
                Toast.makeText(ReturnProductSelectChangeActivity.this, "Se alcanzo la cantidad maxima", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ReturnProductSelectChangeActivity.this, "Producto agregado", Toast.LENGTH_LONG).show();
                quantity = quantity + 1;
            }
            txtQuantityCodeBar.setText(String.valueOf(quantity));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("onRequestPermissionsR", String.valueOf(requestCode));
        switch (requestCode) {
            case CODIGO_PERMISOS_CAMARA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (permisoSolicitadoDesdeBoton) {
                        showDialogScanner(false, lastCode);
                    }
                    permisoCamaraConcedido = true;
                } else {
                    permisoDeCamaraDenegado();
                }
                break;
        }
    }

    private void permisoDeCamaraDenegado() {
        Toast.makeText(ReturnProductSelectChangeActivity.this, "No puedes escanear si no das permiso", Toast.LENGTH_LONG).show();
    }

    private void sendValidation(String data, int typeValidation) {
        Log.i("Send Validation", String.valueOf(typeValidation));
        Log.i("Send Validation", String.valueOf(data));
        ReturnProductValidationRequest request = new ReturnProductValidationRequest(data, typeValidation);
        ReturnProductValidationTaskController task = new ReturnProductValidationTaskController();
        task.setListener(ReturnProductSelectChangeActivity.this);
        task.execute(request);
    }

    private boolean readValidation(String barCode, int code, int typeValidation) {
        Log.i("readValidation", barCode + "," + code + "," + typeValidation);

        boolean response = false;
        if (code == 200) {
            response = true;

            if (typeValidation == CODE_FLOW_MASTER_BOX) {
                Log.i("Assign Barcode", barCode);
                codes.add(barCode);
                txtCodeBar.setText(barCode);
            }
            if (typeValidation == CODE_FLOW_UBICATION) {
                Log.i("Assign Barcode", barCode);
                codes.add(barCode);
                txtCodeBar.setText(barCode);
            }

        } else {
            Log.i("sendValidation", "Ocurrio un problema");
            if (typeValidation == CODE_FLOW_MASTER_BOX) {
                Toast.makeText(ReturnProductSelectChangeActivity.this, "Caja maestra inválida", Toast.LENGTH_LONG).show();
            }
            if (typeValidation == CODE_FLOW_UBICATION) {
                Toast.makeText(ReturnProductSelectChangeActivity.this, "Ubicación inválida", Toast.LENGTH_LONG).show();
            }
        }
        return response;
    }

    @Override
    public void onValidationMasterBoxUbication(ReturnProductValidationResponse
                                                       returnProductValidationResponse) {
        Log.i("ReadResponseActivity", returnProductValidationResponse.getMessage());
        Log.i("ReadResponseActivity", String.valueOf(returnProductValidationResponse.getCode()));
        this.readValidation(
                returnProductValidationResponse.getBarCode(),
                returnProductValidationResponse.getCode(),
                returnProductValidationResponse.getTypeValidation()
        );
    }
}
