package tingtel.payment.models.delete_transaction;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteTransactionSendObject {

    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("ref")
    @Expose
    private String ref;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

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
