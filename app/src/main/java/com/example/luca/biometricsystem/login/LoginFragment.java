package com.example.luca.biometricsystem.login;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.biometricsystem.R;


public class LoginFragment extends Fragment implements OnLoginListener {
    private static final String TAG = "LoginFragment";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_login, container, false);
        inflate.findViewById(R.id.forgot_password).setOnClickListener(v ->
            getActivity().startActivity(new Intent(getActivity(), ForgotPassword.class )));
        return inflate;
    }

    @Override
    public void login() {
        SignUpIn signUpIn = new SignUpIn(getActivity().findViewById(R.id.email_login), getActivity().findViewById(R.id.password_login));
        //signUpIn.setActivity(getActivity());
        signUpIn.signIn(getActivity());
        //parseEmailPassword.changeActivity(getActivity());
    }
}
