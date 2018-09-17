package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.develop_for_android.taifun.data.AddressEntry;
import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.FoodWithCount;
import ru.develop_for_android.taifun.data.OrderEntry;
import ru.develop_for_android.taifun.data.OrderWithFood;
import timber.log.Timber;

public class OrderConfirmationViewModel extends AndroidViewModel {

    MutableLiveData<OrderWithFood> unfinishedOrder;
    LiveData<List<AddressEntry>> addresses;
    LiveData<AddressEntry> selectedAddress;
    MutableLiveData<Integer> selectedAddressId;
    MutableLiveData<Integer> finishedId;
    MutableLiveData<String> networkResult;

    public static final String RESULT_OK = "success";

    public OrderConfirmationViewModel(@NonNull Application application) {
        super(application);
        unfinishedOrder = new MutableLiveData<>();
        AppExecutors.getInstance().diskIO().execute(() ->
                unfinishedOrder.postValue(AppDatabase.getInstance(getApplication()).foodDao().
                getOrderWithContent(OrderEntry.UNFINISHED_ORDER_ID)));
        addresses = AppDatabase.getInstance(getApplication()).foodDao().getActualAddresses();

        selectedAddressId = new MutableLiveData<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        selectedAddressId.postValue(preferences.getInt(MyInfoViewModel.DEFAULT_ADDRESS_ID_KEY, -100));

        selectedAddress = Transformations.switchMap(selectedAddressId,
                id -> AppDatabase.getInstance(getApplication()).foodDao().getAddress(id));
        finishedId = new MutableLiveData<>();
        networkResult = new MutableLiveData<>();
    }

    public void finishOrder(String additionalInfo, Date schedule) {
        if (selectedAddressId.getValue() == null) {
            networkResult.postValue(getApplication().getString(R.string.no_selected_address));
            return;
        }
        if (unfinishedOrder.getValue() == null || selectedAddressId.getValue() == null) {
            networkResult.postValue(getApplication().getString(R.string.order_is_not_ready));
            Timber.w("No correct value while confirming order");
            return;
        }

        OrderWithFood orderWithFood = unfinishedOrder.getValue();
        OrderEntry order = orderWithFood.getOrderEntry();
        order.setComment(additionalInfo);
        order.setDateStamp(new Date().getTime());
        order.setAddressId(selectedAddressId.getValue());
        if (schedule != null) {
            order.setScheduleStamp(schedule.getTime());
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        order.setPerson(preferences.getString(MyInfoViewModel.NAME_KEY, "-"));
        order.setPhone(preferences.getString(MyInfoViewModel.PHONE_KEY, "-"));

        String address;
        if (selectedAddressId.getValue() == OrderEntry.NO_DELIVERY) {
            address = "pick-up";
        } else {
            address = selectedAddress.getValue().getAddressLine1();
        }
        saveOrderInCloud(orderWithFood, address);
    }

    private void saveOrderInCloud(OrderWithFood order, String address) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", order.getOrderEntry().getPerson());
        data.put("address", address);
        data.put("phone", order.getOrderEntry().getPhone());
        data.put("comment", order.getOrderEntry().getComment());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    String remoteId = documentReference.getId();
                    Timber.i("order entry added with ID: %s", remoteId);
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        AppDatabase.getInstance(getApplication()).foodDao()
                                .updateRemoteId(order.getOrderEntry().getId(), remoteId);
                        finishedId.postValue(AppDatabase.getInstance(getApplication()).foodDao()
                                .finishOrder(order.getOrderEntry(), getApplication()));
                    });
                    saveFoodInOrder(order, remoteId);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e);
                    networkResult.postValue(e.getLocalizedMessage());
                });
    }

    private void saveFoodInOrder(OrderWithFood order, String documentId) {
        for (FoodWithCount foodInOrder : order.getFoodInOrder()) {
            Map<String, Object> data = new HashMap<>();
            data.put("foodId", foodInOrder.getId());
            data.put("categoryId", foodInOrder.getCategoryId());
            data.put("count", foodInOrder.getCount());
            data.put("price", foodInOrder.getPrice());
            data.put("discount", foodInOrder.getDiscount());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("orders").document(documentId).collection("food")
                    .add(data)
                    .addOnSuccessListener(documentReference -> {
                        String remoteId = documentReference.getId();
                        Timber.i("food entry added written with ID: %s", remoteId);
                    })
                    .addOnFailureListener(e -> {
                        Timber.w(e);
                        networkResult.postValue(e.getLocalizedMessage());
                    });
        }
    }
}
