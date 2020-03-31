package tingtel.payment.models.transaction_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionHistorySendObject {
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("source_network")
    @Expose
    private String sourceNetwork;
    @SerializedName("beneficiary_network")
    @Expose
    private String beneficiaryNetwork;
    @SerializedName("beneficiary_msisdn")
    @Expose
    private String beneficiaryMsisdn;
    @SerializedName("ref")
    @Expose
    private String ref;
    @SerializedName("hash")
    @Expose
    private String hash;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSourceNetwork() {
        return sourceNetwork;
    }

    public void setSourceNetwork(String sourceNetwork) {
        this.sourceNetwork = sourceNetwork;
    }

    public String getBeneficiaryNetwork() {
        return beneficiaryNetwork;
    }

    public void setBeneficiaryNetwork(String beneficiaryNetwork) {
        this.beneficiaryNetwork = beneficiaryNetwork;
    }

    public String getBeneficiaryMsisdn() {
        return beneficiaryMsisdn;
    }

    public void setBeneficiaryMsisdn(String beneficiaryMsisdn) {
        this.beneficiaryMsisdn = beneficiaryMsisdn;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
