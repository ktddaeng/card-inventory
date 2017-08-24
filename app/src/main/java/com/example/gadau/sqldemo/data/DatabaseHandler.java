package com.example.gadau.sqldemo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVWriter;


/**
 * Created by gadau on 7/10/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    //Column Names
    private static final String KEY_ID = "inventory_id";
    private static final String KEY_VENDOR = "inventory_vendor";
    private static final String KEY_ITEMNO = "iventory_barcode";
    private static final String KEY_ROW = "inventory_row";
    private static final String KEY_COL = "inventory_col";
    private static final String KEY_QTY = "inventory_qty";

    //STATIC VARIABLES
    private static DatabaseHandler instance;

     //Order By Query Codes

    private DatabaseHandler(Context context) {
        super(context, Contants.DATABASE_NAME, null, Contants.DATABASE_VERSION);
    }

    public static synchronized DatabaseHandler getInstance(Context context){
        if (instance == null) {
            instance = new DatabaseHandler(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INVENTORY = "CREATE TABLE " +
                Contants.TABLE_INVENTORY + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ITEMNO + " TEXT, " + KEY_VENDOR + " TEXT, "
                + KEY_COL + " TEXT, " + KEY_ROW + " INTEGER, " + KEY_QTY + " INTEGER)";
        db.execSQL(CREATE_INVENTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contants.TABLE_INVENTORY);
        onCreate(db);
    }

    public void addItem(DataItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEMNO, item.getID());
        values.put(KEY_VENDOR, item.getVendor());
        values.put(KEY_COL, item.getCol());
        values.put(KEY_ROW, item.getRowInt());
        values.put(KEY_QTY, item.getQtyInt());

        db.insert(Contants.TABLE_INVENTORY, KEY_ITEMNO, values);
        db.close();
    }

    public DataItem getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Contants.TABLE_INVENTORY,
                new String[]{ KEY_ID, KEY_ITEMNO, KEY_VENDOR, KEY_COL, KEY_ROW, KEY_QTY},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor == null && cursor.moveToFirst()){
            DataItem item = new DataItem(cursor.getString(1), cursor.getString(2),
            cursor.getString(3) + cursor.getString(4),
            cursor.getString(5));
            return item;
        }
        return null;
    }

    public DataItem getItemByID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Contants.TABLE_INVENTORY,
                new String[]{ KEY_ID, KEY_ITEMNO, KEY_VENDOR, KEY_COL, KEY_ROW, KEY_QTY},
                KEY_ITEMNO + "=?",
                new String[]{id},
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            DataItem item = new DataItem(cursor.getString(1), cursor.getString(2),
                    cursor.getString(3) + cursor.getString(4),
                    cursor.getString(5));
            return item;
        }
        return null;
    }

    public List<DataItem> getListofData(int order) {
        List<DataItem> listItems = new ArrayList<>();
        boolean excludeZeros = true; //TODO: DUmmy value, allow for customizing in parameters
        String selectQuery = "SELECT * FROM " + Contants.TABLE_INVENTORY ;// + " ORDER BY " + KEY_QTY;

        switch (order){
            case Contants.ORDER_BY_ID:
                selectQuery += " ORDER BY " + KEY_ITEMNO;
                break;
            case Contants.ORDER_BY_POS:
                selectQuery += " ORDER BY " + KEY_COL + ", " + KEY_ROW;
                break;
            case Contants.ORDER_BY_QTY:
                selectQuery += " ORDER BY " + KEY_QTY;
                break;
            case Contants.ORDER_BY_SEASON:
                selectQuery += " ORDER BY " + KEY_ITEMNO + " WHERE (" + KEY_COL + " == Y ) OR (" + KEY_COL + " == Z)";
                break;
            case Contants.ORDER_BY_QTY_ID:
                selectQuery += " ORDER BY " + KEY_QTY + ", " + KEY_ITEMNO;
                break;
            default: //allid do nothing
                selectQuery += " ORDER BY " + KEY_QTY;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        DataItem di = null;
        if (cursor.moveToFirst()) {
            do {
                di = new DataItem();
                di.setID(cursor.getString(1));
                di.setVendor(cursor.getString(2));
                di.setLocation(cursor.getString(3) + cursor.getString(4));
                di.setQty(cursor.getString(5));
                listItems.add(di);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return listItems;
    }

    public Cursor getAllItemsCursor(int order){
        String selectQuery = "SELECT * FROM " + Contants.TABLE_INVENTORY ;// + " ORDER BY " + KEY_QTY;

        switch (order){
            case Contants.ORDER_BY_ID:
                selectQuery += " ORDER BY " + KEY_ITEMNO;
                break;
            case Contants.ORDER_BY_POS:
                selectQuery += " ORDER BY " + KEY_COL + ", " + KEY_ROW;
                break;
            case Contants.ORDER_BY_QTY:
                selectQuery += " ORDER BY " + KEY_QTY;
                break;
            case Contants.ORDER_BY_SEASON:
                selectQuery += " ORDER BY " + KEY_ITEMNO + " WHERE (" + KEY_COL + " == Y ) OR (" + KEY_COL + " == Z)";
                break;
            default: //allid do nothing
                selectQuery += " ORDER BY " + KEY_QTY;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public int getItemCount() {
        String countQuery = "SELECT * FROM " + Contants.TABLE_INVENTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int ct = cursor.getCount();
        cursor.close();
        return ct;
    }

    public int updateItem(DataItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEMNO, item.getID());
        values.put(KEY_VENDOR, item.getVendor());
        values.put(KEY_COL, item.getCol());
        values.put(KEY_ROW, item.getRowInt());
        values.put(KEY_QTY, item.getQtyInt());

        return db.update(Contants.TABLE_INVENTORY, values, KEY_ITEMNO + " = ?",
                new String[] { String.valueOf(item.getID()) });
    }

    public void deleteItem(DataItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contants.TABLE_INVENTORY, KEY_ITEMNO + " = ?",
                new String[] { String.valueOf(item.getID()) });
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + Contants.TABLE_INVENTORY);
        onCreate(db);
    }

    public void exportDatabase(int order) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd__HH_mm",
                Locale.getDefault());
        String date = df.format(new Date());
        Cursor cursor = getAllItemsCursor(order);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsoluteFile();
        String filename = "CardInventory_"+ date +".csv";

        try {

            File saveFile = new File(path, filename);
            FileWriter fw = new FileWriter(saveFile);

            BufferedWriter bw = new BufferedWriter(fw);
            int rowCount = cursor.getCount();
            int colCount = cursor.getColumnCount();
            if (rowCount > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < colCount; i++){
                    if (i != colCount -1) {
                        bw.write(cursor.getColumnName(i) + ",");
                    } else {
                        bw.write(cursor.getColumnName(i));
                    }
                }
                bw.newLine();
                for (int i = 0; i < rowCount; i++) {
                    cursor.moveToPosition(i);
                    for (int j = 0; j < colCount; j++) {
                        if (j != colCount - 1)
                            bw.write(cursor.getString(j) + ",");
                        else
                            bw.write(cursor.getString(j));
                    }
                    bw.newLine();
                }
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO: Allow backup capabilities
        //* Establish path to save log files
        //* Convert DB to CSV files
        //* Label CSV files by date and time
    }
}
/*

    class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
        //private final ProgressDialog dialog = new ProgressDialog();

        @Override
        protected Boolean doInBackground(String... params) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd__HH_mm",
                    Locale.getDefault());
            String date = df.format(new Date());

            File exportDir = new File(Environment.getExternalStorageDirectory(), "");

            if (!exportDir.exists()){
                exportDir.mkdirs();
            }

            //File file = new File(exportDir, "inventory" + date + ".csv");
            File file = new File("testing.csv");

            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                SQLiteDatabase db = this.getReadableDatabase;
            } catch (SQLException eSQL) {
                Log.e("DatabaseHandler", eSQL.getMessage(), eSQL);
                return false;
            } catch (IOException eIO) {
                Log.e("DatabaseHandler", eIO.getMessage(), eIO);
                return false;
            }
            return null;
        }
    }
    */