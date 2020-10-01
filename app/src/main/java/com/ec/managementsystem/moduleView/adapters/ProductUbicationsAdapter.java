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
import com.ec.managementsystem.clases.ProductUbication;
import com.ec.managementsystem.util.Utils;

import java.util.List;

/**
 * Created by Gorky Muñoz on 20/9/2020.
 * Indra
 * gamunozg@indracompany.com
 */
public class ProductUbicationsAdapter extends RecyclerView.Adapter<ProductUbicationsAdapter.ProductUbicationsVH> {

    private List<ProductUbication> productsUbications;

    public ProductUbicationsAdapter(List<ProductUbication> productsUbications) {
        this.productsUbications = productsUbications;
    }

    @NonNull
    @Override
    public ProductUbicationsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_to_prepare, parent, false);
        return new ProductUbicationsVH(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductUbicationsVH holder, int position) {
        if (productsUbications.get(position).getRvItem() == Utils.HEADER_TYPE) {
            holder.bindHeader();
        } else {
            holder.bind(productsUbications.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return productsUbications.get(position).getRvItem();
    }

    @Override
    public int getItemCount() {
        return productsUbications.size();
    }

    class ProductUbicationsVH extends RecyclerView.ViewHolder {

        private TextView tvReference;
        private TextView tvUnits;

        public ProductUbicationsVH(@NonNull View itemView) {
            super(itemView);
            TextView tvPosition = itemView.findViewById(R.id.tv_producttoprepare_position);
            tvReference = itemView.findViewById(R.id.tv_producttoprepare_reference);
            tvUnits = itemView.findViewById(R.id.tv_producttoprepare_units);
            ImageView ivAction = itemView.findViewById(R.id.iv_producttoprepare);
            ConstraintLayout clIcon = itemView.findViewById(R.id.cl_producttoprepare);
            hideViews(tvPosition, ivAction, clIcon);
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
            changeHeaderColor(tvReference, tvUnits);
            tvUnits.setGravity(Gravity.CENTER);
            tvReference.setGravity(Gravity.CENTER);
            tvReference.setText("Código ubicación");
            tvUnits.setText("Código caja master");
        }

        private void bind(final ProductUbication productToPrepare) {
            tvReference.setText(productToPrepare.getUbicationCode());
            tvUnits.setText(String.valueOf(productToPrepare.getBoxMasterBarCode()));
        }
    }
}
