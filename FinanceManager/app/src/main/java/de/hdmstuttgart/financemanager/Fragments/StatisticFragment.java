package de.hdmstuttgart.financemanager.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.Database.Transaction;
import de.hdmstuttgart.financemanager.View.BarChartView;


public class StatisticFragment extends Fragment {

    private double totalAmount;
    private BarChartView mBarChartView;

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
        TextView totalAmountView = rootView.findViewById(R.id.totalAmount);

        for(Transaction item : Transaction.outcomingBills){
            String valueOfAmount = item.getmAmount().replaceAll("[-€,]", "");
            totalAmount += Double.parseDouble(valueOfAmount);
        }


        totalAmountView.setText(new DecimalFormat("#,###.00").format(totalAmount));

        return rootView;

    }
}
