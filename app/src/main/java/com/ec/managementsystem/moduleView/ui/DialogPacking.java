package com.ec.managementsystem.moduleView.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.ec.managementsystem.R;


public class DialogPacking extends AppCompatDialogFragment {

    LinearLayout llSearch;
    ImageView ivCloseDialog;
    EditText etNumberPedido;
    private DialogOrdenCompra.DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_packing, null);
        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        ivCloseDialog = (ImageView) view.findViewById(R.id.ivCloseDialog);
        etNumberPedido = (EditText) view.findViewById(R.id.etNumberPedido);

        builder.setView(view);
        alertDialog = builder.create();
        llSearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Search
                        String number = etNumberPedido.getText().toString();
                        if (!number.equals("")) {
                            listener.applyTexts(number);
                            alertDialog.dismiss();
                        } else {

                            Toast ToastGravity = Toast.makeText(alertDialog.getContext(), "Ingrese un número de pedido", Toast.LENGTH_SHORT);
                            ToastGravity.setGravity(Gravity.CENTER, 0, 0);
                            ToastGravity.show();
                        }

                    }
                }
        );

        ivCloseDialog.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Aceptar
                        alertDialog.dismiss();
                    }
                }
        );

        return alertDialog;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogOrdenCompra.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface DialogListener {
        void applyTexts(String numcompra);
    }
}
