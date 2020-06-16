package de.hdmstuttgart.financemanager.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.Database.Transaction;
import de.hdmstuttgart.financemanager.View.BarChartView;


public class StatisticFragment extends Fragment {

    public static float totalAmount;

    private BarChartView mBarChartView;
    private TextView totalAmountView;
    private TextView yAxisMax;
    private TextView yAxisHalf;

    public static float totalGrocery;
    public static float totalFood;
    public static float totalEducation;
    public static float totalLeisure;
    public static float totalFee;
    public static float totalMisc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(() -> {
            Transaction.outcomingBills.clear();
            Transaction.incomingBills.clear();
            for(Transaction item : Transaction.itemList){
                if(item.getmAmount().contains("-")){
                    Transaction.outcomingBills.add(item);
                } else {
                    Transaction.incomingBills.add(item);
                }
            }
        }).start();


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistic, container, false);

        mBarChartView = rootView.findViewById(R.id.barChartView);

        //Gesamt
        totalAmountView = rootView.findViewById(R.id.totalAmount);
        yAxisMax = rootView.findViewById(R.id.yAxisMax);
        yAxisHalf = rootView.findViewById(R.id.yAxisHalf);


        calcTotalAmount();
        calcGrocery();
        calcFood();
        calcEducation();
        calcLeisure();
        calcFee();
        calcMisc();

        return rootView;
    }
    private void calcTotalAmount(){
        totalAmount = 0;
        for(Transaction item : Transaction.outcomingBills){
            String valueOfAmount = item.getmAmount().replaceAll("[-€,]", "");
            totalAmount += Double.parseDouble(valueOfAmount);
        }
        if(totalAmount != 0){
            yAxisMax.setVisibility(View.VISIBLE);
            yAxisHalf.setVisibility(View.VISIBLE);
            totalAmountView.setText(new DecimalFormat("#,###.00").format(totalAmount));
            yAxisMax.setText(totalAmountView.getText().toString());
            yAxisHalf.setText(new DecimalFormat("#,###.00").format(totalAmount/2));
        } else {
            totalAmountView.setText("0.00 €");
            yAxisMax.setText("0.00 €");
            yAxisMax.setVisibility(View.INVISIBLE);
            yAxisHalf.setVisibility(View.INVISIBLE);
        }
        Log.d("Total", String.valueOf(totalAmount));
    }

    private void calcGrocery(){
        totalGrocery = 0;
        List<Transaction> list = Transaction.outcomingBills
                .stream()
                .filter(s -> s.getmCategory().equals("Einkauf"))
                .collect(Collectors.toList());

        for(Transaction item : list){
            String valueOfAmount = item.getmAmount().replaceAll("[-€,]", "");
            totalGrocery += Double.parseDouble(valueOfAmount);
        }
        Log.d("Grocery", String.valueOf(totalGrocery));
    }
    private void calcFood(){
        totalFood = 0;
        List<Transaction> list = Transaction.outcomingBills
                .stream()
                .filter(s -> s.getmCategory().equals("Essen"))
                .collect(Collectors.toList());

        for(Transaction item : list){
            String valueOfAmount = item.getmAmount().replaceAll("[-€,]", "");
            totalFood += Double.parseDouble(valueOfAmount);
        }
        Log.d("Food", String.valueOf(totalFood));
    }
    private void calcEducation(){
        totalEducation = 0;
        List<Transaction> list = Transaction.outcomingBills
                .stream()
                .filter(s -> s.getmCategory().equals("Studium/Beruf"))
                .collect(Collectors.toList());

        for(Transaction item : list){
            String valueOfAmount = item.getmAmount().replaceAll("[-€,]", "");
            totalEducation += Double.parseDouble(valueOfAmount);
        }
        Log.d("Education", String.valueOf(totalEducation));
    }
    private void calcLeisure(){
        totalLeisure = 0;
        List<Transaction> list = Transaction.outcomingBills
                .stream()
                .filter(s -> s.getmCategory().equals("Freizeit"))
                .collect(Collectors.toList());

        for(Transaction item : list){
            String valueOfAmount = item.getmAmount().replaceAll("[-€,]", "");
            totalLeisure += Double.parseDouble(valueOfAmount);
        }
        Log.d("Leisure", String.valueOf(totalLeisure));
    }
    private void calcFee(){
        totalFee = 0;
        List<Transaction> list = Transaction.outcomingBills
                .stream()
                .filter(s -> s.getmCategory().equals("Gebühren"))
                .collect(Collectors.toList());

        for(Transaction item : list){
            String valueOfAmount = item.getmAmount().replaceAll("[-€,]", "");
            totalFee += Double.parseDouble(valueOfAmount);
        }
        Log.d("Fee", String.valueOf(totalFee));
    }
    private void calcMisc(){
        totalMisc = 0;
        List<Transaction> list = Transaction.outcomingBills
                .stream()
                .filter(s -> s.getmCategory().equals("Sonstige"))
                .collect(Collectors.toList());

        for(Transaction item : list){
            String valueOfAmount = item.getmAmount().replaceAll("[-€,]", "");
            totalMisc += Double.parseDouble(valueOfAmount);
        }
        Log.d("Misc", String.valueOf(totalMisc));
    }
}
