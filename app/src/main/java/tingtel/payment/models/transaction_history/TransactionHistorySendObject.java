package tingtel.payment.models.transaction_history;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionHistorySendObject {

    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("hash")
    @Expose
    private String hash;

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
