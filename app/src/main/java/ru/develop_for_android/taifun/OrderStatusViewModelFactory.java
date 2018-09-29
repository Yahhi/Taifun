package ru.develop_for_android.taifun;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ru.develop_for_android.taifun_data.AppDatabase;

public class OrderStatusViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase databaseInstance;
    private final int orderId;

    OrderStatusViewModelFactory(AppDatabase database, int orderId) {
        this.databaseInstance = database;
        this.orderId = orderId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new OrderStatusViewModel(databaseInstance, orderId);
    }
}
