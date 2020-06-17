package de.hdmstuttgart.financemanager.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.hdmstuttgart.financemanager.Activity.BarChartActivity;
import de.hdmstuttgart.financemanager.Activity.CategoryDetailActivity;
import de.hdmstuttgart.financemanager.Activity.ItemDetailActivity;
import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.Database.Transaction;
import de.hdmstuttgart.financemanager.View.BarChartView;


public class StatisticFragment extends Fragment {
    private ListView listView;

    private LinkedHashMap<String, String> categoryAmount = new LinkedHashMap<>();

    public static float totalAmount; //Gesamtbetrag Zahlungsausgang
    public static float maxValue; //Höchster Gesamtbetrag unter den Kategorien

    private BarChartView mBarChartView;
    private TextView totalAmountView;

    private Button chartActivity;

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

        listView = rootView.findViewById(R.id.listViewStatistic);
        mBarChartView = rootView.findViewById(R.id.barChartView);

        chartActivity = rootView.findViewById(R.id.chartActivity);
        chartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BarChartActivity.class);// New activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //Gesamt
        totalAmountView = rootView.findViewById(R.id.totalAmount);

        calcTotalAmount();
        calcGrocery();
        calcFood();
        calcEducation();
        calcLeisure();
        calcFee();
        calcMisc();
        maxAmount();

        List<LinkedHashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems, R.layout.statistic_item,
                new String[]{"Category", "Amount"}, //Keys
                new int[]{R.id.statisticCategory, R.id.statisticAmount});
        //Durch HashMap iterieren
        for (Map.Entry<String, String> stringStringEntry : categoryAmount.entrySet()) {
            LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
            resultMap.put("Category", ((Map.Entry) stringStringEntry).getKey().toString()); //Key
            resultMap.put("Amount", ((Map.Entry) stringStringEntry).getValue().toString()); //Value
            listItems.add(resultMap);
        }

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CategoryDetailActivity.class);
                String categoryKey = String.valueOf(categoryAmount.keySet().toArray()[position]);
                intent.putExtra("CategoryKey", categoryKey);
                startActivity(intent);
            }
        });

        return rootView;
    }
    private void calcTotalAmount(){
        totalAmount = 0;
        for(Transaction item : Transaction.outcomingBills){
            String valueOfAmount = item.getmAmount().replaceAll("[-€,]", "");
            totalAmount += Double.parseDouble(valueOfAmount);
        }
        if(totalAmount != 0){
            totalAmountView.setText(new DecimalFormat("#,###.00").format(totalAmount));
        } else {
            totalAmountView.setText("0.00 €");
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
        if(totalGrocery != 0){
            categoryAmount.put("Einkauf", "-" + totalGrocery + " €");
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
        if(totalFood != 0){
            categoryAmount.put("Essen", "-" + totalFood + " €");
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
        if(totalEducation != 0){
            categoryAmount.put("Studium/Beruf", "-" + totalEducation + " €");
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
        if(totalLeisure != 0){
            categoryAmount.put("Freizeit", "-" + totalLeisure + " €");
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
        if(totalFee != 0){
            categoryAmount.put("Gebühren", "-" + totalFee + " €");
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
        if(totalMisc != 0){
            categoryAmount.put("Sonstige", "-" + totalMisc + " €");
        }
        Log.d("Misc", String.valueOf(totalMisc));
    }

    private void maxAmount(){

        maxValue = 0;

        ArrayList<Float> maxAmountCategory = new ArrayList<>(
                Arrays.asList(totalGrocery, totalFood, totalEducation, totalLeisure, totalFee, totalMisc)
        );

        maxValue = Collections.max(maxAmountCategory);
    }
}
