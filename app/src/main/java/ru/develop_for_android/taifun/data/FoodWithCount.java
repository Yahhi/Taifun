package ru.develop_for_android.taifun.data;

import android.support.annotation.NonNull;

public class FoodWithCount extends FoodEntry {
    int count;

    public FoodWithCount(@NonNull String id, String categoryId, String title, String description, long weight, String weightQuantifier, Long price, String imageAddressNetwork) {
        super(id, categoryId, title, description, weight, weightQuantifier, price, imageAddressNetwork);
    }

    public int getCount() {
        return count;
    }

    void setCount(int count) {
        this.count = count;
    }
}
