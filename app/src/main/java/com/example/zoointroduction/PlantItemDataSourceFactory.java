package com.example.zoointroduction;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

public class PlantItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PlantItemDataSource> itemLiveDataSource = new MutableLiveData<>();
    private String queryString;

    public PlantItemDataSourceFactory(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public DataSource<Integer, Results> create() {
        PlantItemDataSource itemDataSource = new PlantItemDataSource(queryString);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PlantItemDataSource> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
