package ru.develop_for_android.taifun_data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "category",
        indices = {@Index("title")})
public class CategoryEntry {
    @PrimaryKey()
    @NonNull private String id;
    String title;

    public CategoryEntry(@NonNull String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
