package tingtel.payment.models.add_sim;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddSimSendObject {

    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("new_phone")
    @Expose
    private String newPhone;
    @SerializedName("sim_network")
    @Expose
    private String simNetwork;
    @SerializedName("sim_serial")
    @Expose
    private String simSerial;
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

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    public String getSimNetwork() {
        return simNetwork;
    }

    public void setSimNetwork(String simNetwork) {
        this.simNetwork = simNetwork;
    }

    public String getSimSerial() {
        return simSerial;
    }

    public void setSimSerial(String simSerial) {
        this.simSerial = simSerial;
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
