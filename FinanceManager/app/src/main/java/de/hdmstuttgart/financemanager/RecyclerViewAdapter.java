package de.hdmstuttgart.financemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<TransactionItem> mItemList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mText1;
        public TextView mText2;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mText1 = itemView.findViewById(R.id.text1);
            mText2 = itemView.findViewById(R.id.text2);
        }
    }

    public RecyclerViewAdapter(ArrayList<TransactionItem> itemList){
        this.mItemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //Pass Values to the items on the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionItem currentItem = mItemList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mText1.setText(currentItem.getmText1());
        holder.mText2.setText(currentItem.getmText2());
    }

    //How many items are in the list
    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
