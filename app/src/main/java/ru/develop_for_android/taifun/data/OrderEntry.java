package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.develop_for_android.taifun.MyInfoViewModel;
import ru.develop_for_android.taifun.R;

@Entity(tableName = "orders",
        foreignKeys = @ForeignKey(entity = AddressEntry.class, parentColumns = "id", childColumns = "address_id"),
        indices = @Index(value = "address_id"))
public class OrderEntry {
    public static final int STATUS_NEW = 0;
    public static final int STATUS_PLACED = 1;
    private static final int statusPlacedTime = 50;
    public static final int STATUS_CONFIRMED = 2;
    private static final int statusConfirmedTime = 45;
    public static final int STATUS_PROCESSED = 3;
    private static final int statusProcessedTime = 25;
    private static final int statusReadyTime = 5;
    public static final int STATUS_READY_TO_PICKUP = 4;
    public static final int STATUS_IN_DELIVERY = 5;
    public static final int STATUS_FINISHED = 10;
    public static final int STATUS_SCHEDULED = 11;

    public static final int UNFINISHED_ORDER_ID = 100;

    @PrimaryKey(autoGenerate = false)
    int id;
    @ColumnInfo(name = "global_number")
    String globalNumber;
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
    @ColumnInfo(name = "delivery_price")
    Long deliveryPrice = 0L;

    @Ignore
    public OrderEntry(int addressId, String phone, String person, String comment) {
        this.addressId = addressId;
        this.phone = phone;
        this.person = person;
        this.comment = comment;
        dateStamp = new Date().getTime();
    }

    public OrderEntry(int id, Long dateStamp, Long scheduleStamp, int status, int addressId,
                      String phone, String person, String comment, Long deliveryPrice) {
        this.id = id;
        this.dateStamp = dateStamp;
        this.scheduleStamp = scheduleStamp;
        this.status = status;
        this.addressId = addressId;
        this.phone = phone;
        this.person = person;
        this.comment = comment;
        this.deliveryPrice = deliveryPrice;
    }

    public static String getStatusReadable(int status) {
        switch (status) {
            case STATUS_PLACED:
                return "In progress. Estimated time: " + statusPlacedTime;
            case STATUS_CONFIRMED:
                return "In progress. Estimated time: " + statusConfirmedTime;
            case STATUS_PROCESSED:
                return "In progress. Estimated time: " + statusProcessedTime;
            case STATUS_READY_TO_PICKUP:
                return "Ready. Please take your order home";
            case STATUS_IN_DELIVERY:
                return "In delivery";
            case STATUS_SCHEDULED:
                return "Waiting for suitable time";
            case STATUS_FINISHED:
                return "Finished";
            default:
                return "";
        }
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

    public Long getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Long deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getTitle(Context context) {
        Date created = new Date(dateStamp);
        if (status == STATUS_FINISHED) {
            Date now = new Date();
            String createdString;
            if ((now.getTime() - dateStamp) > 1000 * 60 * 60 * 24) {
                createdString = new SimpleDateFormat("dd MMM", Locale.getDefault())
                        .format(created);
            } else {
                createdString = new SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(created);
            }
            return context.getString(R.string.order_finished_at, createdString);
        } else {
            return context.getString(R.string.in_progress,
                    new SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(created));

        }
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

    public String getGlobalNumber() {
        return globalNumber;
    }

    void setGlobalNumber(String globalNumber) {
        this.globalNumber = globalNumber;
    }

    public int getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }
}
