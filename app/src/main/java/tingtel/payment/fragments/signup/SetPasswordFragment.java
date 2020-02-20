package tingtel.payment.fragments.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import tingtel.payment.MainActivity;
import tingtel.payment.R;
import tingtel.payment.utils.AppUtils;
import tingtel.payment.utils.SessionManager;


public class SetPasswordFragment extends Fragment {

    private TextInputEditText tvPassword1;
    private TextInputEditText tvPassword2;
    private Button btnSetPassword;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_password, container, false);

        initViews(view);
        initListeners(view);

        return view;
    }

    private void initListeners(View view) {
        btnSetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Objects.requireNonNull(getActivity()).startActivity(intent);

            sessionManager.setIsRegistered(true);
            getActivity().finish();
        });

    }

    private void initViews(View view) {
        tvPassword1 = view.findViewById(R.id.tv_password1);
        tvPassword2 = view.findViewById(R.id.tv_password2);
        btnSetPassword = view.findViewById(R.id.btn_set_password);

        sessionManager = AppUtils.getSessionManagerInstance();
    }
}
