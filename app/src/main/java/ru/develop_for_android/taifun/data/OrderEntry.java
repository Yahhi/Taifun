package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Date;

import ru.develop_for_android.taifun.MyInfoViewModel;

@Entity(tableName = "orders", foreignKeys = @ForeignKey(entity = AddressEntry.class, parentColumns = "id", childColumns = "address_id"))
public class OrderEntry {
    public static final int STATUS_NEW = 0;
    public static final int STATUS_PLACED = 1;
    public static final int STATUS_CONFIRMED = 2;
    public static final int STATUS_PROCESSED = 3;
    public static final int STATUS_READY_TO_PICKUP = 4;
    public static final int STATUS_IN_DELIVERY = 5;
    public static final int STATUS_FINISHED = 10;
    public static final int STATUS_SCHEDULED = 11;

    public static final int UNFINISHED_ORDER_ID = 100;

    @PrimaryKey(autoGenerate = false)
    int id;
    @ColumnInfo(name = "date_stamp")
    Long dateStamp;
    @ColumnInfo(name = "schedule_stamp")
    Long scheduleStamp;
    int status = STATUS_NEW;
    @ColumnInfo(name = "address_id")
    int addressId;
    String phone;
    String person;
    String comment;
    @ColumnInfo(name = "total")
    Long totalPrice;
    Long discount;
    @ColumnInfo(name = "delivery_price")
    Long deliveryPrice = 0L;

    public OrderEntry(int addressId, String phone, String person, String comment) {
        this.addressId = addressId;
        this.phone = phone;
        this.person = person;
        this.comment = comment;
        dateStamp = new Date().getTime();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public String getPerson() {
        return person;
    }

    public String getComment() {
        return comment;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Long deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public static OrderEntry getNewOrder(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        OrderEntry order = new OrderEntry(preferences.getInt(MyInfoViewModel.DEFAULT_ADDRESS_ID_KEY, 1),
                preferences.getString(MyInfoViewModel.NAME_KEY, ""),
                preferences.getString(MyInfoViewModel.PHONE_KEY, ""), "");
        order.id = UNFINISHED_ORDER_ID;
        Log.i("FOOD", order.toString());
        return order;
    }
}
