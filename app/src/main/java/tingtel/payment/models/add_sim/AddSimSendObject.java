package tingtel.payment.models.add_sim;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddSimSendObject {

    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("phone2")
    @Expose
    private String phone2;
    @SerializedName("sim2_network")
    @Expose
    private String sim2Network;
    @SerializedName("sim2_serial")
    @Expose
    private String sim2Serial;
    @SerializedName("phone_sim")
    @Expose
    private String phoneSim;
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

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getSim2Network() {
        return sim2Network;
    }

    public void setSim2Network(String sim2Network) {
        this.sim2Network = sim2Network;
    }

    public String getSim2Serial() {
        return sim2Serial;
    }

    public void setSim2Serial(String sim2Serial) {
        this.sim2Serial = sim2Serial;
    }

    public String getPhoneSim() {
        return phoneSim;
    }

    public void setPhoneSim(String phoneSim) {
        this.phoneSim = phoneSim;
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
