package de.hdmstuttgart.financemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

import de.hdmstuttgart.financemanager.Fragments.MainFragment;


public class ItemDetailActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Dialog myDialog; //Popup Fenster

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button editBill;

    private TextView mPurpose;
    private TextView mAmount;
    private TextView mDate;
    private TextView mMethod;

    private EditText payPurpose;
    private EditText payAmount;
    private TextView mDisplayDate;
    private EditText payMethod;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent in = this.getIntent();
        String purpose = in.getStringExtra("Purpose");
        String amount = in.getStringExtra("Amount");
        String date = in.getStringExtra("Date");
        String method = in.getStringExtra("Method");
        position = in.getIntExtra("Position", 0);

        myDialog = new Dialog(this);

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

        mPurpose = findViewById(R.id.detailPurpose);
        mAmount = findViewById(R.id.detailAmount);
        mDate = findViewById(R.id.detailDate);
        mMethod = findViewById(R.id.detailMethod);

        mPurpose.setText(purpose);
        mAmount.setText(amount);
        mDate.setText(date);
        mMethod.setText(method);

        fab = findViewById(R.id.fab_edit);
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
                //Initialisierung der TextViews in dem Dialog
                payPurpose = myDialog.findViewById(R.id.inputPurpose);
                payAmount = myDialog.findViewById(R.id.inputAmount);
                payMethod = myDialog.findViewById(R.id.inputMethod);
                mDisplayDate = myDialog.findViewById(R.id.inputDate);

                //Setzt den Anfangstext der Edit Felder im Dialog
                payPurpose.setText(mPurpose.getText().toString());
                payAmount.setText(mAmount.getText().toString());
                payMethod.setText(mMethod.getText().toString());
                mDisplayDate.setText(mDate.getText().toString());

                //Öffnet Datum-Feld
                mDisplayDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(ItemDetailActivity.this,
                                android.R.style.Theme_DeviceDefault_Light_Dialog, //Calender Style
                                mDateSetListener,
                                year, month, day);
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        dialog.show();
                    }
                });
                //Button im Dialog um Rechnung hinzuzufügen
                editBill = myDialog.findViewById(R.id.addBill);
                editBill.setText("Bestätigen");
                editBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Verändert den Inhalt der Liste
                        TransactionItem.itemList.set(position, new TransactionItem(R.drawable.ic_euro_black,
                                payPurpose.getText().toString(),
                                payAmount.getText().toString(),
                                mDisplayDate.getText().toString(),
                                payMethod.getText().toString()
                        ));
                        //Ändert den Inhalt in der ItemDetailActivity
                        mPurpose.setText(payPurpose.getText().toString());
                        mAmount.setText(payAmount.getText().toString());
                        mDisplayDate.setText(mDisplayDate.getText().toString());
                        mMethod.setText(payMethod.getText().toString());

                        MainFragment.mAdapter.notifyDataSetChanged();
                        myDialog.dismiss();
                    }
                });
            }
        });

    }
}