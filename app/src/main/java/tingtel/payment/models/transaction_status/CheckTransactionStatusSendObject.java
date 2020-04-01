package tingtel.payment.models.transaction_status;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckTransactionStatusSendObject {

    @SerializedName("ref")
    @Expose
    private String ref;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}