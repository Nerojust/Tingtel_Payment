package tingtel.payment.models.transaction_history;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionHistoryResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

}
