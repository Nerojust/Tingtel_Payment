package tingtel.payment.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerLoginSendObject {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("hash")
    @Expose
    private String hash;

    private CustomerLoginSendObject(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.hash = builder.hash;
    }

    public static Builder newCustomerLoginSendObject() {
        return new Builder();
    }


    public static final class Builder {
        private String username;
        private String password;
        private String hash;

        public Builder() {
        }

        public CustomerLoginSendObject build() {
            return new CustomerLoginSendObject(this);
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder hash(String hash) {
            this.hash = hash;
            return this;
        }
    }
}