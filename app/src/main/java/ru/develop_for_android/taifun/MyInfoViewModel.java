package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.List;

import ru.develop_for_android.taifun_data.AddressEntry;
import ru.develop_for_android.taifun_data.AppDatabase;

import static ru.develop_for_android.taifun_data.AppDatabase.DEFAULT_ADDRESS_ID_KEY;
import static ru.develop_for_android.taifun_data.AppDatabase.NAME_KEY;
import static ru.develop_for_android.taifun_data.AppDatabase.PHONE_KEY;

public class MyInfoViewModel extends AndroidViewModel {

    private String userName;
    private String userPhone;
    private MutableLiveData<Integer> defaultAddressId;
    private LiveData<List<AddressEntry>> addresses;

    public MyInfoViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        userName = preferences.getString(NAME_KEY, "");
        userPhone = preferences.getString(PHONE_KEY, "");
        defaultAddressId = new MutableLiveData<>();
        defaultAddressId.postValue(preferences.getInt(DEFAULT_ADDRESS_ID_KEY, -1));

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        addresses = database.foodDao().getActualAddresses();
    }

    public LiveData<List<AddressEntry>> getAddresses() {
        return addresses;
    }

    void setUserName(String userName) {
        this.userName = userName;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString(NAME_KEY, userName);
        prefEditor.apply();
    }

    void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString(PHONE_KEY, userPhone);
        prefEditor.apply();
    }

    void setDefaultAddressId(int id) {
        this.defaultAddressId.postValue(id);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putInt(DEFAULT_ADDRESS_ID_KEY, id);
        prefEditor.apply();
    }

    MutableLiveData<Integer> getDefaultAddressId() {
        return defaultAddressId;
    }

    String getUserName() {
        return userName;
    }

    String getUserPhone() {
        return userPhone;
    }

    void deleteAddress(AddressEntry address) {

        AppExecutors.getInstance().diskIO().execute(() -> AppDatabase
                .getInstance(getApplication()).foodDao().makeAddressInvisible(address));
    }

    void updateAddress(AddressEntry addressEntry) {
        AppExecutors.getInstance().diskIO().execute(() -> AppDatabase.getInstance(getApplication()).foodDao().updateAddress(addressEntry));
    }
}
