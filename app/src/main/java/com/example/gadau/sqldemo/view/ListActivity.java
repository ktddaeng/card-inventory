package com.example.gadau.sqldemo.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gadau.sqldemo.R;
import com.example.gadau.sqldemo.data.DataItem;
import com.example.gadau.sqldemo.data.DatabaseHandler;
import com.example.gadau.sqldemo.logic.ListController;

import java.util.List;

public class ListActivity extends AppCompatActivity implements ViewInterface {
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String READY_TO_LOAD = "READY_TO_LOAD";
    private static final String IS_EXISTING = "IS_EXISTING";
    private static final int WAS_CHANGED = 1;

    private static final int ORDER_BY_ID = 11;
    private static final int ORDER_BY_POS = 12;
    private static final int ORDER_BY_QTY = 13;
    private static final int ORDER_BY_SEASON = 14;
    private static final int ORDER_BY_ALLID = 15;

    private List<DataItem> listOfData;
    private DatabaseHandler dB;
    private RecyclerView recyclerView;
    private LayoutInflater layoutInflater;
    private CustomAdapter adapter;
    private ListController listController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        dB = DatabaseHandler.getInstance(this);

        recyclerView = (RecyclerView) findViewById(R.id.rec_list_activity);
        layoutInflater = getLayoutInflater();

        listController = new ListController(this, dB, ORDER_BY_ID);
    }

    private void launchFloatingContextMenu() {
        //Todo: Launch floating context menu for sorting
    }

    private void refreshPage() {
        //Todo: Refresh Page for new changes on drag down, button click, and on resume
    }

    @Override
    public void launchInfoPage(String itemID){
        Intent intent = new Intent(this, InfoPage.class);
        intent.putExtra(EXTRA_ID, itemID);
        //Todo: put extra ready to load and is existing
        startActivity(intent);
    }

    @Override
    public void setUpAdapterAndView(List<DataItem> listOfData) {
        this.listOfData = listOfData;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = layoutInflater.inflate(R.layout.fragment_list_item, parent, false);
            return new CustomViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            //super.onBindViewHolder(holder, position, payloads);
            DataItem currentItem = listOfData.get(position);
            holder.itemId.setText(currentItem.getID());
            holder.itemLoc.setText(currentItem.getLocation());
            holder.itemLoc.setText(currentItem.getQty());
        }

        @Override
        public int getItemCount() { return listOfData.size(); }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView itemId;
            private TextView itemLoc;
            private TextView itemQty;
            private ViewGroup container;

            public CustomViewHolder(View itemView) {
                super(itemView);

                this.itemId = (TextView) itemView.findViewById(R.id.list_id);
                this.itemLoc = (TextView) itemView.findViewById(R.id.list_loc);
                this.itemQty = (TextView) itemView.findViewById(R.id.list_qty);
                this.container = (ViewGroup) itemView.findViewById(R.id.root_list_item);

                this.container.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                DataItem dataItem = listOfData.get(this.getAdapterPosition());
                //ToDO: launch info page
                listController.onItemClick(dataItem);
            }
        }
    }
}