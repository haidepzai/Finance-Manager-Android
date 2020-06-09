package de.hdmstuttgart.financemanager;

import java.util.ArrayList;

public class TransactionItem {
    private int mImageResource; //int because R.drawable Images are saved as integers
    private String mText1;
    private String mText2;

    //Die Liste, die im MainFragment angezeigt wird.
    public static ArrayList<TransactionItem> itemList = new ArrayList<>();

    public TransactionItem(int imageResource, String text1, String text2){
        this.mImageResource = imageResource;
        this.mText1 = text1;
        this.mText2 = text2;
    }

    public static void addEntry(TransactionItem item){
        itemList.add(0, item);
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getmText1() {
        return mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}
