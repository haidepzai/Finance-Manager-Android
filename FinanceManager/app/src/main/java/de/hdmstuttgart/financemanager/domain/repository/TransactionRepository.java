package de.hdmstuttgart.financemanager.domain.repository;

import androidx.lifecycle.LiveData;
import java.util.List;
import de.hdmstuttgart.financemanager.domain.model.TransactionDetail;

/**
 * Interface for transaction data operations.
 */
public interface TransactionRepository {
    LiveData<List<TransactionDetail>> getAllTransactions();
    void insertTransaction(TransactionDetail transaction);
    void deleteTransaction(TransactionDetail transaction);
    void deleteTransactionById(int id);
}
