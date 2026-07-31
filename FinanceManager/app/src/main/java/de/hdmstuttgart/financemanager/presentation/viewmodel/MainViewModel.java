package com.hdmstuttgart.financemanager.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import java.util.List;
import com.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import com.hdmstuttgart.financemanager.domain.usecase.AddTransactionUseCase;
import com.hdmstuttgart.financemanager.domain.usecase.DeleteTransactionUseCase;
import com.hdmstuttgart.financemanager.domain.usecase.GetTransactionsUseCase;

public class MainViewModel extends ViewModel {
    private final GetTransactionsUseCase getTransactionsUseCase;
    private final AddTransactionUseCase addTransactionUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;

    private final LiveData<List<TransactionDetail>> transactions;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final LiveData<List<TransactionDetail>> filteredTransactions;
    private final LiveData<Double> totalBalance;

    public MainViewModel(GetTransactionsUseCase getTransactionsUseCase,
                         AddTransactionUseCase addTransactionUseCase,
                         DeleteTransactionUseCase deleteTransactionUseCase) {
        this.getTransactionsUseCase = getTransactionsUseCase;
        this.addTransactionUseCase = addTransactionUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;

        this.transactions = getTransactionsUseCase.execute();

        this.filteredTransactions = Transformations.switchMap(searchQuery, query -> 
            Transformations.map(transactions, list -> {
                if (query == null || query.isEmpty()) return list;
                String lowerQuery = query.toLowerCase();
                return list.stream()
                        .filter(t -> t.getPurpose().toLowerCase().contains(lowerQuery))
                        .collect(java.util.stream.Collectors.toList());
            })
        );
        
        this.totalBalance = Transformations.map(transactions, list -> {
            double balance = 0.0;
            if (list != null) {
                for (TransactionDetail t : list) {
                    try {
                        String amountStr = t.getAmount()
                                .replace("€", "")
                                .replace(",", ".")
                                .trim();
                        
                        // Handle potential multiple dots/commas from formatting
                        // If it's something like 1.234,56, the above converts to 1.234.56 which fails
                        // But original CurrencyFormatter seemed simpler. 
                        // Let's ensure we only have one dot.
                        int lastDot = amountStr.lastIndexOf('.');
                        if (lastDot != -1) {
                            String before = amountStr.substring(0, lastDot).replace(".", "");
                            String after = amountStr.substring(lastDot);
                            amountStr = before + after;
                        }

                        double value = Double.parseDouble(amountStr.replace("+", ""));
                        // The sign is usually preserved in amountStr after parsing
                        // However, if the student formatted it as "- 10,00" or similar, 
                        // we check the original string for the minus sign.
                        if (t.getAmount().contains("-")) {
                            balance -= Math.abs(value);
                        } else {
                            balance += Math.abs(value);
                        }
                    } catch (NumberFormatException e) {
                        // Handle parsing error
                    }
                }
            }
            return balance;
        });
    }

    public LiveData<List<TransactionDetail>> getTransactions() {
        return filteredTransactions;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<Double> getTotalBalance() {
        return totalBalance;
    }

    public void addTransaction(TransactionDetail transaction) {
        addTransactionUseCase.execute(transaction);
    }

    public void deleteTransaction(TransactionDetail transaction) {
        deleteTransactionUseCase.execute(transaction);
    }
}
