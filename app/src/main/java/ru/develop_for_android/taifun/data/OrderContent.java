package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "order_content", foreignKeys = {
        @ForeignKey(entity = OrderEntry.class, parentColumns = "id", childColumns = "order_id",
                onUpdate = CASCADE, onDelete = CASCADE),
        @ForeignKey(entity = FoodEntry.class, parentColumns = "id", childColumns = "food_id")},
        indices = {@Index("food_id"), @Index("order_id")})
public class OrderContent {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "food_id")
    String foodId;
    int count;
    @ColumnInfo(name = "order_id")
    int orderId;
    @ColumnInfo(name = "actual_price")
    Long actualPrice;
    @ColumnInfo(name = "actual_discount")
    Long finalPrice;

    public OrderContent(String foodId, int orderId, Long actualPrice, Long finalPrice) {
        this.foodId = foodId;
        this.orderId = orderId;
        this.actualPrice = actualPrice;
        this.finalPrice = finalPrice;
    }

    public String getFoodId() {
        return foodId;
    }

    public Long getActualPrice() {
        return actualPrice;
    }

    public Long getFinalPrice() {
        return finalPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}