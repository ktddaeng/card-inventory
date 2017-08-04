package com.example.gadau.sqldemo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gadau.sqldemo.R;

/**
 * Created by gadau on 8/2/2017.
 */

public class FragListItem extends RecyclerView.ViewHolder {
    public TextView itemID;
    public TextView itemLoc;
    public TextView itemQty;

    public FragListItem (View itemView){
        super(itemView);
        itemID = (TextView) itemView.findViewById(R.id.list_id);
        itemLoc = (TextView) itemView.findViewById(R.id.list_loc);
        itemQty = (TextView) itemView.findViewById(R.id.list_qty);
    }
}
