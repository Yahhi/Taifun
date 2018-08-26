package ru.develop_for_android.taifun.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static ru.develop_for_android.taifun.data.OrderEntry.UNFINISHED_ORDER_ID;

@Dao
public abstract class FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void addCategories(CategoryEntry categoryEntry);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void addFood(List<FoodEntry> foodEntry);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void addIngredients(List<IngredientEntry> ingredientEntry);
    @Transaction
    public void addDownloadedFoodInfo(CategoryEntry categoryEntries, List<FoodEntry> foodEntries) {
        addCategories(categoryEntries);
        addFood(foodEntries);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void addPromo(List<PromoEntry> promoEntries);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void addPromoFoodDefinitions(List<PromoActiveFoodEntry> foodEntries);
    @Transaction
    public void addDownloadedPromoInfo(List<PromoEntry> promoEntries, List<PromoActiveFoodEntry> foodEntries) {
        addPromo(promoEntries);
        if (foodEntries != null) {
            addPromoFoodDefinitions(foodEntries);
        }
    }

    @Query("SELECT * FROM category ORDER BY title")
    public abstract LiveData<List<CategoryEntry>> getCategories();

    @Transaction
    @Query("SELECT * FROM food WHERE category_id = :categoryId ORDER BY title")
    public abstract LiveData<List<FoodWithIngredients>> getFoodInCategory(String categoryId);

    @Transaction
    @Query(("SELECT * FROM food WHERE id = :id"))
    public abstract LiveData<FoodWithIngredients> getFoodById(String id);

    @Query("SELECT * FROM orders WHERE status NOT IN (" +
            OrderEntry.STATUS_FINISHED + ", " + OrderEntry.STATUS_NEW + ")")
    public abstract LiveData<List<OrderEntry>> getActiveOrders();


    @Query("SELECT * FROM orders WHERE status <> " +
            OrderEntry.STATUS_FINISHED + " AND status <> " + OrderEntry.STATUS_NEW + "")
    abstract List<OrderEntry> getFinalActiveOrders();

    @Query("SELECT * FROM order_content WHERE order_id = :orderId AND food_id = :foodId")
    public abstract LiveData<OrderContentEntry> getFoodInOrder(int orderId, String foodId);

    @Query("UPDATE order_content SET count = count + 1 WHERE order_id = :orderId AND food_id = :foodId")
    public abstract void increaseFoodCount(int orderId, String foodId);

    @Query("SELECT * FROM orders WHERE status = " + OrderEntry.STATUS_FINISHED)
    public abstract List<OrderEntry> getFinalArchiveOrders();

    @Query("SELECT * FROM orders WHERE status = " + OrderEntry.STATUS_FINISHED)
    public abstract LiveData<List<OrderEntry>> getArchiveOrders();

    @Insert
    public abstract void newOrder(OrderEntry orderEntry);

    @Query("UPDATE orders SET id = :id, status = " + OrderEntry.STATUS_PLACED + " WHERE id = " + UNFINISHED_ORDER_ID)
    abstract void updateUnfinishedOrderId(int id);

    @Insert
    public abstract void addFoodToOrder(OrderContentEntry foodInOrder);

    @Query("DELETE FROM order_content WHERE order_id = :orderId AND food_id = :foodId")
    abstract void removeFoodFromOrder(int orderId, String foodId);

    @Query("UPDATE order_content SET count = :foodCount WHERE order_id = :orderId AND food_id = :foodId")
    abstract void updateFoodCount(long orderId, String foodId, int foodCount);

    public void updateFoodCount(String foodId, int foodCount) {
        if (foodCount == 0) {
            removeFoodFromOrder(OrderEntry.UNFINISHED_ORDER_ID, foodId);
        } else {
            updateFoodCount(OrderEntry.UNFINISHED_ORDER_ID, foodId, foodCount);
        }
    }

    @Query("SELECT id FROM orders ORDER BY id DESC LIMIT 1")
    abstract int getLastOrderId();

    @Transaction
    public int finishOrder(OrderEntry unfinishedOrder, Context context) {
        int id = getLastOrderId() + 1;
        updateUnfinishedOrderId(id);
        newOrder(OrderEntry.getNewOrder(context));
        return id;
    }

    @Query("SELECT * FROM orders WHERE id = :id")
    abstract OrderEntry getFinalOrderById(int id);

    @Query("SELECT * FROM orders WHERE id = :id")
    abstract LiveData<OrderEntry> getOrderById(int id);

    public LiveData<OrderEntry> getUnfinishedOrder() {
        return getOrderById(UNFINISHED_ORDER_ID);
    }

    public OrderWithFood getOrderWithContent(int orderId){
        OrderEntry order = getFinalOrderById(orderId);
        List<FoodWithCount> foodInOrder = getFoodListInOrder(orderId);
        return new OrderWithFood(order, foodInOrder);
    }

    public ArrayList<OrderWithFood> getOrdersWithFood(List<OrderEntry> orders) {
        ArrayList<OrderWithFood> ordersWithFood = new ArrayList<>();
        for (OrderEntry order : orders) {
            List<FoodWithCount> foodEntries = getFoodListInOrder(order.id);
            ordersWithFood.add(new OrderWithFood(order, foodEntries));
        }
        Log.i("ORDERS", "there are " + ordersWithFood.size() + " orders");
        return ordersWithFood;
    }

    @Query("SELECT food.*, order_content.count AS count, order_content.actual_price AS final_price, " +
            "order_content.actual_discount as discount " +
            "FROM order_content LEFT JOIN food ON order_content.food_id = food.id WHERE order_id = :orderId")
    abstract List<FoodWithCount> getFoodListInOrder(long orderId);

    @Query("SELECT * FROM order_status WHERE order_id = :orderId ORDER BY status ASC")
    public abstract List<OrderStatusEntry> getOrderStatuses(long orderId);

    @Insert
    public abstract void addOrderStatus(OrderStatusEntry statusEntry);

    @Query("SELECT * FROM promo")
    public abstract LiveData<List<PromoEntry>> getAllPromo();

    @Query("SELECT * FROM address WHERE visible > 0 ORDER BY title")
    public abstract LiveData<List<AddressEntry>> getActualAddresses();

    @Query("SELECT * FROM address WHERE id = :id")
    public abstract LiveData<AddressEntry> getAddress(int id);

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

    @Query("SELECT title, discount_currency, discount_percent FROM promo_food_inside LEFT JOIN promo ON promo_id = promo.id " +
            "WHERE food_item_id = :foodId OR food_category_id = :categoryId ORDER BY discount_currency DESC LIMIT 1")
    abstract Discount getBestFixedDiscount(String foodId, String categoryId);

    @Query("SELECT title, discount_currency, discount_percent FROM promo_food_inside LEFT JOIN promo ON promo_id = promo.id " +
            "WHERE food_item_id = :foodId OR food_category_id = :categoryId ORDER BY discount_percent DESC LIMIT 1")
    abstract Discount getBestPercentDiscount(String foodId, String categoryId);

    public Long getPriceWithMaximumDiscount(String foodId, String categoryId, Long initialPrice) {
        Discount percentDiscount = getBestPercentDiscount(foodId, categoryId);
        Discount fixedDiscount = getBestFixedDiscount(foodId, categoryId);
        Long actualPriceWithPercent;
        if (percentDiscount == null) {
            actualPriceWithPercent = initialPrice;
        } else {
            actualPriceWithPercent = percentDiscount.apply(initialPrice);
        }
        Long actualPriceWithFixed;
        if (fixedDiscount == null) {
            actualPriceWithFixed = initialPrice;
        } else {
            actualPriceWithFixed = fixedDiscount.apply(initialPrice);
        }
        if (actualPriceWithPercent > actualPriceWithFixed)
            return actualPriceWithFixed;
        else
            return actualPriceWithPercent;
    }

}
