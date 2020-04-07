package tingtel.payment.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Beneficiary {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String phoneNumber;
    private String network;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
