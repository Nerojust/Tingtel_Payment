package tingtel.payment.activities.settings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tingtel.payment.R;
import tingtel.payment.adapters.SimCardsAdapter;
import tingtel.payment.database.AppDatabase;
import tingtel.payment.models.SimCards;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;

public class ManageSimActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {

    List<SimCards> simCards = new ArrayList<>();
    AppDatabase appDatabase;
    RecyclerView recyclerView;
    SimCardsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sim);


        recyclerView = findViewById(R.id.rv_simCards);

        appDatabase = AppDatabase.getDatabaseInstance(this);

        simCards = appDatabase.simCardsDao().getAllItems();
        adapter = new SimCardsAdapter(this, simCards, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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
