package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.OrderEntry;
import ru.develop_for_android.taifun.data.OrderWithFood;

public class OrderListViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<OrderWithFood>> orders;
    LiveData<Integer> update;

    public OrderListViewModel(@NonNull Application application) {
        super(application);
        orders = new MutableLiveData<>();
        LiveData<List<OrderEntry>> simpleOrders = AppDatabase.getInstance(getApplication()).foodDao().getActiveOrders();
        update = Transformations.map(simpleOrders, o -> {
            AppExecutors.getInstance().diskIO().execute(() -> {
                        ArrayList<OrderWithFood> rrr = AppDatabase.getInstance(getApplication())
                                .foodDao().getOrdersWithFood(o);
                    if (rrr != null) {
                        orders.postValue(rrr);
                        Log.i("FOOD","orders calculated in background " + rrr.size());
                    }
            });
            return 1;
        });
    }

    public LiveData<ArrayList<OrderWithFood>> getOrders() {
        return orders;
    }

    public void openOrderInfo(Context context, int orderId) {
        Intent orderInfoOpener = new Intent(context, OrderStatusActivity.class);
        orderInfoOpener.putExtra(OrderStatusActivity.ORDER_ID_KEY, orderId);
        context.startActivity(orderInfoOpener);
    }
}
