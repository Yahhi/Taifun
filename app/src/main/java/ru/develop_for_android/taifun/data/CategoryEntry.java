package ru.develop_for_android.taifun.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "category",
        indices = {@Index("title")})
public class CategoryEntry {
    @PrimaryKey(autoGenerate = false)
    @NonNull private String id;
    String title;

    public CategoryEntry(@NonNull String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
