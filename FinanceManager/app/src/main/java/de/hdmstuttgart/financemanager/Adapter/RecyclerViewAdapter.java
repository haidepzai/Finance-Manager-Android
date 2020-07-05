package de.hdmstuttgart.financemanager.Adapter;

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

import de.hdmstuttgart.financemanager.Activity.MainActivity;
import de.hdmstuttgart.financemanager.Database.Transaction;
import de.hdmstuttgart.financemanager.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private ArrayList<Transaction> mItemList;
    private ArrayList<Transaction> mItemListFull; //Kopie der Liste (Für Suchen)

    private OnNoteListener mOnNoteListener; //Click Listener für die einzelnen Elemente

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mIcon;
        public TextView mPurpose;
        public TextView mAmount;
        public TextView mDate;

        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.itemIcon);
            mPurpose = itemView.findViewById(R.id.text1);
            mAmount = itemView.findViewById(R.id.text2);
            mDate = itemView.findViewById(R.id.text3);

            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition()); //Position des angeklickten Element wird übergeben
        }
    }

    //Interface für OnClickListener
    public interface OnNoteListener {
        void onNoteClick(int position); //Diese Methode ist zu implementieren (MainFragment)
    }

    public RecyclerViewAdapter(ArrayList<Transaction> itemList, OnNoteListener onNoteListener) {
        this.mItemList = itemList;
        this.mItemListFull = new ArrayList<>(mItemList); //Kopie der Liste um eigenständig zu nutzen
        this.mOnNoteListener = onNoteListener;
    }

    //Methode wird aufgerufen, sobald ViewHolder initialisiert werden muss
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        ViewHolder vh = new ViewHolder(v, mOnNoteListener);

        List<Transaction> transactions = MainActivity.db.transactionDetailDao().getList();

        Transaction.itemList.clear();
        Transaction.itemList.addAll(transactions);

        return vh;
    }

    //Übergibt die Werte in den RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction currentItem = mItemList.get(position);

        holder.mIcon.setImageResource(currentItem.getmImageResource());
        holder.mPurpose.setText(currentItem.getmPurpose());
        holder.mAmount.setText(currentItem.getmAmount());
        holder.mDate.setText(currentItem.getmDate());

        if (holder.mAmount.getText().toString().contains("+")) {
            holder.mAmount.setTextColor(Color.parseColor("#00BF27"));
        } else {
            holder.mAmount.setTextColor(Color.RED);
        }
    }

    //Anzahl der Elemente in der Liste (Größe der Liste)
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    //Suche (Filter)
    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Transaction> filteredList = new ArrayList<>(); //Liste mit gefilterten Items

            //Wenn nichts angegeben ist, alles anzeigen
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mItemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim(); //trim = ignoriert whitespace am Anfang/Ende

                //Iterate in full List to check match
                for (Transaction item : mItemListFull) {
                    if (item.getmPurpose().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            //FilteredResults beinhaltet entweder komplette Liste oder nur die gesuchten Strings
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        //Neue Liste wird angezeigt, je nach dem was man sucht
        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mItemList.clear();
            List<Transaction> list = (List<Transaction>) results.values;
            mItemList.addAll(list);
            notifyDataSetChanged();
        }
    };
}
