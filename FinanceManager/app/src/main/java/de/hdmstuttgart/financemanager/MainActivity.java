package de.hdmstuttgart.financemanager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    FloatingActionButton fab;
    Dialog myDialog; //Popup Fenster

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDialog = new Dialog(this);

        fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtClose;
                myDialog.setContentView(R.layout.custompopup);
                txtClose = myDialog.findViewById(R.id.txtClose);
                txtClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
            }
        });

        ArrayList<TransactionItem> itemList = new ArrayList<>();
        itemList.add(new TransactionItem(R.drawable.ic_euro, "Mensa Aufladung", "-10,00 €"));
        itemList.add(new TransactionItem(R.drawable.ic_dollar, "Google Pay Aufladung", "-20,00 $"));
        itemList.add(new TransactionItem(R.drawable.ic_euro, "Vapiano SE", "-9,00 €"));

        //Initialize RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewAdapter(itemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
