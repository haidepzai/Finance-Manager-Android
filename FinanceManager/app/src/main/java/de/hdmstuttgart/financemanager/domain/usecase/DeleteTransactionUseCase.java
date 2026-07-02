package de.hdmstuttgart.financemanager.domain.usecase;

import de.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import de.hdmstuttgart.financemanager.domain.repository.TransactionRepository;

public class DeleteTransactionUseCase {
    private final TransactionRepository repository;

    public DeleteTransactionUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute(TransactionDetail transaction) {
        repository.deleteTransaction(transaction);
    }
}
