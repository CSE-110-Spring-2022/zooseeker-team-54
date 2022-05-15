package com.example.zooseeker_team54;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class RouteDirectionAdapter extends GeneralRecyclerAdapter<LocEdge> {

    public RouteDirectionAdapter(){ super(); }

    @NonNull
    @Override
    public RouteDirectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.loc_edge, parent, false);
        return new RouteDirectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralRecyclerAdapter.ViewHolder holder, int position) {
        ((RouteDirectionAdapter.ViewHolder) holder).setItem(getItems().get(position));
    }

    public class ViewHolder extends GeneralRecyclerAdapter.ViewHolder{
        private TextView directionGuideText;

        /**
         * Constructor for ViewHolder
         * @param itemView given View to be used
         */
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            this.directionGuideText = itemView.findViewById(R.id.direction);
        }

        public void setItem(LocEdge locEdge){
            super.setItem(locEdge);
            this.directionGuideText.setText(locEdge.toString());
        }
    }
}
