package ru.develop_for_android.taifun_data;

import android.content.Context;

import java.text.DecimalFormat;
import java.util.List;

public class OrderWithFood {
    OrderEntry orderEntry;
    List<FoodWithCount> foodInOrder;

    private Long foodPrice, discount, totalPrice;

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

    public String getTotalPrice(Context context) {
        if (totalPrice == null) {
            calculateTotalValues();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        double priceWithDot = (double) totalPrice / 100;
        return context.getString(R.string.money, df.format(priceWithDot));
    }

    private void calculateTotalValues() {
        foodPrice = 0L;
        discount = 0L;
        totalPrice = 0L;
        if (foodInOrder != null) {
            for (FoodWithCount foodWithCount : foodInOrder) {
                foodPrice += foodWithCount.price * foodWithCount.count;
                discount += foodWithCount.discount * foodWithCount.count;
                totalPrice += foodWithCount.final_price * foodWithCount.count;
            }
        }
    }

    public String getTotalDiscount(Context context) {
        if (discount == null) {
            calculateTotalValues();
        }
        if (discount == 0) {
            return "-";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        double priceWithDot = (double) discount / 100;
        return context.getString(R.string.money, df.format(priceWithDot));
    }

    public String getFoodPrice(Context context) {
        if (foodPrice == null) {
            calculateTotalValues();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        double priceWithDot = (double) foodPrice / 100;
        return context.getString(R.string.money, df.format(priceWithDot));
    }

    public int getItemsCount() {
        int count = 0;
        for (FoodWithCount food : foodInOrder) {
            count += food.count;
        }
        return count;
    }
}
