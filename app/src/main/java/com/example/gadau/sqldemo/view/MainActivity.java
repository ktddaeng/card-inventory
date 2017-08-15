package com.example.gadau.sqldemo.view;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gadau.sqldemo.R;
import com.example.gadau.sqldemo.data.Contants;
import com.example.gadau.sqldemo.data.DataItem;
import com.example.gadau.sqldemo.data.DatabaseHandler;
import com.example.gadau.sqldemo.data.MenuOption;
import com.example.gadau.sqldemo.logic.AnyOrientationActivity;
import com.example.gadau.sqldemo.logic.ItemClickListener;
import com.example.gadau.sqldemo.logic.StartAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, ItemClickListener {
    final Context context = this;
    private DatabaseHandler dB;
    private RecyclerView mRecycleView;
    private StartAdapter mAdapter;
    private List<MenuOption> listOfData;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dB = DatabaseHandler.getInstance(this);
        ImageView buttonBack = (ImageView) findViewById(R.id.button_cancel2);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });

        listOfData = new ArrayList<>();
        listOfData.add(new MenuOption(R.string.header3, R.string.desc3, R.color.colorAccent));
        listOfData.add(new MenuOption(R.string.header4, R.string.desc4, R.color.colorPrimary));
        listOfData.add(new MenuOption(R.string.header5, R.string.desc5, R.color.colorAccent));

        setUpRecycler();
        if (savedInstanceState != null){
            finish();
        }
    }

    private void setUpRecycler() {
        mRecycleView = (RecyclerView) findViewById(R.id.rec_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);

        mAdapter = new StartAdapter(listOfData);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        final MenuOption data = listOfData.get(position);
        switch (data.getHeader()) {
            case R.string.header3:
                inputNumberSearch();
                break;
            case R.string.header4:
                barcodeMode();
                break;
            case R.string.header5:  //Manual
                goEditData(",", false);
                break;
        }
    }

    private void inputNumberSearch(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Quantity");
        View subView = getLayoutInflater().inflate(R.layout.fragment_edit_id, null);
        final EditText inID = (EditText) subView.findViewById(R.id.input_dialog_IDNo);
        builder.setView(subView);
        inID.requestFocus();

        builder
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        identifyID(inID.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Action Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    //Figure out if the item exists on database
    private void identifyID(String gottenId){
        String s;    //goes by 5 last char by default
        if (gottenId.length() == 12){
            s = gottenId.substring(Contants.INDEX_CARD_INIT, Contants.INDEX_CARD_FIN);
        } else if (gottenId.length() == 10) {
            s = gottenId.substring(Contants.INDEX_CARD_INIT - 1, Contants.INDEX_CARD_FIN - 1);
        } else if (gottenId.length() == 5){
            s = gottenId;
        } else {
            Toast.makeText(MainActivity.this, "Not a valid ID!", Toast.LENGTH_SHORT).show();
            return;
        }
        DataItem di = dB.getItemByID(s);

        if (di != null) {
            launchInfoPage(s);
        } else {
            launchAlertNew(gottenId);
        }
    }

    //Launches the Detailed View
    private void launchInfoPage(String itemID){
        Intent intent = new Intent(this, InfoPage.class);
        intent.putExtra(Contants.EXTRA_ID, itemID);
        startActivity(intent);
    }

    //Launches the Barcode Scanner
    private void barcodeMode(){
        qrScan = new IntentIntegrator(this);
        qrScan.setCaptureActivity(AnyOrientationActivity.class);
        qrScan.setOrientationLocked(false);
        qrScan.setPrompt("Scan a barcode");
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        qrScan.initiateScan();
    }

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
                Toast.makeText(context, "Pulling help", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.options_main_purge:
                onPurge();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                identifyID(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Launches an Alert Dialog if an item doesn't exist on the DB yet
    private void launchAlertNew(String id) {
        final String s = id;
        AlertDialog.Builder alertA = new AlertDialog.Builder(context);
        alertA.setTitle("Item ID Not Found");
        //should make icons to follow the different options.
        alertA
                .setMessage("Would you like to add this item?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        //Go Edit Data
                        dialog.dismiss();
                        goEditData(s, true);
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

    private void goEditData(String id, boolean gotID){
        Intent intent = new Intent(this, EditInfo.class);

        if (!gotID) {
            intent.putExtra(Contants.READY_TO_LOAD, false);
            intent.putExtra(Contants.IS_EXISTING, false);
            startActivity(intent);
            return;
        }
        String s = id;
        String v;
        if (id.length() == 12){
            s = id.substring(Contants.INDEX_CARD_INIT, Contants.INDEX_CARD_FIN);
            v = id.substring(Contants.INDEX_VENDOR_INIT, Contants.INDEX_VENDOR_FIN);
        } else if (id.length() == 10) {
            s = id.substring(Contants.INDEX_CARD_INIT - 1, Contants.INDEX_CARD_FIN - 1);
            v = id.substring(Contants.INDEX_VENDOR_INIT - 1, Contants.INDEX_VENDOR_FIN - 1);
        } else if (id.length() == 5 ){
            v = "77054";
        } else {
            Toast.makeText(MainActivity.this, "ID Not Valid!", Toast.LENGTH_SHORT);
            return;
        }
        intent.putExtra(Contants.EXTRA_ID, s);
        intent.putExtra(Contants.EXTRA_VENDOR, v);
        intent.putExtra(Contants.READY_TO_LOAD, true);
        intent.putExtra(Contants.IS_EXISTING, false);
        startActivity(intent);
    }

    private void exportLog() {
        dB.exportDatabase(Contants.ORDER_BY_ID);
        Toast.makeText(this, "Database successfully exported!", Toast.LENGTH_SHORT).show();
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

    private void onPurge() {
        AlertDialog.Builder alertA = new AlertDialog.Builder(context);
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
    }
}
