package tingtel.payment.models.credit_notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditNotificationSendObject {
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }


}
