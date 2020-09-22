package com.ec.managementsystem.moduleView.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.PendingTransferOrder;
import com.ec.managementsystem.interfaces.OnItemClickListener;

import java.util.List;

public class PendingTransferOrderAdapter extends RecyclerView.Adapter<PendingTransferOrderAdapter.TransferOrderViewHolder> {

    private List<PendingTransferOrder> pendingTransferOrders;
    OnItemClickListener<PendingTransferOrder> pendingTransferOrderOnItemClickListener;

    public PendingTransferOrderAdapter(List<PendingTransferOrder> pendingTransferOrders, OnItemClickListener<PendingTransferOrder> pendingTransferOrderOnItemClickListener) {
        this.pendingTransferOrders = pendingTransferOrders;
        this.pendingTransferOrderOnItemClickListener = pendingTransferOrderOnItemClickListener;
    }

    @NonNull
    @Override
    public TransferOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer_order, parent, false);
        return new TransferOrderViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransferOrderViewHolder holder, int position) {
        holder.bind(pendingTransferOrders.get(position), pendingTransferOrderOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return pendingTransferOrders.size();
    }

    static class TransferOrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvSeriesNumber, tvSeOrderNumber;
        ImageView ivItemTransferOrderAssigned;

        public TransferOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvItemTransOrdOrderNumber);
            tvSeriesNumber = itemView.findViewById(R.id.tvItemTransOrdSeriesNumber);
            tvSeOrderNumber = itemView.findViewById(R.id.tvItemTransfOrdOrderSeriesNumber);
            ivItemTransferOrderAssigned = itemView.findViewById(R.id.ivItemTransferOrderAssigned);
        }

        public void bind(final PendingTransferOrder pendingTransferOrder, final OnItemClickListener<PendingTransferOrder> pendingTransferOrderOnItemClickListener) {
            tvOrderNumber.setText(itemView.getContext().getString(R.string.itemtransferorder_order_number, pendingTransferOrder.getOrderNumber()));
            tvSeriesNumber.setText(itemView.getContext().getString(R.string.itemtransferorder_series_number, pendingTransferOrder.getSeriesNumber()));
            tvSeOrderNumber.setText(itemView.getContext().getString(R.string.itemtransferorder_series_order_number, pendingTransferOrder.getSupedidopc()));
            if (!pendingTransferOrder.isAssigned()) {
                ivItemTransferOrderAssigned.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pendingTransferOrderOnItemClickListener.onClick(pendingTransferOrder);
                    }
                });
            }else{
                ivItemTransferOrderAssigned.setEnabled(false);
                ivItemTransferOrderAssigned.setImageResource(R.drawable.profile_image);
            }
        }
    }
}
