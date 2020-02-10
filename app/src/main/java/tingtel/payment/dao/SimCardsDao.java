package tingtel.payment.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tingtel.payment.models.SimCards;

@Dao
public interface SimCardsDao {

    @Query("SELECT * FROM SimCards")
    List<SimCards> getAllItems();

    @Insert
    void insertAll(SimCards... SimCards);

    @Insert
    void insert(SimCards SimCards);


    @Query("DELETE FROM SimCards")
    void delete();

    @Query("SELECT * FROM SimCards WHERE Serial = :Serial")
    List<SimCards> getSerial(String Serial);
}
