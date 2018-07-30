package ru.develop_for_android.taifun.networking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.AppExecutors;
import ru.develop_for_android.taifun.data.CategoryEntry;
import ru.develop_for_android.taifun.data.FoodEntry;

public class FoodSyncService extends JobIntentService {
    public static final int jobId = 1234;
    FirebaseFirestore remoteDb;

    public FoodSyncService() {
        remoteDb = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        syncFood();
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
                            document1.getLong("price"),
                            document1.getString("image_address")
                    ));
                } catch (RuntimeException e) {
                    Crashlytics.log(Log.ERROR, "FIREBASE", "weight is not number for " + document1.getId());
                }
            }
            AppExecutors.getInstance().diskIO().execute(() ->
                    AppDatabase.getInstance(getBaseContext()).foodDao()
                            .addDownloadedFoodInfo(categoryEntry, foodInCategory));
        });
    }
}
