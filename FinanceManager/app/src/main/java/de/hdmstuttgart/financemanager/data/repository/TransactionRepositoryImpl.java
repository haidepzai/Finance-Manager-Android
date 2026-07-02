package de.hdmstuttgart.financemanager.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import de.hdmstuttgart.financemanager.data.local.TransactionDao;
import de.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import de.hdmstuttgart.financemanager.domain.repository.TransactionRepository;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final TransactionDao transactionDao;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public TransactionRepositoryImpl(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public LiveData<List<TransactionDetail>> getAllTransactions() {
        return Transformations.map(transactionDao.getAllTransactions(), TransactionMapper::toDomainList);
    }

    @Override
    public void insertTransaction(TransactionDetail transaction) {
        executorService.execute(() -> transactionDao.insert(TransactionMapper.toEntity(transaction)));
    }

    @Override
    public void deleteTransaction(TransactionDetail transaction) {
        executorService.execute(() -> transactionDao.delete(TransactionMapper.toEntity(transaction)));
    }

    @Override
    public void deleteTransactionById(int id) {
        executorService.execute(() -> transactionDao.deleteById(id));
    }
}
