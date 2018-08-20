package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
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

    public void openOrderInfo(Context context, int orderId) {
        Intent orderInfoOpener = new Intent(context, OrderInfoActivity.class);
        orderInfoOpener.putExtra(OrderInfoActivity.ORDER_ID_KEY, orderId);
        context.startActivity(orderInfoOpener);
    }
}
