package ru.develop_for_android.taifun.taifun.data;

import android.arch.persistence.room.Relation;

import java.util.List;

public class OrderWithFood extends OrderEntry {
    @Relation(parentColumn = "id", entityColumn = "food_id", entity = OrderContent.class)
    List<OrderContent> foodInOrder;

    public OrderWithFood(int addressId, String phone, String person, String comment) {
        super(addressId, phone, person, comment);
    }
}
