package com.example.zooseeker_team54;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ShowRouteAdapter extends RecyclerView.Adapter<ShowRouteAdapter.ViewHolder> {
    private List<LocItem> locItems = Collections.emptyList();

    public ShowRouteAdapter(){
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setLocItems(List<LocItem> newLocItems) {
        this.locItems.clear();
        this.locItems = newLocItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShowRouteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.route_plan_loc_item, parent, false);

        return new ShowRouteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralRecyclerAdapter.ViewHolder holder, int position) {
        ((ShowRouteAdapter.ViewHolder) holder).setItem(super.getItems().get(position));
    }

    public class ViewHolder extends GeneralRecyclerAdapter.ViewHolder {
        private TextView locNameText;
        private TextView distanceText;

        /**
         * Constructor method with a given View
         * @param itemView View to be used
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.locNameText = itemView.findViewById(R.id.loc_name);
            this.distanceText = itemView.findViewById(R.id.distance);
        }

        public void setItem(LocItem locItem) {
            super.setItem(locItem);
            this.locNameText.setText(locItem.name);
            this.distanceText.setText(locItem.getCurrDist());
        }
    }
}
