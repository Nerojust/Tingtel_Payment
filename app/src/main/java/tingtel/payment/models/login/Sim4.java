package tingtel.payment.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sim4 {

    @SerializedName("phone4")
    @Expose
    private String phone4;
    @SerializedName("sim4_serial")
    @Expose
    private String sim4Serial;
    @SerializedName("sim4_user_network")
    @Expose
    private String sim4UserNetwork;

    public String getPhone4() {
        return phone4;
    }

    public void setPhone4(String phone4) {
        this.phone4 = phone4;
    }

    public String getSim4Serial() {
        return sim4Serial;
    }

    public void setSim4Serial(String sim4Serial) {
        this.sim4Serial = sim4Serial;
    }

    public String getSim4UserNetwork() {
        return sim4UserNetwork;
    }

    public void setSim4UserNetwork(String sim4UserNetwork) {
        this.sim4UserNetwork = sim4UserNetwork;
    }
}
