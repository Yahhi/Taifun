package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredient")
public class IngredientEntry {
    @PrimaryKey(autoGenerate = true)
    int id;
    String title;

    public IngredientEntry(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
