package ru.develop_for_android.taifun_data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;

import java.text.DecimalFormat;


@Entity (tableName = "food", indices = @Index("category_id"),
    foreignKeys = @ForeignKey(entity = CategoryEntry.class, parentColumns = "id", childColumns = "category_id"))
public class FoodEntry {
    @PrimaryKey()
    @NonNull String id;
    @ColumnInfo(name = "category_id")
    String categoryId;
    String title;
    String description;
    long weight;
    @ColumnInfo(name = "weight_quantifier")
    String weightQuantifier;
    Long price;
    @ColumnInfo(name = "image_local")
    String imageAddressLocal;
    @ColumnInfo(name = "image_network")
    String imageAddressNetwork;

    public FoodEntry(@NonNull String id, String categoryId, String title, String description, long weight, String weightQuantifier, Long price, String imageAddressNetwork) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.weight = weight;
        this.weightQuantifier = weightQuantifier;
        this.price = price;
        this.imageAddressNetwork = imageAddressNetwork;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getWeight() {
        return weight;
    }

    public String getWeightQuantifier() {
        return weightQuantifier;
    }

    public Long getPrice() {
        return price;
    }

    public String getReadablePrice(Context context) {
        if (price == null) {
            return context.getString(R.string.no_price);
        } else {
            DecimalFormat df = new DecimalFormat("0.00");
            double priceWithDot = (double) price / 100;
            return context.getString(R.string.money, df.format(priceWithDot));
        }
    }

    public String getImageAddressLocal() {
        return imageAddressLocal;
    }

    public String getImageAddressNetwork() {
        return imageAddressNetwork;
    }

    public void setImageAddressLocal(String imageAddressLocal) {
        this.imageAddressLocal = imageAddressLocal;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getReadableWeight() {
        return String.valueOf(weight) + " " + weightQuantifier;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
