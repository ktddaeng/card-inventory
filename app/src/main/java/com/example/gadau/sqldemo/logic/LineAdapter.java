package com.example.gadau.sqldemo.logic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.gadau.sqldemo.data.DataItem;
import com.example.gadau.sqldemo.view.FragListItem;
import com.example.gadau.sqldemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gadau on 8/2/2017.
 */

public class LineAdapter extends RecyclerView.Adapter<FragListItem> {
    private final List<DataItem> inventory;

    public LineAdapter(List<DataItem> inventory) {
        this.inventory = inventory;
    }

    @Override
    public FragListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FragListItem(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.fragment_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FragListItem holder, int position) {
        holder.itemID.setText(inventory.get(position).getID());
        holder.itemLoc.setText(inventory.get(position).getLocation());
        holder.itemQty.setText(inventory.get(position).getQty());
    }

    @Override
    public int getItemCount() {
        return inventory != null ? inventory.size() : 0;
    }

    public void updateData(List<DataItem> list){
        inventory.clear();
        inventory.addAll(list);
        notifyDataSetChanged();
    }
}
