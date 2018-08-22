package ru.develop_for_android.taifun.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.text.DecimalFormat;

import ru.develop_for_android.taifun.R;

public class FoodWithCount extends FoodEntry {
    int count;
    long final_price;
    long discount;

    public FoodWithCount(@NonNull String id, String categoryId, String title, String description, long weight, String weightQuantifier, Long price, String imageAddressNetwork) {
        super(id, categoryId, title, description, weight, weightQuantifier, price, imageAddressNetwork);
    }

    public int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }

    public long getDiscount() {
        return discount;
    }

    void setDiscount(long discount) {
        this.discount = discount;
    }

    public String getFinalPrice(Context context) {
        DecimalFormat df = new DecimalFormat("0.00");
        double priceWithDot = (double) (final_price * count) / 100;
        return context.getString(R.string.money, df.format(priceWithDot));
    }
}
