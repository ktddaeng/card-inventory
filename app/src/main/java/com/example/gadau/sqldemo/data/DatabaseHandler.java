package com.example.gadau.sqldemo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gadau on 7/10/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper implements DataSourceInterface{

    //STATIC VARIABLES
    private static DatabaseHandler instance;

    //DATABASE VERSION
    private static final int DATABASE_VERSION = 1;

    //DATABASE NAME
    private static final String DATABASE_NAME = "inventoryManager";

    //CONTACTS TABLE NAME
    private static final String TABLE_INVENTORY = "inventory";

    //Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_VENDOR = "vendor";
    private static final String KEY_ITEMNO = "barcode";
    private static final String KEY_ROW = "row";
    private static final String KEY_COL = "col";
    private static final String KEY_QTY = "qty";

    //Order By Query Codes
    private static final int ORDER_BY_ID = 11;
    private static final int ORDER_BY_POS = 12;
    private static final int ORDER_BY_QTY = 13;
    private static final int ORDER_BY_SEASON = 14;
    private static final int ORDER_BY_ALLID = 15;

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
                TABLE_INVENTORY + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ITEMNO + " INTEGER, " + KEY_VENDOR + " INTEGER, "
                + KEY_ROW + " TEXT, " + KEY_COL + " TEXT, " + KEY_QTY + " INTEGER)";
        db.execSQL(CREATE_INVENTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        onCreate(db);
    }

    @Override
    public void addItem(DataItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEMNO, item.getID());
        values.put(KEY_VENDOR, item.getVendor());
        values.put(KEY_COL, item.getCol());
        values.put(KEY_ROW, item.getRow());
        values.put(KEY_QTY, item.getQty());

        db.insert(TABLE_INVENTORY, KEY_ITEMNO, values);
        db.close();
    }

    @Override
    public DataItem getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INVENTORY,
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

    @Override
    public DataItem getItemByID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INVENTORY,
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

    @Override
    public List<DataItem> getListofData(int order) {
        List<DataItem> listItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_INVENTORY;
        /*
        switch (order){
            case ORDER_BY_ID:
                selectQuery += " ORDER BY " + KEY_ITEMNO + " WHERE (" + KEY_QTY + " > 0)";
                break;
            case ORDER_BY_POS:
                selectQuery += " ORDER BY " + KEY_COL + ", " + KEY_ROW  + " WHERE (" + KEY_QTY + " > 0)";
                break;
            case ORDER_BY_QTY:
                selectQuery += " ORDER BY " + KEY_QTY + " WHERE (" + KEY_QTY + " > 0)";
                break;
            case ORDER_BY_SEASON:
                selectQuery += "ORDER BY " + KEY_ITEMNO + " WHERE (" + KEY_COL + " == Y ) OR (" + KEY_COL + " == Z)";
                break;
            default: //allid do nothing
        }*/
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null); //TODO: Problem!!

        if (cursor.moveToFirst()) {
            do {
                DataItem di = new DataItem();
                di.setID(cursor.getString(1));
                di.setVendor(cursor.getString(2));
                di.setLocation(cursor.getString(3) + cursor.getString(4));
                di.setQty(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        return listItems;
    }

    @Override
    public Cursor getAllItemsCursor(){
        Cursor cursor = getReadableDatabase().query(TABLE_INVENTORY,
                new String[]{KEY_ID, KEY_ITEMNO, KEY_VENDOR, KEY_ROW, KEY_COL,
                KEY_QTY},
                null, null, null, null, null);
        return cursor;
    }

    @Override
    public int getItemCount() {
        String countQuery = "SELECT * FROM " + TABLE_INVENTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    @Override
    public int updateItem(DataItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEMNO, item.getID());
        values.put(KEY_VENDOR, item.getVendor());
        values.put(KEY_ROW, item.getRow());
        values.put(KEY_COL, item.getCol());
        values.put(KEY_QTY, item.getQty());

        return db.update(TABLE_INVENTORY, values, KEY_ITEMNO + " = ?",
                new String[] { String.valueOf(item.getID()) });
        //TODO: fix later
    }

    @Override
    public void deleteItem(DataItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INVENTORY, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getID()) });
        db.close();;
        //TODO: fix later
    }

    @Override
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        onCreate(db);
    }
}