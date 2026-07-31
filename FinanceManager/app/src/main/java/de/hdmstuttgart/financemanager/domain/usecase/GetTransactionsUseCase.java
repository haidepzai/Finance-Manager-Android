package com.hdmstuttgart.financemanager.domain.usecase;

import androidx.lifecycle.LiveData;
import java.util.List;
import com.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import com.hdmstuttgart.financemanager.domain.repository.TransactionRepository;

public class GetTransactionsUseCase {
    private final TransactionRepository repository;

    public GetTransactionsUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<TransactionDetail>> execute() {
        return repository.getAllTransactions();
    }
}
