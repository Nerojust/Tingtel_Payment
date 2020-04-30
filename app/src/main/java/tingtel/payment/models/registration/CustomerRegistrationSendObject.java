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

    private CustomerRegistrationSendObject(Builder builder) {
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.password = builder.password;
        this.username = builder.username;
        this.hash = builder.hash;
        this.sim1 = builder.sim1;
        this.sim2 = builder.sim2;
    }

    public static Builder newCustomerRegistrationSendObject() {
        return new Builder();
    }


    public static final class Builder {
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        private String username;
        private String hash;
        private Sim1 sim1;
        private Sim2 sim2;

        public Builder() {
        }

        public CustomerRegistrationSendObject build() {
            return new CustomerRegistrationSendObject(this);
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder hash(String hash) {
            this.hash = hash;
            return this;
        }

        public Builder sim1(Sim1 sim1) {
            this.sim1 = sim1;
            return this;
        }

        public Builder sim2(Sim2 sim2) {
            this.sim2 = sim2;
            return this;
        }
    }
}


