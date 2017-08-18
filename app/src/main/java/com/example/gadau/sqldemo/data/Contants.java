package com.example.gadau.sqldemo.data;

/**
 * Created by gadau on 8/2/2017.
 */

public class Contants {
    public static final int ORDER_BY_ID = 11;
    public static final int ORDER_BY_POS = 12;
    public static final int ORDER_BY_QTY = 13;
    public static final int ORDER_BY_SEASON = 14;
    public static final int ORDER_NONE = 0;

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_VENDOR = "EXTRA_VENDOR";
    public static final String READY_TO_LOAD = "READY_TO_LOAD";
    public static final String IS_EXISTING = "IS_EXISTING";
    public static final int RESULT_DELETED = 12345;
    public static final int MY_PERMISSIONS_REQUEST = 54321;
    public static final int NOTIFICATION_ID = 108;
    public static final int INDEX_VENDOR_INIT = 1;
    public static final int INDEX_VENDOR_FIN = 6;
    public static final int INDEX_CARD_INIT = 6;
    public static final int INDEX_CARD_FIN = 11;

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;

    //DATABASE NAME
    public static final String DATABASE_NAME = "inventoryManager";

    //CONTACTS TABLE NAME
    public static final String TABLE_INVENTORY = "inventory";


    private Contants() {
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();

    }
}