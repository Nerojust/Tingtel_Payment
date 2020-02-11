package tingtel.payment.models;

import androidx.room.PrimaryKey;

import java.util.Date;

public class History {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private  String SenderNetwork;

    private  String SenderPhoneNumber;

    private String ReceiverNetwork;

    private String ReceiverPhoneNumber;

    private int Amount;

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

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }
}
