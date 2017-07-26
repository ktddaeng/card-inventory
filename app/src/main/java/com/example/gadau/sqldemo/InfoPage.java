package com.example.gadau.sqldemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InfoPage extends AppCompatActivity {
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_VENDOR = "EXTRA_VENDOR";
    private static final int WAS_CHANGED = 1;
    private static final String READY_TO_LOAD = "READY_TO_LOAD";
    private static final String IS_EXISTING = "IS_EXISTING";
    private static final int REQUEST_CODE_EDIT = 234;
    private DataItem di;
    private String s;
    private DatabaseHandler dB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page);
        dB = DatabaseHandler.getInstance(this);

        //Set edit button in toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Item Information"); toolbar.setSubtitle("");

        //Take apart package for viewing
        Intent i = getIntent();
        s = getIntent().getExtras().getString(EXTRA_ID);
        fillData();

    }

    private void fillData(){
        di = dB.getItemByID(s);

        TextView outID = (TextView) findViewById(R.id.output_id);
        TextView outVendor = (TextView) findViewById(R.id.output_vendor);
        TextView outLoc = (TextView) findViewById(R.id.output_location);
        TextView outQty = (TextView) findViewById(R.id.output_qty);

        outID.setText(di.getID());
        outVendor.setText(di.getVendor());
        outLoc.setText(di.getLocation());
        outQty.setText(di.getQty());
    }

    private void launchEditPage(){
        Intent intent = new Intent(this, EditInfo.class);
        intent.putExtra(EXTRA_ID, s);
        intent.putExtra(EXTRA_VENDOR, "");
        intent.putExtra(READY_TO_LOAD, true);
        intent.putExtra(IS_EXISTING, true);
        startActivityForResult(intent, WAS_CHANGED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WAS_CHANGED){
            if (resultCode == RESULT_OK) {
                fillData();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mi_edit:
                launchEditPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
