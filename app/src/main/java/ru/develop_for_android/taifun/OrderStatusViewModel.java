package ru.develop_for_android.taifun;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.OrderStatusEntry;
import ru.develop_for_android.taifun.data.OrderWithFood;
import ru.develop_for_android.taifun.networking.CheckStatus;
import ru.develop_for_android.taifun.networking.RemoteApi;

public class OrderStatusViewModel extends ViewModel
{
    public static final String RESULT_SUCCESS = "success";
    private MutableLiveData<OrderWithFood> order;
    private LiveData<List<OrderStatusEntry>> statuses;
    private AppDatabase database;
    private final int orderId;
    private MutableLiveData<String> networkResult;

    public OrderStatusViewModel(@NonNull AppDatabase database, int orderId) {
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

    public void updateOrderStatus() {
        sendNetworkRequest(order.getValue().getOrderEntry().getGlobalNumber());
    }


    private void sendNetworkRequest(long remoteId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://178.206.238.2:3444/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RemoteApi api = retrofit.create(RemoteApi.class);

        Call<Integer> status = api.getOrderStatus(new CheckStatus(remoteId));

        status.enqueue(new Callback<Integer>() {

            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful()) {
                    Log.i("NETWORK", String.valueOf(response.body()));
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        OrderStatusEntry statusEntry = new OrderStatusEntry(orderId, response.body(), 0);
                        database.foodDao().addOrderStatus(statusEntry);
                        database.foodDao().updateOrderStatus(orderId, response.body());
                        computeOrderData();
                        networkResult.postValue(RESULT_SUCCESS);
                    });
                } else {
                    Log.i("NETWORK", "response code: " + response.code());
                    networkResult.postValue(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                Log.i("NETWORK", "failure " + t.getMessage());
                networkResult.postValue(t.getLocalizedMessage());
            }
        });
    }

    public LiveData<List<OrderStatusEntry>> getStatuses() {
        return statuses;
    }

    public MutableLiveData<String> getNetworkResult() {
        return networkResult;
    }
}
