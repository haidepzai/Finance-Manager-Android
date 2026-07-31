package com.hdmstuttgart.financemanager.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.hdmstuttgart.financemanager.FinanceManagerApp;
import com.hdmstuttgart.financemanager.R;
import com.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import com.hdmstuttgart.financemanager.presentation.viewmodel.MainViewModel;
import com.hdmstuttgart.financemanager.presentation.viewmodel.MainViewModelFactory;

public class CategoryDetailActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private TransactionAdapter mAdapter;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        categoryName = getIntent().getStringExtra("Category");
        setTitle(categoryName);

        FinanceManagerApp app = (FinanceManagerApp) getApplication();
        MainViewModelFactory factory = new MainViewModelFactory(
                app.appContainer.getTransactionsUseCase,
                app.appContainer.addTransactionUseCase,
                app.appContainer.deleteTransactionUseCase
        );
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerCategoryView);
        mAdapter = new TransactionAdapter(this::onTransactionClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        viewModel.getTransactions().observe(this, transactions -> {
            List<TransactionDetail> filtered = transactions.stream()
                    .filter(t -> t.getCategory().equals(categoryName))
                    .collect(Collectors.toList());
            mAdapter.setTransactions(filtered);
            
            double total = filtered.stream()
                    .mapToDouble(t -> {
                        try {
                            return Double.parseDouble(t.getAmount().replaceAll("[-€+,]", "").replace(",", "."));
                        } catch (Exception e) { return 0.0; }
                    }).sum();
            
            ((TextView)findViewById(R.id.categoryHeader)).setText(String.format(Locale.GERMANY, "%s: %.2f €", categoryName, total));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onTransactionClick(TransactionDetail transaction) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
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
