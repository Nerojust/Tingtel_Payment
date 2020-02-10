package tingtel.payment.models;

import androidx.room.Entity;

@Entity
public class SimCards {

    private int id;
    private String SimSerial;
    private String SimNetwork;
    private String PhoneNumber;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSimSerial() {
        return SimSerial;
    }

    public void setSimSerial(String simSerial) {
        SimSerial = simSerial;
    }

    public String getSimNetwork() {
        return SimNetwork;
    }

    public void setSimNetwork(String simNetwork) {
        SimNetwork = simNetwork;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
