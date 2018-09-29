package ru.develop_for_android.taifun;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import ru.develop_for_android.taifun_data.AppDatabase;
import ru.develop_for_android.taifun_data.OrderStatusEntry;
import ru.develop_for_android.taifun_data.OrderWithFood;

public class OrderStatusViewModel extends ViewModel
{
    static final String RESULT_SUCCESS = "success";
    private MutableLiveData<OrderWithFood> order;
    private LiveData<List<OrderStatusEntry>> statuses;
    private AppDatabase database;
    private final int orderId;
    private MutableLiveData<String> networkResult;

    OrderStatusViewModel(@NonNull AppDatabase database, int orderId) {
        this.database = database;
        this.orderId = orderId;
        order = new MutableLiveData<>();
        statuses = database.foodDao().getOrderStatuses(orderId);
        networkResult = new MutableLiveData<>();
        computeOrderData();
    }

    private void computeOrderData() {
        AppExecutors.getInstance().diskIO().execute(() ->
                order.postValue(database.foodDao().getOrderWithContent(orderId)));
    }

    public MutableLiveData<OrderWithFood> getOrder() {
        return order;
    }

    LiveData<List<OrderStatusEntry>> getStatuses() {
        return statuses;
    }

    MutableLiveData<String> getNetworkResult() {
        return networkResult;
    }
}
