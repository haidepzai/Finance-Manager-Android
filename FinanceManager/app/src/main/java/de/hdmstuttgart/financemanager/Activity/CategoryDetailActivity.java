package de.hdmstuttgart.financemanager.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdmstuttgart.financemanager.Adapter.RecyclerViewAdapter;
import de.hdmstuttgart.financemanager.Database.Transaction;
import de.hdmstuttgart.financemanager.R;

public class CategoryDetailActivity extends AppCompatActivity implements RecyclerViewAdapter.OnNoteListener {
    public static RecyclerViewAdapter mAdapter;

    private ArrayList<Transaction> resultList;

    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        TextView headerView = findViewById(R.id.categoryHeader);

        resultList = new ArrayList<>();

        Intent in = this.getIntent();
        category = in.getStringExtra("CategoryKey");

        headerView.setText(category);

        addCategoryToList();

        RecyclerView mRecyclerView = findViewById(R.id.recyclerCategoryView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewAdapter(resultList, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    //Click Listener der Items in der RecyclerView
    @Override
    public void onNoteClick(int position) {
        //Übergibt die Informationen mit putExtra
        Intent intent = new Intent(this, ItemDetailActivity.class);

        for (Transaction item : Transaction.itemList) {
            if (resultList.get(position).uid == item.uid) {
                intent.putExtra("ID", item.uid);
                int index = Transaction.itemList.indexOf(item);
                intent.putExtra("Position", index);
            }
        }
        intent.putExtra("Image", resultList.get(position).getmImageResource());
        intent.putExtra("Purpose", resultList.get(position).getmPurpose());
        intent.putExtra("Amount", resultList.get(position).getmAmount());
        intent.putExtra("Date", resultList.get(position).getmDate());
        intent.putExtra("Category", resultList.get(position).getmCategory());
        intent.putExtra("Method", resultList.get(position).getmMethod());

        /*
         * Unteres geht nicht, da die aktuelle Position in dieser Activity übergeben wird
         * und somit, beim Ändern eines Eintrages in der ItemDetailActivity, der falsche Eintrag
         * in der Hauptliste geändert wird!!
        */
        startActivity(intent);
    }

    private void addCategoryToList() {

        resultList.clear();
        for (Transaction item : Transaction.itemList) {
            if (item.mCategory.equals(category)) {
                resultList.add(item);
            }
        }
    }
}