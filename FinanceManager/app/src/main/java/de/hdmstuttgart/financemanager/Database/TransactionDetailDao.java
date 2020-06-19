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

    @Query("SELECT * FROM `transaction` ORDER BY uid DESC;")
    List<Transaction> getList();

    @Query(("SELECT * FROM `transaction` ORDER BY date DESC;"))
    List<Transaction> getListByDate();

    @Query("INSERT INTO `transaction` VALUES(:id, :new_image, :new_purpose, :new_amount," +
            ":new_date, :new_category, :new_method)")
    void insertItem(long id, int new_image, String new_purpose, String new_amount, String new_date,
                    String new_category, String new_method);

    @Query("DELETE FROM `transaction` WHERE uid = :id")
    void deleteItem(long id);

    @Query("UPDATE `transaction` SET image = :new_image WHERE uid = :id")
    void updateImage(long id, int new_image);

    @Query("UPDATE `transaction` SET purpose = :new_purpose  WHERE uid = :id")
    void updatePurpose(long id, String new_purpose);

    @Query("UPDATE `transaction` SET amount = :new_amount  WHERE uid = :id")
    void updateAmount(long id, String new_amount);

    @Query("UPDATE `transaction` SET category = :new_category  WHERE uid = :id")
    void updateCategory(long id, String new_category);

    @Query("UPDATE `transaction` SET method = :new_method  WHERE uid = :id")
    void updateMethod(long id, String new_method);

    @Query("UPDATE `transaction` SET date = :new_date  WHERE uid = :id")
    void updateDate(long id, String new_date);

    @Query("SELECT * FROM `transaction` WHERE uid = :id")
    List<Transaction> getSingle(int id);

    @Insert
    void insert(Transaction transactionDetail);

    @Delete
    void delete(Transaction transactionDetail);


}