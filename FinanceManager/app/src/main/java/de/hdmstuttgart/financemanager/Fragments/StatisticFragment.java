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
import de.hdmstuttgart.financemanager.TransactionItem;
import de.hdmstuttgart.financemanager.View.StatisticView;


public class StatisticFragment extends Fragment {

    private double totalAmount;
    private StatisticView mStatisticView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistic, container, false);

        mStatisticView = (StatisticView) rootView.findViewById(R.id.statisticView);
        TextView totalAmountView = rootView.findViewById(R.id.totalAmount);

        for(TransactionItem item : TransactionItem.itemList){
            String valueOfAmount = item.getmAmount().replaceAll("[-€]", "");
            totalAmount += Double.parseDouble(valueOfAmount);
        }

        totalAmountView.setText(new DecimalFormat("#,###.00").format(totalAmount));

        return rootView;

    }
}
