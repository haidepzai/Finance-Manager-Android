package com.hdmstuttgart.financemanager.data.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaction")
public class TransactionEntity {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "image")
    public int imageResource;

    @ColumnInfo(name = "purpose")
    public String purpose;

    @ColumnInfo(name = "amount")
    public String amount;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "method")
    public String method;

    public TransactionEntity() {}

    public TransactionEntity(int imageResource, String purpose, String amount, String date, String category, String method) {
        this.imageResource = imageResource;
        this.purpose = purpose;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.method = method;
    }
}
