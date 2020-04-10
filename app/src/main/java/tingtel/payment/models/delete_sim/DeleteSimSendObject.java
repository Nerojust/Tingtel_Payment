package tingtel.payment.models.delete_sim;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteSimSendObject {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("phone_number")
    @Expose
    private String phone_number;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}