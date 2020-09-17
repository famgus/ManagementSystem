package com.ec.managementsystem.moduleView.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.BoxMaster;
import com.ec.managementsystem.clases.request.BoxMasterRequest;
import com.ec.managementsystem.clases.responses.BundleResponse;
import com.ec.managementsystem.clases.responses.GenericResponse;
import com.ec.managementsystem.interfaces.IDelegateBoxMasterTaskControl;
import com.ec.managementsystem.moduleView.ScannerActivity;
import com.ec.managementsystem.moduleView.login.User;
import com.ec.managementsystem.moduleView.merchandiseReception.PurchaseOrderDetailsActivity;
import com.ec.managementsystem.task.BoxMasterTaskController;
import com.ec.managementsystem.util.MySingleton;


public class  DialogLocationBoxMaster extends AppCompatDialogFragment implements DialogScanner.DialogScanerFinished, IDelegateBoxMasterTaskControl {

    private static final int CODE_INTENT = 1, CODE_INTENT_LOCATION = 400;
    LinearLayout llSearch;
    DialogListener listener;
    ImageView ivClose;
    ImageView ivScanBarCode;
    EditText etLocation;
    FragmentManager fragmentManager;
    String location;
    AlertDialog alertDialog;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public EditText getEtLocation() {
        return etLocation;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialoglocation, null);
        llSearch = view.findViewById(R.id.llScan);
        ivClose = view.findViewById(R.id.ivCancel);
        ivScanBarCode = view.findViewById(R.id.ivScanBarCode);
        etLocation = view.findViewById(R.id.etLocation);

        builder.setView(view);
        alertDialog = builder.create();
        llSearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Search
                        location = etLocation.getText().toString();
                        if (!location.equals("")) {
                           ValidateBarCode(location);
                        } else {
                            Toast ToastGravity = Toast.makeText(alertDialog.getContext(), "Ingrese una ubicación para la caja master", Toast.LENGTH_SHORT);
                            ToastGravity.setGravity(Gravity.CENTER, 0, 0);
                            ToastGravity.show();
                        }
                    }
                }
        );
        ivClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Aceptar
                        alertDialog.dismiss();
                    }
                }
        );
        ivScanBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanBarCode();
            }
        });
        return alertDialog;
    }

    private void ValidateBarCode(String bardCode){
        BoxMasterRequest request = new BoxMasterRequest();
        request.setActionPath(6);
        request.setBarCodeBoxMasterOrigin(bardCode);
        BoxMasterTaskController task = new BoxMasterTaskController();
        task.setListener(this);
        task.execute(request);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    @Override
    public void onScannerBarCode(BundleResponse bundleResponse, int action) {
        if (action == CODE_INTENT_LOCATION) {
            if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                etLocation.setText(codeBar);
                location = codeBar;
                ValidateBarCode(location);
            }
        }
    }

    public interface DialogListener {
        void applyTexts(String numcompra);
    }

    private void ScanBarCode() {
        Intent i = new Intent(getActivity(), ScannerActivity.class);
        i.putExtra("scanMultiple", false);
        i.setAction(String.valueOf(CODE_INTENT_LOCATION));
        startActivityForResult(i, CODE_INTENT_LOCATION);
        // showDialogScanner(false, CODE_INTENT_LOCATION);
    }

    private void showDialogScanner(boolean scanMultiple, int codeIntent) {
        DialogScanner dialogScanner = new DialogScanner();
        dialogScanner.setScanMultiple(scanMultiple);
        dialogScanner.setCode_intent(codeIntent);
        dialogScanner.setPermisoCamaraConcedido(true);
        dialogScanner.setPermisoSolicitadoDesdeBoton(true);
        dialogScanner.show(fragmentManager, "alert dialog generate codes");
        dialogScanner.setListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getAction().equals(String.valueOf(CODE_INTENT_LOCATION))) {
                BundleResponse bundleResponse = (BundleResponse) data.getSerializableExtra("codigo");
                if (bundleResponse != null && bundleResponse.getMapCodes().size() > 0) {
                    String codeBar = bundleResponse.getMapCodes().keySet().iterator().next();
                    etLocation.setText(codeBar);
                    location = codeBar;
                    ValidateBarCode(location);
                }
            }
        }
    }
    @Override
    public void onBoxMasterResponse(GenericResponse response) {
        if (response != null && response.getCode() == 200) {
            listener.applyTexts(location);
            alertDialog.dismiss();
        } else {
            Toast.makeText(getContext(), "El código de barras no existe en el sistema de ubicaciones", Toast.LENGTH_LONG).show();
        }
    }
}
