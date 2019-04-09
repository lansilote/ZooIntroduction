package com.example.zoointroduction;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class PlantItemViewModelFactory implements ViewModelProvider.Factory {
    private String mQueryString;

    public PlantItemViewModelFactory(String mQueryString) {
        this.mQueryString = mQueryString;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new PlantItemViewModel(mQueryString);
    }
}
