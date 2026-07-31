package com.hdmstuttgart.financemanager.presentation.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

import com.hdmstuttgart.financemanager.Category;
import com.hdmstuttgart.financemanager.FinanceManagerApp;
import com.hdmstuttgart.financemanager.Helper.CurrencyFormatter;
import com.hdmstuttgart.financemanager.PaymentMethods;
import com.hdmstuttgart.financemanager.R;
import com.hdmstuttgart.financemanager.domain.model.TransactionDetail;

public class ItemDetailActivity extends AppCompatActivity {

    private FloatingActionButton fab_main;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private EditText mPurpose, mAmount, mDate;
    private Spinner mCategory, mMethod;
    private ImageButton calenderBtn;
    private ImageView headerImg;

    private int id_Position;
    private String paymentMethod, category, billType, formattedCurrency;
    private int category_logo;
    private boolean isIncomingBill = false;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent in = this.getIntent();
        id_Position = in.getIntExtra("ID", 0);
        final String purpose = in.getStringExtra("Purpose");
        final String amount = in.getStringExtra("Amount");
        final String date = in.getStringExtra("Date");
        category_logo = in.getIntExtra("Image", 0);
        category = in.getStringExtra("Category");
        paymentMethod = in.getStringExtra("Method");

        headerImg = findViewById(R.id.detailHeader);
        setHeaderImg();
        initializeText();

        if (amount != null && amount.contains("+")) {
            isIncomingBill = true;
        }

        billType = isIncomingBill ? "+" : "-";
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, 
                isIncomingBill ? Category.incomingTypeSpinnerDetail : Category.categorySpinnerDetail);
        mCategory.setAdapter(catAdapter);

        mDateSetListener = (view, year, month, dayOfMonth) -> {
            mDate.setText(dayOfMonth + "." + (month + 1) + "." + year);
        };

        mPurpose.setText(purpose);
        mAmount.setText(amount);
        mDate.setText(date);

        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, PaymentMethods.methodSpinnerDetail);
        mMethod.setAdapter(methodAdapter);
        mMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentMethod = parent.getItemAtPosition(position).toString();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
                category_logo = getCategoryLogo(category);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        deactivateText(methodAdapter, catAdapter);
        
        fab_main = findViewById(R.id.fab_main);
        fab_main.setOnClickListener(v -> {
            if (!isEditMode) {
                enterEditMode(amount, methodAdapter, catAdapter);
            } else {
                saveAndExitEditMode(methodAdapter, catAdapter);
            }
        });
    }

    private void enterEditMode(String amount, ArrayAdapter<String> methodAdapter, ArrayAdapter<String> catAdapter) {
        isEditMode = true;
        mAmount.setText(amount.replaceAll("[-€+,]", ""));
        activateText(methodAdapter, catAdapter);
        calenderBtn.setOnClickListener(v1 -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, 
                    mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });
        setGreenFieldBorder();
        fab_main.setImageResource(R.drawable.done); // Changed to tick icon
    }

    private void saveAndExitEditMode(ArrayAdapter<String> methodAdapter, ArrayAdapter<String> catAdapter) {
        if (validateFields()) {
            formattedCurrency = CurrencyFormatter.formatNumberCurrency(mAmount.getText().toString());
            TransactionDetail updated = new TransactionDetail(
                    id_Position, category_logo, mPurpose.getText().toString(),
                    billType + formattedCurrency + "€", mDate.getText().toString(),
                    category, paymentMethod
            );
            
            FinanceManagerApp app = (FinanceManagerApp) getApplication();
            app.appContainer.transactionRepository.insertTransaction(updated);

            mAmount.setText(updated.getAmount());
            deactivateText(methodAdapter, catAdapter);
            calenderBtn.setVisibility(View.INVISIBLE);
            setHeaderImg();
            fab_main.setImageResource(R.drawable.ic_edit); // Back to edit icon
            isEditMode = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeText() {
        mPurpose = findViewById(R.id.detailPurpose);
        mAmount = findViewById(R.id.detailAmount);
        mDate = findViewById(R.id.detailDate);
        mCategory = findViewById(R.id.detailCategory);
        mMethod = findViewById(R.id.detailMethod);
        calenderBtn = findViewById(R.id.calendarButton);
        calenderBtn.setVisibility(View.INVISIBLE);
    }

    private void activateText(ArrayAdapter<String> methodAdapter, ArrayAdapter<String> catAdapter) {
        mPurpose.setFocusableInTouchMode(true); mPurpose.setFocusable(true);
        mAmount.setFocusableInTouchMode(true); mAmount.setFocusable(true);
        mDate.setFocusableInTouchMode(true); mDate.setFocusable(true);
        mMethod.setEnabled(true); mMethod.setClickable(true);
        mMethod.setSelection(methodAdapter.getPosition(paymentMethod));
        mCategory.setEnabled(true); mCategory.setClickable(true);
        mCategory.setSelection(catAdapter.getPosition(category));
        calenderBtn.setVisibility(View.VISIBLE);
    }

    private void deactivateText(ArrayAdapter<String> methodAdapter, ArrayAdapter<String> catAdapter) {
        mPurpose.setFocusable(false); mPurpose.setFocusableInTouchMode(false);
        mAmount.setFocusable(false); mAmount.setFocusableInTouchMode(false);
        mDate.setFocusable(false); mDate.setFocusableInTouchMode(false);
        mMethod.setEnabled(false); mMethod.setClickable(false);
        mMethod.setSelection(methodAdapter.getPosition(paymentMethod));
        mCategory.setEnabled(false); mCategory.setClickable(false);
        mCategory.setSelection(catAdapter.getPosition(category));
        mPurpose.setBackgroundColor(Color.TRANSPARENT);
        mAmount.setBackgroundColor(Color.TRANSPARENT);
        mDate.setBackgroundColor(Color.TRANSPARENT);
    }

    private boolean validateFields() {
        if (mPurpose.getText().toString().isEmpty()) { mPurpose.setBackgroundResource(R.drawable.edit_border_red); return false; }
        if (mAmount.getText().toString().isEmpty()) { mAmount.setBackgroundResource(R.drawable.edit_border_red); return false; }
        if (mDate.getText().toString().isEmpty()) { mDate.setBackgroundResource(R.drawable.edit_border_red); return false; }
        return true;
    }

    private void setGreenFieldBorder() {
        mPurpose.setBackgroundResource(R.drawable.edit_border);
        mAmount.setBackgroundResource(R.drawable.edit_border);
        mDate.setBackgroundResource(R.drawable.edit_border);
    }

    private int getCategoryLogo(String category) {
        switch (category) {
            case "Einkauf": return R.drawable.logo_shopping;
            case "Essen": return R.drawable.logo_restaurant;
            case "Studium/Beruf": return R.drawable.logo_education;
            case "Freizeit": return R.drawable.logo_freetime;
            case "Gebühren": return R.drawable.gebuehr_logo;
            case "Geschenk": return R.drawable.logo_gift;
            case "Lohn/Gehalt": return R.drawable.logo_money;
            default: return R.drawable.logo_other;
        }
    }

    private void setHeaderImg() {
        switch (category) {
            case "Einkauf": headerImg.setImageResource(R.drawable.einkaufen_picture); break;
            case "Essen": headerImg.setImageResource(R.drawable.essen_picture); break;
            case "Studium/Beruf": headerImg.setImageResource(R.drawable.studium_picture); break;
            case "Freizeit": headerImg.setImageResource(R.drawable.freizeit_picture); break;
            case "Sonstige": headerImg.setImageResource(R.drawable.sonstiges_picture); break;
            case "Gebühren": headerImg.setImageResource(R.drawable.gebuehr_picture); break;
            case "Lohn/Gehalt": headerImg.setImageResource(R.drawable.gehalt_picture); break;
            case "Geschenk": headerImg.setImageResource(R.drawable.geschenk_picture); break;
            default: Log.d("-AG-", "Unknown category");
        }
    }
}
