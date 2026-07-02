package de.hdmstuttgart.financemanager.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {

    @Query("SELECT * FROM `transaction` ORDER BY uid DESC")
    LiveData<List<TransactionEntity>> getAllTransactions();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TransactionEntity transaction);

    @Delete
    void delete(TransactionEntity transaction);

    @Query("DELETE FROM `transaction` WHERE uid = :id")
    void deleteById(int id);
    
    @Update
    void update(TransactionEntity transaction);
}
