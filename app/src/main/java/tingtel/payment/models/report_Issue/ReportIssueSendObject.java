package tingtel.payment.models.report_Issue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportIssueSendObject {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("hash")
    @Expose
    private String hash;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}