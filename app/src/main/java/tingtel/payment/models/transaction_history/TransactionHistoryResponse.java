package tingtel.payment.models.transaction_history;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TransactionHistoryResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("phone1_transactions")
    @Expose
    private List<Phone1Transaction> phone1Transactions = null;
    @SerializedName("phone2_transactions")
    @Expose
    private List<Phone2Transaction> phone2Transactions = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Phone1Transaction> getPhone1Transactions() {
        return phone1Transactions;
    }

    public void setPhone1Transactions(List<Phone1Transaction> phone1Transactions) {
        this.phone1Transactions = phone1Transactions;
    }

    public List<Phone2Transaction> getPhone2Transactions() {
        return phone2Transactions;
    }

    public void setPhone2Transactions(List<Phone2Transaction> phone2Transactions) {
        this.phone2Transactions = phone2Transactions;
    }

}
