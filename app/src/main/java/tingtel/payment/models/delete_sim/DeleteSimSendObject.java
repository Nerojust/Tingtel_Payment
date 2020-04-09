package tingtel.payment.models.delete_sim;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteSimSendObject {

    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("sim_number")
    @Expose
    private String simNumber;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}