package ru.develop_for_android.taifun;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import ru.develop_for_android.taifun_data.AppDatabase;
import ru.develop_for_android.taifun_data.FoodWithIngredients;
import ru.develop_for_android.taifun_data.OrderContentEntry;
import ru.develop_for_android.taifun_data.OrderEntry;

public class FoodInfoViewModel extends ViewModel {

    private LiveData<FoodWithIngredients> food;
    private AppDatabase database;
    private String foodId;
    private LiveData<OrderContentEntry> existingFoodInOrder;

    FoodInfoViewModel(@NonNull AppDatabase database, String foodId) {
        this.database = database;
        this.foodId = foodId;
        food = database.foodDao().getFoodById(foodId);
        existingFoodInOrder = database.foodDao().getFoodInOrder(OrderEntry.UNFINISHED_ORDER_ID, foodId);
    }

    public LiveData<FoodWithIngredients> getFood() {
        return food;
    }

    void addFoodToOrder() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (existingFoodInOrder.getValue() == null) {
                String categoryId = food.getValue().getCategoryId();
                long initialPrice = food.getValue().getPrice();
                long actualPrice = database.foodDao().getPriceWithMaximumDiscount(foodId, categoryId, initialPrice);
                OrderContentEntry newItemInOrder = new OrderContentEntry(foodId, OrderEntry.UNFINISHED_ORDER_ID, actualPrice,
                        initialPrice - actualPrice);
                database.foodDao().addFoodToOrder(newItemInOrder);
            } else {
                database.foodDao().increaseFoodCount(OrderEntry.UNFINISHED_ORDER_ID, foodId);
            }
        });
    }

    LiveData<OrderContentEntry> getExistingFoodInOrder() {
        return existingFoodInOrder;
    }
}
