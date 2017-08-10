package com.example.gadau.sqldemo.data;

import android.content.Context;
import android.content.Intent;

/**
 * Created by gadau on 8/10/2017.
 */

public class MenuOption{
    private int header;
    private int desc;
    private int color;
    private Intent intent;
    public MenuOption(int h, int d, int c, Intent i) {
        header = h;
        desc = d;
        color = c;
        intent = i;
    }

    public int getHeader() { return header; }
    public int getDesc() { return desc; }
    public int getColor() { return color; }
    public Intent getIntent() {return intent; }
    public void setHeader(int header) { this.header = header; }
    public void setDesc(int desc) { this.desc = desc; }
    public void setColor(int color) {this.color = color; }
    public void setIntent(Intent i) { intent = i; }
}