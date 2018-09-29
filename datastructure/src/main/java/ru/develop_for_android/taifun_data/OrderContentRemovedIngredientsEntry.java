package ru.develop_for_android.taifun_data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "removed_ingredients", foreignKeys = {
        @ForeignKey(entity = OrderContentEntry.class, parentColumns = "id",
                childColumns = "order_content_id", onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = IngredientEntry.class, parentColumns = "id", childColumns = "ingredient_id")
}, indices = {@Index("ingredient_id"),  @Index("order_content_id")})
public class OrderContentRemovedIngredientsEntry {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "order_content_id")
    int orderContentId;
    @ColumnInfo(name = "ingredient_id")
    int ingredientId;

    public OrderContentRemovedIngredientsEntry(int orderContentId, int ingredientId) {
        this.orderContentId = orderContentId;
        this.ingredientId = ingredientId;
    }

    int getOrderContentId() {
        return orderContentId;
    }
}
