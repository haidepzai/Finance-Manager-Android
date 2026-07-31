package com.hdmstuttgart.financemanager.di;

import android.content.Context;
import com.hdmstuttgart.financemanager.data.local.AppDatabase;
import com.hdmstuttgart.financemanager.data.repository.TransactionRepositoryImpl;
import com.hdmstuttgart.financemanager.domain.repository.TransactionRepository;
import com.hdmstuttgart.financemanager.domain.usecase.AddTransactionUseCase;
import com.hdmstuttgart.financemanager.domain.usecase.DeleteTransactionUseCase;
import com.hdmstuttgart.financemanager.domain.usecase.GetTransactionsUseCase;

/**
 * Manual Dependency Injection container.
 */
public class AppContainer {
    public final TransactionRepository transactionRepository;
    public final GetTransactionsUseCase getTransactionsUseCase;
    public final AddTransactionUseCase addTransactionUseCase;
    public final DeleteTransactionUseCase deleteTransactionUseCase;

    public AppContainer(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        this.transactionRepository = new TransactionRepositoryImpl(db.transactionDao());
        this.getTransactionsUseCase = new GetTransactionsUseCase(transactionRepository);
        this.addTransactionUseCase = new AddTransactionUseCase(transactionRepository);
        this.deleteTransactionUseCase = new DeleteTransactionUseCase(transactionRepository);
    }
}
