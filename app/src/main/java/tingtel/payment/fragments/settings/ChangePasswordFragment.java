package tingtel.payment.fragments.settings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;


public class ChangePasswordFragment extends Fragment {

    EditText edCurrentPassword, edNewPassword, edRenterNewPassword;
    Button btnChangePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);


        edCurrentPassword = view.findViewById(R.id.ed_current_password);
        edNewPassword = view.findViewById(R.id.ed_new_password);
        edRenterNewPassword = view.findViewById(R.id.ed_reenter_new_password);

        btnChangePassword = view.findViewById(R.id.btn_change_password);





        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.showDialog("Password Successfully Changed", getActivity());
            }
        });


        return view;
    }


}
