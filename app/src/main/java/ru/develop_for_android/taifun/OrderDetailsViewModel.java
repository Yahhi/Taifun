package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.OrderEntry;
import ru.develop_for_android.taifun.data.OrderWithFood;

public class OrderDetailsViewModel extends AndroidViewModel {

    private MutableLiveData<OrderWithFood> order;

    public OrderDetailsViewModel(@NonNull Application application) {
        super(application);
        AppExecutors.getInstance().diskIO().execute(() ->
                order.postValue(AppDatabase.getInstance(application).foodDao()
                        .getOrderWithContent(OrderEntry.UNFINISHED_ORDER_ID)));
    }

    public MutableLiveData<OrderWithFood> getOrder() {
        return order;
    }
}
