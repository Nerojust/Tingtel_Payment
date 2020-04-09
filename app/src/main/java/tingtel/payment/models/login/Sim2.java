package tingtel.payment.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sim2 {

    @SerializedName("phone2")
    @Expose
    private String phone2;
    @SerializedName("sim2_serial")
    @Expose
    private String sim2Serial;
    @SerializedName("sim2_user_network")
    @Expose
    private String sim2UserNetwork;

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getSim2Serial() {
        return sim2Serial;
    }

    public void setSim2Serial(String sim2Serial) {
        this.sim2Serial = sim2Serial;
    }

    public String getSim2UserNetwork() {
        return sim2UserNetwork;
    }

    public void setSim2UserNetwork(String sim2UserNetwork) {
        this.sim2UserNetwork = sim2UserNetwork;
    }

}
