package de.hdmstuttgart.financemanager.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import de.hdmstuttgart.financemanager.R;

public class StatisticViewAdapter extends RecyclerView.Adapter<StatisticViewAdapter.StatisticViewHolder> {

    private HashMap<String, String> mItemList;

    public static class StatisticViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIcon;
        public TextView mCategory;
        public TextView mAmount;

        public StatisticViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.itemIcon);
            mCategory = itemView.findViewById(R.id.text1);
            mAmount = itemView.findViewById(R.id.text2);
        }
    }

    public StatisticViewAdapter(HashMap<String, String> itemList){
        this.mItemList = itemList;
    }

    @NonNull
    @Override
    public StatisticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        StatisticViewHolder vh = new StatisticViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull StatisticViewHolder holder, int position) {
        //String currentItem = mItemList.get(position);

        //holder.mIcon.setImageResource(currentItem.getmImageResource());
        //holder.mCategory.setText(currentItem.toString());
        //holder.mAmount.setText(currentItem.toString());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
