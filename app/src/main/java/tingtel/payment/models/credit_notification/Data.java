package tingtel.payment.models.credit_notification;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Data {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("trans_id")
    @Expose
    private String transId;
    @SerializedName("sent")
    @Expose
    private Integer sent;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public Integer getSent() {
        return sent;
    }

    public void setSent(Integer sent) {
        this.sent = sent;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
