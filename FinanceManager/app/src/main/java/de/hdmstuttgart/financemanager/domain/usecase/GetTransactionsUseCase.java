package de.hdmstuttgart.financemanager.domain.usecase;

import androidx.lifecycle.LiveData;
import java.util.List;
import de.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import de.hdmstuttgart.financemanager.domain.repository.TransactionRepository;

public class GetTransactionsUseCase {
    private final TransactionRepository repository;

    public GetTransactionsUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<TransactionDetail>> execute() {
        return repository.getAllTransactions();
    }
}
