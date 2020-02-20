package tingtel.payment.fragments.transfer_airtime;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import tingtel.payment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetTransferPinTutorialFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_transfer_pin_tutorial, container, false);

        ExpandableLinearLayout mtnContent = (ExpandableLinearLayout) view.findViewById(R.id.mtn_content);
        RelativeLayout mtnHeader = (RelativeLayout) view.findViewById(R.id.mtn_header);

        ExpandableLinearLayout airtelContent = (ExpandableLinearLayout) view.findViewById(R.id.airtel_content);
        RelativeLayout airtelHeader = (RelativeLayout) view.findViewById(R.id.airtel_header);

        ExpandableLinearLayout nmobileContent = (ExpandableLinearLayout) view.findViewById(R.id.nmobile_content);
        RelativeLayout nmobileHeader = (RelativeLayout) view.findViewById(R.id.nmobile_header);

        ExpandableLinearLayout gloContent = (ExpandableLinearLayout) view.findViewById(R.id.glo_content);
        RelativeLayout gloHeader = (RelativeLayout) view.findViewById(R.id.glo_header);

//to toggle content
        mtnHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtnContent.toggle();
            }
        });

        airtelHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                airtelContent.toggle();
            }
        });

        nmobileHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nmobileContent.toggle();
            }
        });

        gloHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gloContent.toggle();
            }
        });

        return view;
    }

}
