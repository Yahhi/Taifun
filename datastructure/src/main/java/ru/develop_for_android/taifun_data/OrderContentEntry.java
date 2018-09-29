package ru.develop_for_android.taifun_data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "order_content", foreignKeys = {
        @ForeignKey(entity = OrderEntry.class, parentColumns = "id", childColumns = "order_id",
                onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = FoodEntry.class, parentColumns = "id", childColumns = "food_id")},
        indices = {@Index("food_id"), @Index("order_id")})
public class OrderContentEntry implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "food_id")
    String foodId;
    int count;
    @ColumnInfo(name = "order_id")
    int orderId;
    @ColumnInfo(name = "actual_price")
    Long actualPrice;
    @ColumnInfo(name = "actual_discount")
    Long actualDiscount;

    public OrderContentEntry(String foodId, int orderId, Long actualPrice, Long actualDiscount) {
        this.foodId = foodId;
        this.orderId = orderId;
        this.actualPrice = actualPrice;
        this.actualDiscount = actualDiscount;
        count = 1;
    }

    protected OrderContentEntry(Parcel in) {
        id = in.readInt();
        foodId = in.readString();
        count = in.readInt();
        orderId = in.readInt();
        if (in.readByte() == 0) {
            actualPrice = null;
        } else {
            actualPrice = in.readLong();
        }
        if (in.readByte() == 0) {
            actualDiscount = null;
        } else {
            actualDiscount = in.readLong();
        }
    }

    public static final Creator<OrderContentEntry> CREATOR = new Creator<OrderContentEntry>() {
        @Override
        public OrderContentEntry createFromParcel(Parcel in) {
            return new OrderContentEntry(in);
        }

        @Override
        public OrderContentEntry[] newArray(int size) {
            return new OrderContentEntry[size];
        }
    };

    public String getFoodId() {
        return foodId;
    }

    public Long getActualPrice() {
        return actualPrice;
    }

    public Long getActualDiscount() {
        return actualDiscount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(foodId);
        dest.writeInt(count);
        dest.writeInt(orderId);
        if (actualPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(actualPrice);
        }
        if (actualDiscount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(actualDiscount);
        }
    }
}
