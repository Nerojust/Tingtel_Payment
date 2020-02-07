package tingtel.payment.fragments.signup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import tingtel.payment.MainActivity;
import tingtel.payment.R;


public class SetPasswordFragment extends Fragment {

    TextInputEditText tvPassword1;
    TextInputEditText tvPassword2;
    Button btnSetPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_password, container, false);

        initViews(view);
        initListeners(view);

        return view;
    }

    private void initListeners(View view) {

        btnSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }

    private void initViews(View view) {

        tvPassword1 = view.findViewById(R.id.tv_password1);
        tvPassword2= view.findViewById(R.id.tv_password2);

        btnSetPassword = view.findViewById(R.id.btn_set_password);





    }


}
