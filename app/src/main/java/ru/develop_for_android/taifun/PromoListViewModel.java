package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.develop_for_android.taifun_data.AppDatabase;
import ru.develop_for_android.taifun_data.PromoEntry;

public class PromoListViewModel extends AndroidViewModel {

    LiveData<List<PromoEntry>> promoEntries;

    public PromoListViewModel(@NonNull Application application) {
        super(application);
        promoEntries = AppDatabase.getInstance(application).foodDao().getAllPromo();
    }


}
