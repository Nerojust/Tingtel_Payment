package tingtel.payment.activities.history;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import tingtel.payment.R;
import tingtel.payment.activities.MainActivity;
import tingtel.payment.activities.history.main.SectionsPagerAdapter;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.MyApplication;

public class HistoryActivity extends AppCompatActivity implements MyApplication.LogOutTimerUtil.LogOutListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String NoOfSIm = AppUtils.getSessionManagerInstance().getSimStatus();

        switch (NoOfSIm) {
            case "NO SIM":
                Toast.makeText(this, getResources().getString(R.string.no_sim_detected), Toast.LENGTH_SHORT).show();
                break;

            case "SIM1":
                setContentView(R.layout.activity_history_2);

                break;
            case "SIM1 SIM2":
                setContentView(R.layout.activity_history);

                SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
                ViewPager viewPager = findViewById(R.id.view_pager);
                viewPager.setAdapter(sectionsPagerAdapter);
                TabLayout tabs = findViewById(R.id.tabs);
                tabs.setupWithViewPager(viewPager);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();

        if (intent.getStringExtra("task") != null) {
            if (intent.getStringExtra("task").equalsIgnoreCase("preview_status")) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
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
