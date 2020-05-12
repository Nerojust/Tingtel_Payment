package tingtel.payment.models.validate_user;

import com.google.gson.annotations.SerializedName;

public class ValidateUserSendObject {

    @SerializedName("transaction_value")

    private String transactionValue;
    @SerializedName("hash")

    private String hash;


    public String getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(String transactionValue) {
        this.transactionValue = transactionValue;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
