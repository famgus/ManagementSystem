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


public class DialogOrdenCompra extends AppCompatDialogFragment {

    private LinearLayout btnsearch;
    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialognumcompra, null);
        LinearLayout btnsearch = (LinearLayout) view.findViewById(R.id.llSearch);
        ImageView btnclose = (ImageView) view.findViewById(R.id.btncnl_dialog);
        final EditText orden = (EditText) view.findViewById(R.id.etnocompra);

        builder.setView(view);
        alertDialog = builder.create();
        btnsearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Search
                        String numberpurchase = orden.getText().toString();
                        if (!numberpurchase.equals("")) {
                            listener.applyTexts(numberpurchase);
                            alertDialog.dismiss();
                        } else {

                            Toast ToastGravity = Toast.makeText(alertDialog.getContext(), "Ingrese un número de compra", Toast.LENGTH_SHORT);
                            ToastGravity.setGravity(Gravity.CENTER, 0, 0);
                            ToastGravity.show();
                        }

                    }
                }
        );

        btnclose.setOnClickListener(
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
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface DialogListener {
        void applyTexts(String numcompra);
    }
}
