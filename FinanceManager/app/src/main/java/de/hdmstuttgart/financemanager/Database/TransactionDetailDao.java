package de.hdmstuttgart.financemanager.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDetailDao {

    @Query("SELECT * FROM `transaction`")
    List<Transaction> getAll();

    @Insert
    void insert(Transaction transactionDetail);

    @Delete
    void delete(Transaction transactionDetail);
}