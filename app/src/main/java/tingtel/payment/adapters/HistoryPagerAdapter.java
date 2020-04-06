package tingtel.payment.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import tingtel.payment.fragments.transaction_history.SimOneHistoryFragment;
import tingtel.payment.fragments.transaction_history.SimTwoHistoryFragment;

public class HistoryPagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;

    public HistoryPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new SimOneHistoryFragment();
            case 1:
                return new SimTwoHistoryFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
