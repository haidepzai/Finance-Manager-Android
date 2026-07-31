package com.hdmstuttgart.financemanager.presentation.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.hdmstuttgart.financemanager.domain.usecase.AddTransactionUseCase;
import com.hdmstuttgart.financemanager.domain.usecase.DeleteTransactionUseCase;
import com.hdmstuttgart.financemanager.domain.usecase.GetTransactionsUseCase;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private final GetTransactionsUseCase getTransactionsUseCase;
    private final AddTransactionUseCase addTransactionUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;

    public MainViewModelFactory(GetTransactionsUseCase getTransactionsUseCase,
                                AddTransactionUseCase addTransactionUseCase,
                                DeleteTransactionUseCase deleteTransactionUseCase) {
        this.getTransactionsUseCase = getTransactionsUseCase;
        this.addTransactionUseCase = addTransactionUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(getTransactionsUseCase, addTransactionUseCase, deleteTransactionUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
