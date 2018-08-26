package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.develop_for_android.taifun.data.AddressEntry;
import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.OrderEntry;
import ru.develop_for_android.taifun.data.OrderWithFood;
import ru.develop_for_android.taifun.networking.RemoteApi;

public class OrderConfirmationViewModel extends AndroidViewModel {

    MutableLiveData<OrderWithFood> unfinishedOrder;
    LiveData<List<AddressEntry>> addresses;
    LiveData<AddressEntry> selectedAddress;
    MutableLiveData<Integer> selectedAddressId;
    MutableLiveData<Integer> finishedId;

    public OrderConfirmationViewModel(@NonNull Application application) {
        super(application);
        unfinishedOrder = new MutableLiveData<>();
        AppExecutors.getInstance().diskIO().execute(() ->
                unfinishedOrder.postValue(AppDatabase.getInstance(getApplication()).foodDao().
                getOrderWithContent(OrderEntry.UNFINISHED_ORDER_ID)));
        addresses = AppDatabase.getInstance(getApplication()).foodDao().getActualAddresses();

        selectedAddressId = new MutableLiveData<>();
        AppExecutors.getInstance().diskIO().execute(() -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
            selectedAddressId.postValue(preferences.getInt(MyInfoViewModel.DEFAULT_ADDRESS_ID_KEY, -100));
        });
        selectedAddress = Transformations.switchMap(selectedAddressId,
                id -> AppDatabase.getInstance(getApplication()).foodDao().getAddress(id));
        finishedId = new MutableLiveData<>();
    }

    public void finishOrder(String additionalInfo, Date schedule) {
        OrderEntry order = unfinishedOrder.getValue().getOrderEntry();
        order.setComment(additionalInfo);
        order.setDateStamp(new Date().getTime());
        order.setAddressId(selectedAddressId.getValue());
        if (schedule != null) {
            order.setScheduleStamp(schedule.getTime());
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        order.setPerson(preferences.getString(MyInfoViewModel.NAME_KEY, "-"));
        order.setPhone(preferences.getString(MyInfoViewModel.PHONE_KEY, "-"));
        AppExecutors.getInstance().diskIO().execute(() -> {
            finishedId.postValue(AppDatabase.getInstance(getApplication()).foodDao().finishOrder(order, getApplication()));
            sendNetworkRequest(order);
        });
    }

    private void sendNetworkRequest(OrderEntry order) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://178.206.238.2:3444/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RemoteApi api = retrofit.create(RemoteApi.class);

        Call<Long> remoteId = api.sendOrder(order);

        remoteId.enqueue(new Callback<Long>() {

            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    Log.i("NETWORK", String.valueOf(response.body()));
                } else {
                    Log.i("NETWORK", "response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.i("NETWORK", "failure " + t);
            }
        });
    }
}
