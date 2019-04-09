package com.example.zoointroduction;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

public class ItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<ItemDataSource> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, Results> create() {
        ItemDataSource itemDataSource = new ItemDataSource();
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<ItemDataSource> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
