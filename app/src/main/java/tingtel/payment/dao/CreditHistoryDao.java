package tingtel.payment.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tingtel.payment.models.credit_notification.Data;

@Dao
public interface CreditHistoryDao {

    @Query("SELECT * FROM Data")
    List<Data> getAllCreditNotifications();

    @Insert
    void insertAll(Data... Data);

    @Insert
    void insert(Data Data);


    @Query("DELETE FROM Data")
    void delete();


    @Query("DELETE FROM Data WHERE id = :id")
    void deleteCreditNotification(int id);

}
