package com.ec.managementsystem.moduleView.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.GuideModel;

import java.util.List;

public class CustomerInvoicesForSendAdapter extends RecyclerView.Adapter<CustomerInvoicesForSendAdapter.CustomerInvoicesForSendVH> {

    private List<GuideModel> invoices;

    public CustomerInvoicesForSendAdapter(List<GuideModel> invoices) {
        this.invoices = invoices;
    }

    public void updateData(List<GuideModel> dataChanged){
        invoices.clear();
        invoices.addAll(dataChanged);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerInvoicesForSendVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facturas, parent, false);
        return new CustomerInvoicesForSendVH(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerInvoicesForSendVH holder, int position) {
        holder.bind(invoices.get(position));
    }

    @Override
    public int getItemCount() {
        return invoices != null ? invoices.size() : 0;
    }

    static class CustomerInvoicesForSendVH extends RecyclerView.ViewHolder {
        private TextView tvBillNumber;
        private TextView tvSeriesNumber;
        private CheckBox chChecked;
        public CustomerInvoicesForSendVH(@NonNull View itemView) {
            super(itemView);
            tvBillNumber = itemView.findViewById(R.id.tvNumberFactura);
            tvSeriesNumber = itemView.findViewById(R.id.tvNumberSerie);
            chChecked = itemView.findViewById(R.id.chChecked);
        }

        public void bind(GuideModel currentGuide){
            chChecked.setEnabled(false);
            tvBillNumber.setText(String.valueOf(currentGuide.getBillNumber()));
            tvSeriesNumber.setText(currentGuide.getSeriesNumber());
            chChecked.setChecked(currentGuide.isVerified());
        }
    }

}
