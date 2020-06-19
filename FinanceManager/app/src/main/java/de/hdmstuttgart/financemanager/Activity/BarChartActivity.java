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

        initializeView();
        groceryView.setVisibility(View.GONE);
        foodView.setVisibility(View.GONE);
        educationView.setVisibility(View.GONE);
        leisureView.setVisibility(View.GONE);
        feeView.setVisibility(View.GONE);
        miscView.setVisibility(View.GONE);

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

        setTextGrocery();
        setTextFood();
        setTextEducation();
        setTextLeisure();
        setTextFee();
        setTextMisc();
    }

    private void initializeView(){
        mBarChartView = findViewById(R.id.barChartView);

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
    private void setTextGrocery(){
        if(StatisticFragment.totalGrocery != 0){
            String amount = "Einkauf: " + new DecimalFormat("#,###.00").format(StatisticFragment.totalGrocery) + " €";
            groceryView.setText(amount);
            groceryView.setVisibility(View.VISIBLE);
        }
    }
    private void setTextFood(){
        if(StatisticFragment.totalFood != 0){
            String amount = "Essen: " + new DecimalFormat("#,###.00").format(StatisticFragment.totalFood) + " €";
            foodView.setText(amount);
            foodView.setVisibility(View.VISIBLE);
        }
    }
    private void setTextEducation(){
        if(StatisticFragment.totalEducation != 0){
            String amount = "Studium/Beruf: " + new DecimalFormat("#,###.00").format(StatisticFragment.totalEducation) + " €";
            educationView.setText(amount);
            educationView.setVisibility(View.VISIBLE);
        }
    }
    private void setTextLeisure(){
        if(StatisticFragment.totalLeisure != 0){
            String amount = "Freizeit: " + new DecimalFormat("#,###.00").format(StatisticFragment.totalLeisure) + " €";
            leisureView.setText(amount);
            leisureView.setVisibility(View.VISIBLE);
        }
    }
    private void setTextFee(){
        if(StatisticFragment.totalFee != 0){
            String amount = "Gebühren: " + new DecimalFormat("#,###.00").format(StatisticFragment.totalFee) + " €";
            feeView.setText(amount);
            feeView.setVisibility(View.VISIBLE);
        }
    }
    private void setTextMisc(){
        if(StatisticFragment.totalMisc != 0){
            String amount = "Sonstige: " + new DecimalFormat("#,###.00").format(StatisticFragment.totalMisc) + " €";
            miscView.setText(amount);
            miscView.setVisibility(View.VISIBLE);
        }
    }
}