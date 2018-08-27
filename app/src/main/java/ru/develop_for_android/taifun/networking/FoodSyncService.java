package ru.develop_for_android.taifun.networking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import ru.develop_for_android.taifun.AppExecutors;
import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.CategoryEntry;
import ru.develop_for_android.taifun.data.FoodEntry;
import ru.develop_for_android.taifun.data.PromoEntry;

public class FoodSyncService extends JobIntentService {
    public static final int jobId = 1234;
    FirebaseFirestore remoteDb;
    public static final String KEY_JOB_TYPE = "job_type";
    public static final String TYPE_FOOD = "food";
    public static final String TYPE_PROMO = "promo";

    public FoodSyncService() {
        remoteDb = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent.hasExtra(KEY_JOB_TYPE)) {
            if (intent.getStringExtra(KEY_JOB_TYPE).equals(TYPE_FOOD)) {
                syncFood();
            } else {
                syncPromo();
            }
        } else {
            syncFood();
            syncPromo();
        }
    }

    private void syncPromo() {
        remoteDb.collection("promo")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<PromoEntry> promoEntries = new ArrayList<>();
                        for (QueryDocumentSnapshot promoItem : task.getResult()) {
                            promoEntries.add(getPromoItem(promoItem));
                        }
                        AppExecutors.getInstance().diskIO().execute(() ->
                                AppDatabase.getInstance(getBaseContext()).foodDao()
                                        .addDownloadedPromoInfo(promoEntries, null));
                    }
                });
    }

    void syncFood() {
        remoteDb.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document :task.getResult()) {
                            saveCategory(document);
                        }
                    }
                });
    }

    private PromoEntry getPromoItem(QueryDocumentSnapshot promoItem) {
        String id = promoItem.getId();
        PromoEntry promoEntry = new PromoEntry(id, promoItem.getString("title"),
                promoItem.getString("description"), null, null,
                0, 0L, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                1, 1, "",
                promoItem.getString("imageAddress"));
        return promoEntry;
    }

    private void saveCategory(QueryDocumentSnapshot document) {
        String categoryId = document.getId();
        CategoryEntry categoryEntry = new CategoryEntry(categoryId, document.getString("title"));
        ArrayList<FoodEntry> foodInCategory = new ArrayList<>();
        remoteDb.collection("categories").document(categoryId).collection("food")
                .get()
        .addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document1 : task.getResult()) {
                try {
                    Long weight = document1.getLong("weight");
                    foodInCategory.add(new FoodEntry(
                            document1.getId(),
                            categoryId,
                            document1.getString("title"),
                            document1.getString("description"),
                            weight==null?0:weight,
                            document1.getString("weightQuantifier"),
                            document1.getLong("price_dollar"),
                            document1.getString("imageAddress")
                    ));
                } catch (RuntimeException e) {
                    Crashlytics.log(Log.ERROR, "FIREBASE", "weight is not number for " + document1.getId());
                }
            }
            Log.i("FSTORAGE", "saving remote data with categoryId=" + categoryId + " and " + foodInCategory.size() + " foods");
            AppExecutors.getInstance().diskIO().execute(() ->
                    AppDatabase.getInstance(getBaseContext()).foodDao()
                            .addDownloadedFoodInfo(categoryEntry, foodInCategory));
        });
    }
}
