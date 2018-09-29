package ru.develop_for_android.taifun_data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredient_in_food",
        foreignKeys = {@ForeignKey(entity = FoodEntry.class, parentColumns = "id", childColumns = "food_id"),
                        @ForeignKey(entity = IngredientEntry.class, parentColumns = "id", childColumns = "ingredient_id")},
        indices = {@Index("ingredient_id"), @Index("food_id")})
public class IngredientsInFoodEntry {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "ingredient_id")
    int ingredientId;
    @ColumnInfo(name = "food_id")
    String foodId;

    public IngredientsInFoodEntry(int ingredientId, String foodId) {
        this.ingredientId = ingredientId;
        this.foodId = foodId;
    }
}
