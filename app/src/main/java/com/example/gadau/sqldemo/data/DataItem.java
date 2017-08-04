package com.example.gadau.sqldemo.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by gadau on 7/10/2017.
 */

public class DataItem implements Parcelable{
    private int DBID;
    private String ID;
    private String vendor;
    private String location;
    private String qty;

    public DataItem() {
        DBID = 0;
        ID = "";
        vendor = "";
        location = "";
        qty = "";
    }

    public DataItem(String ID, String vendor){
        DBID = 0;
        this.ID = ID;
        this.vendor = vendor;
        location = "";
        qty = "";
    }

    public DataItem(String ID, String vendor, String loc, String quantity) {
        DBID = 0;
        this.ID = ID;
        this.vendor = vendor;
        location = loc;
        qty = quantity;
    }

    public DataItem(String ID, String loc, String quantity) {
        DBID = 0;
        this.ID = ID;
        this.vendor = "0";
        location = loc;
        qty = quantity;
    }

    public String getID() {
        Log.i("DataItem Class: ", ID);
        return ID;
    }

    public String getLocation() {
        return location;
    }

    public String getCol() { return location.substring(0,1); }
    public String getRow() { return location.substring(1); }

    public String getQty() {
        return qty;
    }

    public String getVendor() { return vendor; }

    public void setVendor(String vendor) { this.vendor = vendor; }

    public void setID(String ID) { this.ID = ID; }

    public void setLocation(String loc) {
        location = loc;
    }

    public void setQty(String quantity) {
        qty = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(vendor);
        dest.writeString(location);
        dest.writeString(qty);
    }

    public static final Parcelable.Creator<DataItem> CREATOR = new Parcelable.Creator<DataItem>(){
        public DataItem createFromParcel(Parcel pc){
            return new DataItem(pc);
        }
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };

    public DataItem(Parcel pc){
        ID = pc.readString();
        vendor = pc.readString();
        location = pc.readString();
        qty = pc.readString();
    }
}
