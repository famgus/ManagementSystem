package com.ec.managementsystem.moduleView.adapters;

import android.graphics.Color;
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
import com.ec.managementsystem.clases.TransferSubOrder;
import com.ec.managementsystem.interfaces.OnItemClickListener;
import com.ec.managementsystem.util.Utils;

import java.util.List;

public class ProductToPrepareAdapter extends RecyclerView.Adapter<ProductToPrepareAdapter.ProductToPrepareVH> {

    private List<TransferSubOrder> productsToPrepare;
    private boolean isEditable = false;
    private OnItemClickListener<TransferSubOrder> clickListener;

    public ProductToPrepareAdapter(List<TransferSubOrder> productsToPrepare, OnItemClickListener<TransferSubOrder> clickListener) {
        this.productsToPrepare = productsToPrepare;
        this.clickListener = clickListener;
    }

    public void changeIsEditable() {
        isEditable = !isEditable;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductToPrepareVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_to_prepare, parent, false);
        return new ProductToPrepareVH(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductToPrepareVH holder, int position) {
        if (productsToPrepare.get(position).getRvItem() == Utils.HEADER_TYPE) {
            holder.bindHeader();
        } else {
            holder.bind(productsToPrepare.get(position), position + "");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return productsToPrepare.get(position).getRvItem();
    }

    @Override
    public int getItemCount() {
        return productsToPrepare.size();
    }

    class ProductToPrepareVH extends RecyclerView.ViewHolder {

        private TextView tvPosition, tvReference, tvUnits;
        private ImageView ivAction;
        private ConstraintLayout clIcon;

        public ProductToPrepareVH(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tv_producttoprepare_position);
            tvReference = itemView.findViewById(R.id.tv_producttoprepare_reference);
            tvUnits = itemView.findViewById(R.id.tv_producttoprepare_units);
            ivAction = itemView.findViewById(R.id.iv_producttoprepare);
            clIcon = itemView.findViewById(R.id.cl_producttoprepare);
        }

        private void changeHeaderColor(View... views) {
            for (View view : views) {
                view.setBackgroundResource(R.drawable.bg_header_table);
            }
        }

        private void bindHeader() {
            changeHeaderColor(tvPosition, tvReference, tvUnits, ivAction);
            tvPosition.setGravity(Gravity.CENTER);
            tvReference.setGravity(Gravity.CENTER);
            tvUnits.setGravity(Gravity.CENTER);
            ivAction.setVisibility(View.GONE);
            clIcon.setVisibility(isEditable ? View.VISIBLE : View.GONE);
        }

        private void bind(final TransferSubOrder productToPrepare, String position) {
            tvPosition.setText(position);
            tvReference.setText(productToPrepare.getReference());
            tvUnits.setText(String.valueOf(productToPrepare.getRequestedUnits()));
            ivAction.setVisibility(isEditable ? View.VISIBLE : View.GONE);
            clIcon.setVisibility(isEditable ? View.VISIBLE : View.GONE);
            ivAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(productToPrepare);
                }
            });
            if (productToPrepare.isRegistered() && productToPrepare.isCompleted()) {
                ivAction.setImageResource(R.drawable.baseline_check_circle_outline_white_48);
                int color = Color.parseColor("#000000");
                ivAction.setColorFilter(color);
            } else if(productToPrepare.isRegistered()){
                ivAction.setImageResource(R.drawable.baseline_query_builder_white_48);
                int color = Color.parseColor("#000000");
                ivAction.setColorFilter(color);
            } else {
                ivAction.setImageResource(R.drawable.recibir_mercaderia);
            }
            ivAction.setEnabled(!(productToPrepare.isRegistered() && productToPrepare.isCompleted()));
        }
    }
}
