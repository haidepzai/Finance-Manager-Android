package de.hdmstuttgart.financemanager.presentation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdmstuttgart.financemanager.FinanceManagerApp;
import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import de.hdmstuttgart.financemanager.presentation.viewmodel.MainViewModel;
import de.hdmstuttgart.financemanager.presentation.viewmodel.MainViewModelFactory;

public class StatisticFragment extends Fragment {
    private MainViewModel viewModel;
    private TextView spendingView;
    private LinearLayout categoryListContainer;

    public static double totalAmount = 0;
    public static double totalGrocery, totalFood, totalEducation, totalLeisure, totalFee, totalMisc;
    public static float maxValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FinanceManagerApp app = (FinanceManagerApp) requireActivity().getApplication();
        MainViewModelFactory factory = new MainViewModelFactory(
                app.appContainer.getTransactionsUseCase,
                app.appContainer.addTransactionUseCase,
                app.appContainer.deleteTransactionUseCase
        );
        viewModel = new ViewModelProvider(requireActivity(), factory).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistic, container, false);

        spendingView = rootView.findViewById(R.id.totalAmount);
        categoryListContainer = rootView.findViewById(R.id.categoryListContainer);

        viewModel.getTransactions().observe(getViewLifecycleOwner(), transactions -> {
            calculateStats(transactions);
            spendingView.setText(String.format(Locale.GERMANY, "%.2f€", totalAmount));
            updateListView();
        });

        Button btnShowChart = rootView.findViewById(R.id.chartActivity);
        btnShowChart.setOnClickListener(v -> startActivity(new Intent(getContext(), BarChartActivity.class)));

        return rootView;
    }

    private void calculateStats(List<TransactionDetail> transactions) {
        totalAmount = 0; totalGrocery = 0; totalFood = 0; totalEducation = 0;
        totalLeisure = 0; totalFee = 0; totalMisc = 0;
        
        if (transactions == null) return;

        for (TransactionDetail t : transactions) {
            if (t.getAmount().contains("-")) {
                double val = parseAmount(t.getAmount());
                totalAmount += val;
                switch (t.getCategory()) {
                    case "Einkauf": totalGrocery += val; break;
                    case "Essen": totalFood += val; break;
                    case "Studium/Beruf": totalEducation += val; break;
                    case "Freizeit": totalLeisure += val; break;
                    case "Gebühren": totalFee += val; break;
                    case "Sonstige": totalMisc += val; break;
                }
            }
        }
        
        maxValue = (float) Math.max(totalGrocery, Math.max(totalFood, Math.max(totalEducation, 
                   Math.max(totalLeisure, Math.max(totalFee, totalMisc)))));
    }

    private double parseAmount(String amount) {
        try {
            String clean = amount.replaceAll("[-€+,]", "").replace(",", ".");
            int lastDot = clean.lastIndexOf('.');
            if (lastDot != -1) {
                String before = clean.substring(0, lastDot).replace(".", "");
                String after = clean.substring(lastDot);
                clean = before + after;
            }
            return Double.parseDouble(clean);
        } catch (Exception e) { return 0.0; }
    }

    private void updateListView() {
        if (categoryListContainer == null) return;
        categoryListContainer.removeAllViews();
        
        addCategoryItem("Einkauf", totalGrocery, R.drawable.logo_shopping);
        addCategoryItem("Essen", totalFood, R.drawable.logo_restaurant);
        addCategoryItem("Studium/Beruf", totalEducation, R.drawable.logo_education);
        addCategoryItem("Freizeit", totalLeisure, R.drawable.logo_freetime);
        addCategoryItem("Gebühren", totalFee, R.drawable.gebuehr_logo);
        addCategoryItem("Sonstige", totalMisc, R.drawable.logo_other);
    }

    private void addCategoryItem(String name, double amount, int icon) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.statistic_item, categoryListContainer, false);
        ((ImageView)v.findViewById(R.id.imageCategory)).setImageResource(icon);
        ((TextView)v.findViewById(R.id.statisticCategory)).setText(name);
        ((TextView)v.findViewById(R.id.statisticAmount)).setText(String.format(Locale.GERMANY, "%.2f€", amount));
        categoryListContainer.addView(v);
        
        // Add some margin between items
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.bottomMargin = 16;
        v.setLayoutParams(params);
    }
}
