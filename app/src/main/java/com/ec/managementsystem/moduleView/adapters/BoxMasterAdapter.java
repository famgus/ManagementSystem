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
import com.ec.managementsystem.clases.BoxMaster;
import com.ec.managementsystem.interfaces.IListenerBoxMaster;

import java.util.List;


public class BoxMasterAdapter extends RecyclerView.Adapter<BoxMasterAdapter.PickingDetailViewHolder> {
    private List<BoxMaster> boxMasterItems;
    private IListenerBoxMaster listenerBoxMaster;

    public IListenerBoxMaster getListenerBoxMaster() {
        return listenerBoxMaster;
    }

    public void setListenerBoxMaster(IListenerBoxMaster listenerBoxMaster) {
        this.listenerBoxMaster = listenerBoxMaster;
    }

    public BoxMasterAdapter(List<BoxMaster> pedidoList) {
        this.boxMasterItems = pedidoList;
    }

    @NonNull
    @Override
    public BoxMasterAdapter.PickingDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item_boxmaster_detail;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PickingDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoxMasterAdapter.PickingDetailViewHolder holder, int position) {
        holder.bind(boxMasterItems.get(position), position, holder);
    }

    @Override
    public int getItemCount() {
        return boxMasterItems.size();
    }


    public void setPickingItems(List<BoxMaster> assets) {
        this.boxMasterItems = assets;
        notifyDataSetChanged();
    }

    public void animateTo(List<BoxMaster> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<BoxMaster> newModels) {
        for (int i = boxMasterItems.size() - 1; i >= 0; i--) {
            final BoxMaster model = boxMasterItems.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<BoxMaster> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final BoxMaster model = newModels.get(i);
            if (!boxMasterItems.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<BoxMaster> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final BoxMaster model = newModels.get(toPosition);
            final int fromPosition = boxMasterItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private BoxMaster removeItem(int position) {
        final BoxMaster model = boxMasterItems.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, BoxMaster model) {
        boxMasterItems.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final BoxMaster model = boxMasterItems.remove(fromPosition);
        boxMasterItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    class PickingDetailViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView tvNumber;
        private TextView tvDetail;
        private TextView tvQuantity;
        private ImageView ivAction;
        private ImageView ivRemove;
        private LinearLayout item_container;

        PickingDetailViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivAction = itemView.findViewById(R.id.ivAction);
            ivRemove = itemView.findViewById(R.id.ivRemove);
            item_container = itemView.findViewById(R.id.item_container);
        }

        void bind(final BoxMaster item, final int position, final PickingDetailViewHolder holder) {
            if (item != null && position == 0) {
                holder.tvNumber.setText("No.");
                holder.tvDetail.setText("Código de Barras");
                holder.tvQuantity.setText("Cantidad");
                holder.ivAction.setVisibility(View.INVISIBLE);
                holder.ivRemove.setVisibility(View.INVISIBLE);
            }else {
                if (item != null) {
                    holder.tvNumber.setText(String.valueOf(position));
                    holder.tvDetail.setText(item.getBarCode());
                    holder.tvQuantity.setText(String.valueOf(item.getCountProduct()));
                    holder.ivAction.setVisibility(View.VISIBLE);
                    holder.ivRemove.setVisibility(View.VISIBLE);
                    holder.item_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listenerBoxMaster != null) {
                                //listenerPicking.onItemClick(item);
                            }
                        }
                    });

                    holder.ivAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listenerBoxMaster != null) {
                                listenerBoxMaster.onItemActionClick(item);
                            }
                        }
                    });

                    holder.ivRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listenerBoxMaster != null) {
                                listenerBoxMaster.onItemRemoveClick(item);
                            }
                        }
                    });
                    if(item.isComplete()){
                        holder.ivAction.setImageResource(R.drawable.baseline_done_black_48);
                        holder.ivAction.setForegroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
                        holder.ivRemove.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

}
