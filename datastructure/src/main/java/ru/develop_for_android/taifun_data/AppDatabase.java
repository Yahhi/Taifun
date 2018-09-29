package ru.develop_for_android.taifun_data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import static ru.develop_for_android.taifun_data.OrderEntry.STATUS_NEW;
import static ru.develop_for_android.taifun_data.OrderEntry.UNFINISHED_ORDER_ID;


@Database(entities = {CategoryEntry.class, FoodEntry.class, IngredientEntry.class,
            OrderContentEntry.class, OrderContentRemovedIngredientsEntry.class,
            OrderEntry.class, OrderStatusEntry.class,
        PromoEntry.class, PromoActiveFoodEntry.class, AddressEntry.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String NAME_KEY = "name";
    public static final String PHONE_KEY = "phone";
    public static final String DEFAULT_ADDRESS_ID_KEY = "address_id";

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "food";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                AppExecutors.getInstance().diskIO().execute(() -> {
                                            getInstance(context).foodDao().addAddress(new AddressEntry(
                                                    context.getString(R.string.address_title_home), ""));
                                            getInstance(context).foodDao().newOrder(getNewOrder(context));
                                            
                                        }
                                );
                            }
                        })
                        .build();
            }
        }
        return sInstance;
    }


    public static OrderEntry getNewOrder(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        OrderEntry order = new OrderEntry(preferences.getInt(DEFAULT_ADDRESS_ID_KEY, 1),
                preferences.getString(NAME_KEY, ""),
                preferences.getString(PHONE_KEY, ""), "");
        order.id = UNFINISHED_ORDER_ID;
        order.status = STATUS_NEW;
        return order;
    }

    public abstract FoodDao foodDao();
}
