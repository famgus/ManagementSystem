package com.ec.managementsystem.moduleView.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.Vendedores;

import java.util.List;

public class AssignUserPendingOrderAdapter extends RecyclerView.Adapter<AssignUserPendingOrderAdapter.AssignUserPendingOrderVH> {

    private List<Vendedores> vendors;

    public AssignUserPendingOrderAdapter(List<Vendedores> vendors) {
        this.vendors = vendors;
    }

    @NonNull
    @Override
    public AssignUserPendingOrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_assign_pending_order, parent, false);
        return new AssignUserPendingOrderVH(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignUserPendingOrderVH holder, int position) {
        holder.bind(vendors.get(position));
    }

    @Override
    public int getItemCount() {
        return vendors.size();
    }

    class AssignUserPendingOrderVH extends RecyclerView.ViewHolder{

        TextView tvItemUserAssignName;
        CheckBox cbItemUserAssign;

        public AssignUserPendingOrderVH(@NonNull View itemView) {
            super(itemView);
            tvItemUserAssignName = itemView.findViewById(R.id.tvItemUserAssingName);
            cbItemUserAssign = itemView.findViewById(R.id.cbItemUserAssing);
        }

        public void bind(final Vendedores vendor){
            tvItemUserAssignName.setText(vendor.getNomvendedor());
            cbItemUserAssign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    vendor.setSelected(isChecked);
                }
            });
        }
    }
}
