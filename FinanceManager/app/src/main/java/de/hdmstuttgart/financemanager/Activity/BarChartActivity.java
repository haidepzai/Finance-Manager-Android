package de.hdmstuttgart.financemanager.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import de.hdmstuttgart.financemanager.Fragments.StatisticFragment;
import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.View.BarChartView;

public class BarChartActivity extends AppCompatActivity {

    private BarChartView mBarChartView;

    private TextView totalAmountView;
    private TextView yAxisMax;
    private TextView yAxisHalf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        mBarChartView = findViewById(R.id.barChartView);

        totalAmountView = findViewById(R.id.totalAmount);
        yAxisMax = findViewById(R.id.yAxisMax);
        yAxisHalf = findViewById(R.id.yAxisHalf);

        if(StatisticFragment.totalAmount != 0){
            totalAmountView.setText(new DecimalFormat("#,###.00").format(StatisticFragment.totalAmount));
            yAxisMax.setVisibility(View.VISIBLE);
            yAxisHalf.setVisibility(View.VISIBLE);
            //yAxisMax.setText(totalAmountView.getText().toString());
            yAxisMax.setText(new DecimalFormat("#,###.00").format((StatisticFragment.maxValue+(StatisticFragment.maxValue*0.1))));
            yAxisHalf.setText(new DecimalFormat("#,###.00").format((StatisticFragment.maxValue+(StatisticFragment.maxValue*0.1))/2));
        } else {
            totalAmountView.setText("0.00 €");
            yAxisMax.setText("0.00 €");
            yAxisMax.setVisibility(View.INVISIBLE);
            yAxisHalf.setVisibility(View.INVISIBLE);
        }
    }
}