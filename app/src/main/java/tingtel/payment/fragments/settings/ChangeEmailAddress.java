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
public class ChangeEmailAddress extends Fragment {


    EditText edCurrentEmail, edNewEmail, edRenterNewEmail;
    Button btnChangeEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_email_address, container, false);

        edCurrentEmail = view.findViewById(R.id.ed_current_email);
        edNewEmail = view.findViewById(R.id.ed_new_email);
        edRenterNewEmail = view.findViewById(R.id.ed_reenter_new_email);

        btnChangeEmail = view.findViewById(R.id.btn_change_email);





        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.showDialog("Email Successfully Changed", getActivity());
            }
        });


        return view;
    }

}
