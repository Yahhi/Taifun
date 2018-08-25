package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.List;

import ru.develop_for_android.taifun.data.AddressEntry;
import ru.develop_for_android.taifun.data.AppDatabase;

public class MyInfoViewModel extends AndroidViewModel {
    public static final String NAME_KEY = "name";
    public static final String PHONE_KEY = "phone";
    public static final String DEFAULT_ADDRESS_ID_KEY = "address_id";

    private String userName;
    private String userPhone;
    private int defaultAddressId;
    private LiveData<List<AddressEntry>> addresses;

    public MyInfoViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        userName = preferences.getString(NAME_KEY, "Name");
        userPhone = preferences.getString(PHONE_KEY, "Phone");
        defaultAddressId = preferences.getInt(DEFAULT_ADDRESS_ID_KEY, -1);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        addresses = database.foodDao().getActualAddresses();
    }

    public LiveData<List<AddressEntry>> getAddresses() {
        return addresses;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString(NAME_KEY, userName);
        prefEditor.apply();
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString(PHONE_KEY, userPhone);
        prefEditor.apply();
    }

    public void setDefaultAddressId(int id) {
        this.defaultAddressId = id;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putInt(DEFAULT_ADDRESS_ID_KEY, id);
        prefEditor.apply();
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }
}
