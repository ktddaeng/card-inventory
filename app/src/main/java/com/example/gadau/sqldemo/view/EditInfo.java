package com.example.gadau.sqldemo.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gadau.sqldemo.R;
import com.example.gadau.sqldemo.data.Contants;
import com.example.gadau.sqldemo.data.DataItem;
import com.example.gadau.sqldemo.data.DatabaseHandler;

public class EditInfo extends AppCompatActivity {
    private static final String[] letters = { "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private DataItem di;
    private boolean update_flag = false;
    private DatabaseHandler dB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        dB = DatabaseHandler.getInstance(this);

        //Set Toolbar
        setUpToolbar();

        TextView saveButton = (TextView) findViewById(R.id.button_save);
        ImageView cancelButton = (ImageView) findViewById(R.id.button_cancel);

        //Set Intent Fields
        Intent i = getIntent();
        String s = i.getExtras().getString(Contants.EXTRA_ID);
        String v = i.getExtras().getString(Contants.EXTRA_VENDOR);

        final View idWrapper = (View) findViewById(R.id.input_idWrapper);
        final View vendorWrapper = (View) findViewById(R.id.input_vendorWrapper);
        final View locWrapper = (View) findViewById(R.id.input_locWrapper);
        final View qtyWrapper = (View) findViewById(R.id.input_qtyWrapper);
        final View deleteWrapper = (View) findViewById(R.id.input_delete);
        final EditText textID = (EditText) findViewById(R.id.input_dialog_ID);
        final EditText textVendor = (EditText) findViewById(R.id.input_dialog_vendor);
        final TextView textLoc = (TextView) findViewById(R.id.input_dialog_location);
        final TextView textQty = (TextView) findViewById(R.id.input_dialog_qty);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (getIntent().getExtras().getBoolean(Contants.READY_TO_LOAD)){
            textID.setText(s);
        }

        if (getIntent().getExtras().getBoolean(Contants.IS_EXISTING)){
            update_flag = true;
            di = dB.getItemByID(s);
            textVendor.setText(di.getVendor());
            textLoc.setText(di.getLocation());
            textQty.setText(di.getQty());
        } else {
            di = new DataItem(s, v);
            textVendor.setText(v);
            deleteWrapper.setVisibility(View.GONE);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveItem(textID.getText().toString(),
                        textVendor.getText().toString(),
                        textLoc.getText().toString(),
                        textQty.getText().toString());
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelEdit();
            }
        });

        idWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textID.requestFocus();
                textID.setSelection(textID.getText().length());
                imm.showSoftInput(textID, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        textID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textID.requestFocus();
                textID.setSelection(textID.getText().length());
                imm.showSoftInput(textID, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        vendorWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textVendor.requestFocus();
                textVendor.setSelection(textVendor.getText().length());
                imm.showSoftInput(textVendor, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        textVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textVendor.requestFocus();
                textVendor.setSelection(textVendor.getText().length());
                imm.showSoftInput(textVendor, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        qtyWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setQtyByDialog(); }
        });
        textQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setQtyByDialog(); }
        });

        locWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationByDialog();
            }
        });
        locWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationByDialog();
            }
        });

        deleteWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(di);
            }
        });

        if (savedInstanceState != null){
            finish();
        }
    }

    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(""); toolbar.setSubtitle("");
    }

    private void setLocationByDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditInfo.this);
        builder.setTitle("Choose Location");
        View subView = getLayoutInflater().inflate(R.layout.fragment_edit_location, null);
        final TextView locWrapper = (TextView) findViewById(R.id.input_dialog_location);
        int row, col = 0;
        for (int i=0;i<letters.length;i++) {
            if (letters[i].equals(String.valueOf( locWrapper.getText().toString().substring(0,1) ))) {
                col = i;
                break;
            }
        }
        row = Integer.valueOf( locWrapper.getText().toString().substring(1) );
        final NumberPicker colPicker = (NumberPicker) subView.findViewById(R.id.dialog_picker_col);
        colPicker.setMaxValue(25);
        colPicker.setMinValue(0);
        colPicker.setValue(col);
        colPicker.setDisplayedValues(letters);
        final NumberPicker rowPicker = (NumberPicker) subView.findViewById(R.id.dialog_picker_row);
        rowPicker.setMaxValue(13);
        rowPicker.setMinValue(1);
        rowPicker.setValue(row);
        builder.setView(subView);

        builder
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String s = (letters[(colPicker.getValue())]) +
                                (Integer.toString(rowPicker.getValue()));
                        locWrapper.setText(s);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(EditInfo.this, "Action Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setQtyByDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditInfo.this);
        builder.setTitle("Choose Quantity");
        View subView = getLayoutInflater().inflate(R.layout.fragment_edit_qty, null);
        final TextView qtyWrapper = (TextView) findViewById(R.id.input_dialog_qty);
        int col = 0;

        final NumberPicker qtyPicker = (NumberPicker) subView.findViewById(R.id.dialog_picker_qty);
        qtyPicker.setMaxValue(12);
        qtyPicker.setMinValue(0);
        qtyPicker.setValue(col);
        qtyPicker.setWrapSelectorWheel(false);
        builder.setView(subView);

        builder
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        qtyWrapper.setText(Integer.toString(qtyPicker.getValue()));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(EditInfo.this, "Action Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveItem(String id, String vendor, String loc, String qty){
        if (!safetyCheck()) {
            Toast.makeText(this, "Action Invalid. Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        //System.out.println("id: " + id + ", vendor: " + vendor + ", loc" + loc + ", qty: " + qty);
        di.setID(id);
        di.setVendor(vendor);
        di.setLocation(loc);
        di.setQty(qty);
        //add stuff to DB!
        if (update_flag) {
            dB.updateItem(di);
        } else {
            dB.addItem(di);
        }
        Toast.makeText(EditInfo.this, "Item Saved!", Toast.LENGTH_SHORT).show();
        //Ensure Info page is updated, too
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        EditInfo.this.finish();
    }

    private void cancelEdit(){
        Toast.makeText(EditInfo.this, "Action Canceled", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        EditInfo.this.finish();
    }

    private void deleteItem(DataItem d) {
        final DataItem dataItem = d;
        AlertDialog.Builder alertA = new AlertDialog.Builder(this);
        alertA.setTitle("Deleting Item");
        //should make icons to follow the different options.
        alertA
                .setMessage("Are you sure you want to delete this item? This action cannot be undone.")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        dB.deleteItem(dataItem);
                        Intent returnIntent = new Intent();
                        setResult(Contants.RESULT_DELETED, returnIntent);
                        EditInfo.this.finish();
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

    private boolean safetyCheck(){
        EditText idWrapper = (EditText) findViewById(R.id.input_dialog_ID);
        EditText vendorWrapper = (EditText) findViewById(R.id.input_dialog_vendor);
        TextView locWrapper = (TextView) findViewById(R.id.input_dialog_location);
        TextView qtyWrapper = (TextView) findViewById(R.id.input_dialog_qty);

        String s = idWrapper.getText().toString().trim();
        if (s.isEmpty() || s.length() == 0 || s.equals("") || s == null){
            return false;
        }
        s = vendorWrapper.getText().toString().trim();
        if (s.isEmpty() || s.length() == 0 || s.equals("") || s == null){
            return false;
        }
        s = locWrapper.getText().toString().trim();
        if (s.isEmpty() || s.length() == 0 || s.equals("") || s == null){
            return false;
        }
        s = qtyWrapper.getText().toString().trim();
        if (s.isEmpty() || s.length() == 0 || s.equals("") || s == null){
            return false;
        }
        return true;
    }
}
