package com.ec.managementsystem.moduleView.adapters;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.BoxTransferPendingOrder;
import com.ec.managementsystem.util.Utils;

import java.util.List;

/**
 * Created by Gorky Muñoz on 21/9/2020.
 * Indra
 * gamunozg@indracompany.com
 */
public class BoxTransferPendingOrdersAdapter extends RecyclerView.Adapter<BoxTransferPendingOrdersAdapter.BoxTransferPendingOrdersVH> {

    List<BoxTransferPendingOrder> boxTransferPendingOrders;

    public BoxTransferPendingOrdersAdapter(List<BoxTransferPendingOrder> boxTransferPendingOrders) {
        this.boxTransferPendingOrders = boxTransferPendingOrders;
    }

    @NonNull
    @Override
    public BoxTransferPendingOrdersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_to_prepare, parent, false);
        return new BoxTransferPendingOrdersVH(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoxTransferPendingOrdersVH holder, int position) {
        if (boxTransferPendingOrders.get(position).getRvType() == Utils.HEADER_TYPE) {
            holder.bindHeader();
        } else {
            holder.bind(boxTransferPendingOrders.get(position), position + "");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return boxTransferPendingOrders.get(position).getRvType();
    }

    @Override
    public int getItemCount() {
        return boxTransferPendingOrders.size();
    }

    class BoxTransferPendingOrdersVH extends RecyclerView.ViewHolder {

        private TextView tvReference;
        private TextView tvUnits;
        private final TextView tvPosition;

        public BoxTransferPendingOrdersVH(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tv_producttoprepare_position);
            tvReference = itemView.findViewById(R.id.tv_producttoprepare_reference);
            tvUnits = itemView.findViewById(R.id.tv_producttoprepare_units);
            ImageView ivAction = itemView.findViewById(R.id.iv_producttoprepare);
            ConstraintLayout clIcon = itemView.findViewById(R.id.cl_producttoprepare);
            hideViews(ivAction, clIcon);
        }

        private void hideViews(View... views) {
            for (View view : views) {
                view.setVisibility(View.GONE);
            }
        }

        private void changeHeaderColor(View... views) {
            for (View view : views) {
                view.setBackgroundResource(R.drawable.bg_header_table);
            }
        }

        private void bindHeader() {
            changeHeaderColor(tvPosition, tvReference, tvUnits);
            tvUnits.setGravity(Gravity.CENTER);
            tvReference.setGravity(Gravity.CENTER);
            tvReference.setText("Código caja master");
            tvUnits.setText("Cantidad artículos");
        }

        private void bind(final BoxTransferPendingOrder productToPrepare, String index) {
            tvPosition.setText(index);
            tvReference.setText(productToPrepare.getBoxCode());
            tvUnits.setText(String.valueOf(productToPrepare.getTotalRegisteredProducts()));
        }
    }
}
