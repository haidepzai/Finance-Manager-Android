package de.hdmstuttgart.financemanager.Database;

import java.util.ArrayList;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaction")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "image")
    public int mImageResource; //int because R.drawable Images are saved as integers
    @ColumnInfo(name = "purpose")
    public String mPurpose;
    @ColumnInfo(name = "amount")
    public String mAmount;
    @ColumnInfo(name = "date")
    public String mDate;
    @ColumnInfo(name = "category")
    public String mCategory;
    @ColumnInfo(name = "method")
    public String mMethod;

    //private double amountNumber;

    //Die Liste, die im MainFragment angezeigt wird.
    public static ArrayList<Transaction> itemList = new ArrayList<>();
    public static ArrayList<Transaction> outcomingBills = new ArrayList<>();
    public static ArrayList<Transaction> incomingBills = new ArrayList<>();

    public Transaction(int imageResource, String purpose, String amount, String date, String category, String method){
        this.mImageResource = imageResource;
        this.mPurpose = purpose;
        this.mAmount = amount;
        this.mDate = date;
        this.mCategory = category;
        this.mMethod = method;
    }

    public static void addEntry(Transaction item){
        itemList.add(0, item); //0 = an oberster Stelle der Liste
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getmPurpose() {
        return mPurpose;
    }

    public String getmAmount() {
        return mAmount;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmMethod() {
        return mMethod;
    }

    public String getmCategory() {
        return mCategory;
    }

}
