package tingtel.payment.models.transaction_status;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckTransactionStatusSendObject {

    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}