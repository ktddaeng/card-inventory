package com.example.gadau.sqldemo.view;

import com.example.gadau.sqldemo.data.DataItem;

import java.util.List;

/**
 * Created by gadau on 7/28/2017.
 */

public interface ViewInterface {
    void launchInfoPage(String itemID);
    void setUpAdapterAndView(List<DataItem> listOfData);
}
