package de.hdmstuttgart.financemanager.Adapter;

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
import de.hdmstuttgart.financemanager.TransactionItem;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private ArrayList<TransactionItem> mItemList;
    private ArrayList<TransactionItem> mItemListFull; //Kopie der Liste (Für Suchen)

    private OnNoteListener mOnNoteListener; //Click Listener für die einzelnen Elemente

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImageView;
        public TextView mText1;
        public TextView mText2;

        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mText1 = itemView.findViewById(R.id.text1);
            mText2 = itemView.findViewById(R.id.text2);

            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition()); //Position des angeklickten Element wird übergeben
        }
    }
    //Interface für OnClickListener
    public interface OnNoteListener{
        void onNoteClick(int position); //Diese Methode ist zu implementieren (MainFragment)
    }

    public RecyclerViewAdapter(ArrayList<TransactionItem> itemList, OnNoteListener onNoteListener) {
        this.mItemList = itemList;
        this.mItemListFull = new ArrayList<>(mItemList); //Kopie der Liste um eigenständig zu nutzen
        this.mOnNoteListener = onNoteListener;
    }

    //Methode wird aufgerufen, sobald ViewHolder initialisiert werden muss
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ViewHolder vh = new ViewHolder(v, mOnNoteListener);
        return vh;
    }

    //Übergibt die Werte in den RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionItem currentItem = mItemList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mText1.setText(currentItem.getmPurpose());
        holder.mText2.setText(currentItem.getmAmount());
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
            List<TransactionItem> filteredList = new ArrayList<>(); //Liste mit gefiltereten Items

            //Wenn nichts angegeben ist, alles anzeigen
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mItemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                //Iterate in full List to check match
                for (TransactionItem item : mItemListFull){
                    if(item.getmPurpose().toLowerCase().contains(filterPattern)){
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
        protected void publishResults (CharSequence constraint, FilterResults results){
            mItemList.clear();
            mItemList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
