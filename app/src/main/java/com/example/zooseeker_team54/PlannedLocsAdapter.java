package com.example.zooseeker_team54;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public class PlannedLocsAdapter extends GeneralRecyclerAdapter<LocItem> {

    @NonNull
    @Override
    public PlannedLocsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.deletable_loc_item, parent, false);

        return new PlannedLocsAdapter.ViewHolder(view);
    }

    /**
     * Sets a given LocItem from a ViewHolder to a given position
     * @param holder ViewHolder passed in
     * @param position int position to be placed at
     */
    @Override
    public void onBindViewHolder(@NonNull GeneralRecyclerAdapter.ViewHolder holder, int position) {
        ((PlannedLocsAdapter.ViewHolder) holder).setItem(super.getItems().get(position));
    }

    public class ViewHolder extends GeneralRecyclerAdapter.ViewHolder {
        private TextView locNameText;
        private TextView delView;

        /**
         * ViewHolder constructor method from a given View
         * @param itemView View to be used
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.locNameText = itemView.findViewById(R.id.loc_name);
            this.delView = itemView.findViewById(R.id.loc_delete_selected);

            this.delView.setOnClickListener(view ->{
                Consumer<LocItem> itemOnDeleteListener = getItemOnDeleteListener();
                if(itemOnDeleteListener == null) return;
                LocItem locItem = (LocItem) super.getItem();
                itemOnDeleteListener.accept(locItem);
            });
        }

        public void setItem(LocItem locItem) {
            super.setItem(locItem);
            this.locNameText.setText(locItem.name);
        }
    }
}
