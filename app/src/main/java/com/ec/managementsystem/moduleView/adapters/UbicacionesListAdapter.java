package com.ec.managementsystem.moduleView.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.LocationDetail;
import com.ec.managementsystem.interfaces.IListenerPackingDetail;
import com.ec.managementsystem.interfaces.IListenerUbicaciones;

import java.util.List;


public class UbicacionesListAdapter extends RecyclerView.Adapter<UbicacionesListAdapter.PickingDetailViewHolder> {
    private List<LocationDetail> pedidoItems;
    private IListenerUbicaciones listener;

    public IListenerUbicaciones getListener() {
        return listener;
    }

    public void setListener(IListenerUbicaciones listener) {
        this.listener = listener;
    }

    public UbicacionesListAdapter(List<LocationDetail> pedidoList) {
        this.pedidoItems = pedidoList;
    }

    @NonNull
    @Override
    public UbicacionesListAdapter.PickingDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_ubicacion;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PickingDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UbicacionesListAdapter.PickingDetailViewHolder holder, int position) {
        holder.bind(pedidoItems.get(position), position, holder);
    }

    @Override
    public int getItemCount() {
        return pedidoItems.size();
    }


    public void setPickingItems(List<LocationDetail> assets) {
        this.pedidoItems = assets;
        notifyDataSetChanged();
    }

    public void animateTo(List<LocationDetail> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<LocationDetail> newModels) {
        for (int i = pedidoItems.size() - 1; i >= 0; i--) {
            final LocationDetail model = pedidoItems.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<LocationDetail> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final LocationDetail model = newModels.get(i);
            if (!pedidoItems.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<LocationDetail> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final LocationDetail model = newModels.get(toPosition);
            final int fromPosition = pedidoItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private LocationDetail removeItem(int position) {
        final LocationDetail model = pedidoItems.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, LocationDetail model) {
        pedidoItems.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final LocationDetail model = pedidoItems.remove(fromPosition);
        pedidoItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    class PickingDetailViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tvNumber;
        private TextView tvCodeLocation;
        private TextView tvCodeBarMasterBox;
        private TextView tvQuantity;
        private LinearLayout item_container;

        PickingDetailViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvCodeLocation = itemView.findViewById(R.id.tvCodeLocation);
            tvCodeBarMasterBox = itemView.findViewById(R.id.tvCodeBarMasterBox);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            item_container = itemView.findViewById(R.id.item_container);
        }

        void bind(final LocationDetail item, final int position, final PickingDetailViewHolder holder) {
            if (item != null && position == 0) {
                holder.tvNumber.setText("No.");
                holder.tvCodeLocation.setText("Ubicación");
                holder.tvCodeBarMasterBox.setText("Caja Master");
                holder.tvQuantity.setText("Cantidad");
                holder.tvQuantity.setVisibility(View.GONE);
            } else {
                if (item != null) {
                    holder.tvNumber.setText(String.valueOf(position));
                    holder.tvCodeLocation.setText(item.codubicacion);
                    holder.tvCodeBarMasterBox.setText(item.codbarrascajamaster);
                    holder.tvQuantity.setText(String.valueOf(item.cantidadarticulo));
                    holder.tvQuantity.setVisibility(View.GONE);
                    holder.item_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                //listenerPicking.onItemClick(item);
                            }
                        }
                    });
                }
            }
        }
    }

}
