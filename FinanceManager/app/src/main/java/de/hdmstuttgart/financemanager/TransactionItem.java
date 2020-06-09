package de.hdmstuttgart.financemanager;

import java.util.ArrayList;

public class TransactionItem {
    private int mImageResource; //int because R.drawable Images are saved as integers
    private String mPurpose;
    private String mAmount;
    private String mDate;
    private String mMethod;

    //Die Liste, die im MainFragment angezeigt wird.
    public static ArrayList<TransactionItem> itemList = new ArrayList<>();

    public TransactionItem(int imageResource, String purpose, String amount, String date, String method){
        this.mImageResource = imageResource;
        this.mPurpose = purpose;
        this.mAmount = amount;
        this.mDate = date;
        this.mMethod = method;
    }

    public static void addEntry(TransactionItem item){
        itemList.add(0, item);
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
}
