package com.hdmstuttgart.financemanager.domain.usecase;

import com.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import com.hdmstuttgart.financemanager.domain.repository.TransactionRepository;

public class DeleteTransactionUseCase {
    private final TransactionRepository repository;

    public DeleteTransactionUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute(TransactionDetail transaction) {
        repository.deleteTransaction(transaction);
    }
}
