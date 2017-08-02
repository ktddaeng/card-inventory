package com.example.gadau.sqldemo.logic;

import com.example.gadau.sqldemo.data.DataItem;
import com.example.gadau.sqldemo.data.DataSourceInterface;
import com.example.gadau.sqldemo.view.ViewInterface;

/**
 * Created by gadau on 8/2/2017.
 */

public class ListController {
    private ViewInterface view;
    private DataSourceInterface dataSource;

    public ListController(ViewInterface view, DataSourceInterface dataSource, int order) {
        this.view = view;
        this.dataSource = dataSource;

        getListFromDataSource(order);
    }

    public void getListFromDataSource(int order) {
        view.setUpAdapterAndView(
                dataSource.getListofData(order)
        );
    }

    public void onItemClick(DataItem dataItem) {
        view.launchInfoPage(dataItem.getID());
    }
}
