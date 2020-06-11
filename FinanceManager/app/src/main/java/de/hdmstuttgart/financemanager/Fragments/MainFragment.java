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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

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
import de.hdmstuttgart.financemanager.Helper.CurrencyFormatter;
import de.hdmstuttgart.financemanager.ItemDetailActivity;
import de.hdmstuttgart.financemanager.PaymentMethods;
import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.TransactionItem;


public class MainFragment extends Fragment implements RecyclerViewAdapter.OnNoteListener, AdapterView.OnItemSelectedListener{
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
    private EditText mDisplayDate;
    private Spinner payMethod;
    private ImageButton calendar_btn;

    private static ArrayAdapter<String> mSpinner; //Adapter für Spinner (Dropdown Liste)

    private String paymentMethod; //Neue Zahlungsmethode wird in dieser Variable gespeichert
    private String formattedCurrency; //Formatierte Währung mit 2 Nachkommastellen

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Dialog (Popup Fenster) zum Hinzufügen weitere Elemente
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Floating Action Button unten rechts
        fab = rootView.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btnClose;
                myDialog.setContentView(R.layout.custompopup);

                btnClose = myDialog.findViewById(R.id.btnClose);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.show();
                //Initialisierung der TextViews in dem Dialog
                payPurpose = myDialog.findViewById(R.id.inputPurpose);
                payAmount = myDialog.findViewById(R.id.inputAmount);
                payMethod = myDialog.findViewById(R.id.inputMethod);
                mDisplayDate = myDialog.findViewById(R.id.inputDate);
                calendar_btn = myDialog.findViewById(R.id.calendar_btn);

                //Öffnet Datum-Feld
                calendar_btn.setOnClickListener(new View.OnClickListener() {
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

                mSpinner = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.spinner_item_main, PaymentMethods.MainSpinnerList);
                payMethod.setOnItemSelectedListener(MainFragment.this);
                payMethod.setAdapter(mSpinner);

                //Button im Dialog um Rechnung hinzuzufügen
                addBill = myDialog.findViewById(R.id.addBill);
                addBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String number = payAmount.getText().toString();
                        if(!number.equals("")){
                            formattedCurrency = CurrencyFormatter.formatNumberCurrency(number);
                        }
                        TransactionItem.addEntry(new TransactionItem(R.drawable.ic_euro_black,
                                payPurpose.getText().toString(),
                                "-" + formattedCurrency + " €",
                                mDisplayDate.getText().toString(),
                                paymentMethod
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
        //Übergibt die Informationen mit putExtra
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("Purpose", TransactionItem.itemList.get(position).getmPurpose());
        intent.putExtra("Amount", TransactionItem.itemList.get(position).getmAmount());
        intent.putExtra("Date", TransactionItem.itemList.get(position).getmDate());
        intent.putExtra("Method", TransactionItem.itemList.get(position).getmMethod());
        intent.putExtra("Position", position);
        startActivity(intent);
    }
    //Verhalten des Spinners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Weist die Variable paymentMethod das ausgewählt Item zu
        paymentMethod = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}