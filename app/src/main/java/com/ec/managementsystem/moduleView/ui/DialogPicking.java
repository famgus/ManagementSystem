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


public class DialogPicking extends AppCompatDialogFragment {
    private DialogListener listener;
    ImageView ivCloseDialog, ivPicking, ivPacking, ivSend;

    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final AlertDialog alertDialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_picking, null);
        ivCloseDialog =  view.findViewById(R.id.ivCloseDialog);
        ivPicking =  view.findViewById(R.id.ivPicking);
        ivPacking =  view.findViewById(R.id.ivPacking);
        ivSend =  view.findViewById(R.id.ivSend);

        builder.setView(view);
        alertDialog = builder.create();
        ivPicking.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.completeSelectPicking(1);
                        alertDialog.dismiss();
                    }
                }
        );
        ivPacking.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.completeSelectPicking(2);
                        alertDialog.dismiss();
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
        ivSend.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.completeSelectPicking(3);
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
        void completeSelectPicking(Integer path);
    }
}
