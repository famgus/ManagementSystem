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
import com.ec.managementsystem.clases.responses.PedidoPickingResponse;
import com.ec.managementsystem.interfaces.IListenerSendPicking;

import java.util.List;


public class SendPickingAdapter extends RecyclerView.Adapter<SendPickingAdapter.PickingViewHolder> {
    private List<PedidoPickingResponse> pickingItems;
    private IListenerSendPicking listenerPicking;

    public IListenerSendPicking getListenerPicking() {
        return listenerPicking;
    }

    public void setListenerPicking(IListenerSendPicking listenerPicking) {
        this.listenerPicking = listenerPicking;
    }

    public SendPickingAdapter(List<PedidoPickingResponse> assetList) {
        this.pickingItems = assetList;
    }

    @NonNull
    @Override
    public SendPickingAdapter.PickingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_picking;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PickingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SendPickingAdapter.PickingViewHolder holder, int position) {
        holder.bind(pickingItems.get(position), position, holder);
    }

    @Override
    public int getItemCount() {
        return pickingItems.size();
    }


    public void setPickingItems(List<PedidoPickingResponse> assets) {
        this.pickingItems = assets;
        notifyDataSetChanged();
    }

    public void animateTo(List<PedidoPickingResponse> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<PedidoPickingResponse> newModels) {
        for (int i = pickingItems.size() - 1; i >= 0; i--) {
            final PedidoPickingResponse model = pickingItems.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<PedidoPickingResponse> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final PedidoPickingResponse model = newModels.get(i);
            if (!pickingItems.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<PedidoPickingResponse> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final PedidoPickingResponse model = newModels.get(toPosition);
            final int fromPosition = pickingItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private PedidoPickingResponse removeItem(int position) {
        final PedidoPickingResponse model = pickingItems.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, PedidoPickingResponse model) {
        pickingItems.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final PedidoPickingResponse model = pickingItems.remove(fromPosition);
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

        void bind(final PedidoPickingResponse item, final int position, final PickingViewHolder holder) {
            if (item != null) {
                holder.tvNumberPedido.setText(item.getNumberPedido());
                holder.tvNameClient.setText(item.getNameClient());
                holder.tvDatePedido.setText(item.getDatePedido());
                holder.tvDatePicking.setText(item.getDatePicking());
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
