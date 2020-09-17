package com.ec.managementsystem.moduleView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.BoxMaster;
import com.ec.managementsystem.clases.request.BoxMasterRequest;
import com.ec.managementsystem.clases.request.PedidoRequest;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.clases.responses.ListPedidoPickingResponse;
import com.ec.managementsystem.clases.responses.PedidoResponse;
import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;
import com.ec.managementsystem.interfaces.IDelegateBoxMasterTaskControl;
import com.ec.managementsystem.interfaces.IDelegatePedidoTaskControl;
import com.ec.managementsystem.interfaces.IDelegatePickingPedidoUserTaskControl;
import com.ec.managementsystem.interfaces.IDelegatePickingTaskControl;
import com.ec.managementsystem.moduleView.boxMaster.BoxMasterActivity;
import com.ec.managementsystem.moduleView.login.User;
import com.ec.managementsystem.moduleView.merchandiseReception.LeaderReception;
import com.ec.managementsystem.moduleView.merchandiseReception.PurchaseOrderDetailsActivity;
import com.ec.managementsystem.moduleView.packing.ListPackingActivity;
import com.ec.managementsystem.moduleView.picking.ListPickingActivity;
import com.ec.managementsystem.moduleView.qualityControl.QualityControlActivity;
import com.ec.managementsystem.moduleView.send.SendPickingActivity;
import com.ec.managementsystem.moduleView.ui.DialogOrdenCompra;
import com.ec.managementsystem.moduleView.ui.DialogPicking;
import com.ec.managementsystem.task.BoxMasterTaskController;
import com.ec.managementsystem.task.PedidoTaskController;
import com.ec.managementsystem.task.PickingPedidoUserTaskController;
import com.ec.managementsystem.util.MySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends BaseActivity implements DialogOrdenCompra.DialogListener, IDelegatePedidoTaskControl, DialogPicking.DialogListener, IDelegatePickingTaskControl, IDelegatePickingPedidoUserTaskControl, IDelegateBoxMasterTaskControl {

    private AppBarConfiguration mAppBarConfiguration;
    public CardView cvMercaderia, cvBoxMaster, cvQualityControl, cvPicking;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tbar_purchase);
        setSupportActionBar(toolbar);
        cvMercaderia = (CardView) findViewById(R.id.cvMercaderia);
        cvBoxMaster = (CardView) findViewById(R.id.cvBoxMaster);
        cvQualityControl = (CardView) findViewById(R.id.cvQualityControl);
        cvPicking = (CardView) findViewById(R.id.cvPicking);
        user = MySingleton.getUser();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!MySingleton.getInstance().isRegisterPurchaseOrder() && MySingleton.getInstance().getBoxMasterList().size() > 0) {
            for (BoxMaster boxMaster : MySingleton.getInstance().getBoxMasterList()) {
                BoxMasterRequest request = new BoxMasterRequest();
                request.setActionPath(5);
                request.setBarCodeBoxMasterOrigin(boxMaster.getBarCode());
                BoxMasterTaskController task = new BoxMasterTaskController();
                task.setListener(MainActivity.this);
                task.execute(request);
            }
        }
    }

    public void ToPurchaseOrders(View v) {
        DialogOrdenCompra examplealertdialog = new DialogOrdenCompra();
        examplealertdialog.show(getSupportFragmentManager(), "alert dialog purchase");
    }

    public void ToBoxMaster(View v) {
        Intent i = new Intent(MainActivity.this, BoxMasterActivity.class);
        startActivity(i);
    }

    public void ToQualityControl(View v) {
        Intent i = new Intent(MainActivity.this, QualityControlActivity.class);
        startActivity(i);
    }

    public void ToPicking(View v) {
        DialogPicking dialogPicking = new DialogPicking();
        dialogPicking.show(getSupportFragmentManager(), "alert dialog purchase");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void OpenAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialognumcompra);
        LinearLayout dialogButton = (LinearLayout) dialog.findViewById(R.id.llSearch);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Dismissed..!!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    public void applyTexts(String numcompra) {
        PedidoRequest request = new PedidoRequest();
        request.setNumero(Integer.parseInt(numcompra));
        PedidoTaskController pedidoTaskController = new PedidoTaskController();
        pedidoTaskController.setListener(MainActivity.this);
        pedidoTaskController.execute(request);

    }

    @Override
    public void onPedidoResponse(PedidoResponse response) {
        if (response != null && response.getCode() == 200 && response.getPedidoDetailList() != null && response.getPedidoDetailList().size() > 0) {
            MySingleton.getInstance().setPedidoResponse(response);
            if (user.getRol() == 1) {
                Intent i = new Intent(this, PurchaseOrderDetailsActivity.class);
                i.putExtra("numerocompra", response.getPedidoDetailList().get(0).getNumPedido());
                startActivity(i);
            } else {
                Intent i = new Intent(this, LeaderReception.class);
                i.putExtra("numerocompra", response.getPedidoDetailList().get(0).getNumPedido());
                startActivity(i);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Orden de compra no encontrada en el sistema", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void completeSelectPicking(Integer path) {
        if (path == 1) {
            MySingleton.RestorePedidoPiking();
            if (MySingleton.getInstance().getPickingUserResponse() == null) {
                //Download from server list of Pedido for Picking
                PickingPedidoUserTaskController task = new PickingPedidoUserTaskController();
                task.setListener(this);
                task.execute();
            } else {
                Intent i = new Intent(MainActivity.this, ListPickingActivity.class);
                startActivity(i);
            }
        } else if (path == 2) {
            Intent i = new Intent(MainActivity.this, ListPackingActivity.class);
            startActivity(i);
        } else if (path == 3) {
            Intent i = new Intent(MainActivity.this, SendPickingActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Error seleccionando la acción", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListPickingResponse(ListPedidoPickingResponse response) {
        if (response != null) {
            if (response.getAction() == 1) {

            } else if (response.getAction() == 2) {
                Intent i = new Intent(MainActivity.this, ListPackingActivity.class);
                startActivity(i);
            } else if (response.getAction() == 3) {
                Intent i = new Intent(MainActivity.this, SendPickingActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Error seleccionando la acción", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSuccessPickingPedidoUser(PickingPedidoUserResponse response) {
        if (response != null) {
            MySingleton.getInstance().setPickingUserResponse(response);
            Intent i = new Intent(MainActivity.this, ListPickingActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Error seleccionando la acción", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBoxMasterResponse(GenericResponse response) {

    }
}
