package ru.develop_for_android.taifun_data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "address", indices = @Index("title"))
public class AddressEntry {
    @PrimaryKey(autoGenerate = true)
    int id;
    String title;
    String city = "";
    @ColumnInfo(name = "address_line1")
    String addressLine1;
    @ColumnInfo(name = "address_line2")
    String addressLine2 = "";
    int visible = 1;

    public AddressEntry(String title, String addressLine1) {
        this.title = title;
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getTitle() {
        return title;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }
}
