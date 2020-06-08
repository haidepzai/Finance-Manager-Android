package de.hdmstuttgart.financemanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import de.hdmstuttgart.financemanager.Adapter.RecyclerViewAdapter;

public class OldMainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    FloatingActionButton fab;
    Dialog myDialog; //Popup Fenster

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        myDialog = new Dialog(this);

        //Change Date
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDisplayDate = myDialog.findViewById(R.id.input2);
                month = month + 1;
                String date = dayOfMonth + "." + month + "." + year;
                mDisplayDate.setText(date);
            }
        };
        //Floating Action Button unten rechts
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

    /*
    //Methode wird aufgerufen, sobald man auf den TextView "Datum" klickt (Zeigt Kalender)
    public void setDate(View v){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
                android.R.style.Theme_DeviceDefault_Light_Dialog, //Calender Style
                mDateSetListener,
                year, month, day);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
    }
    */
}
