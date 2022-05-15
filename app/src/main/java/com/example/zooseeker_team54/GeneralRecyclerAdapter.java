package com.example.zooseeker_team54;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class GeneralRecyclerAdapter<T> extends RecyclerView.Adapter<GeneralRecyclerAdapter.ViewHolder> {
    private List<T> items = Collections.emptyList();
    private Consumer<T> itemOnClickListener;

    public List<T> getItems() { return items; }

    public void setItems(List<T> newItems) {
        this.items.clear();
        this.items = newItems;
        notifyDataSetChanged();
    }

    public void setItemOnClickListener(Consumer<T> itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public Consumer<T> getItemOnClickListener() { return itemOnClickListener; }

    @NonNull
    @Override
    public abstract GeneralRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(@NonNull GeneralRecyclerAdapter.ViewHolder holder, int position);

    @Override
    public int getItemCount() { return items.size(); }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        private T item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(T item) {
            this.item = item;
        }

        public T getItem() { return item; }
    }
}
