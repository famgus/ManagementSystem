package com.ec.managementsystem.moduleView.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.AssignedTransferOrder;
import com.ec.managementsystem.interfaces.OnItemClickListener;

import java.util.List;

public class AssignedPendingOrdersAdapter extends RecyclerView.Adapter<AssignedPendingOrdersAdapter.AssignedPendingOrdersVH> {

    private List<AssignedTransferOrder> assignedPendingOrders;
    private OnItemClickListener<AssignedTransferOrder> onItemClickListener;

    public AssignedPendingOrdersAdapter(List<AssignedTransferOrder> assignedPendingOrders, OnItemClickListener<AssignedTransferOrder> onItemClickListener) {
        this.assignedPendingOrders = assignedPendingOrders;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AssignedPendingOrdersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer_order, parent, false);
        return new AssignedPendingOrdersVH(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignedPendingOrdersVH holder, int position) {
        holder.bind(assignedPendingOrders.get(position));
    }

    @Override
    public int getItemCount() {
        return assignedPendingOrders == null ? 0 : assignedPendingOrders.size();
    }

    class AssignedPendingOrdersVH extends RecyclerView.ViewHolder {

        TextView tvOrderNumber, tvSeriesNumber, tvSeOrderNumber;

        public AssignedPendingOrdersVH(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tvItemTransOrdOrderNumber);
            tvSeriesNumber = itemView.findViewById(R.id.tvItemTransOrdSeriesNumber);
            tvSeOrderNumber = itemView.findViewById(R.id.tvItemTransfOrdOrderSeriesNumber);
            ImageView ivItemTransferOrderAssigned = itemView.findViewById(R.id.ivItemTransferOrderAssigned);
            ivItemTransferOrderAssigned.setVisibility(View.GONE);
        }

        private void bind(final AssignedTransferOrder assignedPendingOrder) {
            tvOrderNumber.setText(itemView.getContext().getString(R.string.itemtransferorder_order_number, assignedPendingOrder.getOrderNumber()));
            tvSeriesNumber.setText(itemView.getContext().getString(R.string.itemtransferorder_series_number, assignedPendingOrder.getSeriesNumber()));
            tvSeOrderNumber.setText(itemView.getContext().getString(R.string.assignedpendingoreder_vendor_code, assignedPendingOrder.getVendorCode()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(assignedPendingOrder);
                }
            });
        }
    }
}
