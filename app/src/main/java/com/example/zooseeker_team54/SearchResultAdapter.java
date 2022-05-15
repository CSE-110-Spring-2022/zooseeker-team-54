package com.example.zooseeker_team54;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<LocItem> locItems = Collections.emptyList();
    private Consumer<LocItem> onSearchResultClicked;

    public SearchResultAdapter(){
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

    public void setOnSearchResultClicked(Consumer<LocItem> onSearchResultClicked) {
        this.onSearchResultClicked = onSearchResultClicked;
    }

    public List<LocItem> getLocItems() { return locItems; }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.undeletable_loc_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralRecyclerAdapter.ViewHolder holder, int position) {
        ((SearchResultAdapter.ViewHolder) holder).setItem(super.getItems().get(position));
    }

    public List<LocItem> getItems() { return super.getItems(); }

    public Consumer<LocItem> getItemOnClickListener() { return super.getItemOnClickListener(); }


    public class ViewHolder extends GeneralRecyclerAdapter.ViewHolder {
        private TextView locNameText;

        /**
         * Constructor for ViewHolder with a given View
         * @param itemView given View to be used
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.locNameText = itemView.findViewById(R.id.loc_name);

            this.locNameText.setOnClickListener(view -> {

                LocItem locItem = (LocItem) super.getItem();
                getItems().remove(locItem);
                notifyDataSetChanged();

                Consumer<LocItem> itemOnClickListener = getItemOnClickListener();
                if (itemOnClickListener== null) return;
                itemOnClickListener.accept(locItem);

            });
        }

        public void setItem(LocItem locItem) {
            super.setItem(locItem);
            this.locNameText.setText(locItem.name);
        }
    }
}
