package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "removed_ingredients", foreignKeys = {
        @ForeignKey(entity = OrderContent.class, parentColumns = "id",
                childColumns = "order_content_id", onUpdate = CASCADE, onDelete = CASCADE),
        @ForeignKey(entity = IngredientsInFoodEntry.class, parentColumns = "id", childColumns = "ingredient_id")
}, indices = {@Index("ingredient_id"),  @Index("order_content_id")})
public class OrderContentRemovedIngredients {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "order_content_id")
    int orderContentId;
    @ColumnInfo(name = "ingredient_id")
    int ingredientId;

    public OrderContentRemovedIngredients(int orderContentId, int ingredientId) {
        this.orderContentId = orderContentId;
        this.ingredientId = ingredientId;
    }

    int getOrderContentId() {
        return orderContentId;
    }
}
