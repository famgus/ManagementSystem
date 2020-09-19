package com.ec.managementsystem.moduleView.returnproduct;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.request.ReturnProductRequest;
import com.ec.managementsystem.clases.responses.ReturnProductListResponse;
import com.ec.managementsystem.interfaces.IDelegateReturnProductControl;
import com.ec.managementsystem.moduleView.BaseActivity;
import com.ec.managementsystem.task.ReturnProductTaskController;

public class ReturnProductActivity extends BaseActivity implements IDelegateReturnProductControl {

    LinearLayout llSearch;
    EditText et_numerocompra;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_product);
        toolbar = findViewById(R.id.tbar_purchase);
        llSearch = findViewById(R.id.llSearch);

        et_numerocompra = findViewById(R.id.et_numerocompra);

        // Set Toolbar
        this.toolbar.setTitle(R.string.text_toolbar_return_product);
        this.setupToolBar(toolbar);
        this.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order = et_numerocompra.getText().toString().trim();
                if (order == null || order.length() == 0) {
                    Toast.makeText(ReturnProductActivity.this, "Debe ingresar una orden valida", Toast.LENGTH_LONG).show();
                } else {
                    int numberOrder = Integer.parseInt(order);
                    ReturnProductRequest request = new ReturnProductRequest(numberOrder);
                    ReturnProductTaskController task = new ReturnProductTaskController();
                    task.setListener(ReturnProductActivity.this);
                    task.execute(request);
                }
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
                    Toast.makeText(ReturnProductActivity.this, "La orden no tiene productos", Toast.LENGTH_LONG).show();
                } else {
                    Log.i("onReturnProduct", "Loading Products");
                }
                break;
            case 401:
                Log.i("onReturnProduct", "Ocurrio un problema 401");
                Toast.makeText(ReturnProductActivity.this, "Ocurrio un problema 401", Toast.LENGTH_LONG).show();
            case 400:
                Log.i("onReturnProduct", "La orden no fue encontrada");
                Toast.makeText(ReturnProductActivity.this, "La orden no fue encontrada", Toast.LENGTH_LONG).show();
            default:
                Log.i("onReturnProduct", "Ocurrio un problema intente mas tarde");
                Toast.makeText(ReturnProductActivity.this, "Ocurrio un problema intente mas tarde", Toast.LENGTH_LONG).show();
                break;

        }

        Log.i("onReturnProduct", response.getMessage());
        Log.i("onReturnProduct", String.valueOf(response.getPedidoList().size()));
        Log.i("onReturnProduct", String.valueOf(code));
    }

}
