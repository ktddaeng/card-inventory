package com.example.gadau.sqldemo.view;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gadau.sqldemo.R;
import com.example.gadau.sqldemo.data.Contants;
import com.example.gadau.sqldemo.data.DataItem;
import com.example.gadau.sqldemo.data.DatabaseHandler;
import com.example.gadau.sqldemo.logic.LineAdapter;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String READY_TO_LOAD = "READY_TO_LOAD";
    private static final String IS_EXISTING = "IS_EXISTING";
    private static final int WAS_CHANGED = 1;

    private List<DataItem> listOfData;
    private DatabaseHandler dB;
    private SwipeRefreshLayout swiper;
    private RecyclerView mRecyclerView;
    private LineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setUpToolbar();

        dB = DatabaseHandler.getInstance(this);
        listOfData = dB.getListofData(Contants.ORDER_NONE);
        setupRecycler();
        setupSwiper();
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sort);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(""); toolbar.setSubtitle("");

        ImageView backButton = (ImageView) findViewById(R.id.button_cancel2);
        ImageView exportButton = (ImageView) findViewById(R.id.button_export);
        ImageView sortButton = (ImageView) findViewById(R.id.button_sort);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ListActivity.this.finish(); }
        });
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportLog();
            }
        });
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortPage();
            }
        });
    }

    private void setupSwiper() {
        swiper = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage(Contants.ORDER_NONE);
            }
        });
    }

    private void setupRecycler() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_list_activity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new LineAdapter(listOfData);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        );
    }

    private void refreshPage(int order) {
        Toast.makeText(this, "Refreshing Page!", Toast.LENGTH_SHORT).show();
        //Load Items
        listOfData = dB.getListofData(order);
        onItemsLoadComplete();
    }

    void onItemsLoadComplete(){
        //Update adapter and notify dataset change
        mAdapter.updateData(listOfData);
        swiper.setRefreshing(false);
    }

    private void exportLog() {
        Toast.makeText(this, "Exporting Inventory!", Toast.LENGTH_SHORT).show();
    }

    //TODO: Make Dialog instead of Context Menu, because Context Menus Suck!!
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Order List by...");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_sort, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_no:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                refreshPage(Contants.ORDER_BY_ID);
                return true;
            case R.id.sort_pos:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                refreshPage(Contants.ORDER_BY_POS);
                return true;
            case R.id.sort_qty:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                refreshPage(Contants.ORDER_BY_QTY);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}