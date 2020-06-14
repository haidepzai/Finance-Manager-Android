package de.hdmstuttgart.financemanager.Activity;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

import de.hdmstuttgart.financemanager.Category;
import de.hdmstuttgart.financemanager.Fragments.MainFragment;
import de.hdmstuttgart.financemanager.Helper.CurrencyFormatter;
import de.hdmstuttgart.financemanager.PaymentMethods;
import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.TransactionItem;

public class ItemDetailActivity extends AppCompatActivity {

    private FloatingActionButton fab_done;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //Textfelder welche in der Activity angezeigt wird
    private EditText mPurpose;
    private EditText mAmount;
    private EditText mDate;
    private Spinner mCategory;
    private Spinner mMethod;
    private ImageButton calenderBtn;

    private int position; //position des aktuellen Eintrages in der Liste

    private static ArrayAdapter<String> mSpinnerMethodAdapter; //Adapter für Spinner Zahlungsmethode (Dropdown Liste)
    private static ArrayAdapter<String> mSpinnerCategoryAdapter; //Adapter für Spinner Kategorie (Dropdown Liste)

    private String paymentMethod; //Neue Zahlungsmethode wird in dieser Variable gespeichert
    private String formattedCurrency; //Formatierte Währung mit 2 Nachkommastellen

    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent in = this.getIntent();
        final String purpose = in.getStringExtra("Purpose");
        final String amount = in.getStringExtra("Amount");
        final String date = in.getStringExtra("Date");
        category = in.getStringExtra("Category");
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

        initializeText(); //(findViewById)

        mPurpose.setText(purpose);
        mAmount.setText(amount);
        //Add currency filter to amount input field
        mDate.setText(date);

        //Initialisierung des Spinners
        mSpinnerMethodAdapter = new ArrayAdapter<>(ItemDetailActivity.this, R.layout.spinner_item, PaymentMethods.methodSpinnerDetail);
        mMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Verhalten des Spinners
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Weist die Variable paymentMethod das ausgewählt Item zu
                paymentMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerCategoryAdapter = new ArrayAdapter<>(ItemDetailActivity.this, R.layout.spinner_item, Category.categorySpinnerDetail);
        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Verhalten des Spinners
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Weist die Variable paymentMethod das ausgewählt Item zu
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deactivateText(); //Anklicken der Textfelder nicht möglich

        fab_done = findViewById(R.id.fab_done);
        fab_done.hide();

        FloatingActionButton fab_edit = findViewById(R.id.fab_edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAmount.setText(amount.replaceAll("[-€]", "")); //Nur der Wert wird angezeigt (ohne - und €)
                activateText(); //Erlaubt das Anklicken der Textfelder
                //Calender Button
                calenderBtn.setOnClickListener(new View.OnClickListener() {
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

                setGreenFieldBorder();

                fab_done.show();
            }


        });

        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setGreenFieldBorder();
                checkEmptyField();

                if (mPurpose.getText().toString().equals("") || mAmount.getText().toString().equals("") ||
                        mDate.getText().toString().equals("")) {
                    Toast.makeText(ItemDetailActivity.this, "Bitte die rot markierten Felder ausfüllen",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    setGreenFieldBorder();
                    String number = mAmount.getText().toString();
                    StringBuilder str = new StringBuilder(); //StringBuilder: bessere Performance als String (immutable)
                    if (!number.equals("")) {
                        formattedCurrency = CurrencyFormatter.formatNumberCurrency(number);
                        str.append("-").append(formattedCurrency).append(" €");
                    } else {
                        Toast.makeText(ItemDetailActivity.this, "Bitte einen Betrag angeben",
                                Toast.LENGTH_LONG).show();
                    }
                    //Liste aktualisieren
                    TransactionItem.itemList.set(position, new TransactionItem(R.drawable.ic_euro_black,
                            mPurpose.getText().toString(),
                            "-" + formattedCurrency + " €",
                            mDate.getText().toString(),
                            category,
                            paymentMethod
                    ));
                    mAmount.setText(str);

                    deactivateText();
                    fab_done.hide();
                    calenderBtn.setVisibility(View.INVISIBLE);
                    MainFragment.mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initializeText(){
        mPurpose = findViewById(R.id.detailPurpose);
        mAmount = findViewById(R.id.detailAmount);
        mDate = findViewById(R.id.detailDate);
        mCategory = findViewById(R.id.detailCategory);
        mMethod = findViewById(R.id.detailMethod);
        calenderBtn = findViewById(R.id.calendarButton);
        calenderBtn.setVisibility(View.INVISIBLE);
    }

    //Focus der Elemente aktivieren
    private void activateText(){
        //Elemente sind anklickbar
        mPurpose.setFocusableInTouchMode(true);
        mPurpose.setFocusable(true);
        mAmount.setFocusableInTouchMode(true);
        mAmount.setFocusable(true);
        mAmount.setFocusableInTouchMode(true);
        mDate.setFocusable(true);
        mDate.setFocusableInTouchMode(true);
        mCategory.setFocusableInTouchMode(true);
        mCategory.setFocusable(true);
        mMethod.setFocusableInTouchMode(true);
        mMethod.setFocusable(true);
        calenderBtn.setVisibility(View.VISIBLE);
        //Bei onClick des fab_edit -> Spinner Auswahl möglich
        mMethod.setEnabled(true);
        mMethod.setClickable(true);
        mMethod.setAdapter(mSpinnerMethodAdapter);
        //Aktueller Wert des Spinners wird angezeigt (Im Bearbeitungsmodus, sonst erscheint immer Position 0: EC)
        mMethod.setSelection(mSpinnerMethodAdapter.getPosition(paymentMethod));
        mCategory.setEnabled(true);
        mCategory.setClickable(true);
        mCategory.setAdapter(mSpinnerCategoryAdapter);
        //Aktueller Wert des Spinners wird angezeigt (Im Bearbeitungsmodus, sonst erscheint immer Position 0: Einkauf)
        mCategory.setSelection(mSpinnerCategoryAdapter.getPosition(category));
    }

    //Focus der Elemente deaktivieren
    private void deactivateText() {
        //Elemente nicht anklickbar
        mPurpose.setFocusable(false);
        mPurpose.setFocusableInTouchMode(false);
        mAmount.setFocusable(false);
        mAmount.setFocusableInTouchMode(false);
        mDate.setFocusable(false);
        mDate.setFocusableInTouchMode(false);
        mCategory.setFocusable(false);
        mCategory.setFocusableInTouchMode(false);
        mMethod.setFocusable(false);
        mMethod.setFocusableInTouchMode(false);
        //Spinner Nicht anklickbar
        mMethod.setEnabled(false);
        mMethod.setClickable(false);
        mMethod.setAdapter(mSpinnerMethodAdapter);
        mMethod.setSelection(mSpinnerMethodAdapter.getPosition(paymentMethod)); //Output der Zahlungsmethode (Spinner)
        mCategory.setEnabled(false);
        mCategory.setClickable(false);
        mCategory.setAdapter(mSpinnerCategoryAdapter);
        mCategory.setSelection(mSpinnerCategoryAdapter.getPosition(category)); //Output der Zahlungsmethode (Spinner)

        mPurpose.setBackgroundColor(Color.TRANSPARENT);
        mAmount.setBackgroundColor(Color.TRANSPARENT);
        mDate.setBackgroundColor(Color.TRANSPARENT);
        mCategory.setBackgroundColor(Color.TRANSPARENT);
        mMethod.setBackgroundColor(Color.TRANSPARENT);
    }

    private void checkEmptyField(){
        if (mPurpose.getText().toString().equals("")) {
            mPurpose.setBackgroundResource(R.drawable.edit_border_red);
        }
        if (mAmount.getText().toString().equals("")) {
            mAmount.setBackgroundResource(R.drawable.edit_border_red);
        }
        if (mDate.getText().toString().equals("")) {
            mDate.setBackgroundResource(R.drawable.edit_border_red);
        }
    }

    private void setGreenFieldBorder() {
        mPurpose.setBackgroundResource(R.drawable.edit_border);
        mAmount.setBackgroundResource(R.drawable.edit_border);
        mDate.setBackgroundResource(R.drawable.edit_border);
        mCategory.setBackgroundResource(R.drawable.edit_border);
        mMethod.setBackgroundResource(R.drawable.edit_border);
    }
}