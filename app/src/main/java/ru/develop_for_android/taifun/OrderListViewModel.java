package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.OrderWithFood;

public class OrderListViewModel extends AndroidViewModel {

    private MutableLiveData<List<OrderWithFood>> orders;

    public OrderListViewModel(@NonNull Application application) {
        super(application);
        orders = new MutableLiveData<>();
        AppExecutors.getInstance().diskIO().execute(() ->
                orders.postValue(AppDatabase.getInstance(getApplication()).foodDao().getOrdersWithFood()));
    }

    public MutableLiveData<List<OrderWithFood>> getOrders() {
        return orders;
    }
}
