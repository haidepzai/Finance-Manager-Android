package de.hdmstuttgart.financemanager.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.TransactionItem;

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

    //Methode wird aufgerufen, sobald ViewHolder initialisiert werden muss
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //Übergibt die Werte in den RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionItem currentItem = mItemList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mText1.setText(currentItem.getmText1());
        holder.mText2.setText(currentItem.getmText2());
    }

    //Anzahl der Elemente in der Liste (Größe der Liste)
    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
