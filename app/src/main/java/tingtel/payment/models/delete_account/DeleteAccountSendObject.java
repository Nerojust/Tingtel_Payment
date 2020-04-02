package tingtel.payment.models.delete_account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteAccountSendObject {

    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
