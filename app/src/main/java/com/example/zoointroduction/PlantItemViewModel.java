package com.example.zoointroduction;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

public class PlantItemViewModel extends ViewModel {

    LiveData<PagedList<Results>> itemPagedList;
    MutableLiveData<PlantItemDataSource> liveDataSource;

    public PlantItemViewModel(String queryString) {
        PlantItemDataSourceFactory itemDataSourceFactory = new PlantItemDataSourceFactory(queryString);
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(PlantItemDataSource.LIMIT).build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, pagedListConfig))
                .build();
    }
}
