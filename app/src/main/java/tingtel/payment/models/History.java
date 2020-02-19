package tingtel.payment.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class History {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String SenderNetwork;

    private String SenderPhoneNumber;

    private String ReceiverNetwork;

    private String ReceiverPhoneNumber;

    private String SimSerial;

    private String Amount;

    private Date Date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderNetwork() {
        return SenderNetwork;
    }

    public void setSenderNetwork(String senderNetwork) {
        SenderNetwork = senderNetwork;
    }

    public String getSenderPhoneNumber() {
        return SenderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        SenderPhoneNumber = senderPhoneNumber;
    }

    public String getReceiverNetwork() {
        return ReceiverNetwork;
    }

    public void setReceiverNetwork(String receiverNetwork) {
        ReceiverNetwork = receiverNetwork;
    }

    public String getReceiverPhoneNumber() {
        return ReceiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        ReceiverPhoneNumber = receiverPhoneNumber;
    }

    public String getSimSerial() {
        return SimSerial;
    }

    public void setSimSerial(String simSerial) {
        SimSerial = simSerial;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }
}
