package ru.develop_for_android.taifun;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.OrderStatusEntry;
import ru.develop_for_android.taifun.data.OrderWithFood;

public class OrderInfoViewModel extends ViewModel
{
    private MutableLiveData<OrderWithFood> order;
    private MutableLiveData<List<OrderStatusEntry>> statuses;
    private AppDatabase database;
    private int orderId;

    public OrderInfoViewModel(@NonNull AppDatabase database, int orderId) {
        this.database = database;
        this.orderId = orderId;
        order = new MutableLiveData<>();
        statuses = new MutableLiveData<>();
        computeOrderData();
    }

    private void computeOrderData() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            order.postValue(database.foodDao().getOrderWithContent(orderId));
            statuses.postValue(database.foodDao().getOrderStatuses(orderId));
        });
    }

    public MutableLiveData<OrderWithFood> getOrder() {
        return order;
    }

    public void updateOrderStatus() {
        //TODO send network request
        computeOrderData();
    }

    public MutableLiveData<List<OrderStatusEntry>> getStatuses() {
        return statuses;
    }
}
