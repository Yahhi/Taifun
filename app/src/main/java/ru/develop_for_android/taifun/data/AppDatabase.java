package ru.develop_for_android.taifun.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import ru.develop_for_android.taifun.R;

@Database(entities = {CategoryEntry.class, FoodEntry.class, IngredientEntry.class,
            OrderContentEntry.class, OrderContentRemovedIngredientsEntry.class,
            OrderEntry.class, PromoEntry.class, PromoActiveFoodEntry.class, AddressEntry.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "food";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                AppExecutors.getInstance().diskIO().execute(() -> {
                                            getInstance(context).foodDao().addAddress(new AddressEntry(context.getString(R.string.address_title_home), ""));
                                            getInstance(context).foodDao().newOrder(OrderEntry.getNewOrder(context));
                                            
                                        }
                                );
                            }
                        })
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract FoodDao foodDao();
}
