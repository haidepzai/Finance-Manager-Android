package de.hdmstuttgart.financemanager.presentation.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.domain.model.TransactionDetail;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> implements Filterable {

    private final List<TransactionDetail> mItemList = new ArrayList<>();
    private final List<TransactionDetail> mItemListFull = new ArrayList<>();
    private final OnTransactionClickListener mListener;

    public interface OnTransactionClickListener {
        void onTransactionClick(TransactionDetail transaction);
    }

    public TransactionAdapter(OnTransactionClickListener listener) {
        this.mListener = listener;
    }

    public void setTransactions(List<TransactionDetail> transactions) {
        mItemList.clear();
        if (transactions != null) {
            mItemList.addAll(transactions);
        }
        mItemListFull.clear();
        mItemListFull.addAll(mItemList);
        notifyDataSetChanged();
    }

    public List<TransactionDetail> getCurrentList() {
        return mItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionDetail currentItem = mItemList.get(position);

        holder.mIcon.setImageResource(currentItem.getImageResource());
        holder.mPurpose.setText(currentItem.getPurpose());
        holder.mAmount.setText(currentItem.getAmount());
        holder.mDate.setText(currentItem.getDate());

        if (currentItem.getAmount().contains("+")) {
            holder.mAmount.setTextColor(Color.parseColor("#00BF27"));
        } else {
            holder.mAmount.setTextColor(Color.RED);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onTransactionClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIcon;
        public TextView mPurpose;
        public TextView mAmount;
        public TextView mDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.itemIcon);
            mPurpose = itemView.findViewById(R.id.text1);
            mAmount = itemView.findViewById(R.id.text2);
            mDate = itemView.findViewById(R.id.text3);
        }
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private final Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<TransactionDetail> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mItemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TransactionDetail item : mItemListFull) {
                    if (item.getPurpose().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mItemList.clear();
            mItemList.addAll((List<TransactionDetail>) results.values);
            notifyDataSetChanged();
        }
    };
}
