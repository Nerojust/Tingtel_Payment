package tingtel.payment.models.add_sim;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddSimSendObject {

    @SerializedName("user_phone")
    @Expose
    private String user_phone;

    @SerializedName("phone2")
    @Expose
    private String phone2;

    @SerializedName("sim2_network")
    @Expose
    private String sim2_network;

    @SerializedName("sim2_serial")
    @Expose
    private String sim2_serial;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("hash")
    @Expose
    private String hash;


    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getSim2_network() {
        return sim2_network;
    }

    public void setSim2_network(String sim2_network) {
        this.sim2_network = sim2_network;
    }

    public String getSim2_serial() {
        return sim2_serial;
    }

    public void setSim2_serial(String sim2_serial) {
        this.sim2_serial = sim2_serial;
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
