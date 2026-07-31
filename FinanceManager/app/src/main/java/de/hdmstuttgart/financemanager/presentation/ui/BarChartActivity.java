package com.hdmstuttgart.financemanager.presentation.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

import com.hdmstuttgart.financemanager.R;

public class BarChartActivity extends AppCompatActivity {

    private TextView totalAmountView;
    private TextView yAxisMax;
    private TextView yAxisHalf;

    private TextView groceryView;
    private TextView foodView;
    private TextView educationView;
    private TextView leisureView;
    private TextView feeView;
    private TextView miscView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Balkendiagramm");
        }

        initializeView();
        
        if (StatisticFragment.totalAmount != 0) {
            totalAmountView.setText(String.format(Locale.GERMANY, "%.2f €", StatisticFragment.totalAmount));
            yAxisMax.setVisibility(View.VISIBLE);
            yAxisHalf.setVisibility(View.VISIBLE);
            yAxisMax.setText(String.format(Locale.GERMANY, "%.2f", (StatisticFragment.maxValue * 1.1)));
            yAxisHalf.setText(String.format(Locale.GERMANY, "%.2f", (StatisticFragment.maxValue * 1.1) / 2));
        } else {
            totalAmountView.setText(R.string.value_zero);
            yAxisMax.setText(R.string.value_zero);
            yAxisMax.setVisibility(View.INVISIBLE);
            yAxisHalf.setVisibility(View.INVISIBLE);
        }

        updateCategoryTexts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeView() {
        groceryView = findViewById(R.id.textViewGrocery);
        foodView = findViewById(R.id.textViewFood);
        educationView = findViewById(R.id.textViewEducation);
        leisureView = findViewById(R.id.textViewLeisure);
        feeView = findViewById(R.id.textViewFee);
        miscView = findViewById(R.id.textViewLMisc);

        totalAmountView = findViewById(R.id.totalAmount);
        yAxisMax = findViewById(R.id.yAxisMax);
        yAxisHalf = findViewById(R.id.yAxisHalf);
    }

    private void updateCategoryTexts() {
        setCategoryText(groceryView, "Einkauf: ", StatisticFragment.totalGrocery);
        setCategoryText(foodView, "Essen: ", StatisticFragment.totalFood);
        setCategoryText(educationView, "Studium/Beruf: ", StatisticFragment.totalEducation);
        setCategoryText(leisureView, "Freizeit: ", StatisticFragment.totalLeisure);
        setCategoryText(feeView, "Gebühren: ", StatisticFragment.totalFee);
        setCategoryText(miscView, "Sonstige: ", StatisticFragment.totalMisc);
    }

    private void setCategoryText(TextView view, String label, double amount) {
        if (amount != 0) {
            view.setText(String.format(Locale.GERMANY, "%s %.2f €", label, amount));
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
