package com.ec.managementsystem.moduleView.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.DispatchedProduct;
import com.ec.managementsystem.interfaces.OnItemClickListener;
import com.ec.managementsystem.util.Utils;

import java.util.List;

import static com.ec.managementsystem.util.Utils.changeHeaderColor;

public class DispatchedProductsAdapter extends RecyclerView.Adapter<DispatchedProductsAdapter.DispatchedProductsVH> {

    private List<DispatchedProduct> dispatchedProducts;
    private OnItemClickListener<DispatchedProduct> listener;
    private boolean isEditable = false;

    public DispatchedProductsAdapter(List<DispatchedProduct> dispatchedProducts, OnItemClickListener<DispatchedProduct> listener) {
        this.dispatchedProducts = dispatchedProducts;
        this.listener = listener;
    }

    public void changeIsEditable() {
        isEditable = !isEditable;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DispatchedProductsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_to_prepare, parent, false);
        return new DispatchedProductsVH(rootView);
    }

    @Override
    public int getItemViewType(int position) {
        return dispatchedProducts.get(position).getRvType();
    }

    @Override
    public void onBindViewHolder(@NonNull DispatchedProductsVH holder, int position) {
        if (dispatchedProducts.get(position).getRvType() == Utils.ITEM_TYPE) {
            holder.bind(dispatchedProducts.get(position), String.valueOf(position));
        } else {
            holder.bindHeader();
        }
    }

    @Override
    public int getItemCount() {
        return dispatchedProducts.size();
    }

    class DispatchedProductsVH extends RecyclerView.ViewHolder {

        private TextView tvPosition, tvReference, tvUnits, tvFourth, tvFifth, tvSixth;
        private ImageView ivAction;
        private ConstraintLayout clIcon;


        public DispatchedProductsVH(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tv_producttoprepare_position);
            tvReference = itemView.findViewById(R.id.tv_producttoprepare_reference);
            tvUnits = itemView.findViewById(R.id.tv_producttoprepare_units);
            tvFourth = itemView.findViewById(R.id.tv_producttoprepare_fourth);
            tvFifth = itemView.findViewById(R.id.tv_producttoprepare_fifth);
            tvSixth = itemView.findViewById(R.id.tv_producttoprepare_sixth);
            ivAction = itemView.findViewById(R.id.iv_producttoprepare);
            clIcon = itemView.findViewById(R.id.cl_producttoprepare);
            tvFourth.setVisibility(View.VISIBLE);
            tvFifth.setVisibility(View.VISIBLE);
            tvSixth.setVisibility(View.VISIBLE);
            tvPosition.setVisibility(View.GONE);
        }

        private void bindHeader() {
            changeHeaderColor(tvPosition, tvReference, tvUnits, clIcon, tvFourth, tvFifth, tvSixth);
            tvReference.setText(R.string.header_reference);
            tvUnits.setText(R.string.header_model);
            tvFourth.setText("Caja traslado");
            tvFifth.setText(R.string.header_total_dispatched);
            tvSixth.setText(R.string.header_total_received);
            clIcon.setVisibility(isEditable ? View.VISIBLE : View.GONE);
            ivAction.setVisibility(View.GONE);
        }

        private void bind(final DispatchedProduct dispatchedProduct, String position) {
            tvReference.setText(String.valueOf(dispatchedProduct.getProductCode()));
            tvUnits.setText(itemView.getContext().getString(R.string.all_model, dispatchedProduct.getSize(), dispatchedProduct.getColor()));
            tvFourth.setText(dispatchedProduct.getCajaTraslado());
            tvFifth.setText(String.valueOf(dispatchedProduct.getTotalDispatched()));
            tvSixth.setText(String.valueOf(dispatchedProduct.getTotalReceived()));
            ivAction.setVisibility(isEditable ? View.VISIBLE : View.GONE);
            clIcon.setVisibility(isEditable ? View.VISIBLE : View.GONE);
            ivAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(dispatchedProduct);
                }
            });
            if (dispatchedProduct.isRegistered()) {
                ivAction.setImageResource(R.drawable.baseline_check_circle_outline_white_48);
                int color = Color.parseColor("#000000");
                ivAction.setColorFilter(color);
            } else {
                ivAction.setImageResource(R.drawable.recibir_mercaderia);
            }
            ivAction.setEnabled(!dispatchedProduct.isRegistered());
        }
    }
}
