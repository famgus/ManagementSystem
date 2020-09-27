package com.ec.managementsystem.moduleView.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.TransferPackOff;
import com.ec.managementsystem.interfaces.OnItemClickListener;
import com.ec.managementsystem.util.Utils;

import java.util.List;

public class PackOffTransferAdapter extends RecyclerView.Adapter<PackOffTransferAdapter.PackOffTransferVH> {

    private List<TransferPackOff> transferPackOffList;
    private OnItemClickListener<TransferPackOff> checkBoxListener;

    public PackOffTransferAdapter(List<TransferPackOff> transferPackOffList, OnItemClickListener<TransferPackOff> checkBoxListener) {
        this.transferPackOffList = transferPackOffList;
        this.checkBoxListener = checkBoxListener;
    }

    @NonNull
    @Override
    public PackOffTransferVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_packoff_transfer, parent, false);
        return new PackOffTransferVH(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull PackOffTransferVH holder, int position) {
        if(transferPackOffList.get(position).getRvType() == Utils.ITEM_TYPE){
            holder.bind(transferPackOffList.get(position), checkBoxListener);
        }else{
            holder.bindHeader();
        }
    }

    public int getItemViewType(int position) {
        return transferPackOffList.get(position).getRvType();
    }

    @Override
    public int getItemCount() {
        return transferPackOffList.size();
    }

    static class PackOffTransferVH extends RecyclerView.ViewHolder{
        private TextView tvBoxMaster, tvRequiredQuantity, tvPackOff;
        CheckBox chPackOffTransfer;
        ConstraintLayout clWrapperQuantity;

        public PackOffTransferVH(@NonNull View itemView) {
            super(itemView);
            tvBoxMaster = itemView.findViewById(R.id.tv_itempackofftransfer_box);
            tvRequiredQuantity = itemView.findViewById(R.id.tv_itempackofftransfer_quantity);
            tvPackOff = itemView.findViewById(R.id.tv_itempackofftransfer_quantity_to_packoff);
            chPackOffTransfer = itemView.findViewById(R.id.cb_itempackoff);
            clWrapperQuantity = itemView.findViewById(R.id.cl_itempackoff_wrapper_quantity);
        }

        private void changeHeaderColor(View... views) {
            for (View view : views) {
                view.setBackgroundResource(R.drawable.bg_header_table);
            }
        }

        private void bindHeader(){
            changeHeaderColor(tvBoxMaster, tvRequiredQuantity, clWrapperQuantity);
            chPackOffTransfer.setVisibility(View.GONE);
            tvPackOff.setVisibility(View.VISIBLE);
        }


        private void bind(final TransferPackOff transferPackOff, final OnItemClickListener<TransferPackOff> listener){

            tvBoxMaster.setText(transferPackOff.getBoxMasterBarCode());
            tvRequiredQuantity.setText(String.valueOf(transferPackOff.getQuantityProductsInBox()));
            chPackOffTransfer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    transferPackOff.setChecked(isChecked);
                    listener.onClick(transferPackOff);
                }
            });

        }
    }
}
