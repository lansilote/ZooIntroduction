package com.example.zoointroduction;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

public class ItemViewModel extends ViewModel {

    LiveData<PagedList<Results>> itemPagedList;
    MutableLiveData<ItemDataSource> liveDataSource;

    public ItemViewModel() {
        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory();
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(ItemDataSource.LIMIT).build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, pagedListConfig))
                .build();
    }
}
