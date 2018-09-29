package ru.develop_for_android.taifun;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.develop_for_android.taifun_data.AppDatabase;
import ru.develop_for_android.taifun_data.CategoryEntry;
import ru.develop_for_android.taifun_data.FoodWithIngredients;

public class CategoriesListViewModel extends AndroidViewModel {
    LiveData<List<CategoryEntry>> categories;
    private Map<String, LiveData<List<FoodWithIngredients>>> foodListsForFragments;

    public CategoriesListViewModel(@NonNull Application application) {
        super(application);
        foodListsForFragments = new HashMap<>();
        categories = AppDatabase.getInstance(getApplication()).foodDao().getCategories();
    }

    LiveData<List<FoodWithIngredients>> getFoodListForCategory(String categoryId) {
        if (!foodListsForFragments.containsKey(categoryId)) {
            foodListsForFragments.put(categoryId, AppDatabase.getInstance(getApplication())
                    .foodDao().getFoodInCategory(categoryId));
        }
        return foodListsForFragments.get(categoryId);
    }


}
