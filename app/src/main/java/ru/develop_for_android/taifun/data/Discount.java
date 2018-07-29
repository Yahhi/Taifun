package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.ColumnInfo;

public class Discount {
    @ColumnInfo(name = "discount_currency")
    Long discountInCurrency;

    String title;

    @ColumnInfo(name = "discount_percent")
    int discountInPercent;

    public Long apply(Long initialPrice) {
        if (discountInPercent == 0) {
            return initialPrice - discountInCurrency;
        } else {
            return ((100 - discountInPercent) / 100) * initialPrice;
        }
    }
}
