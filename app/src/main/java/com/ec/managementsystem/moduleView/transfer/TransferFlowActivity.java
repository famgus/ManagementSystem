package com.ec.managementsystem.moduleView.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.moduleView.ui.DialogScanner;

public class TransferFlowActivity extends AppCompatActivity implements DialogScanner.DialogScanerFinished {

    public static final int CODE_INTENT_CONTAINER_BOX = 10001;
    public static final int CODE_INTENT_BAR_CODE_DETAIL = 10002;
    public static final int CODE_INTENT_PREPARED_QUANTITY = 10003;

    ScannerViewModel scannerViewModel;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_flow);
        scannerViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction() != null) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    scannerViewModel.setScannerResponse(new ScannerResponse(bundleResponse, Integer.parseInt(data.getAction())));
                } else {
                    scannerViewModel.setScannerResponse(null);
                }
            }
        }
    }

    @Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {
        if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
            scannerViewModel.setScannerResponse(new ScannerResponse(bundleResponse, action));
        } else {
            scannerViewModel.setScannerResponse(null);
        }
    }
}