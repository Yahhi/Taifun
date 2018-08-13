package ru.develop_for_android.taifun.data;

import java.util.List;

public class OrderWithFood {
    OrderEntry orderEntry;
    List<FoodWithCount> foodInOrder;

    public OrderWithFood(OrderEntry orderEntry, List<FoodWithCount> foodInOrder) {
        this.orderEntry = orderEntry;
        this.foodInOrder = foodInOrder;
    }

    public OrderEntry getOrderEntry() {
        return orderEntry;
    }

    public List<FoodWithCount> getFoodInOrder() {
        return foodInOrder;
    }
}
