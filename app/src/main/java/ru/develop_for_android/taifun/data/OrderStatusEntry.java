package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "order_status",
        foreignKeys = {@ForeignKey(entity = OrderEntry.class, childColumns = "order_id", parentColumns = "id")},
        indices = {@Index("order_id"), @Index(value = {"order_id", "status"}, unique = true)})
public class OrderStatusEntry {
    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "order_id")
    int orderId;
    int status;
    long timestamp;
    int timeTillReady;

    OrderStatusEntry() {

    }

    @Ignore
    public OrderStatusEntry(int orderId, int status, int timeTillReady) {
        this.orderId = orderId;
        this.status = status;
        this.timeTillReady = timeTillReady;
        this.timestamp = new Date().getTime();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public Date getDateTime() {
        return new Date(timestamp);
    }

    public int getTimeTillReady() {
        return timeTillReady;
    }
    void setTimeTillReady(int timeTillReady) {
        this.timeTillReady = timeTillReady;
    }
}
