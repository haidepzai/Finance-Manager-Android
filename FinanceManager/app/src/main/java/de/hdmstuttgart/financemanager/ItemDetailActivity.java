package de.hdmstuttgart.financemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

import de.hdmstuttgart.financemanager.Fragments.MainFragment;


public class ItemDetailActivity extends AppCompatActivity {

    private FloatingActionButton fab_done;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //TextView welches in der Activity angezeigt wird
    private EditText mPurpose;
    private EditText mAmount;
    private EditText mDate;
    private EditText mMethod;
    private ImageButton calendarButton;

    //position des aktuellen Eintrages in der Liste
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
        mMethod.setText(method);

        deactivateText();

        fab_done = findViewById(R.id.fab_done);
        fab_done.hide();
        FloatingActionButton fab_edit = findViewById(R.id.fab_edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPurpose.setFocusableInTouchMode(true);
                mPurpose.setFocusable(true);
                mAmount.setFocusableInTouchMode(true);
                mAmount.setFocusable(true);
                mAmount.setFocusableInTouchMode(true);
                mDate.setFocusable(true);
                mDate.setFocusableInTouchMode(true);
                calendarButton.setVisibility(View.VISIBLE);

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

                TransactionItem.itemList.set(position, new TransactionItem(R.drawable.ic_euro_black,
                        mPurpose.getText().toString(),
                        mAmount.getText().toString(),
                        mDate.getText().toString(),
                        mMethod.getText().toString()
                ));

                deactivateText();
                fab_done.hide();
                calendarButton.setVisibility(View.INVISIBLE);
                MainFragment.mAdapter.notifyDataSetChanged();
            }
        });

    }

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
}