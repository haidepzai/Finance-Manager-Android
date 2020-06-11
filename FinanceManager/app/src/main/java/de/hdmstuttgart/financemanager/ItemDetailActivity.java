package de.hdmstuttgart.financemanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

import de.hdmstuttgart.financemanager.Fragments.MainFragment;


public class ItemDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FloatingActionButton fab_done;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //TextView welches in der Activity angezeigt wird
    private EditText mPurpose;
    private EditText mAmount;
    private EditText mDate;
    private Spinner mMethod;
    private ImageButton calendarButton;

    private int position; //position des aktuellen Eintrages in der Liste

    private static ArrayAdapter<String> mSpinner; //Adapter für Spinner (Dropdown Liste)

    private String paymentMethod; //Neue Zahlungsmethode wird in dieser Variable gespeichert

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent in = this.getIntent();
        String purpose = in.getStringExtra("Purpose");
        String amount = in.getStringExtra("Amount");
        String date = in.getStringExtra("Date");
        paymentMethod = in.getStringExtra("Method");
        position = in.getIntExtra("Position", 0);

        //Change Date
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "." + month + "." + year;
                mDate.setText(date);
            }
        };

        mPurpose = findViewById(R.id.detailPurpose);
        mAmount = findViewById(R.id.detailAmount);
        mDate = findViewById(R.id.detailDate);
        mMethod = findViewById(R.id.detailMethod);
        calendarButton = findViewById(R.id.calendarButton);
        calendarButton.setVisibility(View.INVISIBLE);

        mPurpose.setText(purpose);
        mAmount.setText(amount);
        mDate.setText(date);
        //Initialisierung des Spinners
        mSpinner = new ArrayAdapter<>(ItemDetailActivity.this, R.layout.spinner_item_detail, PaymentMethods.SpinnerList);
        mMethod.setOnItemSelectedListener(this);
        //Nicht anklickbar
        mMethod.setEnabled(false);
        mMethod.setClickable(false);
        mMethod.setAdapter(mSpinner);
        mMethod.setSelection(mSpinner.getPosition(paymentMethod)); //Default Wert des Spinners

        deactivateText();

        fab_done = findViewById(R.id.fab_done);
        fab_done.hide();

        FloatingActionButton fab_edit = findViewById(R.id.fab_edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Elemente sind anklickbar
                mPurpose.setFocusableInTouchMode(true);
                mPurpose.setFocusable(true);
                mAmount.setFocusableInTouchMode(true);
                mAmount.setFocusable(true);
                mAmount.setFocusableInTouchMode(true);
                mDate.setFocusable(true);
                mDate.setFocusableInTouchMode(true);
                calendarButton.setVisibility(View.VISIBLE);
                //Bei onClick des fab_edit -> Spinner Auswahl möglich
                mMethod.setEnabled(true);
                mMethod.setClickable(true);
                mMethod.setAdapter(mSpinner);
                mMethod.setSelection(mSpinner.getPosition(paymentMethod)); //Default Wert des Spinners

                calendarButton.setOnClickListener(new View.OnClickListener() {
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
                mMethod.setFocusableInTouchMode(true);
                mMethod.setFocusable(true);

                mPurpose.setBackgroundResource(R.drawable.edit_border);
                mAmount.setBackgroundResource(R.drawable.edit_border);
                mDate.setBackgroundResource(R.drawable.edit_border);
                mMethod.setBackgroundResource(R.drawable.edit_border);

                fab_done.show();
            }


        });

        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nicht anklickbar
                mMethod.setEnabled(false);
                mMethod.setClickable(false);
                mMethod.setAdapter(mSpinner);
                mMethod.setSelection(mSpinner.getPosition(paymentMethod)); //Neuer Wert

                TransactionItem.itemList.set(position, new TransactionItem(R.drawable.ic_euro_black,
                        mPurpose.getText().toString(),
                        mAmount.getText().toString(),
                        mDate.getText().toString(),
                        paymentMethod
                ));

                deactivateText();
                fab_done.hide();
                calendarButton.setVisibility(View.INVISIBLE);
                MainFragment.mAdapter.notifyDataSetChanged();
            }
        });

    }

    //Focus der Elemente deaktivieren
    private void deactivateText() {

        mPurpose.setFocusable(false);
        mPurpose.setFocusableInTouchMode(false);
        mAmount.setFocusable(false);
        mAmount.setFocusableInTouchMode(false);
        mDate.setFocusable(false);
        mDate.setFocusableInTouchMode(false);
        mMethod.setFocusable(false);
        mMethod.setFocusableInTouchMode(false);

        mPurpose.setBackground(null);
        mAmount.setBackground(null);
        mDate.setBackground(null);
        mMethod.setBackground(null);
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