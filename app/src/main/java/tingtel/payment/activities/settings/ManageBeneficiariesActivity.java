package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import tingtel.payment.R;
import tingtel.payment.adapters.BeneficiaryAdapter;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.Beneficiary;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;

public class ManageBeneficiariesActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_beneficiaries);

        RecyclerView recyclerView = findViewById(R.id.rv_beneficiary);
        TextView noResult = findViewById(R.id.noResultFoundTextview);
        AppDatabase appDatabase = AppDatabase.getDatabaseInstance(Objects.requireNonNull(this));

        List<Beneficiary> beneficiaryList = appDatabase.beneficiaryDao().getAllItems();
        BeneficiaryAdapter adapter = new BeneficiaryAdapter(this, beneficiaryList, this);

        if (beneficiaryList.size() == 0) {
            noResult.setVisibility(View.VISIBLE);
            noResult.setText(getResources().getString(R.string.no_beneficiary_found));
        } else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            noResult.setVisibility(View.GONE);
            noResult.setText("");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fade_out);
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        MyApplication.LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void doLogout() {
        new Handler(Looper.getMainLooper()).post(() -> AppUtils.logOutInactivitySessionTimeout(this));
    }
}
