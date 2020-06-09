package de.hdmstuttgart.financemanager.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

import de.hdmstuttgart.financemanager.Adapter.RecyclerViewAdapter;
import de.hdmstuttgart.financemanager.ItemDetailActivity;
import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.TransactionItem;


public class MainFragment extends Fragment implements RecyclerViewAdapter.OnNoteListener{
    private RecyclerView mRecyclerView;
    public static RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton fab;
    private Dialog myDialog; //Popup Fenster


    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public static TransactionItem mItem;

    private Button addBill;

    private EditText payPurpose;
    private EditText payAmount;
    private TextView mDisplayDate;
    private EditText payMethod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        myDialog = new Dialog(Objects.requireNonNull(getContext()));

        //Change Date
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDisplayDate = myDialog.findViewById(R.id.inputDate);
                month = month + 1;
                String date = dayOfMonth + "." + month + "." + year;
                mDisplayDate.setText(date);
            }
        };
        //Floating Action Button unten rechts
        fab = rootView.findViewById(R.id.fab1);
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

                payPurpose = myDialog.findViewById(R.id.inputPurpose);
                payAmount = myDialog.findViewById(R.id.inputAmount);
                payMethod = myDialog.findViewById(R.id.inputMethod);
                mDisplayDate = myDialog.findViewById(R.id.inputDate);
                //Öffnet Datum-Feld
                mDisplayDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                                android.R.style.Theme_DeviceDefault_Light_Dialog, //Calender Style
                                mDateSetListener,
                                year, month, day);
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        dialog.show();
                    }
                });
                //Button im Dialog um Rechnung hinzuzufügen
                addBill = myDialog.findViewById(R.id.addBill);
                addBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TransactionItem.addEntry(new TransactionItem(R.drawable.ic_euro_black,
                                payPurpose.getText().toString(),
                                payAmount.getText().toString(),
                                mDisplayDate.getText().toString(),
                                payMethod.getText().toString()
                                ));
                        mAdapter.notifyDataSetChanged();
                        myDialog.dismiss();
                    }
                });
            }
        });

        //Initialize RecyclerView
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecyclerViewAdapter(TransactionItem.itemList, this);

        //Swiper
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) { //Linker Swipe
            //Andere Swipe Richtungen (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP)

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            //Swipe löschen
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                TransactionItem.itemList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    //Wechselt in ItemDetailActivity und zeigt ausführliche Infos an
    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("Purpose", TransactionItem.itemList.get(position).getmPurpose());
        intent.putExtra("Amount", TransactionItem.itemList.get(position).getmAmount());
        intent.putExtra("Date", TransactionItem.itemList.get(position).getmDate());
        intent.putExtra("Method", TransactionItem.itemList.get(position).getmMethod());
        startActivity(intent);
    }
}
