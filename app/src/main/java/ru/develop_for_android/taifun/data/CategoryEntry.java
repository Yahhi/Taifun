package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "category",
        indices = {@Index("title")})
public class CategoryEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    String title;

    public String getTitle() {
        return title;
    }

    public CategoryEntry(String title) {
        this.title = title;
    }

    int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
