package com.example.gadau.sqldemo.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gadau.sqldemo.R;
import com.example.gadau.sqldemo.data.Contants;
import com.example.gadau.sqldemo.data.DataItem;
import com.example.gadau.sqldemo.data.DatabaseHandler;
import com.example.gadau.sqldemo.logic.ItemClickListener;
import com.example.gadau.sqldemo.logic.LineAdapter;

import java.util.List;

public class ListActivity extends AppCompatActivity
        implements PopupMenu.OnMenuItemClickListener,
        ItemClickListener {
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String READY_TO_LOAD = "READY_TO_LOAD";
    private static final String IS_EXISTING = "IS_EXISTING";
    private static final int WAS_CHANGED = 1;

    private List<DataItem> listOfData;
    private DatabaseHandler dB;
    private SwipeRefreshLayout swiperRefresh;
    private RecyclerView mRecyclerView;
    private LineAdapter mAdapter;
    private int order_state;
    private AlertDialog progressDialog;
    private AlertDialog.Builder progressBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order_state = Contants.ORDER_BY_QTY_ID;
        setContentView(R.layout.activity_list);
        setUpToolbar();

        dB = DatabaseHandler.getInstance(this);
        listOfData = dB.getListofData(order_state);
        setupRecycler();
        setupSwiper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRecyclerView != null) {
            refreshPage(order_state);
        }
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sort);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(""); toolbar.setSubtitle("");

        ImageView backButton = (ImageView) findViewById(R.id.button_cancel2);
        ImageView sortButton = (ImageView) findViewById(R.id.button_sort);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ListActivity.this.finish(); }
        });
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortPage();
            }
        });
    }

    private void setupRecycler() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_list_activity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new LineAdapter(listOfData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }

    /*PULL UP EDIT INFO PAGE*/
    @Override
    public void onClick(View view, int position) {
        final DataItem data = listOfData.get(position);
        Intent intent = new Intent(this, InfoPage.class);
        intent.putExtra(Contants.EXTRA_ID, data.getID());
        startActivity(intent);
    }

    /*REFRESH PAGE*/
    private void setupSwiper() {
        swiperRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swiperRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage(order_state);
            }
        });
    }

    private void refreshPage(int order) {
        //Load Items
        listOfData = dB.getListofData(order);
        onItemsLoadComplete();
    }

    void onItemsLoadComplete(){
        //Update adapter and notify dataset change
        mAdapter.updateData(listOfData);
        swiperRefresh.setRefreshing(false);
    }

    /*MENU OPTIONS*/
    public void showMenu(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.options_main_activity, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.options_main_export:
                canWriteExportPlease();
                return true;
            case R.id.options_main_help:
                Toast.makeText(this, "Pulling help", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.options_main_purge:
                onPurge();
                return true;
            case R.id.options_main_import:
                canImportPlease();
                return true;
            default:
                return false;
        }
    }

    /*PURGE DATABASE*/
    private void onPurge() {
        AlertDialog.Builder alertA = new AlertDialog.Builder(this);
        alertA.setTitle("Delete the Database?");
        //should make icons to follow the different options.
        alertA
                .setMessage("Are you sure you want to delete the database?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        //Go Edit Data
                        dialog.dismiss();
                        clearDatabase();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertA.create();
        alertDialog.show();
    }

    private void clearDatabase(){
        dB.clearDatabase();
        Toast.makeText(this, "Database has been cleared!", Toast.LENGTH_SHORT);
        refreshPage(Contants.ORDER_NONE);
    }

    /*EXPORT DATABASE*/
    private void exportLog() {
        dB.exportDatabase(order_state);
        Toast.makeText(this, "Table has been exported!", Toast.LENGTH_SHORT).show();
    }

    public void canWriteExportPlease(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Contants.MY_PERMISSIONS_REQUEST);

            }
        } else {
            exportLog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Contants.MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work....
                    exportLog();
                } else {
                    Toast.makeText(this, "Can't write to external storage.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' statements for other permssions
        }
    }

    private void importData(){
        //Toast.makeText(context, "attempting import", Toast.LENGTH_SHORT).show();
        new importHandler().execute();
    }

    private class importHandler extends AsyncTask<Void, Void, Void> {
        DatabaseHandler db = DatabaseHandler.getInstance(ListActivity.this);
        private String s;

        @Override
        protected void onPreExecute() {
            progressBuild = new AlertDialog.Builder(ListActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.fragment_progress, null);
            progressBuild.setView(dialogView)
                    .setCancelable(false);
            progressDialog = progressBuild.create();
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            s = db.importDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toast.makeText(ListActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    public void canImportPlease(){
        //Toast.makeText(context, "asking for permission...", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Contants.MY_PERMISSIONS_REQUEST);
            }
        } else {
            importData();
        }
    }

    /*SORT LIST*/
    private void sortPage(){
        AlertDialog.Builder alertA = new AlertDialog.Builder(this);
        //should make icons to follow the different options.
        alertA
                .setTitle(R.string.context_sort_title)
                .setItems(R.array.context_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(ListActivity.this, "item: " + which, Toast.LENGTH_SHORT).show();
                        contextChoice(which);
                    }
                });
        AlertDialog alertDialog = alertA.create();
        alertDialog.show();
    }

    public boolean contextChoice(int item) {
        switch (item) {
            case 0:
                Toast.makeText(this, "Ordered by Quantity, ID", Toast.LENGTH_SHORT).show();
                order_state = Contants.ORDER_BY_QTY_ID;
                refreshPage(order_state);
                return true;
            case 1:
                Toast.makeText(this, "Ordered by Quantity", Toast.LENGTH_SHORT).show();
                order_state = Contants.ORDER_BY_QTY;
                refreshPage(order_state);
                return true;
            case 2:
                Toast.makeText(this, "Order by ID", Toast.LENGTH_SHORT).show();
                order_state = Contants.ORDER_BY_ID;
                refreshPage(order_state);
                return true;
            case 3:
                Toast.makeText(this, "Ordered by Position", Toast.LENGTH_SHORT).show();
                order_state = Contants.ORDER_BY_POS;
                refreshPage(order_state);
                return true;
            default:
                return true;
        }
    }

}