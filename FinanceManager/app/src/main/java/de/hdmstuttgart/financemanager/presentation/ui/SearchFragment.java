package de.hdmstuttgart.financemanager.presentation.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import de.hdmstuttgart.financemanager.Category;
import de.hdmstuttgart.financemanager.FinanceManagerApp;
import de.hdmstuttgart.financemanager.PaymentMethods;
import de.hdmstuttgart.financemanager.R;
import de.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import de.hdmstuttgart.financemanager.presentation.viewmodel.MainViewModel;
import de.hdmstuttgart.financemanager.presentation.viewmodel.MainViewModelFactory;

public class SearchFragment extends Fragment {
    private MainViewModel viewModel;
    private TransactionAdapter mAdapter;
    private List<TransactionDetail> allTransactions = new ArrayList<>();

    private EditText searchPurpose, searchAmount1, searchAmount2;
    private TextView searchDate1, searchDate2;
    private Spinner categorySpinner, methodSpinner, searchSpinner;
    private Button searchButton;

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
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        initViews(rootView);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewSearch);
        mAdapter = new TransactionAdapter(this::onTransactionClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        viewModel.getTransactions().observe(getViewLifecycleOwner(), transactions -> {
            allTransactions = transactions;
            mAdapter.setTransactions(transactions);
        });

        searchButton.setOnClickListener(v -> performSearch());

        return rootView;
    }

    private void initViews(View v) {
        searchPurpose = v.findViewById(R.id.searchPurpose);
        searchAmount1 = v.findViewById(R.id.searchAmount1);
        searchAmount2 = v.findViewById(R.id.searchAmount2);
        searchDate1 = v.findViewById(R.id.searchDate1);
        searchDate2 = v.findViewById(R.id.searchDate2);
        categorySpinner = v.findViewById(R.id.searchCategory);
        methodSpinner = v.findViewById(R.id.searchMethod);
        searchSpinner = v.findViewById(R.id.searchSpinner);
        searchButton = v.findViewById(R.id.searchButton);

        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.spinner_item, Category.categorySpinnerMain));
        methodSpinner.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.spinner_item, PaymentMethods.methodSpinnerMain));
        
        String[] criteria = {"Zweck", "Kategorie", "Zahlungsmethode", "Betrag", "Datum"};
        searchSpinner.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.spinner_item, criteria));
        
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateVisibility(position);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        setupDatePicker(searchDate1);
        setupDatePicker(searchDate2);
    }

    private void updateVisibility(int position) {
        searchPurpose.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        categorySpinner.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
        methodSpinner.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
        searchAmount1.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
        searchAmount2.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
        searchDate1.setVisibility(position == 4 ? View.VISIBLE : View.GONE);
        searchDate2.setVisibility(position == 4 ? View.VISIBLE : View.GONE);
    }

    private void setupDatePicker(TextView tv) {
        tv.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (view, year, month, day) -> {
                tv.setText(day + "." + (month + 1) + "." + year);
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void performSearch() {
        List<TransactionDetail> results = new ArrayList<>(allTransactions);
        int criteria = searchSpinner.getSelectedItemPosition();

        if (criteria == 0) { // Purpose
            String query = searchPurpose.getText().toString().toLowerCase();
            results = results.stream().filter(t -> t.getPurpose().toLowerCase().contains(query)).collect(Collectors.toList());
        } else if (criteria == 1) { // Category
            String cat = categorySpinner.getSelectedItem().toString();
            if (categorySpinner.getSelectedItemPosition() >= 2) {
                results = results.stream().filter(t -> t.getCategory().equals(cat)).collect(Collectors.toList());
            }
        } else if (criteria == 2) { // Method
            String met = methodSpinner.getSelectedItem().toString();
            if (methodSpinner.getSelectedItemPosition() >= 2) {
                results = results.stream().filter(t -> t.getMethod().equals(met)).collect(Collectors.toList());
            }
        } else if (criteria == 3) { // Amount
            try {
                double min = searchAmount1.getText().toString().isEmpty() ? Double.MIN_VALUE : Double.parseDouble(searchAmount1.getText().toString());
                double max = searchAmount2.getText().toString().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(searchAmount2.getText().toString());
                results = results.stream().filter(t -> {
                    double val = Math.abs(parseAmount(t.getAmount()));
                    return val >= min && val <= max;
                }).collect(Collectors.toList());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Ungültiger Betrag", Toast.LENGTH_SHORT).show();
            }
        } else if (criteria == 4) { // Date
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("d.M.yyyy", Locale.GERMANY);
                Date min = searchDate1.getText().toString().equals("Min. Datum") ? new Date(0) : sdf.parse(searchDate1.getText().toString());
                Date max = searchDate2.getText().toString().equals("Max. Datum") ? new Date(Long.MAX_VALUE) : sdf.parse(searchDate2.getText().toString());
                results = results.stream().filter(t -> {
                    try {
                        Date d = sdf.parse(t.getDate());
                        return d != null && !d.before(min) && !d.after(max);
                    } catch (Exception e) { return false; }
                }).collect(Collectors.toList());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Ungültiges Datum", Toast.LENGTH_SHORT).show();
            }
        }
        
        mAdapter.setTransactions(results);
    }

    private double parseAmount(String amount) {
        try {
            return Double.parseDouble(amount.replaceAll("[-€+,]", "").replace(",", "."));
        } catch (Exception e) { return 0.0; }
    }

    private void onTransactionClick(TransactionDetail transaction) {
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        intent.putExtra("ID", transaction.getId());
        intent.putExtra("Purpose", transaction.getPurpose());
        intent.putExtra("Amount", transaction.getAmount());
        intent.putExtra("Date", transaction.getDate());
        intent.putExtra("Image", transaction.getImageResource());
        intent.putExtra("Category", transaction.getCategory());
        intent.putExtra("Method", transaction.getMethod());
        startActivity(intent);
    }
}
