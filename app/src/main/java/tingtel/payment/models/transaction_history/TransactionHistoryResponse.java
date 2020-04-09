package tingtel.payment.models.transaction_history;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TransactionHistoryResponse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
