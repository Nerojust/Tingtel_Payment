package tingtel.payment.models.registration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerRegistrationSendObject {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("sim1")
    @Expose
    private Sim1 sim1;
    @SerializedName("sim2")
    @Expose
    private Sim2 sim2;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Sim1 getSim1() {
        return sim1;
    }

    public void setSim1(Sim1 sim1) {
        this.sim1 = sim1;
    }

    public Sim2 getSim2() {
        return sim2;
    }

    public void setSim2(Sim2 sim2) {
        this.sim2 = sim2;
    }

}


