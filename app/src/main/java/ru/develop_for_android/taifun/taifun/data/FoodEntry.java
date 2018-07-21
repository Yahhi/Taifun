package ru.develop_for_android.taifun.taifun.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "food", indices = @Index("category_id"),
    foreignKeys = @ForeignKey(entity = CategoryEntry.class, parentColumns = "id", childColumns = "category_id"))
public class FoodEntry {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "category_id")
    int categoryId;
    String title;
    String description;
    int weight;
    @ColumnInfo(name = "weight_quantifier")
    String weightQuantifier;
    Long price;
    @ColumnInfo(name = "image_local")
    String imageAddressLocal;
    @ColumnInfo(name = "image_network")
    String imageAddressNetwork;

    public FoodEntry(int categoryId, String title, String description, int weight, String weightQuantifier, Long price, String imageAddressNetwork) {
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

    public int getWeight() {
        return weight;
    }

    public String getWeightQuantifier() {
        return weightQuantifier;
    }

    public Long getPrice() {
        return price;
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

    public int getId() {
        return id;
    }

    public String getReadableWeight() {
        return String.valueOf(weight) + " " + weightQuantifier;
    }

    int getCategoryId() {
        return categoryId;
    }

    void setId(int id) {
        this.id = id;
    }
}
