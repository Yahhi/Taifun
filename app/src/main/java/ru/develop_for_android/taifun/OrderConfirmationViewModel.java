package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

import ru.develop_for_android.taifun.data.AddressEntry;
import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.OrderEntry;
import ru.develop_for_android.taifun.data.OrderWithFood;

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
        OrderEntry orderWithFood = unfinishedOrder.getValue().getOrderEntry();
        orderWithFood.setComment(additionalInfo);
        orderWithFood.setDateStamp(new Date().getTime());
        orderWithFood.setAddressId(selectedAddressId.getValue());
        if (schedule != null) {
            orderWithFood.setScheduleStamp(schedule.getTime());
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        orderWithFood.setPerson(preferences.getString(MyInfoViewModel.NAME_KEY, "-"));
        orderWithFood.setPhone(preferences.getString(MyInfoViewModel.PHONE_KEY, "-"));
        AppExecutors.getInstance().diskIO().execute(() -> {
            finishedId.postValue(AppDatabase.getInstance(getApplication()).foodDao().finishOrder(orderWithFood, getApplication()));
            //TODO send network request
        });
    }
}
