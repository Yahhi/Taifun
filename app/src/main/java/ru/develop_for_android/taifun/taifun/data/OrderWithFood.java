package ru.develop_for_android.taifun.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Relation;

import java.util.List;

public class OrderWithFood extends OrderEntry {
    @Relation(parentColumn = "id", entityColumn = "food_id", entity = OrderContent.class)
    List<OrderContent> foodInOrder;

    public OrderWithFood(String address, String phone, String person, String comment) {
        super(address, phone, person, comment);
    }
}
