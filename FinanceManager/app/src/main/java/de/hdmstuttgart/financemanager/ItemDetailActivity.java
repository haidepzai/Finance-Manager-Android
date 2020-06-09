package de.hdmstuttgart.financemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class ItemDetailActivity extends AppCompatActivity {

    private TextView mPurpose;
    private TextView mAmount;
    private TextView mDate;
    private TextView mMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        mPurpose = findViewById(R.id.detailPurpose);
        mAmount = findViewById(R.id.detailAmount);
        mDate = findViewById(R.id.detailDate);
        mMethod = findViewById(R.id.detailMethod);

        Intent in = this.getIntent();
        String purpose = in.getStringExtra("Purpose");
        String amount = in.getStringExtra("Amount");
        String date = in.getStringExtra("Date");
        String method = in.getStringExtra("Method");

        mPurpose.setText(purpose);
        mAmount.setText(amount);
        mDate.setText(date);
        mMethod.setText(method);

    }
}