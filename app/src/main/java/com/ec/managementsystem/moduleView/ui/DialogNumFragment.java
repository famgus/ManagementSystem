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
        import androidx.appcompat.app.AppCompatDialogFragment;

        import com.ec.managementsystem.R;


public class DialogNumFragment extends AppCompatDialogFragment {

    private DialogListenerFraccionar listener;
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState){
        final androidx.appcompat.app.AlertDialog alertDialog;
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialognumfragment, null);
        LinearLayout btnsearch = (LinearLayout) view.findViewById(R.id.llSearchcolaboradores);
        ImageView btnclose = (ImageView) view.findViewById(R.id.btncnl_dialog_numerofrag);
        final EditText orden = (EditText) view.findViewById(R.id.etnofrag);

        builder.setView(view);
        alertDialog = builder.create();
        btnsearch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Search
                        String numberpurchase =orden.getText().toString();
                        if(!numberpurchase.equals("")) {
                            listener.applyTexts(numberpurchase);
                            alertDialog.dismiss();
                        }
                        else{

                            Toast ToastGravity =  Toast.makeText(alertDialog.getContext(), "Ingrese un número de cantidad de colaboradores", Toast.LENGTH_SHORT);
                            ToastGravity.setGravity(Gravity.CENTER,0,0);
                            ToastGravity.show();
                        }

                    }
                }
        );

        btnclose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
            listener = (DialogListenerFraccionar) context;
        }
        catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "must implement DialogListenerFraccionar");
        }
    }

    public interface DialogListenerFraccionar{
        void applyTexts(String numcompra);
    }
}
