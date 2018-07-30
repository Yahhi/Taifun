package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.StringTokenizer;

@Entity(tableName = "promo_food_inside",
        foreignKeys = {
            @ForeignKey(entity = PromoEntry.class, parentColumns = "id", childColumns = "promo_id"),
            @ForeignKey(entity = CategoryEntry.class, parentColumns = "id", childColumns = "food_category_id"),
            @ForeignKey(entity = FoodEntry.class, parentColumns = "id", childColumns = "food_item_id")
        }, indices = {@Index("promo_id"), @Index("food_category_id"), @Index("food_item_id")})
public class PromoActiveFoodEntry {
    @PrimaryKey(autoGenerate = false)
    @NonNull String id;
    @ColumnInfo(name = "promo_id")
    String promoId;
    @ColumnInfo(name = "food_category_id")
    String foodCategoryId;
    @ColumnInfo(name = "food_item_id")
    String foodItemId;

    public String getId() {
        return id;
    }

    public String getPromoId() {
        return promoId;
    }
}
