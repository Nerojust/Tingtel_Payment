package tingtel.payment.fragments.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;


public class ChangePasswordFragment extends Fragment {

    private EditText edCurrentPassword, edNewPassword, edRenterNewPassword;
    private Button btnChangePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        edCurrentPassword = view.findViewById(R.id.current_password);
        edNewPassword = view.findViewById(R.id.new_password);
        edRenterNewPassword = view.findViewById(R.id.re_entered_password);

        btnChangePassword = view.findViewById(R.id.btn_set_password);
        btnChangePassword.setOnClickListener(v -> AppUtils.showDialog("Password Successfully Changed", getActivity()));

        return view;
    }


}
