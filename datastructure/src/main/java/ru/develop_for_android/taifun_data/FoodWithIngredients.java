package ru.develop_for_android.taifun_data;

import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.List;

public class FoodWithIngredients extends FoodEntry {
    @Relation(entity = IngredientEntry.class, parentColumn = "id", entityColumn = "food_id")
    List<IngredientEntry> ingredientEntries;

    public FoodWithIngredients(@NonNull String id, String categoryId, String title,
                               String description, long weight, String weightQuantifier,
                               Long price, String imageAddressNetwork) {
        super(id, categoryId, title, description, weight, weightQuantifier, price, imageAddressNetwork);
    }

    public String getReadableIngredientsList() {
        StringBuilder result = new StringBuilder();
        if (ingredientEntries != null && ingredientEntries.size() > 0) {
            for (IngredientEntry ingredient : ingredientEntries) {
                result.append(ingredient.getTitle());
            }
        }
        return result.toString();
    }

    public List<IngredientEntry> getIngredientEntries() {
        return ingredientEntries;
    }
}
