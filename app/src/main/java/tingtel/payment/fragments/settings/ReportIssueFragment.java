package tingtel.payment.fragments.settings;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportIssueFragment extends Fragment {


  EditText edTitle, edDetails;
  Button btnReport;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_report_issue, container, false);

        edTitle = view.findViewById(R.id.ed_title);
        edDetails = view.findViewById(R.id.ed_details);

        btnReport = view.findViewById(R.id.btn_report);


        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showDialog("Thanks For Reporting your issues, we would look into it", getActivity());
            }
        });

       return view;
    }

}
