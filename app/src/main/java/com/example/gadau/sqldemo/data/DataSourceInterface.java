package com.example.gadau.sqldemo.data;

import android.database.Cursor;

import java.util.List;

/**
 * Created by gadau on 8/2/2017.
 */

public interface DataSourceInterface {
    DataItem getItemByID(String id);
    DataItem getItem(int id);
    Cursor getAllItemsCursor();
    List<DataItem> getListofData(int order);
    int getItemCount();
    int updateItem(DataItem item);
    void deleteItem(DataItem item);
    void clearDatabase();
    void addItem(DataItem item);}
