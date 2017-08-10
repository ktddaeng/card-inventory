package com.example.gadau.sqldemo.view;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gadau.sqldemo.R;
import com.example.gadau.sqldemo.data.Contants;
import com.example.gadau.sqldemo.data.DataItem;
import com.example.gadau.sqldemo.data.DatabaseHandler;
import com.example.gadau.sqldemo.logic.AnyOrientationActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    final Context context = this;
    private DatabaseHandler dB;

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

        Button button = (Button) findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText inID = (EditText) findViewById(R.id.input_barcode);
                identifyID(inID.getText().toString());
            }
        });
        Button barScan = (Button) findViewById(R.id.barcode_button);
        barScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeMode();
            }
        });
        Button manualButton = (Button) findViewById(R.id.manual_button);
        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goEditData(",", false);
            }
        });
        final EditText et = (EditText) findViewById(R.id.input_barcode);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });

        if (savedInstanceState != null){
            finish();
        }
    }

    //Figure out if the item exists on database
    private void identifyID(String gottenId){
        String s = gottenId;    //goes by 5 last char by default
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
                Toast.makeText(context, "Export DB!", Toast.LENGTH_SHORT).show();
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
        String v = id;
        if (id.length() == 12){
            s = id.substring(Contants.INDEX_CARD_INIT, Contants.INDEX_CARD_FIN);
            v = id.substring(Contants.INDEX_VENDOR_INIT, Contants.INDEX_VENDOR_FIN);
        } else if (id.length() == 10) {
            s = id.substring(Contants.INDEX_CARD_INIT - 1, Contants.INDEX_CARD_FIN - 1);
            v = id.substring(Contants.INDEX_VENDOR_INIT - 1, Contants.INDEX_VENDOR_FIN - 1);
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

    //Gives index of item in DB
    //TODO: Put in SQLite
    private int getDBindex(String id) {
        int i = -1;
        /*for (DataItem d : dB){
            if(d.getID().equals(id)){
                //i =  dB.indexOf(d);
            }
        }*/
        return i;
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
