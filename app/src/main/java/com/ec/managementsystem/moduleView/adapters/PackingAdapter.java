package com.ec.managementsystem.moduleView.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.FacturaModel;
import com.ec.managementsystem.interfaces.IListenerPacking;

import java.util.List;


public class PackingAdapter extends RecyclerView.Adapter<PackingAdapter.FacturaViewHolder> {
    private List<FacturaModel> pickingItems;
    private IListenerPacking listenerPacking;
    private boolean isSendView = false;

    public IListenerPacking getListenerPacking() {
        return listenerPacking;
    }

    public void setListenerPacking(IListenerPacking listenerPacking) {
        this.listenerPacking = listenerPacking;
    }

    public boolean isSendView() {
        return isSendView;
    }

    public void setSendView(boolean sendView) {
        isSendView = sendView;
    }

    public PackingAdapter(List<FacturaModel> assetList) {
        this.pickingItems = assetList;
    }

    @NonNull
    @Override
    public FacturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_facturas;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new FacturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturaViewHolder holder, int position) {
        holder.bind(pickingItems.get(position), position, holder);
    }

    @Override
    public int getItemCount() {
        return pickingItems.size();
    }


    public void setPickingItems(List<FacturaModel> assets) {
        this.pickingItems = assets;
        notifyDataSetChanged();
    }

    public void animateTo(List<FacturaModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<FacturaModel> newModels) {
        for (int i = pickingItems.size() - 1; i >= 0; i--) {
            final FacturaModel model = pickingItems.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<FacturaModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final FacturaModel model = newModels.get(i);
            if (!pickingItems.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<FacturaModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final FacturaModel model = newModels.get(toPosition);
            final int fromPosition = pickingItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private FacturaModel removeItem(int position) {
        final FacturaModel model = pickingItems.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, FacturaModel model) {
        pickingItems.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final FacturaModel model = pickingItems.remove(fromPosition);
        pickingItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    class FacturaViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tvNumberFactura;
        private TextView tvDateFactura;
        private LinearLayout item_container;
        private CheckBox chChecked;

        FacturaViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvNumberFactura = itemView.findViewById(R.id.tvNumberFactura);
            tvDateFactura = itemView.findViewById(R.id.tvNumberSerie);
            item_container = itemView.findViewById(R.id.item_container);
            chChecked = itemView.findViewById(R.id.chChecked);
            if (isSendView) {
                chChecked.setVisibility(View.VISIBLE);
            } else {
                chChecked.setVisibility(View.GONE);
            }
        }

        void bind(final FacturaModel item, final int position, final FacturaViewHolder holder) {
            if (item != null) {
                holder.tvNumberFactura.setText(String.valueOf(item.getNumberFactura()));
                holder.tvDateFactura.setText(String.valueOf(item.getNumberSerie()));
                holder.item_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listenerPacking != null && !item.isComplete()) {
                            listenerPacking.onItemClick(item);
                        }else {
                            Toast.makeText(context, "Factura registrada", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                holder.chChecked.setChecked(item.isCkecked());
            }
        }
    }

}
