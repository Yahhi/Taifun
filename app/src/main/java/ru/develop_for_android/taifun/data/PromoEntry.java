package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "promo")
public class PromoEntry {
    @PrimaryKey(autoGenerate = true)
    int id;
    String title;
    String description;
    @ColumnInfo(name = "start_date")
    Long startDate;
    @ColumnInfo(name = "finish_date")
    Long finishDate;

    @ColumnInfo(name = "discount_percent")
    int discountInPercent = 0;
    @ColumnInfo(name = "discount_currency")
    Long discountInCurrency = 0L;

    @ColumnInfo(name = "start0")
    int sundayStartTime = -1;
    @ColumnInfo(name = "end0")
    int sundayEndTime = -1;
    @ColumnInfo(name = "start1")
    int mondayStartTime = -1;
    @ColumnInfo(name = "end1")
    int mondayEndTime = -1;
    @ColumnInfo(name = "start2")
    int tuesdayStartTime = -1;
    @ColumnInfo(name = "end2")
    int tuesdayEndTime = -1;
    @ColumnInfo(name = "start3")
    int wednesdayStartTime = -1;
    @ColumnInfo(name = "end3")
    int wednesdayEndTime = -1;
    @ColumnInfo(name = "start4")
    int thursdayStartTime = -1;
    @ColumnInfo(name = "end4")
    int thursdayEndTime = -1;
    @ColumnInfo(name = "start5")
    int fridayStartTime = -1;
    @ColumnInfo(name = "end5")
    int fridayEndTime = -1;
    @ColumnInfo(name = "start6")
    int saturdayStartTime = -1;
    @ColumnInfo(name = "end6")
    int saturdayEndTime = -1;

    @ColumnInfo(name = "active_delivery")
    int activeOnDelivery = 1;
    @ColumnInfo(name = "active_cafe")
    int activeInCafe = 1;

    @ColumnInfo(name = "image_local")
    String imageAddressLocal;
    @ColumnInfo(name = "image_network")
    String imageAddressNetwork;

    public PromoEntry(int id, String title, String description, Long startDate, Long finishDate,
                      int discountInPercent, Long discountInCurrency, int sundayStartTime,
                      int sundayEndTime, int mondayStartTime, int mondayEndTime, int tuesdayStartTime,
                      int tuesdayEndTime, int wednesdayStartTime, int wednesdayEndTime,
                      int thursdayStartTime, int thursdayEndTime, int fridayStartTime,
                      int fridayEndTime, int saturdayStartTime, int saturdayEndTime,
                      int activeOnDelivery, int activeInCafe, String imageAddressLocal,
                      String imageAddressNetwork) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.discountInPercent = discountInPercent;
        this.discountInCurrency = discountInCurrency;
        this.sundayStartTime = sundayStartTime;
        this.sundayEndTime = sundayEndTime;
        this.mondayStartTime = mondayStartTime;
        this.mondayEndTime = mondayEndTime;
        this.tuesdayStartTime = tuesdayStartTime;
        this.tuesdayEndTime = tuesdayEndTime;
        this.wednesdayStartTime = wednesdayStartTime;
        this.wednesdayEndTime = wednesdayEndTime;
        this.thursdayStartTime = thursdayStartTime;
        this.thursdayEndTime = thursdayEndTime;
        this.fridayStartTime = fridayStartTime;
        this.fridayEndTime = fridayEndTime;
        this.saturdayStartTime = saturdayStartTime;
        this.saturdayEndTime = saturdayEndTime;
        this.activeOnDelivery = activeOnDelivery;
        this.activeInCafe = activeInCafe;
        this.imageAddressLocal = imageAddressLocal;
        this.imageAddressNetwork = imageAddressNetwork;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDiscountInPercent() {
        return discountInPercent;
    }

    public Long getDiscountInCurrency() {
        return discountInCurrency;
    }

    public int getActiveOnDelivery() {
        return activeOnDelivery;
    }

    public int getActiveInCafe() {
        return activeInCafe;
    }

    public Long getStartDate() {
        return startDate;
    }

    public String getImageAddressLocal() {
        return imageAddressLocal;
    }

    public String getImageAddressNetwork() {
        return imageAddressNetwork;
    }
}
