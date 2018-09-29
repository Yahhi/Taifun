package ru.develop_for_android.taifun_data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "ingredient",
        foreignKeys = {@ForeignKey(entity = FoodEntry.class, parentColumns = "id", childColumns = "food_id")},
        indices = {@Index("food_id")})
public class IngredientEntry {
    @PrimaryKey()
    @NonNull String id;
    @ColumnInfo(name = "food_id")
    String foodId;
    String title;

    IngredientEntry(@NonNull String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
