package de.hdmstuttgart.financemanager.domain.usecase;

import de.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import de.hdmstuttgart.financemanager.domain.repository.TransactionRepository;

public class AddTransactionUseCase {
    private final TransactionRepository repository;

    public AddTransactionUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute(TransactionDetail transaction) {
        repository.insertTransaction(transaction);
    }
}
