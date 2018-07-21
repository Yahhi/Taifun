package ru.develop_for_android.taifun.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "promo_food_inside",
        foreignKeys = {
            @ForeignKey(entity = PromoEntry.class, parentColumns = "id", childColumns = "promo_id"),
            @ForeignKey(entity = CategoryEntry.class, parentColumns = "id", childColumns = "food_category_id"),
            @ForeignKey(entity = FoodEntry.class, parentColumns = "id", childColumns = "food_item_id")
        }, indices = {@Index("promo_id"), @Index("food_category_id"), @Index("food_item_id")})
public class PromoActiveFoodEntry {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "promo_id")
    int promoId;
    @ColumnInfo(name = "food_category_id")
    int foodCategoryId;
    @ColumnInfo(name = "food_item_id")
    int foodItemId;

    public int getId() {
        return id;
    }

    public int getPromoId() {
        return promoId;
    }
}
