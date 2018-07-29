package ru.develop_for_android.taifun.taifun.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.content.Context;

import java.util.List;

import static ru.develop_for_android.taifun.taifun.data.OrderEntry.UNFINISHED_ORDER_ID;

@Dao
public abstract class FoodDao {

    @Insert
    abstract void addCategories(List<CategoryEntry> categoryEntry);
    @Insert
    abstract void addFood(List<FoodEntry> foodEntry);
    @Insert
    abstract void addIngredients(List<IngredientEntry> ingredientEntry);
    @Insert
    abstract void addIngredientsInFood(List<IngredientsInFoodEntry> ingredients);
    @Transaction
    public void addDownloadedFoodInfo(List<CategoryEntry> categoryEntries, List<FoodEntry> foodEntries,
                               List<IngredientEntry> ingredients, List<IngredientsInFoodEntry> foodIngredients) {
        addCategories(categoryEntries);
        addFood(foodEntries);
        addIngredients(ingredients);
        addIngredientsInFood(foodIngredients);
    }

    @Insert
    abstract void addPromo(List<PromoEntry> promoEntries);
    @Insert
    abstract void addPromoFoodDefinitions(List<PromoActiveFoodEntry> foodEntries);
    @Transaction
    public void addDownloadedPromoInfo(List<PromoEntry> promoEntries, List<PromoActiveFoodEntry> foodEntries) {
        addPromo(promoEntries);
        addPromoFoodDefinitions(foodEntries);
    }

    @Query("SELECT * FROM category ORDER BY title")
    public abstract LiveData<List<CategoryEntry>> getCategories();

    @Query("SELECT * FROM food WHERE category_id = :categoryId ORDER BY title")
    public abstract LiveData<List<FoodEntry>> getFoodInCategory(int categoryId);

    @Query("SELECT * FROM ingredient_in_food LEFT JOIN ingredient ON " +
            "ingredient_in_food.ingredient_id = ingredient.id WHERE food_id = :foodId")
    public abstract LiveData<List<IngredientEntry>> getIngredientsInFood(int foodId);

    @Query("SELECT * FROM orders WHERE status <> " + OrderEntry.STATUS_FINISHED)
    public abstract LiveData<List<OrderEntry>> getActiveOrders();

    @Query("SELECT * FROM orders WHERE status = " + OrderEntry.STATUS_FINISHED)
    public abstract LiveData<List<OrderEntry>> getArchiveOrders();

    @Insert
    public abstract void newOrder(OrderEntry orderEntry);

    @Update
    public abstract void updateOrderInfo(OrderEntry orderEntry);

    @Insert
    public abstract void addFoodToOrder(OrderContent foodInOrder);

    @Update
    public abstract void updateFoodCount(OrderContent foodInOrder);

    @Query("SELECT id FROM orders ORDER BY id DESC LIMIT 1")
    abstract int getLastOrderId();

    @Transaction
    public void finishOrder(OrderEntry unfinishedOrder, Context context) {
        unfinishedOrder.id = getLastOrderId() + 1;
        updateOrderInfo(unfinishedOrder);
        newOrder(OrderEntry.getNewOrder(context));
    }

    @Query("SELECT * FROM orders WHERE id = :id")
    public abstract LiveData<OrderEntry> getOrderById(int id);

    @Query("SELECT * FROM orders WHERE id = :id")
    abstract OrderEntry getUnfinishedOrder(int id);

    public LiveData<OrderEntry> getUnfinishedOrder() {
        return getOrderById(UNFINISHED_ORDER_ID);
    }

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    public abstract LiveData<OrderWithFood> getOrderWithContent(int orderId);

    @Query("SELECT * FROM promo")
    public abstract LiveData<List<PromoEntry>> getAllPromo();

    @Query("SELECT * FROM address WHERE visible > 0 ORDER BY title")
    public abstract LiveData<List<AddressEntry>> getActualAddresses();

    @Query("SELECT * FROM address WHERE id = :id")
    abstract AddressEntry getAddressById(int id);

    @Insert
    public abstract void addAddress(AddressEntry addressEntry);

    @Update
    abstract void saveAddress(AddressEntry addressEntry);

    @Transaction
    public void updateAddress(AddressEntry addressEntry) {
        /*int oldId = addressEntry.id;
        addAddress(addressEntry);
        AddressEntry oldAddress = getAddressById(oldId);
        oldAddress.visible = 0;*/
        saveAddress(addressEntry);
    }

    public void makeAddressInvisible(AddressEntry addressEntry) {
        addressEntry.visible = 0;
        saveAddress(addressEntry);
    }

    @Query("SELECT discount_currency, discount_percent FROM promo_food_inside LEFT JOIN promo ON promo_id = promo.id " +
            "WHERE food_item_id = :foodId OR food_category_id = :categoryId ORDER BY discount_currency DESC LIMIT 1")
    abstract Discount getBestFixedDiscount(int foodId, int categoryId);

    @Query("SELECT discount_currency, discount_percent FROM promo_food_inside LEFT JOIN promo ON promo_id = promo.id " +
            "WHERE food_item_id = :foodId OR food_category_id = :categoryId ORDER BY discount_percent DESC LIMIT 1")
    abstract Discount getBestPercentDiscount(int foodId, int categoryId);

    public Long getPriceWithMaximumDiscount(int foodId, int categoryId, Long initialPrice) {
        Discount percentDiscount = getBestPercentDiscount(foodId, categoryId);
        Discount fixedDiscount = getBestFixedDiscount(foodId, categoryId);
        Long actualPriceWithPercent = percentDiscount.apply(initialPrice);
        Long actualPriceWithFixed = fixedDiscount.apply(initialPrice);
        if (actualPriceWithPercent > actualPriceWithFixed)
            return actualPriceWithFixed;
        else
            return actualPriceWithPercent;
    }

}
