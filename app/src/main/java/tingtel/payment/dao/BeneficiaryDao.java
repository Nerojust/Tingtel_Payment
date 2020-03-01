package tingtel.payment.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import tingtel.payment.models.Beneficiary;

@Dao
public interface BeneficiaryDao {

    @Query("SELECT * FROM Beneficiary")
    List<Beneficiary> getAllItems();

    @Insert
    void insertAll(Beneficiary... Beneficiary);

    @Insert
    void insert(Beneficiary Beneficiary);


    @Query("DELETE FROM Beneficiary")
    void delete();


    @Query("DELETE FROM Beneficiary WHERE id = :id")
    void deleteBeneficiary(int id);
    
}
