package com.ec.managementsystem.moduleView.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ec.managementsystem.R;
import com.ec.managementsystem.clases.responses.PickingPedidoUserResponse;
import com.ec.managementsystem.interfaces.IListenerPicking;

import java.util.List;


public class PickingAdapter extends RecyclerView.Adapter<PickingAdapter.PickingViewHolder> {
    private List<PickingPedidoUserResponse> pickingItems;
    private IListenerPicking listenerPicking;

    public IListenerPicking getListenerPicking() {
        return listenerPicking;
    }

    public void setListenerPicking(IListenerPicking listenerPicking) {
        this.listenerPicking = listenerPicking;
    }

    public PickingAdapter(List<PickingPedidoUserResponse> assetList) {
        this.pickingItems = assetList;
    }

    @NonNull
    @Override
    public PickingAdapter.PickingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_picking;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PickingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickingAdapter.PickingViewHolder holder, int position) {
        holder.bind(pickingItems.get(position), position, holder);
    }

    @Override
    public int getItemCount() {
        return pickingItems.size();
    }


    public void setPickingItems(List<PickingPedidoUserResponse> assets) {
        this.pickingItems = assets;
        notifyDataSetChanged();
    }

    public void animateTo(List<PickingPedidoUserResponse> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<PickingPedidoUserResponse> newModels) {
        for (int i = pickingItems.size() - 1; i >= 0; i--) {
            final PickingPedidoUserResponse model = pickingItems.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<PickingPedidoUserResponse> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final PickingPedidoUserResponse model = newModels.get(i);
            if (!pickingItems.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<PickingPedidoUserResponse> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final PickingPedidoUserResponse model = newModels.get(toPosition);
            final int fromPosition = pickingItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private PickingPedidoUserResponse removeItem(int position) {
        final PickingPedidoUserResponse model = pickingItems.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, PickingPedidoUserResponse model) {
        pickingItems.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final PickingPedidoUserResponse model = pickingItems.remove(fromPosition);
        pickingItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    class PickingViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tvNameClient;
        private TextView tvDatePicking;
        private TextView tvDatePedido;
        private TextView tvNumberPedido;
        private LinearLayout item_container;

        PickingViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvDatePicking = itemView.findViewById(R.id.tvDatePicking);
            tvDatePedido = itemView.findViewById(R.id.tvDatePedido);
            tvNameClient = itemView.findViewById(R.id.tvNameClient);
            tvNumberPedido = itemView.findViewById(R.id.tvNumberPedido);
            item_container = itemView.findViewById(R.id.item_container);
        }

        void bind(final PickingPedidoUserResponse item, final int position, final PickingViewHolder holder) {
            if (item != null) {
                holder.tvNumberPedido.setText(String.valueOf(item.getNumberPedido()));
                holder.tvNameClient.setText(String.valueOf(item.getNameClient()));
                holder.tvDatePedido.setText(String.valueOf(item.getFechaPedido()));
                holder.tvDatePicking.setText(String.valueOf(item.getFechaPedido()));
                holder.item_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listenerPicking != null) {
                            listenerPicking.onItemClick(item);
                        }
                    }
                });
            }
        }
    }

}
