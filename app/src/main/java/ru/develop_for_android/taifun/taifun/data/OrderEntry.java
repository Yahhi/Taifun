package ru.develop_for_android.taifun.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "orders")
public class OrderEntry {
    public static final int STATUS_NEW = 0;
    public static final int STATUS_PLACED = 1;
    public static final int STATUS_CONFIRMED = 2;
    public static final int STATUS_PROCESSED = 3;
    public static final int STATUS_READY_TO_PICKUP = 4;
    public static final int STATUS_IN_DELIVERY = 5;
    public static final int STATUS_FINISHED = 10;
    public static final int STATUS_SCHEDULED = 11;

    static final int UNFINISHED_ORDER_ID = 1;

    @PrimaryKey(autoGenerate = false)
    int id;
    @ColumnInfo(name = "date_stamp")
    Long dateStamp;
    @ColumnInfo(name = "schedule_stamp")
    Long scheduleStamp;
    int status = STATUS_NEW;
    String address;
    String phone;
    String person;
    String comment;
    @ColumnInfo(name = "total")
    Long totalPrice;
    Long discount;
    @ColumnInfo(name = "delivery_price")
    Long deliveryPrice = 0L;

    public OrderEntry(String address, String phone, String person, String comment) {
        this.address = address;
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

    public String getAddress() {
        return address;
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

    public static OrderEntry getNewOrder() {
        OrderEntry order = new OrderEntry("", "", "", "");
        order.id = UNFINISHED_ORDER_ID;
        return order;
    }
}
