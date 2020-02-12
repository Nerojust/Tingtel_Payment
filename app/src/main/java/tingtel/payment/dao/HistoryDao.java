package tingtel.payment.dao;

import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tingtel.payment.models.History;

public interface HistoryDao {

    @Query("SELECT * FROM History")
    List<History> getAllItems();

    @Insert
    void insertAll(History... History);

    @Insert
    void insert(History History);


    @Query("DELETE FROM History")
    void delete();


    @Query("DELETE FROM History WHERE id = :id")
    void deleteHistory(int id);
}
