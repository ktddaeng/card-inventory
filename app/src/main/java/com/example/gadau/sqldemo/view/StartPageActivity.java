package com.example.gadau.sqldemo.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gadau.sqldemo.R;
import com.example.gadau.sqldemo.data.Contants;
import com.example.gadau.sqldemo.data.DataItem;
import com.example.gadau.sqldemo.data.MenuOption;
import com.example.gadau.sqldemo.logic.ItemClickListener;
import com.example.gadau.sqldemo.logic.LineAdapter;
import com.example.gadau.sqldemo.logic.StartAdapter;

import java.util.ArrayList;
import java.util.List;

public class StartPageActivity extends AppCompatActivity implements ItemClickListener {
    private RecyclerView mRecycleView;
    private StartAdapter mAdapter;
    private List<MenuOption> listOfData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        //Button scanButton = (Button) findViewById(R.id.button_scan);
        //Button listButton = (Button) findViewById(R.id.button_list);

        /**Prepare List**/
        listOfData = new ArrayList<>();
        listOfData.add(new MenuOption(R.string.header1, R.string.desc1, R.color.colorAccent , new Intent(this, MainActivity.class)));
        listOfData.add(new MenuOption(R.string.header2, R.string.desc2, R.color.colorPrimary , new Intent(this, ListActivity.class)));

        setUpToolbar();
        setUpRecycler();
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_start);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.app_name); toolbar.setSubtitle("");
    }
    private void setUpRecycler() {
        mRecycleView = (RecyclerView) findViewById(R.id.rec_start);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);

        mAdapter = new StartAdapter(listOfData);
        mRecycleView.setAdapter(mAdapter);
    }

    private void launchSearchView(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void launchListView(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(this, "Item " + position + " Selected", Toast.LENGTH_SHORT).show();
        /**/
    }
}
