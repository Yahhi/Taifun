package ru.develop_for_android.taifun;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ru.develop_for_android.taifun_data.AppDatabase;

public class FoodInfoViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase databaseInstance;
    private final String foodId;

    FoodInfoViewModelFactory(AppDatabase database, String foodId) {
        this.databaseInstance = database;
        this.foodId = foodId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FoodInfoViewModel(databaseInstance, foodId);
    }
}
